package peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import files.FileManager;
import message.MessageHandler;
import protocol.Backup;
import socket.SenderSocket;
import socket.ThreadedMulticastSocketListener;

public class Peer
{
	// Peer Information
	 private static String protocolVersion;
	 private static int serverId;
	 // service access point
	
	// Socket Listeners
	private static ThreadedMulticastSocketListener MC;
	private static ThreadedMulticastSocketListener MDB;
	private static ThreadedMulticastSocketListener MDR;
	private static SenderSocket SS;
	
	public static void main(String[] args)
	{
		// Temporary Initialization
		String[] addresses = {"224.1.1.1", "224.2.2.2", "224.3.3.3"};
		int[] ports = {5000, 5001, 5002};
		
		initListeners(addresses, ports);
		SS = new SenderSocket();
		
		FileManager FM = new FileManager();
		new Thread(new Backup("../Disk/pena.bmp", 1)).start();
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
			MC = new ThreadedMulticastSocketListener(InetAddress.getByName(addresses[0]), ports[0]);
			MDB = new ThreadedMulticastSocketListener(InetAddress.getByName(addresses[1]), ports[1]);
			MDR = new ThreadedMulticastSocketListener(InetAddress.getByName(addresses[2]), ports[2]);
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
	
	public static ThreadedMulticastSocketListener getMC()
	{
		return MC;
	}
	
	public static ThreadedMulticastSocketListener getMDB()
	{
		return MDB;
	}
	
	public static ThreadedMulticastSocketListener getMDR()
	{
		return MDR;
	}
	
	public static SenderSocket getSenderSocket()
	{
		return SS;
	}
	
}
