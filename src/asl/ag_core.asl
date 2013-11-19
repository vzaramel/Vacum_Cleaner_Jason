
/* Plans */

+!clean_world  
   <- 
   	?pos(X,Y);
   	!check; /* Olha o quadrado e se tiver coisa tenta limpar */
   	!check_local;
   	!check_ponto_objetivo;
   	!next_move(X, Y);
   	-+pos_anterior(X, Y);
   	!!clean_world.

+!next_move(X, Y) : grid_size(N) &  X < N-1  & ponto_objetivo_sel(O_X,_) & X < O_X
	<- move(right);
		-+ultima_direcao(right).
	
+!next_move(X, Y) : grid_size(N) & Y < N-1   & ponto_objetivo_sel(_,O_Y) & Y < O_Y
	<- move(down);
		-+ultima_direcao(down).
	
+!next_move(X, Y) : X > 0 &  ponto_objetivo_sel(O_X,_) & X > O_X
	<- move(left);
		-+ultima_direcao(left).

+!next_move(X, Y) : Y > 0  & ponto_objetivo_sel(_,O_Y) & Y > O_Y
	<- move(up);
		-+ultima_direcao(up).
		
		
		
+!check : cell( X, Y, _) & pos(X, Y)
	<- 
		!limpa(X,Y);
		-cell_suja(X,Y,E).
+!check.	
		
+!check_local : cell_suja( X, Y, E) & not pos(X, Y) & tool(F) & not ~limpa(F,E)
	<- 
		-+ponto_objetivo_local(X, Y).
+!check_local.
		
+cell(X,Y,E)[source(percept)] : true <- +cell_suja(X,Y,E).
+cell(X,Y,E)[source(Name)] : true 
		<- -cell(X,Y,E)[soruce(Name)];
		   +cell_suja(X,Y,E).
	
	
+!check_ponto_objetivo : ponto_objetivo(X,Y) & pos (X,Y) 
	<- 
		!escolher_outro_ponto_obj;
		!check_ponto_objetivo.
			
+!check_ponto_objetivo : ponto_objetivo_local(X, Y) & not pos (X,Y)
	<- 
		-+ponto_objetivo_sel(X,Y).

+!check_ponto_objetivo : ponto_objetivo_local(X, Y) & pos (X,Y)
	<- 
		-ponto_objetivo_local(X,Y);
		!check;
		!check_ponto_objetivo.
		
+!check_ponto_objetivo : ponto_objetivo(X,Y) & not pos (X,Y) 
	<- 
		-+ponto_objetivo_sel(X,Y).


		
+!escolher_outro_ponto_obj : true
	<-  
		?grid_size(N);
		math.random(N,X);
		math.random(N,Y);
		-+ponto_objetivo(X,Y).
		

/*----------- limpa(X, Y, E) - Tenta limpar o empecilho caso saiba ou tenta aprender se consegue limpar */
		
+!limpa(X,Y) : cell( X, Y, E) & limpa(F,E) & tool(F)
	<-
		?tool(F);
		clean(F).

+!limpa(X,Y) : cell( X, Y, E) & not limpa(F, E) & not ~limpa(F,E) & tool(F)
	<- 
		clean(F);
		!verifica_cell(X,Y,F,E).
		
+!limpa(X,Y) : cell( X, Y, E) & ~limpa(F, E) & tool(F)
	<- 
		!comunicar_outro_agente(X,Y,E);
		.print("N‹o tenho ferramenta para limpar ", E).

+!comunicar_outro_agente(X,Y,E) : pode_limpar(Name, E)
	<-
		.send(Name ,tell, cell(X,Y,E)).

+!comunicar_outro_agente(X,Y,E).
		
/*----------verifica_cell(X, Y, F, E) - verifica se a ferramenta que o agente possui conseguiu limpar
 * 										O empecilho. Atualiza na sua base de crena o resultado do teste. 
 * 		
 */
		
+!verifica_cell(X,Y,F,E) : not cell(X,Y,E)
	<-
		?robot(Name);
		+limpa(F,E);
		.broadcast(tell,pode_limpar(Name,E)).
		
+!verifica_cell(X,Y,F,E) : cell(X,Y,E)
	<-
		+~limpa(F,E).


