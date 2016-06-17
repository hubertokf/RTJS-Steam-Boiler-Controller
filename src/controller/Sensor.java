package controller;

public class Sensor {
	
		private boolean status;
		private int valor;
		
		public Sensor(){
			this.status=true;
			this.valor=valor;/*como fazer para pegar o valor que queremos?*/
			
		}

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		public int getValor() {
			return valor;
		}

		public void setValor(int valor) {
			this.valor = valor;
		}
		
	}
}
