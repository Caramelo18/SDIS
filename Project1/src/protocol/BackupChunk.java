package protocol;

import chunk.Chunk;
import message.MessageGenerator;
import peer.Peer;

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
		byte[] message = MessageGenerator.generatePUTCHUNK(chunk);
		Peer.MDB.sendPacket(message);
	}

}
