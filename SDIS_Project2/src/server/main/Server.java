package server.main;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.*;

import server.logic.*;
import server.network.*;

public class Server
{
	private static SSLServerSocket serverSocket;
	private static ArrayList<Socket> masterServers;
	private static int port = 5002;
	
	public static void main(String[] args)
	{	
		PeerChannelsStore.PeerChannelsStoreInit();
		initSocket(port);
		startSocketServerListener();
		initConnectionToMasters();
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
	
	public static void initConnectionToMasters()
	{
		masterServers = new ArrayList<Socket>();
		
		boolean connected = true;
		Socket socket = null;
		
		try
		{
			socket = new Socket(InetAddress.getByName("localhost"), getNextPort(1) + 100);
		}
		catch(Exception e)
		{
			connected = false;
		}
		
		if(connected && socket != null)
		{
			masterServers.add(socket);
			System.out.println("Connected to a new master");
		}
		
		connected = true;
		socket = null;
		
		try
		{
			socket = new Socket(InetAddress.getByName("localhost"), getNextPort(2) + 100);
		}
		catch(Exception e)
		{
			connected = false;
		}
		
		if(connected && socket != null)
		{
			masterServers.add(socket);
			System.out.println("Connected to a new master");
		}
		
		startSocketServerMasterListener();
	}
	
	public static void startSocketServerMasterListener()
	{	
		try
		{
			ServerSocket serverSocket = new ServerSocket(port + 100);
			
			MasterSocketListener socketServerListener = new MasterSocketListener(serverSocket);
			Thread t = new Thread(socketServerListener);
			t.start();
					
			System.out.println("Master Socket Server Listener started");
		}
		catch (IOException e)
		{
			System.out.println("Error opening Server Socket for Masters");
			System.exit(-1);
		}
	}
	
	public static void addMasterSocket(Socket socket)
	{
		masterServers.add(socket);
	}
	
	public static void removeMe(MasterSocket masterSocket)
	{
		
	}
	
	public static int getPort()
	{
		return port;
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