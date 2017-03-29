package files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import chunk.Chunk;

public class FileSplitter
{
	private ArrayList<Chunk> chunkList;
	
	private String filename;
	private int replicationDegree;
	private static final int chunkSize = 64000;
	private boolean read;
	
	private String fileID;
	
	public FileSplitter(String filename, int replicationDegree)
	{
		this.chunkList = new ArrayList<Chunk> ();
		this.filename = filename;
		this.replicationDegree = replicationDegree;
		this.read = false;
		splitFile();
	}
	
	private void splitFile()
	{	
		byte[] buffer = new byte[chunkSize];
		File file = new File(filename);	
		FileIDGenerator fid = new FileIDGenerator(filename);	
		fileID = fid.getHash();
		int chunkNo = 0;
		
		try (BufferedInputStream bufinst = new BufferedInputStream (new FileInputStream(file)))
		{
			int tmp = 0;
			while ((tmp = bufinst.read(buffer)) > 0)
			{
				byte[] body = new byte[tmp];
				System.arraycopy(buffer, 0, body, 0, tmp);
				
				Chunk chunk = new Chunk(fileID, chunkNo, replicationDegree, body);
				chunkList.add(chunk);
				chunkNo++;
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		this.read = true;
	}
	
	public ArrayList<Chunk> getChunkList()
	{
		if(this.read){
			System.out.println(this.chunkList.size());
			return this.chunkList;
		}
		else{
			System.out.println("File hasn't been read yet");
			return null;	
		}
	}
	
	public String getFilename()
	{
		return filename;
	}

	public String getFileID()
	{
		return this.fileID;
	}
}
