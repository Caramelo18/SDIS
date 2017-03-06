package peer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import socket_listener.SocketListener;

public class Peer
{
	private static Integer ID;
	private static SocketListener MC;
	private static SocketListener MDB;
	private static SocketListener MDR;

	public static void main(String[] args)
	{
		ID = Integer.parseInt(args[0]);
		
		try
		{
			InetAddress MC_Address = InetAddress.getByName(args[1]);
			Integer MC_Port = Integer.parseInt(args[2]);
			MC = new SocketListener(MC_Address, MC_Port);
			
			InetAddress MDB_Address = InetAddress.getByName(args[3]);
			Integer MDB_Port = Integer.parseInt(args[4]);
			MDB = new SocketListener(MDB_Address, MDB_Port);
			
			InetAddress MDR_Address = InetAddress.getByName(args[5]);
			Integer MDR_Port = Integer.parseInt(args[6]);
			MDR = new SocketListener(MDR_Address, MDR_Port);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		
	}

}
