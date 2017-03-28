package protocol;

import java.util.ArrayList;
import java.util.HashMap;

public class StoredFiles 
{
	private HashMap<String, HashMap<Integer, ArrayList<Integer> > > storedFiles;
	//first key - fileID
	//second key - chunkNo
	//value - list of peers who own the chunk
	
	public StoredFiles()
	{
		storedFiles = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
	}
	
	public void addStoredChunk(String fileID, int chunkNo, int peerID)
	{
		storedFiles.get(fileID).get(chunkNo).add(peerID);
	}
}
