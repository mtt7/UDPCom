package nothing.yet.udpcom;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

//Klasa odpowiada za wysyłanie pakietów UDP
public class UDPSend extends Thread
{
    String data;
    InetAddress ip;
    int port;
    DatagramSocket socket;
    private boolean running;

    public UDPSend(String data,InetAddress ip,int port,DatagramSocket socket)
    {
        super();
        this.data = data;
        this.ip = ip;
        this.port = port;
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
        //Wysłanie pakietu
        try
        {
            byte[] buf = data.getBytes();

            DatagramPacket packet = new DatagramPacket(buf,buf.length,ip,port);
            socket.send(packet);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
