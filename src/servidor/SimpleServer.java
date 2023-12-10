package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServer {
    private static final int PORT = 8080;
    private static final int MAX_CLIENTES = 50; 

	public static final String delimitador = "!.";
	public static final String vacio = "-";
	
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    pool.execute(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    System.err.println("Error al aceptar conexión de cliente: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor en el puerto " + PORT + ": " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
