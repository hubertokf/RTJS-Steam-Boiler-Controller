package controller;

import javax.realtime.PriorityScheduler;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.RealtimeThread;

public class Main {
	public static void main(String[] args){
		final SteamBoiler caldeira = new SteamBoiler();
		
		float nivelAgua;
		final boolean motor1;
		final boolean motor2;
		final boolean motor3;
		final boolean motor4;
		final boolean valvula;
		float volumeConsumo;
		int maxNormal;
		int minNormal;
		int maxLimite;
		int minLiminte;
		
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
						if (caldeira.getNivelAgua() >= 175)
							caldeira.setMotor1(false);
						if (caldeira.getNivelAgua() <= 25)
							caldeira.setMotor1(true);
						waitForNextPeriod();
					}
				}
			}
	    };
	    
	    /*RealtimeThread TsensorAgua= new RealtimeThread(priorityParametersTsensorAgua,periodicParametersTsensorAgua){
			public void run(){
				while (true){
					synchronized(caldeira){
						nivelAgua = caldeira.getNivelAgua();
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
					}
				}
			}
	    };*/
	    

	    Tsimulacao.start();
	    Tcontrole.start();
	       
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
