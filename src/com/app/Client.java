package com.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;



public class Client extends JFrame{
	
	Socket socket;
	BufferedReader br;//read the data
	PrintWriter out;//write the data
	private JLabel heading = new JLabel("Client Area") ;
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto",Font.PLAIN,20);
	
	public Client() {
		try{
			System.out.println("Sending Request To Server");
			socket = new Socket("localhost",7776);
			System.out.println("Connection Done");
			
			br = new BufferedReader
					(new InputStreamReader(socket.getInputStream()));
			
			out=new PrintWriter(socket.getOutputStream());
			
			createGUI();
			handleEvents();
			
			startReading();
			//startWriting();
			
			
			
			
		}
		catch(Exception e){
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
	
	
	private void createGUI(){
		
		//GUI Code...
		
		this.setTitle("Client Messanger");
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
		
		//layout code for frame..
		
		//this.setLayout(new BorderLayout());
		
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
						
						
						if(msg.equalsIgnoreCase("Exit"))
						{
							System.out.println("Server Terminated the Chat");
							JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
							messageInput.setEnabled(false);
							
							socket.close();
							break;
						}
						
						 messageArea.append("Server :" +msg+"\n");
					}
				
				 }catch (IOException e) {
						
						e.printStackTrace();
					 //System.out.println("Connection Closed");
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
		System.out.println("This is client");
		new Client();
	}

}
