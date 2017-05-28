package server.network;

import java.io.*;
import javax.net.ssl.*;

import server.logic.*;
import server.main.Server;

public class SSLSocketListener implements Runnable
{
	private PeerChannel peerChannel;
	private PrintWriter out;
	private BufferedReader in;
	
	public SSLSocketListener(PeerChannel peerChannel)
	{
		this.peerChannel = peerChannel;
		out = null;
		in = null;
	}

	@Override
	public void run()
	{		
		SSLSocket socket = peerChannel.getSSLSocket();
		
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			PeerChannelsStore.removeSocket(peerChannel);
		}
		
		boolean running = true;
		
		while(running)
		{
			String message = null;
			
			try
			{
				message = in.readLine();
			}
			catch (IOException e)
			{
				PeerChannelsStore.removeSocket(peerChannel);
				running = false;
			}
			
			if(message != null)
			{
				handleMessage(message);
			}
		}
	}
	
	public void handleMessage(String message)
	{		
		String[] messageTokens = message.split(" ");
		
		switch(messageTokens[0])
		{
		case "GetPeers":
			
			System.out.println("Received a request for the peers");
			out.println(PeerChannelsStore.getPeers());
			
			break;
			
		case "Authenticate":
			
			System.out.println("Received an authentication");
			peerChannel.setInfo(Integer.parseInt(messageTokens[1]), Integer.parseInt(messageTokens[2]), Integer.parseInt(messageTokens[3]), Integer.parseInt(messageTokens[4]), Integer.parseInt(messageTokens[5]));
			
			break;
			
		case "Server":
			
			System.out.println("Received a connection from another Master Server");
			Server.connectToOtherMaster(1);
			Server.connectToOtherMaster(2);
			
			break;
			
		case "UPDATE":
			
			if(messageTokens[1].equals("DONE"))
			{
				break;
			}
			else
			{
				PeerChannelsStore.addForeignContact(messageTokens[1], Integer.parseInt(messageTokens[2]), Integer.parseInt(messageTokens[3]), Integer.parseInt(messageTokens[4]), Integer.parseInt(messageTokens[5]), Integer.parseInt(messageTokens[6]));
			}
			
			break;
			
		default:
			
			System.out.println("Received an unknown command");
		
			break;
		}
		
		PeerChannelsStore.printState();
	}
}
