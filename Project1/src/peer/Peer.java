package peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import files.FileManager;
import socket.ThreadedMulticastSocket;

public class Peer
{
	// Peer Information
	 private static String protocolVersion;
	 private static int serverId;
	 // service access point
	
	// Socket Listeners
	private static ThreadedMulticastSocket MC;
	private static ThreadedMulticastSocket MDB;
	private static ThreadedMulticastSocket MDR;
	
	public static void main(String[] args)
	{
		// Temporary Initialization
		String[] addresses = {"224.1.1.1", "224.2.2.2", "224.3.3.3"};
		int[] ports = {5000, 5001, 5002};
		
		initListeners(addresses, ports);
		
		FileManager FM = new FileManager();
	}
	
	public static String getProtocolVersion()
	{
		return protocolVersion;
	}
	
	public static int getServerId()
	{
		return serverId;
	}
	
	private static void initListeners(String[] addresses, int[] ports)
	{
		try
		{
			MC = new ThreadedMulticastSocket(InetAddress.getByName(addresses[0]), ports[0]);
			MDB = new ThreadedMulticastSocket(InetAddress.getByName(addresses[1]), ports[1]);
			MDR = new ThreadedMulticastSocket(InetAddress.getByName(addresses[2]), ports[2]);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		new Thread(MC).start();
		new Thread(MDB).start();
		new Thread(MDR).start();
		
		boolean socketsReady = false;
		while(!socketsReady)
		{
			socketsReady = (MC.isReady() && MDB.isReady() && MDR.isReady());
		}
		System.out.println("Sockets Ready");
	}
}
