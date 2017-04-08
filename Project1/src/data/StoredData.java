package data;

import java.io.Serializable;
import java.util.ArrayList;

import peer.Peer;

public class StoredData implements Serializable
{
	private String fileId;
	private int chunkNo;
	private int desiredReplicationDegree;
	private ArrayList<Integer> peers;
	private int size;
	
	public StoredData(String fileId, int chunkNo, int desiredReplicationDegree, ArrayList<Integer> peers, int size)
	{
		this.fileId = fileId;
		this.chunkNo = chunkNo;
		this.desiredReplicationDegree = desiredReplicationDegree;
		this.peers = peers;
		this.size = size;
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
	
	public String toString()
	{
		String ret = "File ID: " + this.fileId + "  -  " + "Chunk No " + String.valueOf(this.chunkNo) + "\n";
		ret += "Chunk Size(kbytes): " + String.valueOf(this.size) + "  -  ";
		ret += "Number of owners: " + String.valueOf(this.peers.size() + 1);
		
		ret += " ( ";
		for(int i = 0; i < this.peers.size(); i++)
		{
			ret += String.valueOf(this.peers.get(i)) + " ";
		}

		ret += Peer.getServerId() + " ) ";
		
		ret += "\n" + "\n";
		
		
		
		return ret;
	}
}
