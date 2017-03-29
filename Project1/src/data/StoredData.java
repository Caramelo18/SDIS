package data;

import java.io.Serializable;
import java.util.ArrayList;

public class StoredData implements Serializable
{
	private String fileId;
	private int chunkNo;
	private int desiredReplicationDegree;
	private ArrayList<Integer> peers;
	
	public StoredData(String fileId, int chunkNo, int desiredReplicationDegree, ArrayList<Integer> peers)
	{
		this.fileId = fileId;
		this.chunkNo = chunkNo;
		this.desiredReplicationDegree = desiredReplicationDegree;
		this.peers = peers;
	}
	
	// GETS
	
	public String getFileId()
	{
		return fileId;
	}
	
	public int getChunkNo()
	{
		return chunkNo;
	}
	
	public int getDesiredReplicationDegree()
	{
		return desiredReplicationDegree;
	}
	
	public ArrayList<Integer> getPeers()
	{
		return peers;
	}
}
