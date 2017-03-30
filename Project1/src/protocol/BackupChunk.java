package protocol;

import chunk.Chunk;
import message.MessageGenerator;
import peer.Peer;
import received.Stored;
import socket.SenderSocket;

public class BackupChunk implements Runnable
{
	Chunk chunk;
	
	public BackupChunk(Chunk chunk)
	{
		this.chunk = chunk;
	}

	@Override
	public void run()
	{	
		int attempts = 0;
		int waitingTime = (int)Math.pow(2, attempts);
		
		boolean running = true;
		while(running)
		{			
			byte[] message = MessageGenerator.generatePUTCHUNK(chunk);
			// Peer.getMDB().sendPacket(message);
			Peer.getSenderSocket().sendPacket(message, SenderSocket.Destination.MDB);
			
			try
			{
				Thread.sleep((waitingTime * 1000));
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			int storedCount = Stored.peerCount(chunk.getFileId(), chunk.getChunkNo());
			if(storedCount < chunk.getReplicationDegree())
			{
				attempts++;
				
				if(attempts >= 5)
				{
					System.out.println("Finished without the desired replication degree");
					running = false;
				}
				else
				{
					System.out.println("Trying again for chunkNo: " + chunk.getChunkNo());
					waitingTime = (int)Math.pow(2, attempts);
				}
			}
			else
			{
				running = false;
			}
		}
	}

}
