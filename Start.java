// Dulwich College Revision Utility
// (C) Tony Olagbaiye 2013. All rights reserved.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import uk.org.dulwich.SubMenu;
import uk.org.dulwich.Chemistry;
 
public class Start extends SubMenu {

    public Start()
    {
        super(null, "DC Revision");
        Container pane = getContentPane();
        setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        ((JComponent)pane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Pick a Subject:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(title);
        pane.add(Box.createRigidArea(new Dimension(0,5)));

        JButton chemistry = new JButton("Chemistry");
        chemistry.setAlignmentX(Component.CENTER_ALIGNMENT);
        chemistry.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){chemistry();}});
        pane.add(chemistry);
        pane.add(Box.createRigidArea(new Dimension(0,5)));

        pane.add(Box.createRigidArea(new Dimension(0,5)));
        JButton exit = new JButton("Exit");
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){dispose();}});
        pane.add(exit);
    }

    private void chemistry()
    {
        setVisible(false);
        (new Chemistry(this)).run();
    }
 
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Start());
    }
}
