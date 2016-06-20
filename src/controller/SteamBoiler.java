package controller;

public class SteamBoiler {
	//caldeira de 200L
	int modoOperacao = 1;
	
	float nivelAgua;
	float maxNormal = 150;
	float minNormal = 50;
	float maxLimite = 175;
	float minLimite = 25;
	
	float volumeConsumo = 2;
	
	private float volumeInsercao = 5;
	private boolean statusMotor1;
	private boolean statusMotor2;
	private boolean statusMotor3;
	private boolean statusMotor4;

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
	public float getMaxNormal() {
		return maxNormal;
	}

	public void setMaxNormal(int maxNormal) {
		this.maxNormal = maxNormal;
	}

	public float getMinNormal() {
		return minNormal;
	}

	public void setMinNormal(int minNormal) {
		this.minNormal = minNormal;
	}

	public float getMaxLimite() {
		return maxLimite;
	}

	public void setMaxLimite(int maxLimite) {
		this.maxLimite = maxLimite;
	}

	public float getMinLimite() {
		return minLimite;
	}

	public void setMinLiminte(int minLimite) {
		this.minLimite = minLimite;
	}
	

	public int getModoOperacao() {
		return modoOperacao;
	}


	public void setModoOperacao(int modoOperacao) {
		this.modoOperacao = modoOperacao;
	}
	
}


