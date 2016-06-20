package controller;

import javax.realtime.PriorityScheduler;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.RealtimeThread;

public class Main {
	private static SteamBoiler caldeira = new SteamBoiler();
	
	static float nivelAgua;
	static boolean bomba1;
	static boolean bomba2;
	static boolean bomba3;
	static boolean bomba4;
	static boolean valvula;
	static float volumeConsumo;
	static int maxNormal;
	static int minNormal;
	static int maxLimite;
	static int minLimite;
	static int modoOperacao;
	
	public static void main(String[] args){		
		/* priority for new thread: mininum+10 */
	    int priorityTatuacao = PriorityScheduler.instance().getMinPriority()+1;
	    PriorityParameters priorityParametersTatuacao = new PriorityParameters(priorityTatuacao);

	    int priorityTcontrole = PriorityScheduler.instance().getMinPriority()+2;
	    PriorityParameters priorityParametersTcontrole = new PriorityParameters(priorityTcontrole);
	    
	    int priorityTsensorBomba = PriorityScheduler.instance().getMinPriority()+3;
	    PriorityParameters priorityParametersTsensorBomba = new PriorityParameters(priorityTsensorBomba);
	    
	    int priorityTsensorVapor = PriorityScheduler.instance().getMinPriority()+4;
	    PriorityParameters priorityParametersTsensorVapor = new PriorityParameters(priorityTsensorVapor);
	    
	    int priorityTsensorAgua = PriorityScheduler.instance().getMinPriority()+5;
	    PriorityParameters priorityParametersTsensorAgua = new PriorityParameters(priorityTsensorAgua);

	    int priorityTsimulacao = PriorityScheduler.instance().getMinPriority()+6;
	    PriorityParameters priorityParametersTsimulacao = new PriorityParameters(priorityTsimulacao);

	    /* period: 200ms */
	    RelativeTime periodTatuacao = new RelativeTime(1000 /* ms */, 0 /* ns */);
	    RelativeTime periodTcontrole = new RelativeTime(5000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorBomba = new RelativeTime(2000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorVapor = new RelativeTime(2000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorAgua = new RelativeTime(2000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsimulacao = new RelativeTime(1000 /* ms */, 0 /* ns */);

	    /* release parameters for periodic thread: */
	    PeriodicParameters periodicParametersTatuacao = new PeriodicParameters(null,periodTatuacao, null,null,null,null);
	    PeriodicParameters periodicParametersTcontrole = new PeriodicParameters(null,periodTcontrole, null,null,null,null);
	    PeriodicParameters periodicParametersTsensorBomba = new PeriodicParameters(null,periodTsensorBomba, null,null,null,null);
	    PeriodicParameters periodicParametersTsensorVapor = new PeriodicParameters(null,periodTsensorVapor, null,null,null,null);
	    PeriodicParameters periodicParametersTsensorAgua = new PeriodicParameters(null,periodTsensorAgua, null,null,null,null);
	    PeriodicParameters periodicParametersTsimulacao = new PeriodicParameters(null,periodTsimulacao, null,null,null,null);
	    
	    RealtimeThread Tsimulacao= new RealtimeThread(priorityParametersTsimulacao,periodicParametersTsimulacao){
			public void run(){
				while (true)
					synchronized(caldeira){
						caldeira.run();
						System.out.println(caldeira.getNivelAgua());
						waitForNextPeriod();
					}
			}
	    };
	    RealtimeThread Tcontrole= new RealtimeThread(priorityParametersTcontrole,periodicParametersTcontrole){
			public void run(){
				while (true){
					synchronized(caldeira){
						int lastMode = caldeira.getModoOperacao();
						
						switch (lastMode) {
							case 1: //inicialização
								if (nivelAgua < 100){
									caldeira.setMotor1(true);
									caldeira.setMotor2(true);			
								//se ja chegou no fim do modo de inicialização
								}else{
									caldeira.setModoOperacao(2);
								}
								break;
							case 2: //normal mode
								if (nivelAgua > 175 && bomba2==true) //Se nivel crítico
									caldeira.setModoOperacao(3);
								if (nivelAgua < 25 && bomba2==false)
									caldeira.setModoOperacao(3);
								
								if (nivelAgua >= 175)
									caldeira.setMotor2(false);
								if (nivelAgua <= 25)
									caldeira.setMotor2(true);
								
								break;
							case 3: //critical mode
								// se saiu do nivel crítico
									caldeira.setModoOperacao(2);
								break;
	
							default:
								break;
						}
						waitForNextPeriod();
					}
				}
			}
	    };
	    
	    RealtimeThread TsensorAgua= new RealtimeThread(priorityParametersTsensorAgua,periodicParametersTsensorAgua){
			public void run(){
				while (true){
					synchronized(caldeira){
						nivelAgua = caldeira.getNivelAgua();
						waitForNextPeriod();
					}
				}
			}
	    };
	    
	    RealtimeThread TsensorVapor= new RealtimeThread(priorityParametersTsensorVapor,periodicParametersTsensorVapor){
			public void run(){
				while (true){
					synchronized(caldeira){
						volumeConsumo = caldeira.getVolumeConsumo();
						waitForNextPeriod();
					}
				}
			}
	    };
	    
	    RealtimeThread TsensorBomba= new RealtimeThread(priorityParametersTsensorBomba,periodicParametersTsensorBomba){
			public void run(){
				while (true){
					synchronized(caldeira){
						bomba1 = caldeira.isMotor1();
						bomba2 = caldeira.isMotor2();
						bomba3 = caldeira.isMotor3();
						bomba4 = caldeira.isMotor4();
						waitForNextPeriod();
					}
				}
			}
	    };
	    

	    Tsimulacao.start();
	    Tcontrole.start();
	    TsensorAgua.start();
	    TsensorBomba.start();
	    TsensorVapor.start();
	    
	    /* switch(modoOperacao){
    	case((caldeira.volumeConsumo == 0) && ((caldeira.maxNormal<=caldeira.nivelAgua) && (caldeira.nivelAgua <= caldeira.minNormal))
    			&& (caldeira.isMotor1() && caldeira.isMotor2()&& caldeira.isMotor3() && caldeira.isMotor4()) )
    			caldeira.modoOperacao = normalMode;
    	case( )
    	
    	case(/*(caldeira.volumeConsumo !=0) &&(caldeira.nivelAgua>=caldeira.maxLimite)||(caldeira.nivelAgua<=caldeira.minLimite)(caldeira.getNivelAgua(false))
    			||(caldeira.getVolumeConsumo(false))||(caldeira.isMotor1(fail))||caldeira.isMotor2(fail)||caldeira.isMotor3(fail)||caldeira.isMotor4(fail)))
    			caldeira.modoOperacao = emergencyMode;
    	
}   */
	}
}
