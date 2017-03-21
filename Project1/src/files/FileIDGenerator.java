package files;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileIDGenerator {

	private String hash;
	public FileIDGenerator(String filename){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	        FileInputStream fis = new FileInputStream(filename);
	        
	        byte[] dataBytes = new byte[8192];

	        int nread = 0;
	        while ((nread = fis.read(dataBytes)) != -1) {
	          md.update(dataBytes, 0, nread);
	        };
	        byte[] mdbytes = md.digest();


	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        this.hash = sb.toString();
	        System.out.println("SHA: " + this.hash);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}	
	
	
	public String getHash(){
		return this.hash;
	}
	
	public static void main(String[] args){
		FileIDGenerator x = new FileIDGenerator("./ex1.txt");
	}
}
