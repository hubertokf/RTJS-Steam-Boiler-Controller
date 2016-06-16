package controller;

public class SteamBoiler {
	//caldeira de 200L
	private float nivelAgua;
	private float volumeInsercao = 5;
	private float volumeConsumo = 2;
	private float volumeConsumoValvula = 10;
	private boolean motor1;
	private boolean motor2;
	private boolean motor3;
	private boolean motor4;
	private boolean valvula;
	
	public SteamBoiler() {
		this.nivelAgua = 100;
		this.motor1 = true;
		this.motor2 = false;
		this.motor3 = false;
		this.motor4 = false;
		this.valvula = false;
	}

	public void run(){
		if (this.motor1 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.motor2 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.motor3 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.motor4 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.valvula == true){
			this.nivelAgua -= this.volumeConsumoValvula;
		}
		
		this.nivelAgua -= this.volumeConsumo;
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
		return motor1;
	}
	public void setMotor1(boolean motor1) {
		this.motor1 = motor1;
	}
	public boolean isMotor2() {
		return motor2;
	}
	public void setMotor2(boolean motor2) {
		this.motor2 = motor2;
	}
	public boolean isMotor3() {
		return motor3;
	}
	public void setMotor3(boolean motor3) {
		this.motor3 = motor3;
	}
	public boolean isMotor4() {
		return motor4;
	}
	public void setMotor4(boolean motor4) {
		this.motor4 = motor4;
	}
}
