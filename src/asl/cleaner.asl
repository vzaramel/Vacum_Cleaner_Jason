// Agent cleaner in project vacuum_cleaner

/* Initial beliefs */
//robot(r1).
direction(up).
direction(down).
direction(left).
direction(right).


//visitado(X,Y) :- pos(X,Y).

//Conjunto de ferramentas do agente
tool(aspirador).

//Conjunto de empecilhos que o agente conhece
//empecilho(po).

empecilho(E) :- cell(X, Y, E) & pos(X, Y).

//limpa(aspirador, po).

/* Initial goal */
!check. 

/* Plans */

+!check 
   <- 
   	!check.
   	
+!check.



