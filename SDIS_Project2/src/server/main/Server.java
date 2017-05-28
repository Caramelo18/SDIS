package server.main;

import java.io.*;
import java.net.InetAddress;

import javax.net.ssl.*;

import server.logic.*;
import server.network.*;

public class Server
{
	private static SSLServerSocket serverSocket;
	private static SSLSocket masterSocketOne;
	private static SSLSocket masterSocketTwo;
	private static int port = 5000;
	
	public static void main(String[] args)
	{	
		PeerChannelsStore.PeerChannelsStoreInit();
		initSocket(port);
		startSocketServerListener();
		
		connectToOtherMaster(1);
		connectToOtherMaster(2);
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

	public static void connectToOtherMaster(int index)
	{
		SSLSocketFactory sf = (SSLSocketFactory) SSLSocketFactory.getDefault();
		
		int connectingPort = getNextPort(index);
		
		boolean connected = false;
		
		try
		{
			if(index == 1 && masterSocketOne == null)
			{
				masterSocketOne = (SSLSocket) sf.createSocket(InetAddress.getByName("localhost"), connectingPort);
				connected = true;
			}
			else if(masterSocketTwo == null)
			{
				masterSocketTwo = (SSLSocket) sf.createSocket(InetAddress.getByName("localhost"), connectingPort);
				connected = true;
			}
		}
		catch (IOException e)
		{
			
		}
		
		if(!connected)
			return;
		
		try
		{
			PrintWriter PW = null;
			
			if(index == 1 && masterSocketOne != null)
				PW = new PrintWriter (masterSocketOne.getOutputStream(), true);
			else if(masterSocketTwo != null)
				PW = new PrintWriter (masterSocketTwo.getOutputStream(), true);
				
			if(PW != null)
				PW.println("Server");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static int getNextPort(int index)
	{
		if(port == 5000)
		{
			if(index == 1)
			{
				return 5001;
			}
			else
			{
				return 5002;
			}
		}
		
		if(port == 5001)
		{
			if(index == 1)
			{
				return 5000;
			}
			else
			{
				return 5002;
			}
		}
		
		if(port == 5002)
		{
			if(index == 1)
			{
				return 5000;
			}
			else
			{
				return 5001;
			}
		}
		
		return -1;
	}
}