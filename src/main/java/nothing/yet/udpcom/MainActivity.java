package nothing.yet.udpcom;
//Aplikacja wysyłająca komendę po naciśnięciu przycisku.
//Odbiera również otrzymane dane i wyświetla pod przyciskami
//odpowiedzialnymi za sterowanie.
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity
{

    TextView tx_received;
    Button btn_left;
    Button btn_right;
    Button btn_up;
    Button btn_down;
    Button btn_control;
    Button btn_follow;
    Button btn_line;
    static final int PORT = 4210;
    public static final int DATA_RECEIVE = 1;
    static  InetAddress ip;
    UDPSend udpSend;
    UDPReceive udpReceive;
    DatagramSocket socket;

    //Handler wyświetlający otrzymane dane
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if(msg.what==DATA_RECEIVE)
            {
                tx_received.setText((String)msg.obj);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx_received = findViewById(R.id.tx_received);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_up = findViewById(R.id.btn_up);
        btn_down = findViewById(R.id.btn_down);
        btn_control = findViewById(R.id.btn_control);
        btn_follow = findViewById(R.id.btn_follow);
        btn_line = findViewById(R.id.btn_line);

    }

    //Stworzenie socket i ustawienie IP.
    //Przypisanie OnTouchListener przyciskom.
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onStart()
    {
        try
        {
            socket = new DatagramSocket();
            ip = InetAddress.getByName("192.168.4.1");
        }
        catch(SocketException e)
        {
            e.printStackTrace();
            this.finishAffinity();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            this.finishAffinity();
        }

        //Przytrzymanie przycisku wysyła komendę
        //Puszczenie przycisku anuluje komendę
        btn_left.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        udpSend = new UDPSend("LEFTON", ip, PORT, socket);
                        udpSend.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        udpSend = new UDPSend("LEFTOFF", ip, PORT, socket);
                        udpSend.start();
                        return true;
                }
                return false;
            }
        });

        btn_right.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        udpSend = new UDPSend("RIGHTON", ip, PORT, socket);
                        udpSend.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        udpSend = new UDPSend("RIGHTOFF", ip, PORT, socket);
                        udpSend.start();
                        return true;
                }
                return false;
            }
        });

        btn_up.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        udpSend = new UDPSend("UPON", ip, PORT, socket);
                        udpSend.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        udpSend = new UDPSend("UPOFF", ip, PORT, socket);
                        udpSend.start();
                        return true;
                }
                return false;
            }
        });

        btn_down.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        udpSend = new UDPSend("DOWNON", ip, PORT, socket);
                        udpSend.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        udpSend = new UDPSend("DOWNOFF", ip, PORT, socket);
                        udpSend.start();
                        return true;
                }
                return false;
            }
        });

        //Brak potrzeby wysyłania komendy anulującej
        btn_control.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                udpSend = new UDPSend("CONTROL", ip, PORT, socket);
                udpSend.start();
            }
        });
        btn_follow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                udpSend = new UDPSend("FOLLOW", ip, PORT, socket);
                udpSend.start();
            }
        });
        btn_line.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                udpSend = new UDPSend("LINE", ip, PORT, socket);
                udpSend.start();
            }
        });

        //Włączenie nasłuchiwania przychodzących pakietów
        udpReceive = new UDPReceive(handler,socket);
        udpReceive.start();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        //Zatrzymanie działających wątków
        if(udpSend != null)
        {
            udpSend.setRunning(false);
            udpSend = null;
        }
        if(udpReceive != null)
        {
            udpReceive.setRunning(false);
            udpReceive = null;
        }
        if(socket != null)
        {
            socket.close();
        }

        super.onStop();
    }
}
