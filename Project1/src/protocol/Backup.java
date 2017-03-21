package protocol;

import java.util.ArrayList;

import chunk.Chunk;
import files.FileSplitter;
import message.MessageHandler;

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
		ArrayList<Chunk> chunks = FS.getChunkList();
		
		for(int i = 0; i < chunks.size(); i++)
		{
			new Thread(new BackupChunk(chunks.get(i))).start();
		}
	}
	
}
