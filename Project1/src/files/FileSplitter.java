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
		
		String fileID = fid.getHash();
		
		int chunkNo = 0;
		
		try (BufferedInputStream bufinst = new BufferedInputStream (new FileInputStream(file)))
		{
			int tmp = 0;
			while ((tmp = bufinst.read(buffer)) > 0) {
				Chunk chunk = new Chunk(fileID, chunkNo, replicationDegree, buffer);
				chunkList.add(chunk);
				chunkNo++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		FileSplitter x = new FileSplitter("./Teste.mp4", 2);
		
		x.getChunkList();
	}

}
