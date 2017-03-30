package files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

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
		// Falta ver aqui se existe espa�o, pois caso n�o exista retorna false
		
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
	
	public static void restoreFile(File f, HashMap<Integer, byte[]> fileParts)
	{
		try {
			FileOutputStream fos = new FileOutputStream(f);
			fileParts.forEach( (k, v) -> {
				try {
					fos.write(v);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] getChunk(String fileId, int chunkNo)
	{
		String filename = "../Peer" + Peer.getServerId() + "/" + "Chunks/" + fileId + "-" + chunkNo;
		
		File f = new File(filename);
		if(f.exists())
		{
			try (BufferedInputStream bufinst = new BufferedInputStream (new FileInputStream(f)))
			{
				byte[] buffer = new byte[FileSplitter.chunkSize];
				int tmp = bufinst.read(buffer);
				
				byte[] body = new byte[tmp];
				System.arraycopy(buffer, 0, body, 0, tmp);
				return body;
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
