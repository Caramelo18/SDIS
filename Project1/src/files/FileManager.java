package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import chunk.Chunk;

public class FileManager
{
	public FileManager()
	{	
		File disk = new File("../Disk");
		File storedChunks = new File("../Stored_Chunks");
		
		createDir(disk);
		createDir(storedChunks);
	}
	
	public void createDir(File f)
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
		String filename = "../Stored_Chunks/" + fileID + "-" + chunkNo;
		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			fos.write(body);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
