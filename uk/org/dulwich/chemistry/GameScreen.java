package uk.org.dulwich.chemistry;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class GameScreen extends JPanel {

	private OrgSyn window;
	private int start, end;
	private JLabel task;

	public GameScreen(OrgSyn p, int s, int e)
	{
		window = p; start = s; end = e;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel toolbar = new JPanel(new FlowLayout());
		setuptb(toolbar);
		JPanel body = new JPanel(new FlowLayout());
		setupbody(body);
		JLabel status = new JLabel((start+1)+" ... "+(end+1));
		add(toolbar);
		add(body);
		add(status);
		enter();
	}

	private void reset()
	{
		JOptionPane.showMessageDialog(this, "..");
	}

	private void undo()
	{
		JOptionPane.showMessageDialog(this, "...");
	}

	private void setuptb(JPanel toolbar)
	{
		task = new JLabel(new ImageIcon(Scheme.routeHeader(start,end)));
        task.setAlignmentY(Component.CENTER_ALIGNMENT);
        task.setBorder(BorderFactory.createLoweredBevelBorder());
		toolbar.add(task); toolbar.add(Box.createHorizontalGlue());
		JButton mapBtn = new JButton("Open Map");
        mapBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        mapBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {window.scheme();}
		});
		toolbar.add(mapBtn); toolbar.add(Box.createHorizontalGlue());
		JButton undo = new JButton("Undo");
        undo.setAlignmentY(Component.CENTER_ALIGNMENT);
        undo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {undo();}
		});
		toolbar.add(undo); toolbar.add(Box.createHorizontalGlue());
		JButton newGame = new JButton("New Game");
        newGame.setAlignmentY(Component.CENTER_ALIGNMENT);
        newGame.addActionListener(new ActionListener(){ // Make this more specific
			public void actionPerformed(ActionEvent e) {window.startGame();}
		});
		toolbar.add(newGame); toolbar.add(Box.createHorizontalGlue());
		JButton back = new JButton("Back...");
        back.setAlignmentY(Component.CENTER_ALIGNMENT);
        back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {leave();}
		});
		toolbar.add(back); toolbar.add(Box.createHorizontalGlue());
		JButton quit = new JButton("Quit");
        quit.setAlignmentY(Component.CENTER_ALIGNMENT);
        quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {System.exit(0);}
		});
		toolbar.add(quit); toolbar.add(Box.createHorizontalGlue());
	}

	private void setupbody(JPanel body)
	{
		Box col1 = new Box(BoxLayout.PAGE_AXIS);
		Box col2 = new Box(BoxLayout.PAGE_AXIS);
		Box col3 = new Box(BoxLayout.PAGE_AXIS);
		JPanel ladder = new JPanel(new GridLayout(Agent.nAG,1));
		JPanel agents = new JPanel(new GridLayout(Agent.nAG,1));
		JPanel hc     = new JPanel(new GridLayout((int)Math.ceil(Hydrocarbon.nIM/3),3));
		JScrollPane ladderpane = new JScrollPane(ladder);
		ladderpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane agentpane  = new JScrollPane(agents);
		agentpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JScrollPane hcpane     = new JScrollPane(hc);
		hcpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ladder.add(new JLabel(new ImageIcon(Hydrocarbon.getImage(start))));
		for(int i=0; i < Agent.nAG; i++)
		{
			agents.add(new JButton("<html>"+Agent.html[i]+"</html>"));
		}
		for(int i=0; i < Hydrocarbon.nIM; i++)
		{
			Image orig = Hydrocarbon.getImage(i);
			BufferedImage image = new BufferedImage(orig.getWidth(null)/2,
				   orig.getHeight(null)/2, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = image.createGraphics();
			g.scale(0.5,0.5); g.drawImage(orig, 0, 0, null);
			hc.add(new JButton(new ImageIcon(image)));
		}
		ladderpane.setPreferredSize(new Dimension(Hydrocarbon.mw,500));
		agentpane.setPreferredSize(new Dimension(Hydrocarbon.mw,500));
		hcpane.setPreferredSize(new Dimension((int)(Hydrocarbon.mw*2.0),500));
		hc.setPreferredSize(new Dimension((int)(Hydrocarbon.mw*2.0),500));
		col1.add(ladderpane);
		col2.add(agentpane);
		col3.add(hcpane);
		body.add(col1);
		body.add(col2);
		body.add(col3);
	}

	public void enter()
	{
		window.setContentPane(this);
		window.getRootPane().getActionMap().put("escape", new AbstractAction(){public void actionPerformed(ActionEvent e){leave();}});
		window.validate();
	}

	public void leave()
	{
		window.setContentPane(window.pane);
		window.getRootPane().getActionMap().put("escape", new AbstractAction(){public void actionPerformed(ActionEvent e){window.dispose();}});
		window.validate();
	}
}
