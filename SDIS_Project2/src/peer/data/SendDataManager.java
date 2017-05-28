package peer.data;

import peer.main.Peer;
import peer.network.SSLSocketListener;
import java.io.*;

/**
 * Created by fabio on 28-05-2017.
 */
public class SendDataManager implements Runnable {
    public static final long waitTime = 1000 * 60 ; // 1  minute

    @Override
    public void run() {
        boolean running = true;

        while(running)
        {
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            File f = new File("../Peer" + Peer.getPeerID() + "/metadata.ser");
            if(f.exists())  {
                try   {
                    FileInputStream fin = new FileInputStream(f);
                    byte[] array = new byte[100000];
                    fin.read(array);

                    fin.close();

                    Peer.getSocketListener().sendMessage("StoreMetadata");
                    Peer.getSocketListener().sendBytes(array);
                }   catch (IOException e)   {
                    e.printStackTrace();
                }
            }

        }
    }
}
