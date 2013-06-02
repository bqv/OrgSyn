package uk.org.dulwich.chemistry;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.imageio.ImageIO;

import uk.org.dulwich.SubMenu;
import uk.org.dulwich.Chemistry;

public class OrgSyn extends SubMenu {

	private SubMenu map;
	private Random rng;
	protected int start, end;
	protected boolean rand = true;
	public int ease = 0; // 0-2 easy,medium,hard
    public Container pane;
    public JMenuBar menubar;
    
    public OrgSyn(JFrame p)
    {
        super(p, "Organic Synthesis");
		rng = new Random(System.currentTimeMillis());
		Hydrocarbon.init();
        pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));
        ((JComponent)pane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		makeMenu();
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        pane.add(panel);
        pane.add(Box.createRigidArea(new Dimension(20,0)));
        JPanel canvas = new JPanel();
        JLabel page = imageAsLabel("/list_n.png");
		page.setBorder(BorderFactory.createLoweredBevelBorder());
        pane.add(canvas);
        canvas.setBorder(BorderFactory.createTitledBorder("Start/End Points"));
        canvas.add(page);

		JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        controls.setBorder(BorderFactory.createTitledBorder("Settings"));
		panel.add(controls);
		JPanel buttons = new JPanel(new FlowLayout());
		panel.add(buttons);

		JLabel modelabel = new JLabel("Selection mode:");
        modelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		controls.add(modelabel);
		JPanel mode = new JPanel(new FlowLayout());
		controls.add(mode);
		JLabel difflabel = new JLabel("Difficulty level:");
        difflabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		controls.add(difflabel);
		JPanel diff = new JPanel(new FlowLayout());
		controls.add(diff);
		JLabel pathlabel = new JLabel("Path choices:");
        pathlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		controls.add(pathlabel);
		JPanel choices = new JPanel(new FlowLayout());
		controls.add(choices);
		JPanel pathBtns = new JPanel(new FlowLayout());
		controls.add(pathBtns);

		ButtonGroup g_diff = new ButtonGroup();
		final JRadioButton easy = new JRadioButton("Easy", true);
		final JRadioButton medm = new JRadioButton("Medium", false);
		final JRadioButton hard = new JRadioButton("Hard", false);
		ActionListener easeBtns = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(easy.equals(e.getSource()))
					ease = 0;
				if(medm.equals(e.getSource()))
					ease = 1;
				if(hard.equals(e.getSource()))
					ease = 2;
			}
		};
		easy.addActionListener(easeBtns);
		medm.addActionListener(easeBtns);
		hard.addActionListener(easeBtns);
		g_diff.add(easy);
		g_diff.add(medm);
		g_diff.add(hard);
		diff.add(easy);
		diff.add(medm);
		diff.add(hard);

		ButtonGroup g_mode = new ButtonGroup();
		final JRadioButton random = new JRadioButton("Random", true);
		final JRadioButton choice = new JRadioButton("Manual", false);
		choice.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				rand = !choice.isSelected();
				easy.setEnabled(rand);
				medm.setEnabled(rand);
				hard.setEnabled(rand);
			}
		});
		g_mode.add(random);
		g_mode.add(choice);
		mode.add(random);
		mode.add(choice);

		JPanel startComp = new JPanel(new GridLayout(3,1));
		choices.add(startComp);
		startComp.add(new JLabel("  Start at:"));
		final JPanel startPnl = new JPanel();
		final JSpinner startSpnr = new JSpinner(new SpinnerNumberModel(1,1,Hydrocarbon.nIM,1));
		JFormattedTextField field = (JFormattedTextField)startSpnr.getEditor().getComponent(0);
		((DefaultFormatter)field.getFormatter()).setCommitsOnValidEdit(true);
		startSpnr.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				start = Integer.parseInt(startSpnr.getValue().toString()) - 1;
				if(rand) choice.doClick(); startPnl.removeAll();
				startPnl.add(new JLabel(new ImageIcon(Hydrocarbon.getImage(start))));
				startPnl.revalidate(); startPnl.repaint(); pack();
			}
		});
		startComp.add(startSpnr);
        startPnl.setBorder(BorderFactory.createTitledBorder(""));
		startPnl.add(new JLabel(new ImageIcon(Hydrocarbon.getImage(0))));
		choices.add(startPnl);

		JPanel endComp = new JPanel(new GridLayout(3,1));
		choices.add(endComp);
		endComp.add(new JLabel("  Finish at:"));
		final JPanel endPnl = new JPanel();
		final JSpinner endSpnr = new JSpinner(new SpinnerNumberModel(2,1,Hydrocarbon.nIM,1));
		field = (JFormattedTextField)endSpnr.getEditor().getComponent(0);
		((DefaultFormatter)field.getFormatter()).setCommitsOnValidEdit(true);
		endSpnr.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				end = Integer.parseInt(endSpnr.getValue().toString()) - 1;
				if(rand) choice.doClick(); endPnl.removeAll();
				endPnl.add(new JLabel(new ImageIcon(Hydrocarbon.getImage(end))));
				endPnl.revalidate(); endPnl.repaint(); pack();
			}
		});
		endComp.add(endSpnr);
        endPnl.setBorder(BorderFactory.createTitledBorder(""));
		endPnl.add(new JLabel(new ImageIcon(Hydrocarbon.getImage(1))));
		choices.add(endPnl);

		JButton swap = new JButton("Swap");
        swap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int start = Integer.parseInt(startSpnr.getValue().toString());
				int end = Integer.parseInt(endSpnr.getValue().toString());
				startSpnr.setValue(end);
				endSpnr.setValue(start);
			}
		});
		pathBtns.add(swap);
		JButton showRoute = new JButton("Show Route");
        showRoute.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){showRoute();}});
		pathBtns.add(showRoute);

		JButton startQuiz = new JButton("Start Quiz");
        startQuiz.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){startGame();}});
		buttons.add(startQuiz);
		JButton showMap = new JButton("Show Map");
        showMap.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){scheme();}});
		buttons.add(showMap);
		JButton print = new JButton("Print Map");
        print.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){Scheme.print();}});
		buttons.add(print);
		JButton back = new JButton("Back...");
        if (parent == null) back.setText("Exit");
        back.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){dispose();}});
		buttons.add(back);

		rootPane.setDefaultButton(startQuiz);
    }

	private void makeMenu()
	{
		menubar = new JMenuBar();
		menubar.add(fileMenu());
		menubar.add(editMenu());
		menubar.add(Box.createGlue());
		menubar.add(helpMenu());
		setJMenuBar(menubar);
	}

	private JMenu fileMenu()
	{
		JMenu menu = new JMenu("File");
		JMenuItem item = new JMenuItem("New Game");
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("Exit");
		menu.add(item);
		return menu;
	}

	private JMenu editMenu()
	{
		JMenu menu = new JMenu("Edit");
		JMenuItem item = new JMenuItem("Undo");
		menu.add(item);
		item = new JMenuItem("Redo");
		menu.add(item);
		return menu;
	}

	private JMenu helpMenu()
	{
		JMenu menu = new JMenu("Help");
		JMenuItem item = new JMenuItem("About...");
		menu.add(item);
		return menu;
	}

	private int[] createGame()
	{ // Sorry for the change in style. This was just too sparse. Screen estate.
		int st = start; int ed = end;
		if(rand) {
			int llim = (ease*2)+1; int ulim = ((ease+1)*2)+1;
			int ds; boolean imp; do {
				st = rng.nextInt(Hydrocarbon.nIM);
				ds = rng.nextInt(ulim-llim)+llim;
				ed = Scheme.neighbourAt(st, ds);
				imp = false;
				if(st == ed) imp = true;
				//if(Scheme.bfs(st,ed) == null) imp = true; //Superfluous
			} while(imp);
		} else {
			if(st == ed) {
				JOptionPane.showMessageDialog(this, "You can't start and end on the same compound!");
				return null;
			} else if(Scheme.bfs(st,ed) == null) {
				JOptionPane.showMessageDialog(this, "Well that's just not possible!");
				return null;
			}
		}
		return new int[]{st,ed};
	}

	public void startGame()
	{
		int[] game = createGame();
		if(game != null)
		{
			int start = game[0]; int end = game[1];
			new GameScreen(this, start, end);
		}
	}

	public void showRoute()
	{
		int[] game = createGame();
		if(game != null)
		{
			final int a = game[0]; final int b = game[1];
			final int i[] = new int[1];
			Box box = new Box(BoxLayout.PAGE_AXIS);
			JPanel controller = new JPanel();
			box.add(controller);
			final JLabel route = new JLabel(Scheme.route(a, b, i)); // Pseudopointer...
        	route.setAlignmentX(Component.CENTER_ALIGNMENT);
			box.add(route, 1);
			JButton prev = new JButton("Prev");
			final JLabel index = new JLabel(" - 1 - ");
			JButton next = new JButton("Next");
			prev.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					i[0] -= 1;
					route.setIcon(Scheme.route(a, b, i));
					index.setText(" - "+(i[0]+1)+" - ");
				}
			});
			next.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					i[0] += 1;
					route.setIcon(Scheme.route(a, b, i));
					index.setText(" - "+(i[0]+1)+" - ");
				}
			});
			controller.add(prev); controller.add(index); controller.add(next);
        	controller.setAlignmentX(Component.CENTER_ALIGNMENT);
			JOptionPane.showMessageDialog(this, box);
		}
	}

	public void scheme()
	{
		if((map == null) || !map.isDisplayable())
			map = new Scheme(this);
        javax.swing.SwingUtilities.invokeLater(map);
	}

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new OrgSyn(null));
    }
}
