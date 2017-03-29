package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import chunk.Chunk;
import data.DataManager;
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
	
	public static boolean storeChunk(String fileId, int chunkNo, byte[] body, Integer replicationDegree)
	{		
		DataManager DM = Peer.getDataManager();
		String filename = "../Peer" + Peer.getServerId() + "/" + "Chunks/" + fileId + "-" + chunkNo;
		
		File f = new File(filename);
		if(f.exists())
		{
			
			return true;
		}
		
		// TODO
		// Falta ver aqui se existe espaço, pois caso não exista retorna false
		
		try
		{
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(body);
			fos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		DM.addStoredFilesData(fileId, chunkNo, replicationDegree);
		
		return true;
	}
}
