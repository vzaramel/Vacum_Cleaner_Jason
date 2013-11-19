// Agent cleaner in project vacuum_cleaner

/* Initial beliefs */
//robot(r1).
direction(up).
direction(down).
direction(left).
direction(right).


//visitado(X,Y) :- pos(X,Y,T).

//Conjunto de ferramentas do agente
tool(aspirador).

//empecilho(E) :- cell(_,_,E,_).

/* Initial goal */
!clean_world. 

/* Plans */

+!clean_world  
   <- 
   	?pos(X,Y,T);
   	-+step(T);
   	!check_pos(X,Y);
   	+visitado(X, Y, T);
   	//alg.next_move(X, Y, Direction);
   	//move(Direction);
   	!next_move(X, Y);
   	!!clean_world.

	
+!next_move(X, Y) : grid_size(N) &  X < N-1  & not visitado(X+1, Y, _)
	<- move(right).
	
+!next_move(X, Y) : grid_size(N) & Y < N-1   & not visitado(X, Y+1, _)
	<- move(down).
	
+!next_move(X, Y) : X > 0 & not visitado(X-1, Y, _)
	<- move(left).

+!next_move(X, Y) : Y > 0  & not visitado(X, Y-1, _)
	<- move(up).

//+!next_move(X, Y).

//-!next_move(X, Y).
	

	
+!check_pos(X,Y) : cell( X, Y, E) & not visitado(X, Y, _)
	<- 
		?step(T);
		+cell(X, Y, E, T);
		!limpa(X,Y,E);
		.
+!check_pos(X,Y).
		
+!limpa(X,Y,E) : limpa(F,E)
	<-
		?tool(F);
		clean(F).
		
+!limpa(X,Y,E) : ~limpa(F, E)
	<- 
		.print("N‹o tenho ferramenta para limpar ", E).

+!limpa(X,Y,E) : not limpa(F, E) & not ~limpa(F,E) & tool(F)
	<- 
		clean(F);
		!verifica_cell(X,Y,F,E).
		
+!verifica_cell(X,Y,F,E) : not cel(X,Y,E)
	<-
		+limpa(F,E).
		
+!verifica_cell(X,Y,F,E) : cel(X,Y,E)
	<-
		+~limpa(F,E).


//+pos(X,Y,T) <- 
//	+visitado(X,Y).

//+cell(X, Y, E) <- +empecilho(E).

