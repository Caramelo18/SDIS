package message;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import chunk.Chunk;
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
	
	private static volatile boolean receivedChunk = false;
	
	public MessageHandler(DatagramPacket packet)
	{	
		this.packet = packet;	
	}
	
	private void splitMessage()
	{
		byte[] packetData = new byte[packet.getLength()];
		System.arraycopy(packet.getData(), packet.getOffset(), packetData, 0, packet.getLength());
		
		int delimiterIndex = indexOf(packetData, MessageGenerator.CRLF.getBytes());
		
		byte[] headerBytes = Arrays.copyOfRange(packetData, 0, delimiterIndex);
		body = Arrays.copyOfRange(packetData, delimiterIndex + 4, packetData.length);
		
		String headerString = new String(headerBytes, 0, headerBytes.length).trim();
		headerTokens = headerString.split("(?<=[\\S])[ ]+(?=[\\S])");
		
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
			this.receivedChunk = false;
			Random rand = new Random();
			
			int waitTime = rand.nextInt(400);
			
			long currTime = System.currentTimeMillis();
			
			while(System.currentTimeMillis() - currTime < waitTime){}
			
			System.out.println("Waited: " + (System.currentTimeMillis() - currTime));
			if(this.receivedChunk){
				System.out.println("Received chunk, not sending");
				return;
			}
			
			System.out.println("GETCHUNK RECEIVED");
			byte[] read = FileManager.getChunk(headerTokens[3], Integer.valueOf(headerTokens[4]));
			System.out.println("READ: " + read.length);
			if(read != null)
			{
				Chunk chunk = new Chunk(headerTokens[3], Integer.valueOf(headerTokens[4]), 0, read);
				byte[] buf = MessageGenerator.generateCHUNK(chunk);
				Peer.getMDB().sendPacket(buf);
			}
			break;
			
		case "CHUNK":
			this.receivedChunk = true;
			System.out.println("CHUNK: " + body.length);
			ChunkRec.addMessage(headerTokens[3], Integer.valueOf(headerTokens[4]), body);
			break;
			
		case "DELETE":
			System.out.println("DELETE RECEIVED");
			
			String fileID = headerTokens[3];
			
			ArrayList<Integer> chunks = DM.getOwnedFileChunks(fileID);
			
			for(int i = 0; i < chunks.size(); i++)
				FileManager.deleteChunk(fileID, chunks.get(i));
			
			Peer.getDataManager().deleteChunks(fileID);

			break;
			
		case "REMOVED":

			break;
		}
	}

	@Override
	public void run()
	{
		splitMessage();
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
