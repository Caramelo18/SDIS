package protocol;

import java.util.HashMap;

import data.DataManager;
import files.FileManager;
import message.MessageGenerator;
import peer.Peer;
import received.ChunkRec;

public class Restore implements Runnable
{
	private String filename;
	private HashMap<Integer, byte[]> fileParts;
	
	public Restore(String filename)
	{
		this.filename = filename;
		fileParts = new HashMap<Integer, byte[]>();
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
			
			long startTime = System.nanoTime();
			boolean pooling = true;
			
			while(pooling)
			{
				byte[] received = ChunkRec.getMessage(fileId, chunkNo);
				if(received != null)
				{
					fileParts.put(chunkNo, received);
					chunkNo++;
					
					if(received.length < 64000)
						running = false;
					
					pooling = false;
				}
				
				if(((double)startTime - System.nanoTime())/ 1000000 > 500)
				{
					pooling = false;
				}
			}
		}
		
		FileManager.restoreFile(fileParts);
	}
}
