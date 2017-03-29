package data;

import java.io.Serializable;
import java.util.ArrayList;

public class StoredData implements Serializable
{
	private String fileId;
	private String chunk;
	private int desiredReplicationDegree;
	private int currentReplicationDegree;
	
	public StoredData(String fileId, String chunk, int desiredReplicationDegree)
	{
		this.fileId = fileId;
		this.chunk = chunk;
		this.desiredReplicationDegree = desiredReplicationDegree;
		this.currentReplicationDegree = 0;
	}
	
	// GETS
	
	public String getFileId()
	{
		return fileId;
	}
	
	public String getChunk()
	{
		return chunk;
	}
	
	public int getDesiredReplicationDegree()
	{
		return desiredReplicationDegree;
	}
	
	public int getCurrentReplicationDegree()
	{
		return currentReplicationDegree;
	}
}
