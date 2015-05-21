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
	private ServerSocket socketServidor;
	
	public static void main(String[] args) {
		new Servidor().run();
	}
	
	public void run() {
		
		listaClientes = new ArrayList<PrintWriter>();
		
		
		try {
			
			socketServidor = new ServerSocket(6789);
			
			System.out.println("Servidor iniciado");
			
			while (true) {
				
				Socket socket = socketServidor.accept();
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				listaClientes.add(writer);
				
				socketCliente = socket;
				
				InputStreamReader isrUsuario = new InputStreamReader(socketCliente.getInputStream());
				BufferedReader readerUsuario = new BufferedReader(isrUsuario);
				final String usuario = readerUsuario.readLine();
				
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
		
		try {
			socketServidor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void enviarMensagens(String mensagem, String usuario) {
		
		Iterator<PrintWriter> it = listaClientes.iterator();
		String saida = "";
		
		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				//writer.println(usuario.equals("") ? mensagem + "\n" : usuario + " diz: " + mensagem + "\n");
				if (usuario.equals("")) {
					saida = mensagem + "\n";
				} else if (mensagem.equals("[sair]")) {
					saida = usuario + " saiu do chat.\n";
				} else {
					saida = usuario + " diz: " + mensagem + "\n";
				}
				writer.println(saida);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}