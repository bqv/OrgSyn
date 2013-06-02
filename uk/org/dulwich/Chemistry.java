package uk.org.dulwich;

// (C) Tony Olagbaiye and Andre Avshu 2013. All rights reserved.

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;

import uk.org.dulwich.chemistry.*;
 
public class Chemistry extends SubMenu {

    public Chemistry(JFrame p)
    {
        super(p, "Chemistry");
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        ((JComponent)pane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Pick a Topic:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(title);
        pane.add(Box.createRigidArea(new Dimension(0,5)));

        JButton chemistry = new JButton("Organic Synthesis");
        chemistry.setAlignmentX(Component.CENTER_ALIGNMENT);
        chemistry.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){orgSyn();}});
        pane.add(chemistry);
        pane.add(Box.createRigidArea(new Dimension(0,5)));

        pane.add(Box.createRigidArea(new Dimension(0,5)));
        JButton back = new JButton("Back...");
        if (parent == null) back.setText("Exit");
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){dispose();}});
        pane.add(back);
    }

    public void orgSyn()
    {
        setVisible(false);
        (new OrgSyn(this)).run();
    }

    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Chemistry(null));
    }
}
