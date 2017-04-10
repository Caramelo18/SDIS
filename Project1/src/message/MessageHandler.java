package message;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import chunk.Chunk;
import data.DataManager;
import files.FileManager;
import files.FileSplitter;
import peer.Peer;
import protocol.BackupChunk;
import received.ChunkRec;
import received.Stored;
import socket.SenderSocket;

public class MessageHandler implements Runnable
{
	private DatagramPacket packet;
	private String[] headerTokens;
	private byte[] body;
		
	private static volatile ArrayList<String> receivedChunks = new ArrayList<String>();;
	
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
	
	private synchronized void parseMessage()
	{
		if(Integer.valueOf(headerTokens[2]) == Peer.getServerId())
		{
			return;
		}
		else if(peerHasLowerVersion(Peer.getProtocolVersion(), headerTokens[1]))
		{
			System.out.println("Lower protocol version");
			return;
		}
		
		String messageType = headerTokens[0];
		DataManager DM = Peer.getDataManager();
		
		switch(messageType)
		{
		case "PUTCHUNK":
			
			if(Peer.getDataManager().isInitiatorPeer(headerTokens[3]))
				return;
			
			System.out.println("BODY LEN: " + body.length);
			
			Random randPut = new Random();
			int waitTimePut = randPut.nextInt(400);
			
			try
			{
				Thread.sleep(waitTimePut);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(FileManager.storeChunk(headerTokens[3], Integer.valueOf(headerTokens[4]), body, Integer.valueOf(headerTokens[5])))
			{
				byte[] response = MessageGenerator.generateSTORED(headerTokens[3], headerTokens[4]);
				Peer.getSenderSocket().sendPacket(response, SenderSocket.Destination.MC);
				System.out.println("SENT STORED");
			}
			
			break;
			
		case "STORED":
			
			Stored.addMessage(headerTokens[3], Integer.valueOf(headerTokens[4]), Integer.valueOf(headerTokens[2]));
			System.out.println("RECEIVED STORED");
			
			break;
			
		case "GETCHUNK":
			String chunkF = headerTokens[3] + "-" + headerTokens[4];

			Random rand = new Random();
			int waitTime = rand.nextInt(400);
			
			try
			{
				Thread.sleep(waitTime);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if(receivedChunks.contains(chunkF))
			{
				System.out.println("Received chunk, not sending");
				return;
			}
			
			byte[] read = FileManager.getChunk(headerTokens[3], Integer.valueOf(headerTokens[4]));
			
			if(read != null)
			{
				Chunk chunk = new Chunk(headerTokens[3], Integer.valueOf(headerTokens[4]), 0, read);
				byte[] buf = MessageGenerator.generateCHUNK(chunk);
				Peer.getMDB().sendPacket(buf);
			}
			break;
			
		case "CHUNK":
			String chunk = headerTokens[3] + "-" + headerTokens[4];
			
			if(!receivedChunks.contains(chunk)){
				receivedChunks.add(chunk);
				System.out.println("Received chunk " + chunk);
			}
			
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
			System.out.println("REMOVED RECEIVED");
			
			int peerId = Integer.valueOf(headerTokens[2]);
			String fileId = headerTokens[3];
			int chunkNo = Integer.valueOf(headerTokens[4]);
			
			Peer.getDataManager().removeStoredChunk(fileId, chunkNo, peerId);
			Peer.getDataManager().removeBackedUpDataPeer(fileId, chunkNo, peerId);
			
			if(Peer.getDataManager().validReplicationDegree(fileId, chunkNo))
				return;
			
			int desiredReplicationDegree = Peer.getDataManager().getDesiredReplicationDegree(fileId);
			recoverReplicationDegree(fileId, chunkNo, desiredReplicationDegree);

			break;
		}
	}
	
	public boolean peerHasLowerVersion(String peerVersion, String messageVersion)
	{
		if(peerVersion.charAt(0) < messageVersion.charAt(0))
		{
			return true;
		}
		
		if(peerVersion.charAt(0) == messageVersion.charAt(0) && peerVersion.charAt(2) < messageVersion.charAt(2))
		{
			return true;
		}
		
		return false;
	}

	@Override
	public void run()
	{
		splitMessage();
	}
	
	public synchronized static int indexOf(byte[] list, byte[] element)
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
	
	private void recoverReplicationDegree(String fileId, int chunkNo, int replicationDegree)
	{
		Random r = new Random();
		int waitTime1 = r.nextInt(400);
		
		try
		{
			Thread.sleep(waitTime1);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		String chunkName = fileId + "-" + String.valueOf(chunkNo);
		if(receivedChunks.contains(chunkName))
		{
			System.out.println("Received chunk, not sending");
			return;
		}
		
		String path = "../Peer" + Peer.getServerId() + "/Chunks/" + chunkName;
		File file = new File(path);
		
		if(!file.exists())
			return;
		
		BufferedInputStream bufinst = null;
		try {
			bufinst = new BufferedInputStream (new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] body = new byte[FileSplitter.chunkSize];
		try {
			bufinst.read(body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Chunk chunk = new Chunk(fileId, chunkNo, replicationDegree, body);
		
		Thread thread = new Thread(new BackupChunk(chunk));
		thread.start();
				
	}
}
