package data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import files.FileManager;
import peer.Peer;
import received.Stored;

public class DataManager implements Serializable
{
	private volatile ArrayList<StoredData> storedFilesData;
	private volatile ArrayList<BackedUpData> backedUpData;
	
	public DataManager()
	{
		storedFilesData = new ArrayList<StoredData>();
		backedUpData = new ArrayList<BackedUpData>();
	}
	
	// STORED
	
	public void addStoredFilesData(String fileId, int chunkNo, int desiredReplicationDegree, int size)
	{	
		System.out.println("ADDING A NEW STORED DATA WITH THE PEERS: ");
		System.out.println("CALLING PEERS WITH THE FILE ID: " + fileId + " AND CHUNKNO: " + chunkNo);
		ArrayList<Integer> peers = Stored.getPeers(fileId, chunkNo);
		if(peers == null)
		{
			peers = new ArrayList<Integer>();
		}
		peers.add(Peer.getServerId());
		
		storedFilesData.add(new StoredData(fileId, chunkNo, desiredReplicationDegree, peers, size));
		serialize();
		
		for(int i = 0; i < peers.size(); i++)
		{
			System.out.println(peers.get(i));
		}
		System.out.println("FINISHED");
	}
	
	public void updateStoredFilesData(String fileId, int chunkNo, ArrayList<Integer> newPeers)
	{
		for(int i = 0; i < storedFilesData.size(); i++)
		{
			StoredData SD = storedFilesData.get(i);
			if(SD.getFileId().equals(fileId) && SD.getChunkNo() == chunkNo)
			{
				ArrayList<Integer> peers = SD.getPeers();
				peers = newPeers;
				serialize();
			}
		}
	}
	
	public ArrayList<Integer> getOwnedFileChunks(String fileID)
	{
		ArrayList<Integer> chunks = new ArrayList<Integer>();

		for(int i = 0; i < storedFilesData.size(); i++)
		{
			StoredData SD = storedFilesData.get(i);
			if(SD.getFileId().equals(fileID))
			{
				if(SD.getPeers().contains(Peer.getServerId()))
					chunks.add(SD.getChunkNo());
			}
		}
		
		return chunks;
	}
	
	// BACKUP
	
	/*
	Retorna 0 - filename não existe
	Retorna 1 - filename existe com o fileId indicado
	Retorna 2 - filename existe com outro fileId
	*/
	public int addBackedUpData(String filename, String fileId, int desiredReplicationDegree)
	{
		for(int i = 0; i < backedUpData.size(); i++)
		{
			BackedUpData data = backedUpData.get(i);
			
			if(data.getFilename().equals(filename))
			{
				if(data.getFileId().equals(fileId))
				{
					return 1;
				}
				else
				{
					// TODO
					// Chama o delete para o file antigo
					// Actualiza os registos e os metadados
					
					return 2;
				}
			}
		}
		
		System.out.println("FILENAME: " + filename);
		
		BackedUpData data = new BackedUpData(filename, fileId, desiredReplicationDegree);
		backedUpData.add(data);
		serialize();
		
		return 0;
	}
	
	public void deleteBackedUpData(String filename, String fileID)
	{
		for(int i = 0; i < backedUpData.size(); i++)
		{
			BackedUpData data = backedUpData.get(i);
			if(data.getFileId().equals(fileID))
			{
				backedUpData.remove(i);
				break;
			}
		}
		
		serialize();
	}
	
	public void deleteChunks(String fileID)
	{
		for(int i = 0; i < storedFilesData.size(); i++)
		{
			if(storedFilesData.get(i).getFileId().equals(fileID))
			{
				storedFilesData.remove(i);
				i--;
			}
		}
		serialize();
	}
	
	public String getFileId(String filename)
	{
		for(int i = 0; i < backedUpData.size(); i++)
		{
			BackedUpData BUD = backedUpData.get(i);
			
			if(BUD.getFilename().equals(filename))
				return BUD.getFileId();
		}
		return null;
	}
	
	public void addChunkPeers(String fileId, int chunkNo, ArrayList<Integer> peers)
	{
		for(int i = 0; i < backedUpData.size(); i++)
		{
			BackedUpData bud = backedUpData.get(i);
			if(bud.getFileId().equals(fileId))
			{
				bud.addChunkPeers(chunkNo, peers);
			}
		}
		serialize();
	}
	
	// SERIALIZE

	public void serialize()
	{
		boolean done = false;
		while(!done)
		{
			done = true;
			
			FileOutputStream fout;
			try
			{
				fout = new FileOutputStream("../Peer" + Peer.getServerId() + "/metadata.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(this);
				
				oos.close();
				fout.close();
			}
			catch (Exception e)
			{
				System.out.println("WRITER BUSY");
				done = false;
			}
		}
	}
	
	// TO STRING
	
	public String toString()
	{
		String ret = "Received Chunks Information:  \n";
		
		for(int i = 0; i < storedFilesData.size(); i++)
		{
			if(storedFilesData.get(i) != null)
				ret += storedFilesData.get(i).toString();
		}
		
		ret += "\n\nBacked Up Data Information:  \n";
		for(int i = 0; i < backedUpData.size(); i++)
		{
			if(backedUpData.get(i) != null)
				ret += backedUpData.get(i).toString();
		}
		
		
		ret += "\n\n" + "Total Chunks Size Saved(kbytes): " + FileManager.getChunksSize() + "\n";
		
		return ret;
	}

}
