package clase;

import servidor.SimpleServer;

public class Equipo {

	private String Jugador1;
	private String Jugador2;
	
	public Equipo(String jugador1, String jugador2) {
		Jugador1 = jugador1;
		Jugador2 = jugador2;
	}
	
	public Boolean estaElJugador(String nombre) {
		if(this.Jugador1.equals(nombre) || this.Jugador2.equals(nombre)) {
			return true;
		}else {
			return false;
		}
	}
	
	
	public Boolean estaCompleto() {
		if(this.Jugador1.equals(SimpleServer.vacio) || this.Jugador2.equals(SimpleServer.vacio)) {
			return false;
		} else {
			return true;
		}
	}
	
	public String getJugador1() {
		return Jugador1;
	}
	public void setJugador1(String jugador1) {
		Jugador1 = jugador1;
	}
	public String getJugador2() {
		return Jugador2;
	}
	public void setJugador2(String jugador2) {
		Jugador2 = jugador2;
	}

	@Override
	public String toString() {
		return "Equipo [Jugador1=" + Jugador1 + ", Jugador2=" + Jugador2 + "]";
	}
	
	
}
