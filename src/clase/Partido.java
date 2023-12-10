package clase;

import java.util.List;

import servidor.SimpleServer;

public class Partido {
    private String fechaHora;
    private String nivel;
    private String pista;
    private Equipo equipo1;
    private Equipo equipo2;
    private String resultado;


    public Partido(String fechaHora, String nivel, String pista, Equipo equipo1, Equipo equipo2, String resultado) {
        this.fechaHora = fechaHora;
        this.nivel = nivel;
        this.pista = pista;
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.resultado = resultado;
    }
    
    public Boolean haFinalizado() {
    	if(this.resultado.equals(SimpleServer.vacio)) {
    		return false;
    	}else {
    		return true;
    	}
    }

    public Boolean estaElJugador(String nombre) {
    	if(this.equipo1.estaElJugador(nombre) || this.equipo2.estaElJugador(nombre)) {
    		return true;
    	}else {
    		return false;
    	}
    }
    public Boolean estaCompleto() {
    	if(this.equipo1.estaCompleto() && this.equipo2.estaCompleto()) {
    		return true;
    	}else {
    		return false;
    	}
    }

    public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getPista() {
		return pista;
	}

	public void setPista(String pista) {
		this.pista = pista;
	}

	public Equipo getEquipo1() {
		return equipo1;
	}

	public void setEquipo1(Equipo equipo1) {
		this.equipo1 = equipo1;
	}

	public Equipo getEquipo2() {
		return equipo2;
	}

	public void setEquipo2(Equipo equipo2) {
		this.equipo2 = equipo2;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	// Método toString para imprimir detalles del partido
    @Override
    public String toString() {
        return "Partido{" +
                "fechaHora='" + fechaHora + '\'' +
                ", nivel='" + nivel + '\'' +
                ", pista='" + pista + '\'' +
                ", equipo1=" + equipo1.toString() +
                ", equipo2=" + equipo2.toString() +
                ", resultado='" + resultado + '\'' +
                '}';
    }
    
    
    public String toResultado() {
    	return  "Partido{" +
                "fechaHora='" + fechaHora + '\'' +
                ", pista='" + pista + '\'' + "\n" + 
                ", equipo1=" + equipo1.toString() +
                ", equipo2=" + equipo2.toString() +
                ", resultado='" + resultado + '\'' +
                '}'+ "\n\n";
    }
}
