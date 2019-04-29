package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private OutputStream outputStream;
    private InputStream inStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    @Override
    protected void onStart() {
        super.onStart();

        Button b=(Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    init();
                    try{
                        write("hey aashish, its me !!");
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void init() throws IOException {
        TextView tw=(TextView)findViewById(R.id.text);

        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if(bondedDevices.size() > 0) {
                    Object[] devices = (Object []) bondedDevices.toArray();
                    int ctr=bondedDevices.size();
                    String s="";
                    for(int i=0;i<ctr;++i){
                        s=s+"*"+devices[i].toString();
                    }
                    tw.setText(s);
                    BluetoothDevice device = (BluetoothDevice) devices[0];

                    ParcelUuid[] uuids = device.getUuids();
                    tw.setText(uuids[0].toString());
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[ctr-1].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                }

                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }

    public void run() {

    }

    class listen_thread extends Thread{

        @Override
        public void run() {
            TextView tw2=(TextView)findViewById(R.id.textView4);
            final int BUFFER_SIZE = 1024;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytes = 0;
            int b = BUFFER_SIZE;

            while (true) {
                try {
                    bytes = inStream.read(buffer, bytes, BUFFER_SIZE - bytes);
                    tw2.setText(buffer.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
