package protocol;

import java.util.ArrayList;

import chunk.Chunk;
import data.DataManager;
import files.FileSplitter;
import peer.Peer;

public class Backup implements Runnable
{
	private FileSplitter FS;
	
	public Backup(String filename, int replicationDegree)
	{
		FS = new FileSplitter(filename, replicationDegree);
	}

	@Override
	public void run()
	{
		DataManager DM = Peer.getDataManager();
		int result = DM.addBackedUpData(FS.getFilename(), FS.getFileID());
		
		if(result == 1)
		{
			System.out.println("File already backed up");
			return;
		}
		
		if(result == 2)
		{
			System.out.println("A different file with the same filename is backed up");
			
			// TODO
			
			return;
		}
		
		ArrayList<Chunk> chunks = FS.getChunkList();
		
		for(int i = 0; i < chunks.size(); i++)
		{
			Thread thread = new Thread(new BackupChunk(chunks.get(i)));
			thread.start();
		}
	}
}
