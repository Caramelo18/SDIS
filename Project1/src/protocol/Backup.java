package protocol;

import java.rmi.RemoteException;
import java.util.ArrayList;

import chunk.Chunk;
import data.DataManager;
import files.FileSplitter;
import peer.Peer;

public class Backup implements Runnable
{
	private FileSplitter FS;
	private int desiredReplicationDegree;
	
	public Backup(String filename, int replicationDegree)
	{
		FS = new FileSplitter(filename, replicationDegree);
		this.desiredReplicationDegree = replicationDegree;
	}

	@Override
	public void run()
	{
		DataManager DM = Peer.getDataManager();
		boolean success = DM.addBackedUpData(FS.getFilename(), FS.getFileID(), desiredReplicationDegree);
		
		if(!success)
		{
			System.out.println("File already backed up");
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
