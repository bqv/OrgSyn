package uk.org.dulwich;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.UIManager.*;
import javax.imageio.ImageIO;

public abstract class SubMenu extends JFrame implements Runnable,WindowListener {

    protected JFrame parent = null;

    public SubMenu(JFrame p, String title)
    {
        parent = p;
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					SwingUtilities.updateComponentTreeUI(this);
					break;
				}
			}
		} catch (Exception ex) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				SwingUtilities.updateComponentTreeUI(this);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(4);
			}
		}
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(true);
        addWindowListener(this);
		setTitle(title);
		rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		rootPane.getActionMap().put("escape", new AbstractAction(){public void actionPerformed(ActionEvent e){dispose();}});
		rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK), "quit");
		rootPane.getActionMap().put("quit", new AbstractAction(){public void actionPerformed(ActionEvent e){System.exit(0);}});
		setIconImage(imageAsIcon("/icon32.png").getImage());
    }

    public void windowOpened(WindowEvent e){}
    public void windowClosing(WindowEvent e){}
    public void windowClosed(WindowEvent e)
    {
        if (parent == null)
            System.exit(0);
        else
            parent.setVisible(true);
    }
    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}

    public void run()
    {
        pack();
		setLocationRelativeTo(null);
        setVisible(true);
		toFront();
		repaint();
		JButton btn = rootPane.getDefaultButton();
		if(btn != null) btn.requestFocusInWindow();
    }

	public void front()
	{
		setVisible(true);
		setAlwaysOnTop(true);
		toFront();
		requestFocus();
		setAlwaysOnTop(false);
	}

    public static ImageIcon imageAsIcon(String name)
    {
        BufferedImage list_png;
        try {
            list_png = ImageIO.read(Chemistry.class.getResource(name));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "He's dead, Jim :(");
            throw new RuntimeException(e);
        }
        return new ImageIcon(list_png);
    }
    public static JLabel imageAsLabel(String name)
	{
        return new JLabel(imageAsIcon(name));
	}
}
