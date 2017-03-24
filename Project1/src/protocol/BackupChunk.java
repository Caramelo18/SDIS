package protocol;

import chunk.Chunk;
import message.MessageGenerator;
import peer.Peer;
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
		// Start listening to STORED for this specific fileId
		
		int attempts = 0;
		int waitingTime = (int)Math.pow(2, attempts);
		
		boolean running = true;
		while(running)
		{
			// Clear STORED count for this specific fileId
			
			System.out.println("Sending");
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
			
			// Get STORED count
			int storedCount = 1;
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
					System.out.println("Trying again");
					waitingTime = (int)Math.pow(2, attempts);
				}
			}
			else
			{
				System.out.println("Finished with the desired replication degree");
				running = false;
			}
		}
	}

}
