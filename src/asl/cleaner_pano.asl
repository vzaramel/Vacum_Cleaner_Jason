// Agent cleaner in project vacuum_cleaner
{ include("ag_core.asl") }  

/* Initial beliefs */
robot(cleaner2).
ponto_objetivo(0,0).
tool(pano).


/* Initial goal */
!clean_world. 

