package cliente;


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SimpleCliente {


	public static void main(String[] args) {

		String hostName = "localhost";
		int portNumber = 8080;

		try (Socket socket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				Scanner sc = new Scanner(System.in) ) {

			System.out.println(in.readLine());
			Comunicarse(sc,in,out);

		} catch (IOException e) {

			System.out.println("No se pudo conectar al servidor: " + e.getMessage());

		}
	}


	public static void Comunicarse(Scanner sc,BufferedReader in,PrintWriter out ) {

		String leer="";
		String escribir= "";

		while (!escribir.equals("0") && sc.hasNextLine()) {
			escribir = sc.nextLine();
			out.println(escribir);

			try {

				while (!(leer = in.readLine()).equals(servidor.SimpleServer.delimitador)) {
					System.out.println(leer);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
