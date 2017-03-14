package message;

import java.io.IOException;

public class Message {
	
	private String messageType;
	private String version;
	private int senderID;
	private int fileID;
	private int chunkNo;
	private int replicationDegree;
	
	
	public Message(String message){
		String[] arr = message.split("(?<=[^\\s])[ ]+(?=[^\\s])");
		
		this.messageType = arr[0];
		this.parseType(arr);
	}	
	
	private void parseType(String[] arr){
		
		this.version = arr[1];
		this.senderID = Integer.valueOf(arr[2]);
		this.fileID = Integer.valueOf(arr[3]);
		
		switch(this.messageType){
		case("PUTCHUNK"):
			this.chunkNo = Integer.valueOf(arr[4]);
			this.replicationDegree = Integer.valueOf(arr[5]);
			break;
		case("STORED"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;
		case("GETCHUNK"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;
		case("CHUNK"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;		
		case("REMOVED"):
			this.chunkNo = Integer.valueOf(arr[4]);
			break;
		}
	}
	
}
