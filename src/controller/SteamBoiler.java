package controller;

public class SteamBoiler {
	//caldeira de 200L
	int modoOperacao = 1;	
	float nivelAgua;	
	float volumeConsumo = 10;	
	private float volumeInsercao = (float) 7.5;
	private float volumeConsumoValvula = 10;
	private boolean valvula;

	private boolean Motor1;
	private boolean Motor2;
	private boolean Motor3;
	private boolean Motor4;

	private boolean falhaMotor1 = false;
	private boolean falhaMotor2 = false;
	private boolean falhaMotor3 = false;
	private boolean falhaMotor4 = false;
	
	private boolean falhaSensorNivelAgua = true;
	private boolean falhaSensorVapor = true;
	private boolean falhaSensorValvula = true;

	public SteamBoiler() {
		this.nivelAgua = 0;
		this.Motor1 = false;
		this.Motor2 = false;
		this.Motor3 = false;
		this.Motor4 = false;
		this.valvula = false;
	}


	public void run(){
		if (this.Motor1 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.Motor2 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.Motor3 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.Motor4 == true){
			this.nivelAgua += this.volumeInsercao;
		}
		if (this.valvula == true){
			this.nivelAgua -= this.volumeConsumoValvula;
		}
		
		if (this.nivelAgua >= volumeConsumo)
			this.nivelAgua -= this.volumeConsumo;
	}


	public int getModoOperacao() {
		return modoOperacao;
	}


	public void setModoOperacao(int modoOperacao) {
		this.modoOperacao = modoOperacao;
	}


	public float getNivelAgua() {
		return nivelAgua;
	}


	public void setNivelAgua(float nivelAgua) {
		this.nivelAgua = nivelAgua;
	}


	public float getVolumeConsumo() {
		return volumeConsumo;
	}


	public void setVolumeConsumo(float volumeConsumo) {
		this.volumeConsumo = volumeConsumo;
	}


	public float getVolumeInsercao() {
		return volumeInsercao;
	}


	public void setVolumeInsercao(float volumeInsercao) {
		this.volumeInsercao = volumeInsercao;
	}


	public float getVolumeConsumoValvula() {
		return volumeConsumoValvula;
	}


	public void setVolumeConsumoValvula(float volumeConsumoValvula) {
		this.volumeConsumoValvula = volumeConsumoValvula;
	}


	public boolean isValvula() {
		return valvula;
	}


	public void setValvula(boolean valvula) {
		this.valvula = valvula;
	}


	public boolean isMotor1() {
		return Motor1;
	}


	public void setMotor1(boolean motor1) {
		Motor1 = motor1;
	}


	public boolean isMotor2() {
		return Motor2;
	}


	public void setMotor2(boolean motor2) {
		Motor2 = motor2;
	}


	public boolean isMotor3() {
		return Motor3;
	}


	public void setMotor3(boolean motor3) {
		Motor3 = motor3;
	}


	public boolean isMotor4() {
		return Motor4;
	}


	public void setMotor4(boolean motor4) {
		Motor4 = motor4;
	}


	public boolean isFalhaMotor1() {
		return falhaMotor1;
	}


	public void setFalhaMotor1(boolean falhaMotor1) {
		this.falhaMotor1 = falhaMotor1;
	}


	public boolean isFalhaMotor2() {
		return falhaMotor2;
	}


	public void setFalhaMotor2(boolean falhaMotor2) {
		this.falhaMotor2 = falhaMotor2;
	}


	public boolean isFalhaMotor3() {
		return falhaMotor3;
	}


	public void setFalhaMotor3(boolean falhaMotor3) {
		this.falhaMotor3 = falhaMotor3;
	}


	public boolean isFalhaMotor4() {
		return falhaMotor4;
	}


	public void setFalhaMotor4(boolean falhaMotor4) {
		this.falhaMotor4 = falhaMotor4;
	}


	public boolean isFalhaSensorNivelAgua() {
		return falhaSensorNivelAgua;
	}


	public void setFalhaSensorNivelAgua(boolean falhaSensorNivelAgua) {
		this.falhaSensorNivelAgua = falhaSensorNivelAgua;
	}


	public boolean isFalhaSensorVapor() {
		return falhaSensorVapor;
	}


	public void setFalhaSensorVapor(boolean falhaSensorVapor) {
		this.falhaSensorVapor = falhaSensorVapor;
	}


	public boolean isFalhaSensorValvula() {
		return falhaSensorValvula;
	}


	public void setFalhaSensorValvula(boolean falhaSensorValvula) {
		this.falhaSensorValvula = falhaSensorValvula;
	}
	
	
	
}


