package message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import chunk.Chunk;
import peer.Peer;

public class MessageGenerator
{
	public static final String CRLF = "\r\n";
	
	public synchronized static byte[] generatePUTCHUNK(Chunk chunk)
	{
		String header = "PUTCHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + chunk.getReplicationDegree();
		header += " " + CRLF + CRLF;
		
		try {
			return appendBytes(header.getBytes("ASCII"), chunk.getBody());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		// MDB
	}

	public synchronized static byte[] generateSTORED(Chunk chunk)
	{
		String header = "STORED";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		try {
			return header.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}

	public synchronized static byte[] generateGETCHUNK(Chunk chunk)
	{
		String header = "GETCHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		try {
			return header.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}

	public synchronized static byte[] generateCHUNK(Chunk chunk)
	{
		String header = "CHUNK";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		try {
			return appendBytes(header.getBytes("ASCII"), chunk.getBody());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
		
		// MDR
	}

	public synchronized static byte[] generateDELETE(String fileID)
	{
		String header = "DELETE";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + fileID;
		header += " " + CRLF + CRLF;
		
		try {
			return header.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}

	public synchronized static byte[] generateREMOVED(Chunk chunk)
	{
		String header = "REMOVED";
		header += " " + Peer.getProtocolVersion();
		header += " " + Peer.getServerId();
		header += " " + chunk.getFileId();
		header += " " + chunk.getChunkNo();
		header += " " + CRLF + CRLF;
		
		try {
			return header.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
		
		// MC
	}
	
	public synchronized static byte[] appendBytes(byte[] header, byte[] body)
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
