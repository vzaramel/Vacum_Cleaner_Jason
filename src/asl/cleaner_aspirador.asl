// Agent cleaner in project vacuum_cleaner
{ include("ag_core.asl") }    
/* Initial beliefs */
robot(cleaner1).
ponto_objetivo(9,9).
//Conjunto de ferramentas do agente
tool(aspirador).



/* Initial goal */
!clean_world. 
