package peer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote
{
	void backup(String filename, int replicationDegree) throws RemoteException;

	void restore(String filename) throws RemoteException;

	void delete(String filename) throws RemoteException;

	void space(int kbytes) throws RemoteException;
	
	void state() throws RemoteException;
}
