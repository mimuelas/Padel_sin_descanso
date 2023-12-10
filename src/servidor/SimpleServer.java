package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SimpleServer {
	
	private static final String delimitador = "!.";
	
    public static void main(String[] args) {
    	
        int port = 8080;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
        	
            System.out.println("Servidor iniciado en el puerto " + port);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
            
        } catch (IOException e) {
            System.out.println("No se pudo iniciar el servidor en el puerto " + port + ": " + e.getMessage());
        }
    }
}
