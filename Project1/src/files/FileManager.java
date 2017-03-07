package files;

import java.io.File;

public class FileManager
{
	public FileManager()
	{	
		File originalFiles = new File("../Original_Files");
		File storedChunks = new File("../Stored_Chunks");
		
		createDir(originalFiles);
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
}
