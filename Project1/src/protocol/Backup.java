package protocol;

import java.util.ArrayList;
import java.util.HashMap;

import chunk.Chunk;
import files.FileSplitter;
import message.MessageHandler;

public class Backup implements Runnable
{
	private FileSplitter FS;
	private static HashMap<Integer, ArrayList<Integer> > storedChunks; //key - chunkNo, value - list of peers that have the chunk
	private String fileID;
	private int replicationDegree;
	
	public Backup(String filename, int replicationDegree)
	{
		FS = new FileSplitter(filename, replicationDegree);
		this.fileID = FS.getFileID();
		this.replicationDegree = replicationDegree;
		storedChunks = new HashMap<Integer, ArrayList<Integer>>();
	}

	@Override
	public void run()
	{
		ArrayList<Chunk> chunks = FS.getChunkList();
		
		for(int i = 0; i < chunks.size(); i++)
		{
			if(!this.storedChunk(i)){
				Thread thread = new Thread(new BackupChunk(chunks.get(i)));
				thread.start();
			}
			
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
	
	public static void resetStoredChunks()
	{
		storedChunks = new HashMap<Integer, ArrayList<Integer>>();
	}
	
	public static void addStoredChunk(int chunkNo, int peerID)
	{
		if(storedChunks.get(chunkNo) == null)
			storedChunks.put(chunkNo, new ArrayList<Integer>());
		
		storedChunks.get(chunkNo).add(peerID);
		
		storedChunks.forEach( (k, v) -> System.out.println(k + "  " + v));
	}
	
	private boolean storedChunk(int chunkNo)
	{
		if(storedChunks.get(chunkNo) == null)
			return false;
		
		ArrayList<Integer> chunkOwners = storedChunks.get(chunkNo);
		if(chunkOwners.size() < this.replicationDegree)
			return false;
		
		return true;
	}
}
