// Internal action code for project vacuum_cleaner

package alg;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;


import env.WorldModel;

public class next_move extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
    	
        
        ts.getAg().getLogger().info("executing internal action 'alg.next_move'");
        if (true) { // just to show how to throw another kind of exception
            throw new JasonException("not implemented!");
        }
        
        // everything ok, so returns true
        return true;
    }
}
