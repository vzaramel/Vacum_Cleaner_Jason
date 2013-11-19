package env;
import jason.environment.grid.GridWorldView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sun.java2d.pipe.DrawImage;

import env.WorldModel.Objetos;



public class WorldView extends GridWorldView {

	
		CleanerWorld env = null;
	    
	    JLabel     jCycle;
	    JLabel 	   jCollected;
	    JLabel     jEfficiency;
	    JLabel     jAction;
	    
		private BufferedImage po;
		private BufferedImage liquido;
		private BufferedImage solido;
		private BufferedImage android;
		
		static Logger logger = Logger.getLogger(WorldView.class.getName());
		
        public WorldView(WorldModel model) {
            super(model, "Mars World", 600);
            
            try {
    			solido =  ImageIO.read(new File("img" + File.separator + "solido.png"));
    			po =      ImageIO.read(new File("img" + File.separator + "po.png"));
    			liquido = ImageIO.read(new File("img" + File.separator + "liquido.png"));
    			android = ImageIO.read(new File("img" + File.separator + "android.png"));
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
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
                        wm.add(Objetos.PO.value, col, lin);
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
        	  
        	int objIndex = Objetos.getIndex(object);
        	switch (Objetos.values()[objIndex]) {
                case PO: 
                	drawObjeto(g, x, y, po);  
                	break;
				case LIQUIDO:
					drawObjeto(g, x, y, liquido);  
					break;
				case SOLIDO:
					drawObjeto(g, x, y, solido);  
					break;
				default:
					break;
            }
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
            String label = "R"+(id+1);
            g.drawImage(android, x*cellSizeW, y*cellSizeH, cellSizeW, cellSizeH, null);
            super.drawString(g, x, y, defaultFont, label);
        }

        public void drawObjeto(Graphics g, int x, int y, BufferedImage obj) {
        	g.drawImage(obj, x*cellSizeW, y*cellSizeH, cellSizeW, cellSizeH, null);
        }
        
        public void setCycle(int c) {
            if (jCycle != null) {
                jCycle.setText(""+c);
                jCollected.setText(""+ WorldModel.COLLECTED);
                int actions = 0;
                for ( int nAct : WorldModel.num_actions_ag){
                	actions += nAct;
                }
                jAction.setText(""+actions);
                jEfficiency.setText(""+ ((float)WorldModel.COLLECTED)/((float)actions));
                
            }
        }

    }    