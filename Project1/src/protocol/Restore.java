package protocol;

import data.DataManager;
import peer.Peer;

public class Restore implements Runnable
{
	private String filename;
	
	public Restore(String filename)
	{
		this.filename = filename;
	}

	@Override
	public void run()
	{
		DataManager DM = Peer.getDataManager();
		String fileId = DM.getFileId(filename);
		if(fileId == null)
		{
			System.out.println("No such file backed up");
			return;
		}
		System.out.println(fileId);
	}

}
