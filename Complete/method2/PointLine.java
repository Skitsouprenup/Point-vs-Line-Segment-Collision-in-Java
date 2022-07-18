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

public class PointLine {
	
	//mouse-controlled line endpoints
	Point2D mouse_point;
	//ellipse representing the mouse pointer point.
	Ellipse2D mouse_dot;
	//stationary line endpoints
	Point2D ln1_endpoint1,ln1_endpoint2;
	//intersectionPoint
	Ellipse2D intersectionPoint;
	
	boolean isColliding;
	
	public static void main(String[] args) {
		new PointLine();
	}
	
	public PointLine() {
		
		mouse_point = new Point2D.Float();
		
		ln1_endpoint1 = new Point2D.Float(100f,150f);
		ln1_endpoint2 = new Point2D.Float(260f,180f);
		
		mouse_dot = new Ellipse2D.Float();
		intersectionPoint = new Ellipse2D.Float();
		
		EventQueue.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				JFrame jf = new JFrame("PointLine");
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
		mouse_dot.setFrame(mouse_point.getX()-3f,mouse_point.getY()-3f,6f,6f);
		intersectionPoint.setFrame(0,0,0,0);
		
		//ln1
		float x1 = (float)ln1_endpoint1.getX();
		float y1 = (float)ln1_endpoint1.getY();
		float x2 = (float)ln1_endpoint2.getX();
		float y2 = (float)ln1_endpoint2.getY();
		
		//mouse point
		float x3 = (float)mouse_point.getX();
		float y3 = (float)mouse_point.getY();
		
		//get the ratio of the slope of ln1 endpoints
		float snum = 0f; float sdenom = 0f;
		snum = y2 - y1;
		//reverse the sign of sdenom
		sdenom = x1 - x2;
		//A and B coefficients
		float a1 = snum;
		float b1 = sdenom;
		
		//line2(perpendicular to line1)
		//the slope of a line that is perpendicular to another line is -1/m
		float a2 = -1 * sdenom;
		float b2 = snum;
		
		//find C by using the standard form of linear equation
		//and using one of the given points
		float c1 = a1*x1 + b1*y1;
		float c2 = a2*x3 + b2*y3;
		
		//We need to use the cramer's rule to solve two lines that have a solution,
		//no solution, or infinitely many solutions. There are many ways to find
		//a solution for systems of linear equations like elimination or substitution
		//but cramer's rule is simpler to implement in a script. To do cramer's rule,
		//Our line equation should be in standard form.
		
		//Steps for cramer's rule:
		//#1 find the determinant of the coefficients of the standard line equation(A and B)
		float cDet = a1*b2 - a2*b1;
		
		//check if the determinant of the coefficients are zero. If so, then the lines are either
		//parallel or the same. Otherwise, there's a solution.
		if(cDet != 0) {
			
		  //#2 substitute C's coefficients to X's coefficient to find x determinant and substitute
		  //C's coefficients to Y's coefficient to find y determinant.
		  float xDet = (c1*b2 - c2*b1)/cDet;
		  float yDet = (a1*c2 - a2*c1)/cDet;
		  
		  
		  isColliding = false;
		  if(( xDet >= Math.min(x1,x2) && xDet <= Math.max(x1,x2) ) &&
		     ( yDet >= Math.min(y1,y2) && yDet <= Math.max(y1,y2) )){
			   
			   intersectionPoint.setFrame(xDet - 3f, yDet - 3f, 6f, 6f);
			   float offset = 1.7f;
			   if((xDet >= x3-offset && xDet <= x3+offset) &&
   		          (yDet >= y3-offset && yDet <= y3+offset)) isColliding = true; 
			 }
		}
	}
	
	void drawObjects(Graphics2D g2d){
		//System.out.println("drawing objects...");
		g2d.setPaint(Color.GREEN);
		g2d.drawLine((int)ln1_endpoint1.getX(),(int)ln1_endpoint1.getY(),
					(int)ln1_endpoint2.getX(),(int)ln1_endpoint2.getY());	
		
		g2d.setPaint(Color.YELLOW);
		g2d.fill(intersectionPoint);
		
		if(isColliding) g2d.setPaint(Color.RED);
		else g2d.setPaint(Color.YELLOW);
		
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

