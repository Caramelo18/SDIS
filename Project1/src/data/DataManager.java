package data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import peer.Peer;
import protocol.Stored;

public class DataManager implements Serializable
{
	private ArrayList<StoredData> storedFilesData;
	private ArrayList<BackedUpData> backedUpData;
	
	public DataManager()
	{
		storedFilesData = new ArrayList<StoredData>();
		backedUpData = new ArrayList<BackedUpData>();
	}
	
	public void addStoredFilesData(String fileId, int chunkNo, int desiredReplicationDegree)
	{	
		ArrayList<Integer> peers = Stored.getPeers(fileId, chunkNo);
		if(peers == null)
		{
			peers = new ArrayList<Integer>();
		}
		peers.add(Peer.getServerId());
		
		storedFilesData.add(new StoredData(fileId, chunkNo, desiredReplicationDegree, peers));
		serialize();
	}
	
	public void updateStoredFilesData(String fileId, int chunkNo, Integer peerId)
	{
		for(int i = 0; i < storedFilesData.size(); i++)
		{
			StoredData SD = storedFilesData.get(i);
			if(SD.getFileId().equals(fileId) && SD.getChunkNo() == chunkNo)
			{
				ArrayList<Integer> peers = SD.getPeers();
				if(!peers.contains(peerId))
				{
					peers.add(peerId);
					serialize();
				}
			}
		}
	}
	
	/*
	Retorna 0 - filename não existe
	Retorna 1 - filename existe com o fileId indicado
	Retorna 2 - filename existe com outro fileId
	*/
	public int addBackedUpData(String filename, String fileId)
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
		
		BackedUpData data = new BackedUpData(filename, fileId);
		backedUpData.add(data);
		serialize();
		
		return 0;
	}

	public void serialize()
	{
		FileOutputStream fout;
		try
		{
			fout = new FileOutputStream("../Peer" + Peer.getServerId() + "/metadata.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			
			oos.close();
			fout.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
