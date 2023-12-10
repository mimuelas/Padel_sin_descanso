package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
	private static final String delimitador = "!.";
    private String clientName;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }    
    
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
        	
        	Element root = null;
        	Introduccion(in,out,root);

            while (true) {
                out.println("Hola " + clientName + ". Elige una opción:\n1. Despedirse\n2. Saludar\n3. Preguntar qué tal\n0. Salir");
                out.println(delimitador);
                String inputLine = in.readLine();
                
                if (inputLine.equals("0")) {
                    out.println("Fin de la conexion!");
                    out.println(delimitador);
                    break; // Salir del bucle para finalizar la conexión
                    
                } else if (inputLine.matches("[1-3]")) {
                	
                    out.println("Felicidades!!! Vamos a ver cuanto sabes de historia");
                    NodeList preguntas=root.getElementsByTagName("pregunta");
                    NodeList respuestas=root.getElementsByTagName("respuesta");
                    String pregunta;
                    String respuesta;
                    String respestaCorrecta;
                    for(int i = 0;i<preguntas.getLength();i++) {
                    	Node preguntaN = preguntas.item(i);
                    	pregunta = preguntaN.getTextContent();
                    	 out.println(pregunta);
                         out.println(delimitador);
                    	 respestaCorrecta = respuestas.item(i).getTextContent();
                    	 respuesta = in.readLine();
                    	 while(!respuesta.equals(respestaCorrecta)) {
                    		 out.println("Respuesta incorrecta");
                             out.println(delimitador);
                    		 respuesta = in.readLine();
                    		 
                    	 }
                    }
                    out.println("Eres un auténtico sabio");
                    
                } else {
                    out.println("\nRespuesta incorrecta, por favor selecciona un número entre el 0 y el 3 \n");
                    
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
    
    
    public void Introduccion(BufferedReader in,PrintWriter out,Element root) {       
        try {        	
        	
        	//Pedimos nombre de usuario
            out.println("Introduce tu nombre:");
			clientName = in.readLine();
	        System.out.println(clientName + " conectado.");
	        
            //leemos el xml
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newDefaultInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			Document doc=db.parse(".\\src\\preguntas.xml");
			root=doc.getDocumentElement();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}