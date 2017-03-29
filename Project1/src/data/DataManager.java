package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import peer.Peer;

public class DataManager implements Serializable
{
	private ArrayList<StoredData> storedFilesData;
	private ArrayList<BackedUpData> backedUpData;
	
	public DataManager()
	{
		// Tentar ler os metadados guardados antes
		
		storedFilesData = new ArrayList<StoredData>();
		backedUpData = new ArrayList<BackedUpData>();
	}
	
	/*
	Retorna 0 - filename não existe
	Retorna 1 - filename existe com o fileId indicado
	Retorna 2 - filename existe com outro fileId
	*/
	public int addBackedUpFile(String filename, String fileId)
	{
		for(int i = 0; i < backedUpData.size(); i++)
		{
			BackedUpData data = backedUpData.get(i);
			
			if(data.getFilename().equals(filename))
			{
				if(data.getFileId().equals(fileId))
				{
					return 1;
				}
				else
				{
					// Chama o delete para o file antigo
					// Actualiza os registos e os metadados
					
					return 2;
				}
			}
		}
		
		BackedUpData data = new BackedUpData(filename, fileId);
		backedUpData.add(data);
		serialize();
		
		return 0;
	}
	
	public void serialize()
	{
		FileOutputStream fout;
		try
		{
			fout = new FileOutputStream("../Peer" + Peer.getServerId() + "/metadata.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this);
			
			oos.close();
			fout.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
