package server.network;

import java.io.*;
import java.util.ArrayList;

import javax.net.ssl.*;

import server.logic.*;
import server.main.Server;

public class SSLSocketListener implements Runnable
{
	private PeerChannel peerChannel;
	private PrintWriter out;
	private BufferedReader in;
	boolean running;
	
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
		
		running = true;
		
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
			
			ArrayList<String> buffers = Server.getAllBuffers();
			
			for(String messageBuff : buffers)
			{
				out.println(messageBuff);
			}
			
			out.println(PeerChannelsStore.getPeers());
			
			break;
			
		case "Authenticate":
			
			System.out.println("Received an authentication");
			peerChannel.setInfo(Integer.parseInt(messageTokens[1]), Integer.parseInt(messageTokens[2]), Integer.parseInt(messageTokens[3]), Integer.parseInt(messageTokens[4]), Integer.parseInt(messageTokens[5]));
			
			break;
			
		case "StoreMetadata":
			
			int IDs = Integer.parseInt(messageTokens[1]);
			
			try
			{
				byte [] mybytearray  = new byte [100000];
			    InputStream is = peerChannel.getSSLSocket().getInputStream();
			    FileOutputStream fos = new FileOutputStream("../Master/Peer" + IDs);
			    BufferedOutputStream bos = new BufferedOutputStream(fos);
			    int bytesRead = is.read(mybytearray,0,mybytearray.length);
			    int current = bytesRead;
	
			    do
			    {
			         bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
			         if(bytesRead >= 0)
			        	 current += bytesRead;
			    }
			    while(bytesRead > -1);
	
			    bos.write(mybytearray, 0 , current);
			    bos.flush();
			    
			    bos.close();
			    fos.close();
			}
			catch(Exception e)
			{
				break;
			}
			
			break;
			
		case "GetMetadata":
			
			int ID = Integer.parseInt(messageTokens[1]);
			out.println("Metadata");
			
			File myFile = new File("../Master/Peer" + ID);
	        
	        try
	        {
	        	byte [] mybytearray  = new byte [(int)myFile.length()];
	        	FileInputStream fis = new FileInputStream(myFile);
	        	BufferedInputStream bis = new BufferedInputStream(fis);
	        	bis.read(mybytearray,0,mybytearray.length);
	        	peerChannel.getSSLSocket().getOutputStream().write(mybytearray,0,mybytearray.length);
	        	
	        	bis.close();
	        	fis.close();
	        }
	        catch(Exception e)
	        {
	        	break;
	        }
			
			break;
			
		default:
			
			System.out.println("Received an unknown command");
		
			break;
		}
		
		PeerChannelsStore.printState();
	}
}
