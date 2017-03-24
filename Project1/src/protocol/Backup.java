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
			Thread thread = new Thread(new BackupChunk(chunks.get(i)));
			thread.start();
			
			/*
			// Sender Socket eliminated the need to wait
			// Keeping this here in case there will be a future need for it
			
			try
			{
				thread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			*/
		}
	}
	
}
