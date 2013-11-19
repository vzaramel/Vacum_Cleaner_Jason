package env;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.Random;
import java.util.logging.Logger;


//import CleanerWorld.Move;


public class WorldModel extends GridWorldModel {
    	
    	Random random = new Random(System.currentTimeMillis());
    	//private int ID;
    	
    	static Logger logger = Logger.getLogger(WorldModel.class.getName());
    	
    	public static final String AgName = "cleaner";
    	
    	public static final int N = 10; // grid size
    	public static final float P = 0.001f; // propabilidade de ter empecilho
    	public static final int S = 1; // Fator de vizinhança
    	public static int T = 0; //Tempo
    	
    	public static int [] num_actions_ag;
    	public static int COLLECTED = 0;
    	
    	//Tem que sem potencia de 2
    	public static enum Objetos{
    		PO(8), LIQUIDO(16), SOLIDO(32);
    		
    		int value;
    		Objetos(int value){ this.value = value; }
    		public static int getIndex(int value){
    			for( Objetos obj: Objetos.values())
    			{
    				if(obj.value == value)
    				{
    					return obj.ordinal();
    				}
    			}
    			return 0;
    		}
    		@Override
			public String toString(){
				return this.name().toLowerCase();
			}
    	}
    	
    
    	
    	public static enum Ferramentas{
			ASPIRADOR, PANO, VASSOURA;
			
			@Override
			public String toString(){
				return this.name().toLowerCase();
			}
    	};
    	
    	public enum Move {
            UP, DOWN, RIGHT, LEFT
        };

        
        protected WorldModel() {
        	
			super(N, N, 2);
			try {
					num_actions_ag = new int[getNbOfAgs()];
					
					setAgPos(0, 0, 0);
	                constructWorld();
	                
            } catch (Exception e) {
                e.printStackTrace();
            }
			// TODO Auto-generated constructor stub
		}
        public void constructWorld()
        {
        	updateWorld();
        }
        public void updateWorld()
        {
        	for ( int i =0; i < N; i++)
        	{
        		for ( int j = 0; j < N; j++)
        		{
        			for ( Objetos emp : Objetos.values())
        			{
        				
	        			float probabilidade = P;
	        			Location loc = new Location(i,j);
	        			
	        			if ( random.nextFloat() < probabilidade)
	        			{
	        				logger.info("Empecilho: " + emp + "  Ordinal: "+emp.value+" Adicionado em (" + loc + ")");
	        				add(emp.value, loc);
	        			
	        			}
        			}
        		
        		}
        	}
        }
        
        
        synchronized public void move(Move dir, int ag) throws Exception {
            Location loc = getAgPos(ag);
            num_actions_ag[ag]++;
            switch(dir){
	            case UP:
	            		loc.y--;
	            	break;
	            case DOWN:
	            		loc.y++;
	            	break;
	            case LEFT:
	            		loc.x--;
	            	break;
	            case RIGHT:
	            		loc.x++;
	            	break;
	            	
            }
            if ( inGrid(loc))
            {
            	logger.info("InGrid "+ loc);
            	setAgPos(ag, loc);
            }
        }
        
        
        synchronized public void clean(int toolIndex, int ag) {
        	Location agLoc = getAgPos(ag);
        	int obj = Objetos.values()[toolIndex].value;
        	
        	num_actions_ag[ag]++;
        	if ( hasObject(obj, agLoc) )
			{
				COLLECTED++;
				logger.info("Removing Empecilho Codigo: "+ Objetos.values()[toolIndex]);
				remove(obj,agLoc);
		    }
			
               
        }
        
    	public void setCycle()
    	{
    		updateWorld();
    	
    	}
    }