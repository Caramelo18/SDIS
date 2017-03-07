package message;

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
	}	
	
	private void parseType(String[] arr){
		switch(this.messageType){
		case("PUTCHUNK"):
			
			break;
		case("STORED"):
			
			break;
		case("GETCHUNK"):
			
			break;
		
		case("CHUNK"):
			
			break;
		case("DELETE"):
			
			break;
		
		case("REMOVED"):
			break;
		}
	}

}
