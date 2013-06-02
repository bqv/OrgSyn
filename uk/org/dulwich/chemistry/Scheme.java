package uk.org.dulwich.chemistry;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import javax.swing.*;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.*;

import uk.org.dulwich.SubMenu;
import uk.org.dulwich.Chemistry;

public class Scheme extends SubMenu {

	public static final int[][] /*reaction*/ matrix = { // Maybe inefficient...
		{0,5,14,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,17,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{7,0,0,19,20, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,16,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,21,0,0,20, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,21,0,0,0, 7,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		// The original code had no source
		{0,0,0,0,9, 0,13,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,21,0,4, 0,0,15,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,21,0,4, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,1, 8,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,2,0, 3,12,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		// That was just maddeningly unhelpful
		{0,0,0,0,0, 0,0,0,0,0, 6,5,14,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 17,0},
		{0,0,0,0,0, 0,0,0,0,3, 7,0,0,19,20, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,16,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 21,0,0,0,20, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 21,0,0,0,0, 7,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		// I had to create this by hand
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,9, 0,13,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,18,0,0, 0,0,0,0,0, 0,0,0,0,4, 0,21,15,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,21,0,4, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,1, 8,0,8,0,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,2,0, 10,0,3,12,0, 0,0,0,0,0, 0,0},
		// VB is a nightmare to reverse engineer
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,6, 0,5,0,0,14, 0,0,0,0,0, 0,17},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,11, 7,0,0,0,0, 19,0,20,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,6, 0,0,0,5,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,11, 0,0,7,0,0, 0,19,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,16,0,0,0, 0,0,0,0,0, 0,0},
		// Why didn't I calculate space complexity...
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,21,0,0,0, 0,0,20,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,21,0, 0,0,0,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,21,0,0,0, 0,0,0,7,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,9,0,13, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,18,0,0, 0,0,0,0,0, 0,0,0,0,21, 0,0,4,0,0, 15,0},
		// Too late now
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,21, 0,0,4,0,0, 0,0},
		{0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0}};

	public Scheme(JFrame p)
	{
		super(p, "Chemical Map");
        Container pane = getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        ((JComponent)pane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Organic Synthesis Scheme");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(title);
        pane.add(Box.createRigidArea(new Dimension(0,5)));

		JPanel canvas = new JPanel();
        canvas.setBorder(BorderFactory.createTitledBorder(""));
        JLabel scheme = imageAsLabel("/scheme.png");
        scheme.setAlignmentX(Component.CENTER_ALIGNMENT);
		scheme.setBorder(BorderFactory.createLoweredBevelBorder());
        canvas.add(scheme);
        pane.add(canvas);
        pane.add(Box.createRigidArea(new Dimension(0,5)));

		JPanel btns = new JPanel();
        pane.add(Box.createRigidArea(new Dimension(0,5)));
        JButton printbtn = new JButton("Print");
        printbtn.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){print();}});
        btns.add(printbtn);

        btns.add(Box.createRigidArea(new Dimension(5,0)));
        JButton back = new JButton("Close");
        back.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){dispose();}});
        btns.add(back);
        btns.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(btns);
	}

	public void run()
	{
		super.run();
		front();
		setAlwaysOnTop(true);
	}

	public static void print()
	{
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new Copies(1));

		PrintService psvcs[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.PNG, pras);
		PrintService defsvc = PrintServiceLookup.lookupDefaultPrintService();
		PrintService psvc = ServiceUI.printDialog(null, 200, 200, psvcs, defsvc, DocFlavor.INPUT_STREAM.PNG, pras);

		if (psvc != null)
		{
			pras.add(new JobName("Chemistry Scheme", null));
			System.out.println("Printing to " + psvc);
			DocPrintJob job = psvc.createPrintJob();

			PrintJobListener listener = new PrintJobAdapter() {
				public void printDataTransferCompleted(PrintJobEvent e)
				{
					JOptionPane.showMessageDialog(null, " - Print Complete - ");
				}
			};
			job.addPrintJobListener(listener);

			InputStream pdf = Chemistry.class.getResourceAsStream("/print.pdf");
			Doc doc = new SimpleDoc(pdf, DocFlavor.INPUT_STREAM.PDF, null);

			try {
				job.print(doc, pras);
				pdf.close();
			} catch(PrintException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, " - Print Failed - ");
			} catch(IOException e) {
				e.printStackTrace();
				System.exit(3);
			}
		}
	}

	public static int[] bfs(int a, int b)
	/* Dijkstra is O((V+E)*log(V)) whereas BFS is O(V+E) for an
	 * adjacency matrix so in terms of time complexity and given
	 * all edges have equal weight anyway, BFS makes more sense. */
	{
		ArrayList<Integer> seen = new ArrayList<Integer>();
		seen.add(a);
		Queue<LinkedList<Integer>> queue = new LinkedList<LinkedList<Integer>>();
		for(int i=0; i < matrix[a].length; i++)
		{
			if(matrix[a][i] == 0) continue;
			LinkedList<Integer> e = new LinkedList<Integer>();
			e.add(a); e.add(i); seen.add(i);
			queue.add(e);
		}
		while(!queue.isEmpty())
		{
			LinkedList<Integer> c = queue.poll();
			int d = c.getLast();
			if(d == b)
			{
				int[] ret = new int[c.size()];
				for(int i=0; i < ret.length; i++)
					ret[i] = c.get(i)+1;
				return ret;
			}
			for(int i=0; i < matrix[d].length; i++)
			{
				if(matrix[d][i] == 0) continue;
				if(seen.contains(i)) continue;
				LinkedList<Integer> e = new LinkedList<Integer>(c);
				e.add(i);
				queue.add(e);
			}
		}
		return null; // Disconnected
	}

	public static int neighbourAt(int o, int d)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		int[] dl = dijkstra(o);
		for(int i=0; i < dl.length; i++)
		{
			if(dl[i] == d) list.add(new Integer(i));
		}
		if(list.isEmpty()) return neighbourAt(o, d-1); // Recursive compromise
		return list.get((int)(Math.random()*(list.size()-1)));
	}

    public static int[] dijkstra(int start)
	/* It's better(?) for finding all distances; we have no sink. */
    {
		int[] dist = new int[Hydrocarbon.nIM];
		boolean[] seen = new boolean[Hydrocarbon.nIM];
		final int infty = 127; // Arbitrarily large number
		for (int i = 0; i < Hydrocarbon.nIM; i++)
		{
			dist[i] = infty;
			seen[i] = false;
		}
		dist[start] = 0;

		for(int i = 0; i < Hydrocarbon.nIM-1; i++)
		{
			int min = infty;
			int idx = -1;
			for (int j = 0; j < Hydrocarbon.nIM; j++)
				if (seen[j] == false && dist[j] <= min)
				{
					min = dist[j];
					idx = j;
				}
			seen[idx] = true;
			for (int j = 0; j < Hydrocarbon.nIM; j++)
				if (!seen[j] && matrix[idx][j] != 0 && 
						dist[idx] != infty && 
						dist[idx]+1 < dist[j])
					dist[j] = dist[idx]+1;
		} return dist;
	}

	private static String routeTable(int[] path)
	{
		String root = "<html><table border='0'>";
		if(path == null) return "Error 113: No Route To Sink";
		root += "<tr>";
		root += "<th>No.</th>";
		root += "<th>From</th>";
		root += "<th>Using</th>";
		root += "<th>To</th>";
		root += "</tr>";
		for(int i=0; i < path.length-1; i++)
		{
			root += "<tr>";
			root += "<td>"+(i+1)+". </td>";
			root += "<td>"+Hydrocarbon.getName(path[i]-1)+" ("+path[i]+")</td>";
			root += "<td>"+Agent.html[matrix[path[i]-1][path[i+1]-1]-1]+"</td>";
			root += "<td>"+Hydrocarbon.getName(path[i+1]-1)+" ("+path[i]+")</td>";
			root += "</tr>";
		}
		return root+"</table></html>";
	}

	public static BufferedImage routeHeader(int a, int b)
	{
		int[] sza = Hydrocarbon.size[a]; int[] szb = Hydrocarbon.size[b];
		int w = sza[0]+5+160+5+szb[0]; int iw = w/2;
		int h = Math.max(sza[1], szb[1]); int ih = (h/2)+16;
		BufferedImage image = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.fillRect(0, 0, iw, ih); g.scale(0.5,0.5);
		g.drawImage(Hydrocarbon.getImage(a), 0, 16+(h-sza[1])/2, null);
		g.drawImage(imageAsIcon("/to.png").getImage(), sza[0]+5, 16+(h-20)/2, null);
		g.drawImage(Hydrocarbon.getImage(b), w-szb[0], 16+(h-szb[1])/2, null);
		return image;
	}

	private static BufferedImage routeImage(int[] path)
	{
		JLabel lbl = new JLabel(routeTable(path));
		lbl.setSize(lbl.getPreferredSize());
		int w = lbl.getWidth(); int h = lbl.getHeight()+8;
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		lbl.paint(g);
		return image;
	}

	public static ImageIcon route(int a, int b, int[] i)
	{
		int[] path = bfs(a, b); int l = path.length-1;
		i[0] = (i[0] % l + l) % l; // if(i[0]+1 == l) i[0] = 0;
		BufferedImage header = routeHeader(path[i[0]]-1,path[i[0]+1]-1);
		BufferedImage table = routeImage(path);
		int hw = header.getWidth(null); int tw = table.getWidth(null);
		int hh = header.getHeight(null)+16; int th = table.getHeight(null);
		int w = Math.max(hw, tw); int h = hh+th;
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		//g.setColor(Color.WHITE); g.fillRoundRect(0,0,w,h,16,16);
		g.drawImage(header, (w-hw)/2, 0, null);
		g.drawImage(table, (w-tw)/2, hh, null);
		ImageProducer ip = new FilteredImageSource(image.getSource(), 
				new RGBImageFilter(){public int filterRGB(int x, int y, int rgb)
					{
						if ((rgb & 0xFF0000) >> 16 == 0xFF &&
							(rgb & 0xFF00) >> 8 == 0xFF &&
							(rgb & 0xFF) == 0xFF)
							return rgb & 0xFFFFFF;
						else return rgb;
					}
				});
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(ip));
	}
}
