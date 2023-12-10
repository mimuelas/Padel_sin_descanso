package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import clase.Partido;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private String clientName;
	private final String urlXML=".\\src\\preguntas.xml";

	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
	}    

	@Override
	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
			Introduccion(in,out);
			String pregunta,respuesta;

			while (true) {
				out.println("Hola " + clientName + ". Elige una opción:\n1. Crear Partido\n2. Buscar partido\n3. Asignar resultado\n4. Ver historial\n0. Salir");
				out.println(SimpleServer.delimitador);
				String inputLine = in.readLine();

				List<Partido> listaPartidos = XMLPartidoReader.leerPartidosDesdeXML(urlXML);
				
				if (inputLine.equals("0")) {
					out.println("Fin de la conexion!");
					out.println(SimpleServer.delimitador);
					break; // Salir del bucle para finalizar la conexión

				}
				else if(inputLine.equals("1")) {
					crearPartido(in,out);
					
				}
				else if(inputLine.equals("2")) {
					MostrarPartidos(out,listaPartidos);
					InscribirsePartido(in, out, listaPartidos);				

				}
				else if(inputLine.equals("3")) {
					MostrarPartidosTerminados(out,listaPartidos);
					FinalizarPartido(in, out, listaPartidos);				

				}
				else if(inputLine.equals("4")) {
					MostrarPartidosJugador(out,listaPartidos);			

				}
				
				 else {
					out.println("\nRespuesta incorrecta, por favor selecciona un número entre el 0 y el 4 \n");
					
				}
			}
		} catch (IOException e) {
			System.out.println("Excepción al intentar comunicarse con " + clientName + ": " + e.getMessage());

		} finally {
			try {
				clientSocket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(clientName + " desconectado.");
		}
	}

	public void crearPartido(BufferedReader in,PrintWriter out) {
		out.println("Has elegido la opción crear un partido, por favor responde a las siguientes cuestiones");
		
		String nivel,puesto,pista,hora;
		nivel = intercambioMsg(in,out,"¿Nivel del partido?");
//		puesto = intercambioMsg(in,out,"¿Derecha izquierda o indiferente?");
		pista = intercambioMsg(in,out,"¿En que pista se jugara el partido?");
		hora = intercambioMsg(in,out,"¿A que hora se jugara el partido?");
		XMLPartidoWriter.addPartido(urlXML,hora,nivel,pista,clientName);
	}
	
	public void MostrarPartidosJugador(PrintWriter out, List<Partido> listaPartidos) {
		out.println("Mostrando lista de partidos");
		for (Partido partido : listaPartidos) {
			if(partido.estaCompleto() && partido.haFinalizado() && partido.estaElJugador(clientName)) {
				out.println(partido.toResultado());
			}
		}
	}
	
	public void MostrarPartidosTerminados(PrintWriter out, List<Partido> listaPartidos) {
		out.println("Mostrando lista de partidos");
		for (Partido partido : listaPartidos) {
			if(partido.estaCompleto() && partido.estaElJugador(clientName)) {
				out.println(partido.toString());
			}
		}
	}
	
	public void MostrarPartidos(PrintWriter out, List<Partido> listaPartidos) {
		out.println("Mostrando lista de partidos");
		for (Partido partido : listaPartidos) {
			if(!partido.estaCompleto() && !partido.haFinalizado()) {
				out.println(partido.toString());
			}
		}
	}
	
	public void InscribirsePartido(BufferedReader in,PrintWriter out, List<Partido> listaPartidos) {
		Boolean existe = false;

		String fecha="",pista="";

		fecha = intercambioMsg(in,out,"Introduce la fecha hora a la que deseas inscribirte");
		pista = intercambioMsg(in,out,"Introduce la pista en la que deseas inscribirte");
		for (Partido partido : listaPartidos) {
			if(partido.getFechaHora().equals(fecha) && partido.getPista().equals(pista)) {
				 existe = true;
			}
		}
		if(!existe) {
			out.println("No existe ningún partido abierto a la hora y en la pista indicados");
		}else {
			out.println("Felicidades! Inscripcion realizada con exito");
			XMLPartidoWriter.reemplazarJugador(urlXML, fecha, pista, clientName);
		}
		
		
	}
	
	public void FinalizarPartido(BufferedReader in,PrintWriter out, List<Partido> listaPartidos) {
		Boolean existe = false,completo=false,finalizado=false;

		String fecha="",pista="",resultado="";

		fecha = intercambioMsg(in,out,"Introduce la fecha en la que jugaste");
		pista = intercambioMsg(in,out,"Introduce la pista en la que jugaste");
		resultado = intercambioMsg(in,out,"Introduce el resultado del partido");

		for (Partido partido : listaPartidos) {
			if(partido.getFechaHora().equals(fecha) && partido.getPista().equals(pista)) {
				 existe = true;
			}
			if(partido.estaCompleto()) {
				completo=true;
			}
			if(partido.haFinalizado()) {
				finalizado = true;
			}
		}
		if(!existe) {
			out.println("No existe ningún partido abierto a la hora y en la pista indicados");
		}else if(!completo) {
			out.println("No hay 4 jugadores inscritos aún, no se ha podido jugar ese partido");
		}else if(!finalizado) {
			out.println("Este partido ya tiene resultado asignado, alguno de tus compañeros lo ha subido ya a la plataforma");
		}
		else{
			resultado = intercambioMsg(in, out, "A continuación escribe el resultado por favor, no podrá ser modificado por nadie y quedará registrado");
			XMLPartidoWriter.modificarResultadoPartido(urlXML, fecha, pista, resultado);
		}
		
		
	}
	
	public void Introduccion(BufferedReader in,PrintWriter out) {       
		try {        	

			//Pedimos nombre de usuario
			out.println("Introduce tu nombre:");
			clientName = in.readLine();
			System.out.println(clientName + " conectado.");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public String intercambioMsg(BufferedReader in, PrintWriter out, String msg) {
		out.println(msg);
		out.println(SimpleServer.delimitador);
		String respuesta  = "";
		try {
			respuesta= in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return respuesta;
	}
}