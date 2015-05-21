package br.ufac.scmus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ChatServer {

	private ArrayList<PrintWriter> clientWriters;
	private Socket clientSocket;
	
	public static void main(String[] args) {
		new ChatServer().run();
	}
	
	public void run() {
		clientWriters = new ArrayList<PrintWriter>();
		
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(6666);
			System.out.println("Servidor iniciado");
			
			while (true) {
				Socket socket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				clientWriters.add(writer);
				
				clientSocket = socket;
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
							BufferedReader reader = new BufferedReader(isr);
							
							String message;
							
							while ((message = reader.readLine()) != null) {
								System.out.println("O usuário diz: " + message);
								shoot(message);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
				
				t.start();
				System.out.println("Um novo cliente se conectou");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void shoot(String message) {
		Iterator<PrintWriter> it = clientWriters.iterator();
		
		while( it.hasNext() ) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println("O usuário diz:\n" + message);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}