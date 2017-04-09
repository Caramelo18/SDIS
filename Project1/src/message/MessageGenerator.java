package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import chunk.Chunk;
import peer.Peer;

public class MessageGenerator
{
	public static final String CRLF = "\r\n";
	
	public static byte[] generatePUTCHUNK(Chunk chunk)
	{
		String header = "PUTCHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + chunk.getReplicationDegree();
		header += " " + CRLF + CRLF;
		
		try
		{
			return appendBytes(header.getBytes("ASCII"), chunk.getBody());
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return null;
		
		// MDB
	}

	public static byte[] generateSTORED(String fileID, String chunkNo)
	{
		String header = "STORED";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + fileID;
		header += " " + chunkNo;
		header += " " + CRLF + CRLF;
		
		try
		{
			return header.getBytes("ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}

	public static byte[] generateGETCHUNK(String fileId, Integer chunkNo)
	{
		String header = "GETCHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + fileId;
		header += " " + chunkNo;
		header += " " + CRLF + CRLF;
		
		try
		{
			return header.getBytes("ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}

	public static byte[] generateCHUNK(Chunk chunk)
	{
		String header = "CHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		try
		{
			return appendBytes(header.getBytes("ASCII"), chunk.getBody());
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return null;
		
		// MDR
	}

	public static byte[] generateDELETE(String fileID)
	{
		String header = "DELETE";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + fileID;
		header += " " + CRLF + CRLF;
		
		try
		{
			return header.getBytes("ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}

	public static byte[] generateREMOVED(String fileId, String chunkNo)
	{
		String header = "REMOVED";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + fileId;
		header += " " + chunkNo;
		header += " " + CRLF + CRLF;
		
		try
		{
			return header.getBytes("ASCII");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}
	
	public static byte[] appendBytes(byte[] header, byte[] body)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try
		{
			outputStream.write(header);
			outputStream.write(body);
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}
}
