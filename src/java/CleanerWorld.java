// Environment code for project vacuum_cleaner

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.TimeSteppedEnvironment.OverActionsPolicy;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;
import jason.stdlib.foreach;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.JOptionPane;


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
		 super.init(new String[] { "5000" } ); // set step timeout 
		 
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
        logger.info(ag+" doing: "+ action);
        
     // get the agent id based on its name
        int agId = getAgIdBasedOnName(ag);

        logger.info(ag+" ID: "+ agId);
        try {
            if (action.equals(up)) {
                model.move(WorldModel.Move.UP, agId);
            } 
            else if (action.equals(down)) {
                model.move(WorldModel.Move.DOWN, agId);
            } 
            if (action.equals(left)) {
                model.move(WorldModel.Move.LEFT, agId);
            } 
            if (action.equals(right)) {
                model.move(WorldModel.Move.RIGHT, agId);
            }
            else if (action.getFunctor().equals("clean")) {
            	String toolS = action.getTerm(0).toString();
            	int tool = WorldModel.translator.LiteralToInt(toolS);
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
    }
    private void updateAgPercept(String agName, int x, int y) {
        if (model == null || !model.inGrid(x,y)) return;
        for( int empecilho : WorldModel.EE){
	        if (model.hasObject(empecilho, x, y)) {
	        	addPercept(agName,createCellPerception(x, y, empecilho));
	        }
        }
       
    }
    
    public static Literal createCellPerception(int x, int y, int empCode) {
    	String empStr = WorldModel.translator.IntToLiteral(empCode);
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

