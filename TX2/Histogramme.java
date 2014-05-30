import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Histogramme extends JPanel {
private double[] courbe=null;
	public Histogramme(double[] cb) {
		courbe=cb;
		setSize(300, 120);
		setVisible(true);
	}
	
	public Histogramme() {
		
		setSize(100, 120);
		setVisible(true);
	}

	

	public void paint(Graphics g) {
		if(courbe!=null)
		{
			double max=0;
			for(int i=0;i<courbe.length;i++)if(max<courbe[i])max=courbe[i];
			
			for (int x1 = 0; (x1*20) < getSize().width && x1<courbe.length; x1 += 1) {
			int x=x1*20;
			double pos =courbe[x1]-95;
			
			if(pos<0)pos=0;
			pos=pos*20;
			//System.out.println(courbe[x1]+" : "+pos);
			if(courbe[x1]==max)g.setColor(Color.red);
			g.drawLine(x, getSize().height-20, x, (int) (getSize().height-pos-20));
			g.drawString(Integer.toString(x1+1), x-2,getSize().height);
			g.setColor(Color.black);
		}
		}
		
	}
	
	public void setcourbe(double[] cb)
	{
		courbe=cb;
	}
}

