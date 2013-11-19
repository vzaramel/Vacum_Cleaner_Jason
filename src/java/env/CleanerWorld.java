package env;
// Environment code for project vacuum_cleaner

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

import jason.environment.grid.Location;

import java.util.logging.Logger;


import env.WorldModel.Ferramentas;
import env.WorldModel.Objetos;


public class CleanerWorld extends TimeSteppedEnvironment {

	
    public static final Term 	up = Literal.parseLiteral("move(up)");
    public static final Term 	down = Literal.parseLiteral("move(down)");
    public static final Term 	left = Literal.parseLiteral("move(left)");
    public static final Term 	right = Literal.parseLiteral("move(right)");
    public static final Term    clean = Literal.parseLiteral("clean(tool)");
  
    static Logger logger = Logger.getLogger(CleanerWorld.class.getName());

    private WorldModel model;
    private WorldView  view;
    
    //public static final Atom aPo = new Atom("po");
    //public static final Atom aAspirador = new Atom("aspirador");
    
    @Override
    public void init(String[] args) {
    	setOverActionsPolicy(OverActionsPolicy.ignoreSecond);

        // get the parameters
        setSleep(100);
        model = new WorldModel();
        view  = new WorldView(model);
        model.setView(view);
        
        
        initWorld();
        
        //updateAgsPercept();

        //informAgsEnvironmentChanged();
    }

//    @Override
//    protected void updateNumberOfAgents() {
//        setNbAgs(model.getNbOfAgs());
//    }
    
    @Override
    public void stop() {
        super.stop();
    }
    
    private void initWorld() {
    	
    	/******************************************************************/ 
		
    	//Comando necessário para funcionameto
    	//Se retirar isso o programa gera uma nullPointer na inicialização
		 super.init(new String[] { "1000" } ); // set step timeout 
		 
		/******************************************************************/ 
		 updateNumberOfAgents();
		 
		 // add perception for all agents
		 clearPercepts();
		 addPercept(Literal.parseLiteral("grid_size("+ WorldModel.N +")"));

    	//addPercept(Literal.parseLiteral("prob_emp("+ WorldModel.P));
		 updateAgsPercept();
	}

	@Override
    public boolean executeAction(String ag, Structure action) {
        //logger.info(ag+" doing: "+ action);
        
     // get the agent id based on its name
        int agId = getAgIdBasedOnName(ag);

        logger.info(ag+" ID: "+ agId);
        try {
            if (action.equals(up)) {
            	logger.info(ag+" doing: "+ action);
                model.move(WorldModel.Move.UP, agId);
            } 
            else if (action.equals(down)) {
            	logger.info(ag+" doing: "+ action);
                model.move(WorldModel.Move.DOWN, agId);
            } 
            else if (action.equals(left)) {
            	logger.info(ag+" doing: "+ action);
                model.move(WorldModel.Move.LEFT, agId);
            } 
            else if (action.equals(right)) {
            	logger.info(ag+" doing: "+ action);
                model.move(WorldModel.Move.RIGHT, agId);
            }
            else if (action.getFunctor().equals("clean")) {
            	logger.info(ag+" doing: "+ action);
            	String toolS = action.getTerm(0).toString();
            	int tool = Ferramentas.valueOf(toolS.toUpperCase()).ordinal();
            	//int tool = WorldModel.translator.LiteralToInt(toolS);
            	model.clean(tool, agId);
            } else {
                return false;
            }
        } catch (Exception e) {
        	logger.log(Level.SEVERE, "error executing " + action + " for " + ag + " (ag code:"+agId+")", e);
        }
        
        updateAgsPercept();
        
        return true;
    }
    private int getAgIdBasedOnName(String agName) {
        return (Integer.parseInt(agName.substring(7))) - 1;
    }
    
    @Override
    public void updateAgsPercept() {
    	logger.info("Number of Agents: " +model.getNbOfAgs());
        for (int i = 0; i < model.getNbOfAgs()-1; i++) {
            updateAgPercept(i);
        }
    }

    private void updateAgPercept(int ag) {
        updateAgPercept(getAgNameFromID(ag+1), ag);
    }

    private void updateAgPercept(String agName, int ag) {
        clearPercepts(agName);
        // its location
        Location l = model.getAgPos(ag);
        logger.info(agName+" location: "+ l);
        
        Literal p = ASSyntax.createLiteral("pos", 
                ASSyntax.createNumber(l.x), 
                ASSyntax.createNumber(l.y), 
                ASSyntax.createNumber(getStep()));
        
        
        addPercept(agName, p);

        updateAgPercept(agName, l.x, l.y);
        updateAgPercept(agName, l.x+1, l.y);
        updateAgPercept(agName, l.x-1, l.y);
        updateAgPercept(agName, l.x, l.y+1);
        updateAgPercept(agName, l.x, l.y-1);
    }
    private void updateAgPercept(String agName, int x, int y) {
        if (model == null || !model.inGrid(x,y)) return;
        for( Objetos empecilho : WorldModel.Objetos.values()){
        	if (model.hasObject(empecilho.value, x, y)) {
	        	addPercept(agName,createCellPerception(x, y, empecilho.toString()));
	        }
        }
       
    }
    
    public static Literal createCellPerception(int x, int y, String empStr) {
    	return ASSyntax.createLiteral("cell",
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y),
                ASSyntax.createAtom(empStr)); 
    }
    
    
    public String getAgNameFromID(int id) {
        return WorldModel.AgName + id;
    }
    
    @Override
    protected void stepStarted(int step) {
    	if ( model!= null) model.setCycle();
        if (view != null) view.setCycle(getStep());
    }
    
    private long sum = 0;
    
    @Override
    protected void stepFinished(int step, long time, boolean timeout) {
        if (step == 0) {
            sum = 0;
            return;
        }

        sum += time;
        logger.info("Cycle "+step+" finished in "+time+" ms, mean is "+(sum/step)+".");
        
        // test end of match
        try {
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
}

