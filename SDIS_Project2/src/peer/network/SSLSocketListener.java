package peer.network;

import java.io.*;

import javax.net.ssl.SSLSocket;

import peer.main.Peer;

public class SSLSocketListener implements Runnable
{
	private SSLSocket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public SSLSocketListener(SSLSocket socket)
	{
		this.socket = socket;
		out = null;
		in = null;
	}

	@Override
	public void run()
	{
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			System.out.println("Error creating the SSL Socket Listener");
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
				System.out.println("Input stream from SSL Socket Closed");
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
		
		System.out.println(message);
		
		switch(messageTokens[0])
		{		
		case "UPDATE":
			
			if(messageTokens[1].equals("DONE"))
			{
				Peer.getSenderSocket().setContactListUpdated();
			}
			else
			{
				Peer.getSenderSocket().addContact(messageTokens[1], Integer.parseInt(messageTokens[2]), Integer.parseInt(messageTokens[3]), Integer.parseInt(messageTokens[4]), Integer.parseInt(messageTokens[5]), Integer.parseInt(messageTokens[6]));
			}
			
			break;
		
		default:
			
			// System.out.println("Received an unknown command");
		
			break;
		}
	}
	
	public void sendMessage(String message)
	{
		out.println(message);
	}
}
