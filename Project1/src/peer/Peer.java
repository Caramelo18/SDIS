package peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import data.DataManager;
import files.FileManager;
import protocol.Backup;
import protocol.Delete;
import protocol.Reclaim;
import protocol.Restore;
import received.ChunkRec;
import received.Stored;
import socket.SenderSocket;
import socket.ThreadedMulticastSocketListener;

// TESTE "../Files/pena.bmp"

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
	
	// Data Manager
	private volatile static DataManager DM;
	
	// Disk Space
	private static long diskSpaceBytes = 1 * 1000 * 1000 * 1000; // 1 GB
	
	public static void main(String[] args)
	{
		// Temporary Arguments Initialization
		String[] addresses = {"224.1.1.1", "224.2.2.2", "224.3.3.3"};
		int[] ports = {5000, 5001, 5002};
		protocolVersion = "1.0";
		serverId = 4;
		serviceAccessPoint = "RMI" + serverId;
		
		initListeners(addresses, ports);
		SS = new SenderSocket();
		FileManager.initFileManager();
		
		File f = new File("../Peer" + Peer.getServerId() + "/metadata.ser");
		if(f.exists())
		{
			try
			{
				FileInputStream fin = new FileInputStream("../Peer" + Peer.getServerId() + "/metadata.ser");
				ObjectInputStream ois = new ObjectInputStream(fin);
				DM = (DataManager) ois.readObject();
				ois.close();
				fin.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			DM = new DataManager();
		}
		
		initRMI();
		Stored.initStored();
		ChunkRec.initChunkRec();
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
			Registry registry = LocateRegistry.getRegistry();
            registry.rebind(serviceAccessPoint, rmi);
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
	
	public static DataManager getDataManager()
	{
		return DM;
	}
	
	public static long getDiskSpaceBytes()
	{
		return diskSpaceBytes;
	}

	// OTHER METHODS
	
	@Override
	public void backup(String filename, int replicationDegree) throws RemoteException
	{
		if(replicationDegree < 1 || replicationDegree > 9)
			System.out.println("Replication degree must be between 1 and 9");
		
		String filenameWithPath = "../Peer" + Peer.getServerId() + "/Files/" + filename;
		new Thread(new Backup(filenameWithPath, replicationDegree)).start();	
	}

	@Override
	public void restore(String filename) throws RemoteException
	{
		String filenameWithPath = "../Peer" + Peer.getServerId() + "/Files/" + filename;
		new Thread(new Restore(filenameWithPath)).start();
	}

	@Override
	public void delete(String filename) throws RemoteException
	{
		String filenameWithPath = "../Peer" + Peer.getServerId() + "/Files/" + filename;
		new Thread(new Delete(filenameWithPath)).start();
	}

	@Override
	public void reclaim(int kbytes) throws RemoteException
	{
		diskSpaceBytes = kbytes * 1000;
		if(FileManager.getChunksSize() > diskSpaceBytes)
		{
			// TODO
			long toReclaim = FileManager.getChunksSize() - diskSpaceBytes;
			new Thread(new Reclaim(toReclaim, this.getDataManager().getStoredData())).start();
		}
	}

	@Override
	public String state() throws RemoteException
	{
		return DM.toString();
	}
	
}
