package br.ufac.scmus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Servidor {

	private ArrayList<PrintWriter> listaClientes;
	private Socket socketCliente;
	private String usuario;
	
	public static void main(String[] args) {
		new Servidor().run();
	}
	
	public void run() {
		
		listaClientes = new ArrayList<PrintWriter>();
		
		
		try {
			
			ServerSocket socketServidor = new ServerSocket(6789) {
				@Override
				public void close() throws IOException {
					enviarMensagens(usuario + " saiu no chat.", "");
					super.close();
				}
			};
			
			System.out.println("Servidor iniciado");
			
			while (true) {
				
				Socket socket = socketServidor.accept();
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				listaClientes.add(writer);
				
				socketCliente = socket;
				
				InputStreamReader isrUsuario = new InputStreamReader(socketCliente.getInputStream());
				BufferedReader readerUsuario = new BufferedReader(isrUsuario);
				usuario = readerUsuario.readLine();
				
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							InputStreamReader isr = new InputStreamReader(socketCliente.getInputStream());
							BufferedReader reader = new BufferedReader(isr);
							
							String mensagem;
							
							while ((mensagem = reader.readLine()) != null) {
								enviarMensagens(mensagem, usuario);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				t.start();
				
				enviarMensagens(usuario + " entrou no chat.", "");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void enviarMensagens(String mensagem, String usuario) {
		
		Iterator<PrintWriter> it = listaClientes.iterator();
		
		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(usuario.equals("") ? mensagem + "\n" : usuario + " diz: " + mensagem + "\n");
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}