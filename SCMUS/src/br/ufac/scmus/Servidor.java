package br.ufac.scmus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Servidor {

	private ArrayList<PrintWriter> clientWriters;
	//private Map<String, PrintWriter> clientes;
	private Socket clientSocket;
	
	public static void main(String[] args) {
		new Servidor().run();
	}
	
	public void run() {
		clientWriters = new ArrayList<PrintWriter>();
		//clientes = new HashMap<String, PrintWriter>();
		
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(6666);
			System.out.println("Servidor iniciado");
			
			while (true) {
				Socket socket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				clientWriters.add(writer);
				
				clientSocket = socket;
				InputStreamReader isrUsuario = new InputStreamReader(clientSocket.getInputStream());
				BufferedReader readerUsuario = new BufferedReader(isrUsuario);
				
				final String usuario = readerUsuario.readLine();
				
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
							BufferedReader reader = new BufferedReader(isr);
							
							String message;
							
							while ((message = reader.readLine()) != null) {
								System.out.println("O usuário diz: " + message);
								shoot(message, usuario);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
				
				t.start();
				shoot(usuario + " entrou no chat.", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void shoot(String message, String usuario) {
		Iterator<PrintWriter> it = clientWriters.iterator();
		
		while( it.hasNext() ) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				if (usuario.equals("")) {
					writer.println(message);
				} else {
					writer.println(usuario + " diz:\n" + message);
				}
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}