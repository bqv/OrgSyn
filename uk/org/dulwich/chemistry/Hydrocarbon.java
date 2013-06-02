package uk.org.dulwich.chemistry;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.lang.Math;
import javax.swing.*;
import javax.imageio.ImageIO;

import uk.org.dulwich.Chemistry;

public abstract class Hydrocarbon {

	public static final int nIM = 32;
	private static boolean loaded = false;
	private static String[] names = { // = new String[nIM];
		"Chloromethane", "Methanol", "Methylamine",
		"Methanal", "Methanoic Acid", "Methanoyl Chloride",
		"Methanamide", "Hydrogen Cyanide", "Ethane",
		"Ethene", "Chloroethane", "Ethanol",
		"Ethylamine", "Ethanal", "Ethanoic Acid",
		"Ethanoyl Chloride", "Ethanamide", "Methyl Cyanide",
		"Propane", "Propene", "1-Chloropropane",
		"Propan-1-ol", "2-Chloropropane", "Propan-2-ol",
		"Propylamine", "Propanal", "Propanone",
		"Propanoic acid", "Propanoyl Chloride", "Propanamide",
		"Ethyl Cyanide", "Propyl Cyanide"};
	public static final int nGL = 14;
	private static String[] filenames = { // = new String[nGL];
		"P", "C", "Cl", "OH", "NH2", "COH", "COOH", "COCl", "CONH2", "CN", //0-9
		"CH3", "C=", "CH2", "CO"}; //10-13
	protected static BufferedImage[] gly = new BufferedImage[nGL];
	protected static int[][] sz = new int[nGL][2];
	protected static BufferedImage[] img = new BufferedImage[nIM];
	protected static int[][] size = new int[nIM][2];
	public static int mw, mh;
	private static Hydrocarbon[] hc = { // = new Hydrocarbon[nIM];
		new clmtn(), new mtnl(), new mtlamn(),
		new mtnal(), new mtnc(), new mtnlcld(),
		new mtnamd(), new hcn(), new etn(),
		new etne(), new cletn(), new etnl(),
		new etlamn(), new etnal(), new etnc(),
		new etnlcld(), new etnamd(), new mtlcn(),
		new ppn(), new ppne(), new clppn1(),
		new ppn1ol(), new clppn2(), new ppn2ol(),
		new pplamn(), new ppnal(), new ppnone(),
		new ppnc(), new ppnlcld(), new ppnamd(),
		new etlcn(), new pplcn()};

	public static String getName(int i)
	{
		return names[i];
	}

	public static Image getImage(int i)
	{
		ImageProducer ip = new FilteredImageSource(img[i].getSource(), 
				new RGBImageFilter(){public int filterRGB(int x, int y, int rgb)
					{
						if ((rgb & 0xFF0000) >> 16 == 0xFF &&
							(rgb & 0xFF00) >> 8 == 0xFF &&
							(rgb & 0xFF) == 0xFF)
							return rgb & 0xFFFFFF;
						else return rgb;
					}
				});
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	public static int[] getDims(int i)
	{
		return size[i];
	}

	public abstract BufferedImage draw(int n);

	private static BufferedImage loadGlyph(String fn)
	{
		String uri = "/"+fn+".png";
		BufferedImage glyph = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        try {
			glyph = ImageIO.read(Chemistry.class.getResource(uri));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, " - Organic Synthesis - \nCrashed :(");
            throw new RuntimeException(e);
        } finally {
			return glyph;
		}
	}

	public static void init()
	{
		if(loaded) return;
		// Load the glyphs
		for(int n = 0; n < nGL; n++)
			gly[n] = loadGlyph(filenames[n]);
		// Store sizes
		for(int n = 0; n < nGL; n++)
		{
			sz[n][0] = gly[n].getWidth();
			sz[n][1] = gly[n].getHeight();
		}
		// Patch them together for each image
		for(int n = 0; n < nIM; n++)
			img[n] = hc[n].draw(n);
		// Find max width and height
		for(int n = 0; n < nIM; n++)
		{
			if(size[n][0] > mw) mw = size[n][0];
			if(size[n][1] > mh) mh = size[n][1];
		}
		loaded = true;
	}
}

/* Don't bother trying to understand these. I don't. */
class clmtn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz2 = sz[2];
		int w = sz0[0]+sz1[0]+sz2[0];
		int h = 2*(sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz0[0], 0, null); // Top H
		g.drawImage(gly[1], sz0[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz0[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[2], sz0[0]+sz1[0], (h/2)-(sz2[1]/2), null); // Chloride

		return mol;
	}
}

class mtnl extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz3 = sz[3];
		int w = sz0[0]+sz1[0]+sz3[0];
		int h = 2*(sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz0[0], 0, null); // Top H
		g.drawImage(gly[1], sz0[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz0[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[3], sz0[0]+sz1[0], (h/2)-(sz3[1]/2), null); // Hydroxide

		return mol;
	}
}

class mtlamn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz4 = sz[4];
		int w = sz0[0]+sz1[0]+sz4[0];
		int h = 2*(sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz0[0], 0, null); // Top H
		g.drawImage(gly[1], sz0[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz0[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[4], sz0[0]+sz1[0], (h/2)-(sz4[1]/2), null); // Amine

		return mol;
	}
}

class mtnal extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz5 = sz[5];
		int w = sz0[0]+sz5[0];
		int h = sz5[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[5], sz0[0], 0, null); // Aldehyde

		return mol;
	}
}

class mtnc extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz6 = sz[6];
		int w = sz0[0]+sz6[0];
		int h = sz6[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[6], sz0[0], 0, null); // Carboxyl

		return mol;
	}
}

class mtnlcld extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz7 = sz[7];
		int w = sz0[0]+sz7[0];
		int h = sz7[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[7], sz0[0], 0, null); // Acyl Chloride

		return mol;
	}
}

class mtnamd extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz8 = sz[8];
		int w = sz0[0]+sz8[0];
		int h = sz8[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[8], sz0[0], 0, null); // Amide

		return mol;
	}
}

class hcn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz9 = sz[9];
		int w = sz0[0]+sz9[0];
		int h = Math.max(sz0[1], sz9[1]);
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, (h/2)-(sz0[1]/2), null); // Left H
		g.drawImage(gly[9], sz0[0], (h/2)-(sz9[1]/2), null); // Cyanide

		return mol;
	}
}

class etn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10];
		int w = 2*sz10[0];
		int h = sz10[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, 0, null); // Left Methyl
		g.drawImage(gly[10], sz10[0], 0, null); // Right Methyl

		return mol;
	}
}

class etne extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz11 = sz[11];
		int w = (2*sz0[0])+sz11[0];
		int h = sz0[1]+sz11[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[0], 0, 0, null); // Top Left H
		g.drawImage(gly[0], 0, h-sz0[1], null); // Bot Left H
		g.drawImage(gly[11], sz0[0], sz0[1]/2, null); // Center Cs
		g.drawImage(gly[0], w-sz0[0], 0, null); // Top Right H
		g.drawImage(gly[0], w-sz0[0], h-sz0[1], null); // Bot Right H

		return mol;
	}
}

class cletn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz2 = sz[2]; int[] sz10 = sz[10];
		int w = sz10[0]+sz1[0]+sz2[0];
		int h = (2*sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], 0, null); // Top H
		g.drawImage(gly[1], sz10[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[2], sz10[0]+sz1[0], (h/2)-(sz2[1]/2), null); // Chloride

		return mol;
	}
}

class etnl extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz3 = sz[3]; int[] sz10 = sz[10];
		int w = sz10[0]+sz1[0]+sz3[0];
		int h = (2*sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], 0, null); // Top H
		g.drawImage(gly[1], sz10[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[3], sz10[0]+sz1[0], (h/2)-(sz3[1]/2), null); // Hydroxide

		return mol;
	}
}

class etlamn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz4 = sz[4]; int[] sz10 = sz[10];
		int w = sz10[0]+sz1[0]+sz4[0];
		int h = (2*sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], 0, null); // Top H
		g.drawImage(gly[1], sz10[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[4], sz10[0]+sz1[0], (h/2)-(sz4[1]/2), null); // Amine

		return mol;
	}
}

class etnal extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz5 = sz[5];
		int w = sz10[0]+sz5[0];
		int h = sz5[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[5], sz10[0], 0, null); // Aldehyde

		return mol;
	}
}

class etnc extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz6 = sz[6];
		int w = sz10[0]+sz6[0];
		int h = sz6[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[6], sz10[0], 0, null); // Carboxyl

		return mol;
	}
}

class etnlcld extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz7 = sz[7];
		int w = sz10[0]+sz7[0];
		int h = sz7[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[7], sz10[0], 0, null); // Acyl Chloride

		return mol;
	}
}

class etnamd extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz8 = sz[8];
		int w = sz10[0]+sz8[0];
		int h = sz8[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[8], sz10[0], 0, null); // Amide

		return mol;
	}
}

class mtlcn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz9 = sz[9];
		int w = sz10[0]+sz9[0];
		int h = Math.max(sz10[1], sz9[1]);
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[9], sz10[0], (h/2)-(sz9[1]/2), null); // Cyanide

		return mol;
	}
}

class ppn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz12 = sz[12];
		int w = 2*sz10[0]+sz12[0];
		int h = Math.max(sz10[1], sz12[1]);
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, 0, null); // Left Methyl
		g.drawImage(gly[12], sz10[0], 0, null); // Middle Methyl
		g.drawImage(gly[10], sz10[0]+sz12[0], 0, null); // Right Methyl

		return mol;
	}
}

class ppne extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz10 = sz[10]; int[] sz11 = sz[11];
		int w = sz10[0]+sz11[0]+sz0[0];
		int h = ((sz10[0]+sz0[0])/2)+sz11[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, 0, null); // Top Left Methyl
		g.drawImage(gly[0], sz10[0]-sz0[0], h-sz0[1], null); // Bot Left H
		g.drawImage(gly[11], sz10[0], sz10[1]/2, null); // Center Cs
		g.drawImage(gly[0], w-sz0[0], (sz10[1]-sz0[1])/2, null); // Top Right H
		g.drawImage(gly[0], w-sz0[0], h-sz0[1], null); // Bot Right H

		return mol;
	}
}

class clppn1 extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz2 = sz[2];
		int[] sz10 = sz[10]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz1[0]+sz2[0];
		int h = 2*(sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[0], sz10[0]+(sz1[0]/2)-(sz0[0]/2)+sz12[0], 0, null); // Top H
		g.drawImage(gly[1], sz10[0]+sz12[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], sz10[0]+(sz1[0]/2)-(sz0[0]/2)+sz12[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[2], sz10[0]+sz12[0]+sz1[0], (h/2)-(sz2[1]/2), null); // Chloride

		return mol;
	}
}

class ppn1ol extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz3 = sz[3];
		int[] sz10 = sz[10]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz1[0]+sz3[0];
		int h = 2*(sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[0], sz10[0]+(sz1[0]/2)-(sz0[0]/2)+sz12[0], 0, null); // Top H
		g.drawImage(gly[1], sz10[0]+sz12[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], sz10[0]+(sz1[0]/2)-(sz0[0]/2)+sz12[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[3], sz10[0]+sz12[0]+sz1[0], (h/2)-(sz3[1]/2), null); // Hydroxide

		return mol;
	}
}

class clppn2 extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz2 = sz[2]; int[] sz10 = sz[10];
		int w = (2*sz10[0])+sz1[0];
		int h = sz0[1]+sz1[1]+sz2[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[2], (sz1[0]/2)-(sz2[0]/2)+sz10[0], 0, null); // Top Chloride
		g.drawImage(gly[1], sz10[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[10], sz10[0]+sz1[0], (h/2)-(sz2[1]/2), null); // Right Methyl

		return mol;
	}
}

class ppn2ol extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz3 = sz[3]; int[] sz10 = sz[10];
		int w = (2*sz10[0])+sz1[0];
		int h = sz0[1]+sz1[1]+sz3[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[3], (sz1[0]/2)-(sz3[0]/2)+sz10[0], 0, null); // Top Hydroxide
		g.drawImage(gly[1], sz10[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], (sz1[0]/2)-(sz0[0]/2)+sz10[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[10], sz10[0]+sz1[0], (h/2)-(sz3[1]/2), null); // Right Methyl

		return mol;
	}
}

class pplamn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz0 = sz[0]; int[] sz1 = sz[1]; int[] sz4 = sz[4];
		int[] sz10 = sz[10]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz1[0]+sz4[0];
		int h = 2*(sz0[1])+sz1[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[0], sz10[0]+(sz1[0]/2)-(sz0[0]/2)+sz12[0], 0, null); // Top H
		g.drawImage(gly[1], sz10[0]+sz12[0], (h/2)-(sz1[1]/2), null); // Carbon
		g.drawImage(gly[0], sz10[0]+(sz1[0]/2)-(sz0[0]/2)+sz12[0], h-sz0[1], null); // Bot H
		g.drawImage(gly[4], sz10[0]+sz12[0]+sz1[0], (h/2)-(sz4[1]/2), null); // Amine

		return mol;
	}
}

class ppnal extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz5 = sz[5]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz5[0];
		int h = sz5[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[5], sz10[0]+sz12[0], 0, null); // Aldehyde

		return mol;
	}
}

class ppnone extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz13 = sz[13];
		int w = 2*sz10[0]+sz13[0];
		int h = sz13[1]+(sz10[1]-sz[0][1]); // + Kerning corrective factor
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, h-sz10[1], null); // Left Methyl
		g.drawImage(gly[13], sz10[0], 0, null); // Ketone
		g.drawImage(gly[10], sz10[0]+sz13[0], h-sz10[1], null); // Right Methyl

		return mol;
	}
}

class ppnc extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz6 = sz[6]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz6[0];
		int h = sz6[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[6], sz10[0]+sz12[0], 0, null); // Carboxyl

		return mol;
	}
}

class ppnlcld extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz7 = sz[7]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz7[0];
		int h = sz7[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[7], sz10[0]+sz12[0], 0, null); // Acyl Chloride

		return mol;
	}
}

class ppnamd extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz8 = sz[8]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz8[0];
		int h = sz8[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[8], sz10[0]+sz12[0], 0, null); // Amide

		return mol;
	}
}

class etlcn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz9 = sz[9]; int[] sz12 = sz[12];
		int w = sz10[0]+sz12[0]+sz9[0];
		int h = sz12[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Center Methylene
		g.drawImage(gly[9], sz10[0]+sz12[0], (h/2)-(sz9[1]/2), null); // Cyanide

		return mol;
	}
}

class pplcn extends Hydrocarbon {
	public BufferedImage draw(int n)
	{
		int[] sz10 = sz[10]; int[] sz9 = sz[9]; int[] sz12 = sz[12];
		int w = sz10[0]+(2*sz12[0])+sz9[0];
		int h = sz12[1];
		size[n][0] = w; size[n][1] = h;

		BufferedImage mol = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)mol.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);

		g.drawImage(gly[10], 0, (h/2)-(sz10[1]/2), null); // Left Methyl
		g.drawImage(gly[12], sz10[0], (h/2)-(sz10[1]/2), null); // Left Methylene
		g.drawImage(gly[12], sz10[0]+sz12[0], (h/2)-(sz10[1]/2), null); // Right Methylene
		g.drawImage(gly[9], sz10[0]+(2*sz12[0]), (h/2)-(sz9[1]/2), null); // Cyanide

		return mol;
	}
}

