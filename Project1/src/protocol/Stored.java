package protocol;

import java.util.ArrayList;
import java.util.HashMap;

public class Stored
{
	private static volatile HashMap<String, HashMap<Integer, ArrayList<Integer>>> storedMessages;
	// FileId, ChunkNo, PeerList
	
	public static void initStored()
	{
		storedMessages = new HashMap<String, HashMap<Integer, ArrayList<Integer>>>();
	}
	
	public static void resetFile(String fileId)
	{
		storedMessages.remove(fileId);
	}
	
	public static void addMessage(String fileId, Integer chunkNo, Integer peerId)
	{
		HashMap<Integer, ArrayList<Integer>> innerHashMap = storedMessages.get(fileId);
		
		if(innerHashMap == null)
		{
			innerHashMap = new HashMap<Integer, ArrayList<Integer>>();
			storedMessages.put(fileId, innerHashMap);
		}
		
		ArrayList<Integer> peerList = innerHashMap.get(chunkNo);
		if(peerList == null)
		{
			peerList = new ArrayList<Integer>();
			innerHashMap.put(chunkNo, peerList);
		}
		
		if(!peerList.contains(peerId))
			peerList.add(peerId);
	}
	
	public static ArrayList<Integer> getPeers(String fileId, Integer chunkNo)
	{
		HashMap<Integer, ArrayList<Integer>> innerHashMap = storedMessages.get(fileId);
		if(innerHashMap == null)
			return null;
		
		ArrayList<Integer> peerList = innerHashMap.get(chunkNo);
		if(peerList == null)
			return null;
		
		return peerList;
	}
	
	public static int peerCount(String fileId, Integer chunkNo)
	{
		HashMap<Integer, ArrayList<Integer>> innerHashMap = storedMessages.get(fileId);
		if(innerHashMap == null)
			return 0;
		
		ArrayList<Integer> peerList = innerHashMap.get(chunkNo);
		if(peerList == null)
			return 0;
		
		return peerList.size();
	}
}
