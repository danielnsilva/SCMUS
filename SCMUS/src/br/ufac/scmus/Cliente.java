package br.ufac.scmus;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Cliente {

	private JTextArea input;
	private JTextField output;
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private JTextField tfHost;
	private JTextField tfUsuario;
	
	public static void main(String[] args) {
		new Cliente().run();
	}
	
	public void run() {
		JFrame frame = new JFrame("Sistema de Chat Moderno Utilizando Sockets");
		JPanel panel = new JPanel();
		
		input = new JTextArea(20, 40);
		input.setLineWrap(true);
		input.setWrapStyleWord(true);
		input.setEditable(false);
		
		JScrollPane scroll = new JScrollPane(input, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		output = new JTextField(20);
		JButton send = new JButton("Enviar");
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (output.getText().equals("")) {
					return;
				}
				
				writer.println(output.getText());
				writer.flush();
				
				output.setText("");
				output.requestFocus();
			}
		});
		
		panel.add(scroll);
		panel.add(output);
		panel.add(send);
		
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		
		JPanel panelConexao = new JPanel();
		frame.getContentPane().add(panelConexao, BorderLayout.NORTH);
		
		tfUsuario = new JTextField();
		panelConexao.add(tfUsuario);
		tfUsuario.setColumns(10);
		
		tfHost = new JTextField();
		panelConexao.add(tfHost);
		tfHost.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectar(tfHost.getText());
			}
		});
		panelConexao.add(btnConectar);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(java.awt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(java.awt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(java.awt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				if ( JOptionPane.showConfirmDialog(null, "Deseja deixar o chat?", "Sair", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION ) {
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						System.exit(0);
					}
				}
			}
			
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(java.awt.event.WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	protected void conectar(String host) {

		try {
			socket = new Socket(host, 6666);
			InputStreamReader isr = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(isr);
			writer = new PrintWriter(socket.getOutputStream());
			
			writer.println(tfUsuario.getText());
			writer.flush();
			
			input.append("Servidor encontrado, conexão estabilizada!" + "\n");
			//System.out.println("Servidor encontrado, conexão estabilizada!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Nenhum servidor ativo foi encontrado", "Erro de conexão", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
		
		Thread atualizador = new Thread(new Runnable() {
			
			@Override
			public void run() {
				String message;
				
				try {
					while ((message = reader.readLine()) != null) {
						input.append(message + "\n");
					}
				} catch (IOException e1) {
					//e1.printStackTrace();
					System.exit(0);
				}
			}
		});
		
		atualizador.start();
		
	}
	
}