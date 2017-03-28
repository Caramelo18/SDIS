package peer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import files.FileManager;
import protocol.Backup;
import socket.SenderSocket;
import socket.ThreadedMulticastSocketListener;

public class Peer implements RMI
{
	// Peer Information
	private static String protocolVersion;
	private static int serverId;
	private static String serviceAccessPoint;
	
	// Socket Listeners
	private static ThreadedMulticastSocketListener MC;
	private static ThreadedMulticastSocketListener MDB;
	private static ThreadedMulticastSocketListener MDR;
	private static SenderSocket SS;
	
	public static void main(String[] args)
	{
		// Temporary Arguments Initialization
		String[] addresses = {"224.1.1.1", "224.2.2.2", "224.3.3.3"};
		int[] ports = {5000, 5001, 5002};
		protocolVersion = "1.0";
		// serverId = null;
		serviceAccessPoint = "RMI";
		
		initListeners(addresses, ports);
		SS = new SenderSocket();
		
		initRMI();
		FileManager FM = new FileManager();
		
		// new Thread(new Backup("../Disk/pena.bmp", 1)).start();
	}
	
	// INITS
		
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
	
	private static void initRMI()
	{
		Peer peer = new Peer();
		
		try
		{
			RMI rmi = (RMI) UnicastRemoteObject.exportObject(peer, 0);
			// Registry registry = LocateRegistry.getRegistry();
			Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(serviceAccessPoint, rmi); // Rebind not bind, to prevent already bound exception
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	// GETS
	
	public static String getProtocolVersion()
	{
		return protocolVersion;
	}
	
	public static int getServerId()
	{
		return serverId;
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

	// OTHER METHODS
	
	@Override
	public void backup(String filename, int replicationDegree) throws RemoteException
	{
		System.out.println("BACKUP was called");
		new Thread(new Backup(filename, replicationDegree)).start();
	}

	@Override
	public void restore(String filename) throws RemoteException
	{
		System.out.println("RESTORE was called");
	}

	@Override
	public void delete(String filename) throws RemoteException
	{
		System.out.println("DELETE was called");
	}

	@Override
	public void reclaim(int kbytes) throws RemoteException
	{
		System.out.println("RECLAIM was called");
	}

	@Override
	public void state() throws RemoteException
	{
		System.out.println("STATE was called");
	}
	
}
