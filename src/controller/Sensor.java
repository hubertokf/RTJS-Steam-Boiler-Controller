package controller;

public class Sensor {
	
		private boolean status;
		private float valor;
		
		public Sensor(float valor){
			this.status=true;
			this.valor=valor;
			/*como fazer para pegar o valor que queremos?*/
			
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		public float getValor() {
			return valor;
		}

		public void setValor(int valor) {
			this.valor = valor;
		}
}
