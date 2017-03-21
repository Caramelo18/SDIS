package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import chunk.Chunk;
import peer.Peer;

public class MessageGenerator
{
	public static final String CRLF = "\r\n";
	
	public byte[] generatePUTCHUNK(Chunk chunk)
	{
		String header = "PUTCHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + chunk.getReplicationDegree();
		header += " " + CRLF + CRLF;
		
		return appendBytes(header.getBytes(), chunk.getBody());
		
		// MDB
	}

	public byte[] generateSTORED(Chunk chunk)
	{
		String header = "STORED";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		return header.getBytes();
		
		// MC
	}

	public byte[] generateGETCHUNK(Chunk chunk)
	{
		String header = "GETCHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		return header.getBytes();
		
		// MC
	}

	public byte[] generateCHUNK(Chunk chunk)
	{
		String header = "CHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		return appendBytes(header.getBytes(), chunk.getBody());
		
		// MDR
	}

	public byte[] generateDELETE(String fileID)
	{
		String header = "DELETE";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + fileID;
		header += " " + CRLF + CRLF;
		
		return header.getBytes();
		
		// MC
	}

	public byte[] generateREMOVED(Chunk chunk)
	{
		String header = "REMOVED";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		return header.getBytes();
		
		// MC
	}
	
	public byte[] appendBytes(byte[] header, byte[] body)
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
