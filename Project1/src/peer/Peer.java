package peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import files.FileManager;
import socket_listener.SocketListener;

public class Peer
{
	private static Integer ID;
	private static SocketListener MC;
	private static SocketListener MDB;
	private static SocketListener MDR;
	
	private static String file = "";
	private static Integer replicationDeg = 0;

	public static void main(String[] args)
	{
		ID = Integer.parseInt(args[0]);
		
		try
		{
			// InetAddress MC_Address = InetAddress.getByName(args[1]);
			// Integer MC_Port = Integer.parseInt(args[2]);
			MC = new SocketListener(InetAddress.getByName("224.1.1.1"), 5000);
			
			// InetAddress MDB_Address = InetAddress.getByName(args[3]);
			// Integer MDB_Port = Integer.parseInt(args[4]);
			MDB = new SocketListener(InetAddress.getByName("224.2.2.2"), 5001);
			
			// InetAddress MDR_Address = InetAddress.getByName(args[5]);
			// Integer MDR_Port = Integer.parseInt(args[6]);
			MDR = new SocketListener(InetAddress.getByName("224.3.3.3"), 5002);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		FileManager FM = new FileManager();
		
		if(args.length == 2)
		{
			file = args[1];
			replicationDeg = Integer.parseInt(args[2]);
		}
	}

}
