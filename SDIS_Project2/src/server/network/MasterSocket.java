package server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MasterSocket implements Runnable
{
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	public MasterSocket(Socket socket)
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
			System.out.println("Error creating the Master Socket Listener");
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
				Server.removeMe(this);
			}
			
			if(message != null)
			{
				handleMessage(message);
			}
		}
	}
}
