package server.logic;

import javax.net.ssl.SSLSocket;

public class ForeignContact
{
	private String address;
	private Integer peerID;
	private Integer MCPort;
	private Integer MDBPort;
	private Integer MDRPort;
	private Integer senderPort;
	
	public ForeignContact(String address, Integer peerID, Integer MCPort, Integer MDBPort, Integer MDRPort, Integer senderPort)
	{
		this.address = address;
		this.peerID = peerID;
		this.MCPort = MCPort;
		this.MDBPort = MDBPort;
		this.MDRPort = MDRPort;
		this.senderPort = senderPort;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public Integer getPeerID()
	{
		return peerID;
	}
	
	public Integer getMCPort()
	{
		return MCPort;
	}
	
	public Integer getMDBPort()
	{
		return MDBPort;
	}
	
	public Integer getMDRPort()
	{
		return MDRPort;
	}
	
	public Integer getSenderPort()
	{
		return senderPort;
	}
}
