package controller;

public class SteamBoiler {
	//caldeira de 200L
	int modoOperacao = 1;
	
	float nivelAgua;
	
	float volumeConsumo = 2;
	
	private float volumeInsercao = 5;

	private boolean statusMotor1;
	private boolean statusMotor2;
	private boolean statusMotor3;
	private boolean statusMotor4;
	
	private boolean statusSensorNivelAgua = true;
	private boolean statusSensorVapor = true;
	private boolean statusSensorValvula = true;

	private float volumeConsumoValvula = 10;
	private boolean valvula;

	public SteamBoiler() {
		this.nivelAgua = 0;
		this.statusMotor1 = false;
		this.statusMotor2 = false;
		this.statusMotor3 = false;
		this.statusMotor4 = false;
		this.valvula = false;
	}


	public void run(){
		if (this.statusMotor1 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.statusMotor2 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.statusMotor3 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.statusMotor4 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.valvula == true){
			this.nivelAgua -= this.volumeConsumoValvula;
		}
		
		if (this.nivelAgua >= volumeConsumo)
			this.nivelAgua -= this.volumeConsumo;
	}
	
	public float getVolumeConsumo() {
		return volumeConsumo;
	}

	public void setVolumeConsumo(float volumeConsumo) {
		this.volumeConsumo = volumeConsumo;
	}

	public boolean isValvula() {
		return valvula;
	}
	public void setValvula(boolean valvula) {
		this.valvula = valvula;
	}
	public float getNivelAgua() {
		return nivelAgua;
	}
	public void setNivelAgua(float nivelAgua) {
		this.nivelAgua = nivelAgua;
	}
	public float getVolumeInsercao() {
		return volumeInsercao;
	}
	public void setVolumeInsercao(float volumeInsercao) {
		this.volumeInsercao = volumeInsercao;
	}
	public boolean isMotor1() {
		return statusMotor1;
	}
	public void setMotor1(boolean motor1) {
		this.statusMotor1 = motor1;
	}
	public boolean isMotor2() {
		return statusMotor2;
	}
	public void setMotor2(boolean motor2) {
		this.statusMotor2 = motor2;
	}
	public boolean isMotor3() {
		return statusMotor3;
	}
	public void setMotor3(boolean motor3) {
		this.statusMotor3 = motor3;
	}
	public boolean isMotor4() {
		return statusMotor4;
	}
	public void setMotor4(boolean motor4) {
		this.statusMotor4 = motor4;
	}
	

	public int getModoOperacao() {
		return modoOperacao;
	}


	public void setModoOperacao(int modoOperacao) {
		this.modoOperacao = modoOperacao;
	}
	
	public boolean isStatusSensorNivelAgua() {
		return statusSensorNivelAgua;
	}


	public void setStatusSensorNivelAgua(boolean statusSensorNivelAgua) {
		this.statusSensorNivelAgua = statusSensorNivelAgua;
	}


	public boolean isStatusSensorVapor() {
		return statusSensorVapor;
	}


	public void setStatusSensorVapor(boolean statusSensorVapor) {
		this.statusSensorVapor = statusSensorVapor;
	}


	public boolean isStatusSensorValvula() {
		return statusSensorValvula;
	}


	public void setStatusSensorValvula(boolean statusSensorValvula) {
		this.statusSensorValvula = statusSensorValvula;
	}
	
}


