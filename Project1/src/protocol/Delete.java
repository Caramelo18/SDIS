package protocol;

import java.io.File;
import java.nio.file.Files;

import data.DataManager;
import message.MessageGenerator;
import peer.Peer;

public class Delete implements Runnable
{
	private String filename;
	
	
	public Delete(String filename)
	{
		this.filename = filename;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataManager DM = Peer.getDataManager();
		String fileID = DM.getFileId(filename);
		
		if(fileID == null)
		{
			System.out.println("No such file backed up");
			return;
		}
		
		String[] name = filename.split("/");
		String filename = name[name.length - 1];
		String filepath = "../Peer" + Peer.getServerId() + "/Files/" + filename;
		
		File file = new File(filepath);
		file.delete();

		Peer.getDataManager().deleteBackedUpData(filename, fileID);
		
		byte[] message = MessageGenerator.generateDELETE(fileID);
		Peer.getMC().sendPacket(message);
	}
	
}
