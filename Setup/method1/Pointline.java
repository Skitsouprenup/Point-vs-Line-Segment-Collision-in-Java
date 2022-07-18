import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.EventQueue;

public class Pointline {
	
	//mouse-controlled line endpoints
	Point2D mouse_point;
	//ellipse representing the mouse pointer point.
	Ellipse2D mouse_dot;
	//stationary line endpoints
	Point2D ln1_endpoint1,ln1_endpoint2;
	//distance between two endpoints of ln1
	float ln1_dst;
	
	boolean isColliding;
	
	public static void main(String[] args) {
		new Pointline();
	}
	
	public Pointline() {
		
		mouse_point = new Point2D.Float();
		
		ln1_endpoint1 = new Point2D.Float(100f,150f);
		ln1_endpoint2 = new Point2D.Float(260f,180f);
		
		mouse_dot = new Ellipse2D.Float();
		
		//get the x and y distance of the two endpoints of the stationary line segment
		float ln1_dstx = (float)(ln1_endpoint2.getX() - ln1_endpoint1.getX());
		float ln1_dsty = (float)(ln1_endpoint2.getY() - ln1_endpoint1.getY());
		ln1_dst = (float)Math.sqrt(ln1_dstx * ln1_dstx + ln1_dsty * ln1_dsty);
		
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				JFrame jf = new JFrame("Pointline");
				Panel pnl = new Panel();
				pnl.addMouseMotionListener(new MouseMotion());
				jf.add(pnl);
				jf.pack();
				jf.setResizable(false);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setLocationRelativeTo(null);
				jf.setVisible(true);
			}
			
		});
		
	}
	
	void updateData(){
		//System.out.println("updating...");
	}
	
	void drawObjects(Graphics2D g2d){
		//System.out.println("drawing objects...");
		
		g2d.setPaint(Color.GREEN);
		g2d.drawLine((int)ln1_endpoint1.getX(),(int)ln1_endpoint1.getY(),
					(int)ln1_endpoint2.getX(),(int)ln1_endpoint2.getY());	
		
		if(isColliding) g2d.setPaint(Color.RED);
		else g2d.setPaint(Color.YELLOW);
		
		mouse_dot.setFrame(mouse_point.getX()-3f,mouse_point.getY()-3f,6f,6f);
		g2d.fill(mouse_dot);
	}
	
	class Panel extends JPanel {
		
		Panel(){
			Timer timer = new Timer(16, new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent e){
					updateData();
					repaint();
				}
			});
			timer.start();
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(400,400);
		}
		
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setPaint(Color.BLACK);
			g2d.fillRect(0,0,getWidth(),getHeight());
			drawObjects(g2d);
			g2d.setPaint(Color.WHITE);
			g2d.drawString("mouse point will turn red if there's a collision", 60f, 20f);
			g2d.dispose();
		}
	}
	
	class MouseMotion implements MouseMotionListener {
	
		@Override
		public void mouseDragged(MouseEvent e){}
	
		@Override
		public void mouseMoved(MouseEvent e){
			mouse_point.setLocation(e.getX(),e.getY());
		}
	}
	
}

