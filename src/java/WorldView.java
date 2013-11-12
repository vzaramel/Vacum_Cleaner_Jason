import jason.environment.grid.GridWorldView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class WorldView extends GridWorldView {

	
		CleanerWorld env = null;
	    
	    JLabel     jCycle;
	    JLabel 	   jCollected;
	    JLabel     jEfficiency;
	    JLabel     jAction;
	
	    
        public WorldView(WorldModel model) {
            super(model, "Mars World", 600);
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
            setVisible(true);
            repaint();
        }
        
        @Override
        public void initComponents(int width) {
            super.initComponents(width);
            
            
            JPanel msg = new JPanel();
            msg.setLayout(new BoxLayout(msg, BoxLayout.Y_AXIS));
            msg.setBorder(BorderFactory.createEtchedBorder());
            
            
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
            p.add(new JLabel("Cycle:"));
            jCycle = new JLabel("0");
            p.add(jCycle);
            p.add(new JLabel("   Actions: "));
            jAction = new JLabel("0");
            p.add(jAction);
            p.add(new JLabel("   Coletados: "));
            jCollected = new JLabel("0");
            p.add(jCollected);
            p.add(new JLabel("   Efficiencia(Coletado/A›es): "));
            jEfficiency = new JLabel("0");
            p.add(jEfficiency);
           
            msg.add(p);
            
            JPanel s = new JPanel(new BorderLayout());
            s.add(BorderLayout.CENTER, msg);
            getContentPane().add(BorderLayout.SOUTH, s);        

            getCanvas().addMouseListener(new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    int col = e.getX() / cellSizeW;
                    int lin = e.getY() / cellSizeH;
                    if (col >= 0 && lin >= 0 && col < getModel().getWidth() && lin < getModel().getHeight()) {
                        WorldModel wm = (WorldModel)model;
                        wm.add(WorldModel.PO, col, lin);
                        //wm.setInitialNbGolds(wm.getInitialNbGolds()+1);
                        update(col, lin);
                    }
                }
                public void mouseExited(MouseEvent e) {}
                public void mouseEntered(MouseEvent e) {}
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
            });
        }
        /** draw application objects */
        @Override
        public void draw(Graphics g, int x, int y, int object) {
        	
            switch (object) {
                case WorldModel.PO: 
                	drawGarb(g, x, y);  
                	break;
            }
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
            String label = "R"+(id+1);
            c = Color.blue;
    
            if (id == 0) {
                c = Color.yellow;
            }
            super.drawAgent(g, x, y, c, -1);
            if (id == 0) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);                
            }
            super.drawString(g, x, y, defaultFont, label);
        }

        public void drawGarb(Graphics g, int x, int y) {
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "G");
        }
        
        public void setCycle(int c) {
            if (jCycle != null) {
                WorldModel wm = (WorldModel)model;
                jCycle.setText(""+c);
                jCollected.setText(""+ WorldModel.COLLECTED);
                jAction.setText(""+WorldModel.num_actions_ag[0]);
                jEfficiency.setText(""+ ((float)WorldModel.num_actions_ag[0])/((float)c));
                
            }
        }

    }    