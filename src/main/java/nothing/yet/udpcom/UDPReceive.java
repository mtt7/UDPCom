package nothing.yet.udpcom;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

//Klasa odpowiada za nasłuchiwanie i odbiór przychodzących pakietów UDP
public class UDPReceive extends Thread
{
    Handler handler;
    DatagramSocket socket;
    private boolean running;
    public static final int DATA_RECEIVE = 1;

    public UDPReceive(Handler handler,DatagramSocket socket)
    {
        super();
        this.handler = handler;
        this.socket = socket;
    }

    //Funkcja potrzebna do zatrzymania działającego wątku
    public void setRunning(boolean running)
    {
        this.running = running;
    }

    @Override
    public void run()
    {
        running = true;

        //Odbiór pakietu i przesłanie go do handlera
        while(running)
        {
            try
            {
                byte[] buf = new byte[1000];

                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String text = new String(buf, 0, packet.getLength());
                Message msg = handler.obtainMessage();
                msg.what = DATA_RECEIVE;
                msg.obj = text;
                handler.sendMessage(msg);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
