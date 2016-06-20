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
	    int priorityTatuacao = PriorityScheduler.instance().getMinPriority()+7;
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
	    RelativeTime periodTsensorBomba = new RelativeTime(1000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorVapor = new RelativeTime(1000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorAgua = new RelativeTime(1000 /* ms */, 0 /* ns */);
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
				while (true){
					caldeira.run();
					System.out.println("Gerando valores.");
					waitForNextPeriod();
				}
			}
	    };
	    RealtimeThread Tcontrole= new RealtimeThread(priorityParametersTcontrole,periodicParametersTcontrole){
			public void run(){
				while (true){
					System.out.println("Controlando caldeira");
					int lastMode = caldeira.getModoOperacao();
					System.out.println("MODO Antes: "+lastMode);
					int modo;
					
					//pra qual estado vai devido as condições coletadas pelos sensores
					
					switch (lastMode) {
						case 1: //inicialização
							if (nivelAgua < 100){
								modo = 1;
								caldeira.setModoOperacao(1);
							}else{
								modo = 2;
								caldeira.setModoOperacao(2);
							}	
							break;
						case 2: //normal mode
							if (nivelAgua > 175 && bomba2==true){
								caldeira.setModoOperacao(3);
								modo = 3;
							}
							
							if (nivelAgua < 25 && bomba2==false){
								modo = 3;
								caldeira.setModoOperacao(3);
							}
							break;
						case 3: //degraded mode
							if (nivelAgua < 150 && nivelAgua > 35){
								caldeira.setValvula(false);
								caldeira.setMotor3(false);
								modo = 2;
								caldeira.setModoOperacao(2);
							}
							break;
						case 4: //rescue mode
							if (nivelAgua < 150 && nivelAgua > 35){
								caldeira.setValvula(false);
								caldeira.setMotor3(false);
								modo = 2;
								caldeira.setModoOperacao(2);
							}
							break;
						case 5: //emergency stop mode
							if (nivelAgua < 150 && nivelAgua > 35){
								caldeira.setValvula(false);
								caldeira.setMotor3(false);
								modo = 2;
								caldeira.setModoOperacao(2);
							}
							break;
	
						default:
							break;
					}
					

					System.out.println("MODO Depois: "+modo);
					
					
					// oque fazer em cada estado
					switch (modo) {
						case 1: //inicialização
							caldeira.setMotor1(true);
							caldeira.setMotor2(true);	
							break;
						case 2: //normal mode
							
							if (nivelAgua >= 175)
								caldeira.setMotor2(false);
							if (nivelAgua <= 25)
								caldeira.setMotor2(true);
							
							break;
						case 3: //critical mode
							// se saiu do nivel crítico
							if (nivelAgua > 175 && bomba2==true) //Se nivel crítico
								caldeira.setValvula(true);
								
							if (nivelAgua < 25 && bomba2==false)
								caldeira.setMotor3(true);
								
							break;

						default:
							break;
					}
					waitForNextPeriod();	
				}
			}
	    };
	    
	    RealtimeThread TsensorAgua= new RealtimeThread(priorityParametersTsensorAgua,periodicParametersTsensorAgua){
			public void run(){
				while (true){
					nivelAgua = caldeira.getNivelAgua();
					System.out.println("Coletado nivel de agua. Nivel de agua = "+nivelAgua);
					waitForNextPeriod();
				}
			}
	    };
	    
	    RealtimeThread TsensorVapor= new RealtimeThread(priorityParametersTsensorVapor,periodicParametersTsensorVapor){
			public void run(){
				while (true){
					volumeConsumo = caldeira.getVolumeConsumo();
					System.out.println("Coletado volume de consumo de vapor. Consumo de agua = "+volumeConsumo);
					waitForNextPeriod();
				}
			}
	    };
	    
	    RealtimeThread TsensorBomba= new RealtimeThread(priorityParametersTsensorBomba,periodicParametersTsensorBomba){
			public void run(){
				while (true){
					bomba1 = caldeira.isMotor1();
					bomba2 = caldeira.isMotor2();
					bomba3 = caldeira.isMotor3();
					bomba4 = caldeira.isMotor4();
					System.out.println("Coletado estados dos motores.");
					waitForNextPeriod();
				}
			}
	    };
	    

	    Tsimulacao.start();
	    TsensorAgua.start();
	    TsensorBomba.start();
	    TsensorVapor.start();
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
