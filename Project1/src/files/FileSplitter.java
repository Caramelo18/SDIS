package files;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileSplitter {
	private ArrayList<byte[]> chunkList;
	
	public FileSplitter(String filename) throws FileNotFoundException, IOException{
		int chunkSize = 64000;
		this.chunkList = new ArrayList<byte[]> ();
		
		byte[] buffer = new byte[chunkSize];
		
		File file = new File(filename);
		
		try (BufferedInputStream bufinst = new BufferedInputStream (new FileInputStream(file))){
			int tmp = 0;
			while ((tmp = bufinst.read(buffer)) > 0) {
				chunkList.add(buffer);
			}
		}
	}
	
	public ArrayList<byte[]> getChunkList(){
		System.out.println(this.chunkList.size());
		return this.chunkList;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		FileSplitter x = new FileSplitter("./Teste.mp4");
		
		x.getChunkList();
	}

}
