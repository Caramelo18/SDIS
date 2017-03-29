package protocol;

import data.DataManager;
import message.MessageGenerator;
import peer.Peer;
import received.ChunkRec;

public class Restore implements Runnable
{
	private String filename;
	
	public Restore(String filename)
	{
		this.filename = filename;
	}

	@Override
	public void run()
	{
		DataManager DM = Peer.getDataManager();
		String fileId = DM.getFileId(filename);
		if(fileId == null)
		{
			System.out.println("No such file backed up");
			return;
		}

		boolean running = true;
		int chunkNo = 0;
		while(running)
		{
			byte[] message = MessageGenerator.generateGETCHUNK(fileId, chunkNo);
			Peer.getMDB().sendPacket(message);
			
			float currTime = System.nanoTime();
			while(ChunkRec.getMessage(fileId, chunkNo) == null)
			{
				
			}
		}
	}

}
