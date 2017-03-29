package received;

import java.util.HashMap;

public class ChunkRec
{
	private static volatile HashMap<String, HashMap<Integer, byte[]>> chunkMessages;
	// FileId, ChunkNo, Body
	
	public static void initChunkRec()
	{
		chunkMessages = new HashMap<String, HashMap<Integer, byte[]>>();
	}
	
	public static void addMessage(String filename, Integer chunkNo, byte[] buf)
	{
		HashMap<Integer, byte[]> innerHashMap = chunkMessages.get(filename);
		if(innerHashMap == null)
		{
			innerHashMap = new HashMap<Integer, byte[]>();
			chunkMessages.put(filename, innerHashMap);
		}
		innerHashMap.put(chunkNo, buf);
	}
	
	public static byte[] getMessage(String filename, Integer chunkNo)
	{
		HashMap<Integer, byte[]> innerHashMap = chunkMessages.get(filename);
		if(innerHashMap == null)
			return null;
		
		return innerHashMap.get(chunkNo);
	}
}
