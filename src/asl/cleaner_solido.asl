// Agent cleaner in project vacuum_cleaner
{ include("ag_core.asl") }  
/* Initial beliefs */
robot(cleaner3).
ponto_objetivo(0,0).
//Conjunto de ferramentas do agente
tool(vassoura).


/* Initial goal */
!clean_world. 

/* Plans */

