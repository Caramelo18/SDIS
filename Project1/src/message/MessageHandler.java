package message;

import java.net.DatagramPacket;
import java.util.Arrays;

import data.DataManager;
import files.FileManager;
import peer.Peer;
import received.ChunkRec;
import received.Stored;
import socket.SenderSocket;

public class MessageHandler implements Runnable
{
	private DatagramPacket packet;
	private String[] headerTokens;
	private byte[] body;
	
	public MessageHandler(DatagramPacket packet)
	{	
		this.packet = packet;	
		this.splitMessage();
	}
	
	private void splitMessage()
	{
		byte[] packetData = new byte[packet.getLength()];
		System.arraycopy(packet.getData(), packet.getOffset(), packetData, 0, packet.getLength());
		
		int delimiterIndex = indexOf(packetData, MessageGenerator.CRLF.getBytes());
		
		byte[] headerBytes = Arrays.copyOfRange(packetData, 0, delimiterIndex - 1);
		body = Arrays.copyOfRange(packetData, delimiterIndex + 2, packetData.length);
		
		String headerString = new String(headerBytes, 0, headerBytes.length);
		headerTokens = headerString.split(" ");
		
		parseMessage();
	}
	
	private void parseMessage()
	{
		if(Integer.valueOf(headerTokens[2]) == Peer.getServerId()){
			// System.out.println("Receiving packets from self");
			return;
		}
		else if(!headerTokens[1].equals(Peer.getProtocolVersion()))
		{
			System.out.println("Different protocol version");
			return;
		}
		else
		{
			// System.out.println("Receiving packets from outside");
		}
		
		String messageType = headerTokens[0];
		DataManager DM = Peer.getDataManager();
		
		switch(messageType)
		{
		case "PUTCHUNK":
			
			System.out.println("BODY LEN: " + body.length);
			
			if(FileManager.storeChunk(headerTokens[3], Integer.valueOf(headerTokens[4]), body, Integer.valueOf(headerTokens[5])))
			{
				byte[] response = MessageGenerator.generateSTORED(headerTokens[3], headerTokens[4]);
				Peer.getSenderSocket().sendPacket(response, SenderSocket.Destination.MC);
			}
			
			break;
			
		case "STORED":
			
			Stored.addMessage(headerTokens[3], Integer.valueOf(headerTokens[4]), Integer.valueOf(headerTokens[2]));
			break;
			
		case "GETCHUNK":

			ChunkRec.addMessage(headerTokens[3], Integer.valueOf(headerTokens[4]), body);
			break;
			
		case "CHUNK":

			break;
			
		case "DELETE":

			break;
			
		case "REMOVED":

			break;
		}
	}

	@Override
	public void run()
	{
		String received = new String(packet.getData(), 0, packet.getLength());
		String[] parts = received.split(MessageGenerator.CRLF);

	}
	
	public static int indexOf(byte[] list, byte[] element)
	{
	    for(int i = 0; i < list.length - element.length + 1; ++i)
	    {
	        boolean found = true;
	        
	        for(int j = 0; j < element.length; ++j)
	        {
	           if (list[i + j] != element[j])
	           {
	               found = false;
	               break;
	           }
	        }
	        
	        if(found)
	        	return i;
	     }
	   return -1;  
	} 
}
