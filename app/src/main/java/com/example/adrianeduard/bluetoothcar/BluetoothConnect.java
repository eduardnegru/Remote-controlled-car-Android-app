package com.example.adrianeduard.bluetoothcar;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnect extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    /* Adresa MAC al modulului HC-05 */
    private String CAR_MAC = "00:28:13:00:1F:5B";
    private BluetoothDevice car;
    private OutputStream output;
    public boolean carFound = false;

    protected void discoverDevices()
    {
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first", Toast.LENGTH_LONG).show();
        }
        else
        {
            for (BluetoothDevice device : bondedDevices)
            {
                if(device.getAddress().equals(CAR_MAC))
                {
                    car=device;
                    carFound = true;
                    break;
                }
            }
        }

        if(!carFound)
        {
            Toast.makeText(getApplicationContext(),"Car could not be found. Please make sure the car is in range and try again", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(BluetoothConnect.this, MainActivity.class);
        intent.putExtra("BLUETOOTH_DEVICE", car);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        final ImageView start = (ImageView) findViewById(R.id.start);
        assert start != null;

        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesn't Support Bluetooth", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!bluetoothAdapter.isEnabled())
                {
                    Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableAdapter, 0);
                }
                else
                {
                    discoverDevices();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0)
        {
            if(resultCode == RESULT_OK)
            {
                discoverDevices();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"This application requires a bluetooth connection", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
