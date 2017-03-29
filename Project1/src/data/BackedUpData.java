package data;

import java.io.Serializable;

public class BackedUpData implements Serializable
{
	private String filename;
	private String fileId;
	
	public BackedUpData(String filename, String fileId)
	{
		this.filename = filename;
		this.fileId = fileId;
	}
	
	public String getFilename()
	{
		return filename;
	}
	
	public String getFileId()
	{
		return fileId;
	}
}
