package protocol;

import java.io.File;
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
		System.out.println("HERE");
		
		DataManager DM = Peer.getDataManager();
		String fileId = DM.getFileId(filename);
		if(fileId == null)
		{
			System.out.println("No such file backed up");
			return;
		}
		
		File f = new File("../Peer" + Peer.getServerId() + "/Files/" + filename);
		if(f.exists())
		{
			System.out.println("Can't restore a file that is already in the files folder");
			return;
		}

		System.out.println("HERE");
		
		boolean running = true;
		int chunkNo = 0;
		int attempts = 0;
		
		while(running)
		{
			if(attempts > 3)
			{
				System.out.println("Can't restore chunk");
				return;
			}
			
			byte[] message = MessageGenerator.generateGETCHUNK(fileId, chunkNo);
			Peer.getMDB().sendPacket(message);
			
			long startTime = System.nanoTime();
			boolean pooling = true;
			
			boolean found = false;
			while(pooling)
			{
				byte[] received = ChunkRec.getMessage(fileId, chunkNo);
				if(received != null)
				{
					fileParts.put(chunkNo, received);
					chunkNo++;
					attempts = 0;
					
					if(received.length < 64000)
						running = false;
					
					found = true;
					pooling = false;
				}
				else
					System.out.println("NULL");
				
				if(((double)System.nanoTime()- startTime)/ 1000000 > 500)
				{
					pooling = false;
				}
			}
			
			if(!found)
				attempts++;
		}
		
		System.out.println("HERE");
		
		FileManager.restoreFile(f, fileParts);
	}
}
