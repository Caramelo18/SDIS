package socket_listener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

import message.Message;

public class SocketListener implements Runnable
{
	private static final int MAX_SIZE = 65000;
	private MulticastSocket socket;
	
	private InetAddress address;
	private Integer port;
	
	public SocketListener(InetAddress address, Integer port)
	{
		this.address = address;
		this.port = port;
	}

	@Override
	public void run()
	{
		// Opening
		System.out.println("Opening Socket");
		System.out.println("Address: " + address.getHostName());
		System.out.println("Port: " + port);
		
		try
		{
			socket = new MulticastSocket(port);
			socket.setTimeToLive(1);
			socket.joinGroup(address);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Receiving
		byte[] buf = new byte[MAX_SIZE];
		boolean running = true;
		while(running)
		{
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try
			{
				socket.receive(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			handlePacket(packet);
		}
		
		// Closing
		System.out.println("Closing Socket");
		socket.close();
	}
	
	public void handlePacket(DatagramPacket packet)
	{
		String response = new String(packet.getData(), 0, packet.getData().length, StandardCharsets.US_ASCII);
		Message message = new Message(response);
		System.out.print("Handling the packet");
	}
}
