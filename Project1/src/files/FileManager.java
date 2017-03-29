package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import chunk.Chunk;
import peer.Peer;

public class FileManager
{
	public static void initFileManager()
	{	
		File peerFolder = new File("../Peer" + Peer.getServerId());
		File disk = new File("../Peer" + Peer.getServerId() + "/Files");
		File storedChunks = new File("../Peer" + Peer.getServerId() + "/Chunks");
		
		createDir(peerFolder);
		createDir(disk);
		createDir(storedChunks);
	}
	
	public static void createDir(File f)
	{
		// if the directory does not exist, create it
		if (!f.exists())
		{
		    System.out.println("Creating directory: " + f.getName());
		    boolean result = false;

		    try
		    {
		        f.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se)
		    {
		        //handle it
		    }        
		    if(result)
		    {    
		        System.out.println("DIR created");  
		    }
		}
	}
	
	public static void storeChunk(String fileID, String chunkNo, byte[] body)
	{
		String filename = "../Peer" + Peer.getServerId() + "/" + "Chunks/" + fileID + "-" + chunkNo;
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(body);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
