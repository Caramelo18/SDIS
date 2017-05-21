package server.main;

import java.io.*;

import javax.net.ssl.*;

import server.logic.*;
import server.network.*;

public class Server
{
	private static SSLServerSocket serverSocket;
	
	public static void main(String[] args)
	{	
		PeerChannelsStore.PeerChannelsStoreInit();
		initSocket(5000);
		startSocketServerListener();
	}
	
	public static void initSocket(int port)
	{
		System.setProperty("javax.net.ssl.trustStore", "truststore");
		// System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		System.setProperty("javax.net.ssl.keyStore", "server.keys");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault(); 
		
		try
		{
			serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
		}
		catch (IOException e)
		{
			System.out.println("Could not listen on port: " + port);
			System.exit(-1);
		}
		
		System.out.println("SSL Server Socket listening on port: " + port);
		
		// Require client authentication 
		serverSocket.setNeedClientAuth(true);
		
		System.out.println("Clients now need authentication");
	}
	
	public static void startSocketServerListener()
	{
		SSLSocketServerListener socketServerListener = new SSLSocketServerListener(serverSocket);
		Thread t = new Thread(socketServerListener);
		t.start();
				
		System.out.println("SSL Socket Server Listener started");
	}

}