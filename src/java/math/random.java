// Internal action code for project vacuum_cleaner

package math;

import java.util.Iterator;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class random extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
    	try{
    		
	    	if (!args[1].isVar()) {
	            throw new JasonException("The first argument of the internal action 'random' is not a variable.");                
	        }
	        if (!args[0].isNumeric()) {
	            throw new JasonException("The second argument of the internal action 'random' is not a number.");                
	        }
	        final int max = (int)((NumberTerm)args[0]).solve();
	        
	        int rand = (int) (Math.random()*max);
	        return un.unifies(args[1], new NumberTermImpl(rand));
	    } catch (ArrayIndexOutOfBoundsException e) {
	        throw new JasonException("The internal action 'random' has not received the required argument.");
	    } catch (Exception e) {
	        throw new JasonException("Error in internal action 'random': " + e, e);
	    }  
        
    }
}
