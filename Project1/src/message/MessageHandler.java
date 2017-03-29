package message;

import java.net.DatagramPacket;
import java.util.Arrays;

import files.FileManager;
import peer.Peer;
import protocol.Backup;
import protocol.Stored;
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
		byte[] packetData = packet.getData();
		
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
		switch(messageType)
		{
		case "PUTCHUNK":
			//ver se o disco j� est� cheio
			
			FileManager.storeChunk(headerTokens[3], headerTokens[4], body);
			
			byte[] response = MessageGenerator.generateSTORED(headerTokens[3], headerTokens[4]);
			Peer.getSenderSocket().sendPacket(response, SenderSocket.Destination.MC);
			break;
			
		case "STORED":
			
			// Se for um file que est� a fazer store, atualizar o replication degree e responder stored na mesma
			
			Stored.addMessage(headerTokens[3], Integer.valueOf(headerTokens[4]), Integer.valueOf(headerTokens[2]));
			break;
			
		case "GETCHUNK":

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
	
	/*
	private void parseType(String[] arr)
	{
		this.version = arr[1];
		this.senderID = Integer.valueOf(arr[2]);
		this.fileID = Integer.valueOf(arr[3]);
		
		switch(this.messageType){
		case("PUTCHUNK"):
			this.chunkNo = Integer.valueOf(arr[4]);
			this.replicationDegree = Integer.valueOf(arr[5]);
			break;
		case("STORED"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;
		case("GETCHUNK"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;
		case("CHUNK"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;		
		case("REMOVED"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;
		}
	}
	*/
}
