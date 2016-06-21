package controller;

import javax.realtime.PriorityScheduler;
import javax.realtime.PriorityParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.RelativeTime;
import javax.realtime.RealtimeThread;

public class Main {
	static int maxNormal = 750;
	static int minNormal = 250;
	static int maxLimite = 900;
	static int minLimite = 100;
	
	private static SteamBoiler caldeira = new SteamBoiler();
	
	static float nivelAgua;
	static boolean Motor1;
	static boolean Motor2;
	static boolean Motor3;
	static boolean Motor4;

	static boolean falhaMotor1 = false;
	static boolean falhaMotor2 = false;
	static boolean falhaMotor3 = false;
	static boolean falhaMotor4 = false;
	
	static boolean falhaSensorNivelAgua = true;
	static boolean falhaSensorVapor = true;
	static boolean falhaSensorValvula = true;
	static boolean valvula;
	static float volumeConsumo;
	static int modoOperacao;
	
	public static void main(String[] args){		
		/* priority for new thread: mininum+10 */
	    int priorityTfalha = PriorityScheduler.instance().getMinPriority()+7;
	    PriorityParameters priorityParametersTfalha = new PriorityParameters(priorityTfalha);

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
	    RelativeTime periodTfalha = new RelativeTime(20000 /* ms */, 0 /* ns */);
	    RelativeTime periodTcontrole = new RelativeTime(5000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorBomba = new RelativeTime(1000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorVapor = new RelativeTime(1000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsensorAgua = new RelativeTime(1000 /* ms */, 0 /* ns */);
	    RelativeTime periodTsimulacao = new RelativeTime(1000 /* ms */, 0 /* ns */);

	    /* release parameters for periodic thread: */
	    PeriodicParameters periodicParametersTfalha = new PeriodicParameters(null,periodTfalha, null,null,null,null);
	    PeriodicParameters periodicParametersTcontrole = new PeriodicParameters(null,periodTcontrole, null,null,null,null);
	    PeriodicParameters periodicParametersTsensorBomba = new PeriodicParameters(null,periodTsensorBomba, null,null,null,null);
	    PeriodicParameters periodicParametersTsensorVapor = new PeriodicParameters(null,periodTsensorVapor, null,null,null,null);
	    PeriodicParameters periodicParametersTsensorAgua = new PeriodicParameters(null,periodTsensorAgua, null,null,null,null);
	    PeriodicParameters periodicParametersTsimulacao = new PeriodicParameters(null,periodTsimulacao, null,null,null,null);
	    
	    RealtimeThread Tfalha= new RealtimeThread(priorityParametersTfalha,periodicParametersTfalha){
			public void run(){
				while (true){
					synchronized (caldeira) {
						caldeira.setFalhaMotor2(!caldeira.isFalhaMotor2());	
					}
					System.out.println("MOTOR 2 STATUS FALHA = "+!caldeira.isFalhaMotor2());
					waitForNextPeriod();
				}
			}
	    };
	    
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
				int modo = 1;
				while (true){
					System.out.println("Controlando caldeira");
					int lastMode = caldeira.getModoOperacao();
					System.out.println("MODO Antes: "+lastMode);
					
					//pra qual estado vai devido as condições coletadas pelos sensores
					
					switch (lastMode) {
						case 1: //inicialização
							System.out.println("Inicializacao");
							if(falhaSensorNivelAgua || falhaSensorVapor){
								if(nivelAgua < maxNormal && nivelAgua > minNormal){
									caldeira.setValvula(false);
									modo = 2;
									caldeira.setModoOperacao(2);

									caldeira.setMotor3(false);
									caldeira.setMotor4(false);
									break;
								}
								
								if(nivelAgua > maxNormal){
									caldeira.setValvula(true);
									break;
								}
								
								if(nivelAgua < minNormal){
									caldeira.setMotor1(true);
									caldeira.setMotor2(true);
									caldeira.setMotor3(true);
									caldeira.setMotor4(true);
									break;
								}					
							}else{
								caldeira.setValvula(false);
								caldeira.setMotor1(false);
								modo = 4;
								caldeira.setModoOperacao(4);
							}
							break;
						case 2: //normal mode
							System.out.println("Normal");
							if(falhaSensorNivelAgua || falhaSensorVapor){
								if (nivelAgua > maxLimite || nivelAgua < minLimite){
									modo = 4;
									break;
								}
								if (falhaMotor1 || falhaMotor2){
									modo = 3;

									caldeira.setModoOperacao(3);
								}	
								
								if (nivelAgua >= maxNormal){
									caldeira.setMotor1(false);
									caldeira.setMotor2(false);
									caldeira.setMotor3(false);
									caldeira.setMotor4(false);
									
									break;
								}
								
								if (nivelAgua <= minNormal){
									caldeira.setMotor1(true);
									caldeira.setMotor2(true);
									
									if (falhaMotor1 || falhaMotor2){
										modo = 3;

										caldeira.setModoOperacao(3);
										break;
									}									
									break;
								}
							}else{
								caldeira.setValvula(false);
								caldeira.setMotor1(false);
								modo = 4;
								caldeira.setModoOperacao(4);
							}
							break;
						case 3: //degraded mode
							System.out.println("Degradado");

							if (falhaMotor1 && Motor1){
								caldeira.setMotor1(false);
								caldeira.setMotor3(true);
							}
							
							if (falhaMotor2 && Motor2){
								caldeira.setMotor2(false);
								caldeira.setMotor4(true);			
							}
							modo = 2;
							caldeira.setModoOperacao(2);
							break;
						case 4: //emergency stop mode

							System.out.println("Parada");
							caldeira.setMotor1(false);
							caldeira.setMotor2(false);
							caldeira.setMotor3(false);
							caldeira.setMotor4(false);
							System.exit(0);
							
							break;
	
						default:
							break;
					}
					

					System.out.println("MODO Depois: "+modo);
					
					waitForNextPeriod();	
				}
			}
	    };
	    
	    RealtimeThread TsensorAgua= new RealtimeThread(priorityParametersTsensorAgua,periodicParametersTsensorAgua){
			public void run(){
				while (true){
					nivelAgua = caldeira.getNivelAgua();
					falhaSensorNivelAgua = caldeira.isFalhaSensorNivelAgua();
					System.out.println("Coletado nivel de agua. Nivel de agua = "+nivelAgua);
					waitForNextPeriod();
				}
			}
	    };
	    
	    RealtimeThread TsensorVapor= new RealtimeThread(priorityParametersTsensorVapor,periodicParametersTsensorVapor){
			public void run(){
				while (true){
					volumeConsumo = caldeira.getVolumeConsumo();
					falhaSensorVapor = caldeira.isFalhaSensorVapor();
					System.out.println("Coletado volume de consumo de vapor. Consumo de agua = "+volumeConsumo);
					waitForNextPeriod();
				}
			}
	    };
	    
	    RealtimeThread TsensorBomba= new RealtimeThread(priorityParametersTsensorBomba,periodicParametersTsensorBomba){
			public void run(){
				while (true){
					Motor1 = caldeira.isMotor1();
					Motor2 = caldeira.isMotor2();
					Motor3 = caldeira.isMotor3();
					Motor4 = caldeira.isMotor4();
					falhaMotor1 = caldeira.isFalhaMotor1();
					falhaMotor2 = caldeira.isFalhaMotor2();
					falhaMotor3 = caldeira.isFalhaMotor3();
					falhaMotor4 = caldeira.isFalhaMotor4();
					System.out.println("Coletado estados dos motores.");
					waitForNextPeriod();
				}
			}
	    };
	    
	    Tfalha.start();
	    Tsimulacao.start();
	    TsensorAgua.start();
	    TsensorBomba.start();
	    TsensorVapor.start();
	    Tcontrole.start();
	}
}
