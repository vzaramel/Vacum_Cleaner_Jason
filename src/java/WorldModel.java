import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import apple.awt.CFileDialog;

//import CleanerWorld.Move;


public class WorldModel extends GridWorldModel {
    	
    	Random random = new Random(System.currentTimeMillis());
    	//private int ID;
    	
    	static Logger logger = Logger.getLogger(WorldModel.class.getName());
    	
    	public static final String AgName = "cleaner";
    	
    	public static final int N = 5 ; // grid size
    	public static final float P = 0.1f; // propabilidade de ter empecilho
    	public static final float S = 1.5f; // Fator de vizinhança
    	public static int T = 0; //Tempo
    	
    	public static int [] num_actions_ag;
    	public static int COLLECTED = 0;
    	
    	//Descric‹o de Empecilhos
    	public static final int PO = 5;
    	public static final int[] EE = {
    			PO
    	};
    	
    	//Descri‹o de ferramentas
    	public static final int ASPIRADOR = 4;
    	public static final int[] CF = {
    			ASPIRADOR
    	};
    	
    	//Descri‹o de qual ferramenta limpa que tipo de empecilho
    	public static final boolean[][] limpa = { 
    		{ true }// (0,0) -> ( ASPIRADOR, PO)
    	};
    	
    	//Objeto para traduzir de Integer para String e vice versa os literais relacionados a ferramentas
    	//e empecilhos
    	public static TwoWayHashMap translator = new TwoWayHashMap();
    			
    	
    	public enum Move {
            UP, DOWN, RIGHT, LEFT
        };

        
        protected WorldModel() {
        	
			super(N, N, 2);
			try {
					translator.add("aspirador", ASPIRADOR);
					translator.add("po", PO);
					
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
        	for ( int i =0; i < N; i++)
        	{
        		for ( int j = 0; j < N; j++)
        		{
        			for ( int emp : EE)
        			{
	        			float probabilidade = P;
	        			Location loc = new Location(i,j);
	        			Location [] vizinhanca = { 
	        					new Location( i - 1, j),
	        					new Location( i + 1, j),
	        					new Location( i , j - 1),
	        					new Location( i , j + 1)
	        			};
	        			for( Location locI : vizinhanca)
	        			{
	        				if( inGrid(locI) && hasObject(emp, locI))
	        				{
	        					//Atualiza probabiliddade baseado na vizinhana
	        					probabilidade *= S;
	        				}
	        			}
	        			logger.info("Probabilidade: "+ probabilidade);
        				
	        			if ( random.nextFloat() < probabilidade)
	        			{
	        				logger.info("Empecilho: " + translator.IntToLiteral(emp) + " Adicionado em (" + loc + ")");
	        				add(emp, loc);
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
            	setAgPos(ag, loc);
            }
        }
        
        
        synchronized public void clean(int tool, int ag) {
        	Location agLoc = getAgPos(ag);
        	int toolIndex = 0;
        	num_actions_ag[ag]++;
        	//Encontra o index da ferramenta pelo c—digo dela
        	for( int index =0; index< CF.length; index++)
        	{
        		if ( CF[index] == tool)
        			toolIndex = index;
        	}
        	//Itera sob todos os empecilhos fixando uma ferramenta espec’fica
        	for(int i =0; i < limpa[toolIndex].length; i++ )
        	{
        		if( limpa[toolIndex][i])
        		{
        			COLLECTED++;
        			logger.info("Removing Empecilho Codigo: "+ EE[i]);
        			remove(EE[i],agLoc);
        		}
        	}
               
        }
        
    }