package com.app;
import java.net.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
public class Server extends JFrame {
	
	ServerSocket server;
	Socket socket;
	BufferedReader br;//read the data
	PrintWriter out;//write the data
	private JLabel heading = new JLabel("Server Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto",Font.PLAIN,20);
	
	public Server() {
		try {
			
			server = new ServerSocket(7776);
			System.out.println("Server is ready to accept Connection");
			
			createGUI();
			handleEvents();
			
			System.out.println("Waiting...");
			
			socket = server.accept();
			
			br = new BufferedReader
					(new InputStreamReader(socket.getInputStream()));
			
			out=new PrintWriter(socket.getOutputStream());
			
			
			
			
			startReading();
			//startWriting();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	 private void handleEvents() {
		 messageInput.addKeyListener(new KeyListener(){
				
				public void keyTyped(KeyEvent e){
					
					
				}
				public void keyPressed(KeyEvent e){
					
					
				}
				
				public void keyReleased(KeyEvent e){
					//System.out.println("Key Released"+e.getExtendedKeyCode());
					if(e.getKeyCode()==10){
						//System.out.println("you have preseed enter button");
						String contentToSend =messageInput.getText();
						messageArea.append("Me : " +contentToSend +"\n");
						out.println(contentToSend);
						out.flush();
						messageInput.setText("");
						messageInput.requestFocus();
						
						
					}
					
				}

				
				
				});
		
		
	}


	private void createGUI() {
		//GUI Code...
		
		this.setTitle("Server Messenger");
		this.setSize(300, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon("image/letter.png").getImage()); 

		//Component Code ...
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setIcon(new ImageIcon("image/msg.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		messageArea.setEditable(false);
		
		//adding components to frame..
		
				this.add(heading,BorderLayout.NORTH);
				JScrollPane jScrollPane = new JScrollPane(messageArea);
				this.add(jScrollPane,BorderLayout.CENTER);
				this.add(messageInput,BorderLayout.SOUTH);
				this.setVisible(true);
				
		
	}


	public void startReading(){
		//This thread is use to read the data 
		
		 Runnable r1=()->{
			 System.out.println("Reader Started");
				try {
			 while(true){
				 
			
					String msg= br.readLine();
					if(msg.equalsIgnoreCase("exit")){
						System.out.println("Client Terminated The Chat");
						
						socket.close();
						
						break;
					}
					//System.out.println("Client:"+msg);
					 messageArea.append("Client :" +msg+"\n");
				} 
				}
				catch (IOException e) {
					
					e.printStackTrace();
					System.out.println("Connection  Closed");
				}
			 
	    		
	    	};
	    	new Thread(r1).start();
			
		
	}

    public void startWriting(){
	  //This thread will take the data from user and send it too
    	Runnable r2=()->{
    		
    		System.out.println("Writer Started");
    		try {
    		while(!socket.isClosed()){
    			
    		BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
    		
    			
				String content = br1.readLine();
				out.println(content);
				out.flush();
				
				if(content.equals("exit")){
					
					socket.close();
					break;
				}
				
				
			} 
    		System.out.println("Connection is Closed");
    		}
    		catch (IOException e) {
				
				e.printStackTrace();
    			
			}
    	
    		
    	};
    	
		new Thread(r2).start();
		
		
	}
	
	
	
	public static void main(String[]args){
		System.out.println("This is Server ...going to start Server");
		
		new Server();
	}
	

}
