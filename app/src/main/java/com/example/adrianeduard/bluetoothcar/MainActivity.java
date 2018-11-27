package com.example.adrianeduard.bluetoothcar;

import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private OutputStream output;
    private InputStream input;
    private  BluetoothDevice car;
    static final UUID port_uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public String commandUp = "w";
    public String commandDown = "s";
    public String commandLeft = "l";
    public String commandRight = "r";
    public String commandNOP = "j";
    public String commandHorn = "h";
    public String commandLights = "f";
    public String commandStopTractionMotor = "z";
    public String commandStopDirectionMotor = "x";
    public String commandStopHorn = "c";

    public int progress_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        car = getIntent().getExtras().getParcelable("BLUETOOTH_DEVICE");

        ParcelUuid[] uuids = car.getUuids();

        try
        {
            BluetoothSocket socket = car.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            socket.connect();
            output = socket.getOutputStream();
            input = socket.getInputStream();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        final ImageView left = (ImageView) findViewById(R.id.left);
        assert left != null;

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try {
                                output.write(commandLeft.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                output.write(commandStopDirectionMotor.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            return false;
                    }

                return true;
            }
        });

        final ImageView right = (ImageView) findViewById(R.id.right);
        assert right != null;

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try {
                                output.write(commandRight.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                output.write(commandStopDirectionMotor.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            return false;
                    }

                return true;
            }
        });


        final ImageView up = (ImageView) findViewById(R.id.up);
        assert up != null;

        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try {
                                output.write(commandUp.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                output.write(commandStopTractionMotor.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            return false;
                    }

                return true;
            }
        });

        final ImageView down = (ImageView) findViewById(R.id.down);
        assert down != null;

        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction())
                    {
                        case MotionEvent.ACTION_DOWN:
                            try {
                                output.write(commandDown.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            try {
                                output.write(commandStopTractionMotor.getBytes());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            return false;
                    }

                return true;
            }
        });

        final ImageView horn = (ImageView) findViewById(R.id.horn);
        assert horn != null;

        horn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write(commandHorn.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        try {
                            output.write(commandStopHorn.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });

        final ImageView lights = (ImageView) findViewById(R.id.lights);
        assert lights != null;

        lights.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            output.write(commandLights.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });


        final TextView textView = (TextView) findViewById(R.id.speed_indicator);
        assert textView != null;

        final VerticalSeekBar seek_bar = (VerticalSeekBar) findViewById(R.id.seek_bar);
        assert seek_bar != null;

        textView.setText("Speed: " + Integer.toString(0) + "%");
        seek_bar.setMaximum(10);
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress_value = progress;
                textView.setText("Speed: " + Integer.toString(progress_value * 10) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView.setText("Speed: " + Integer.toString(progress_value * 10) + "%");
            }
        });

        seek_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    String commandSpeed = "-";

                    if(progress_value == 0)
                    {
                        commandSpeed = "0";
                    }
                    if(progress_value == 1)
                    {
                        commandSpeed = "1";
                    }
                    if(progress_value == 2)
                    {
                        commandSpeed = "2";
                    }
                    if(progress_value == 3)
                    {
                        commandSpeed = "3";
                    }
                    if(progress_value == 4)
                    {
                        commandSpeed = "4";
                    }
                    if(progress_value == 5)
                    {
                        commandSpeed = "5";
                    }
                    if(progress_value == 6)
                    {
                        commandSpeed = "6";
                    }

                    if(progress_value == 7)
                    {
                        commandSpeed = "7";
                    }

                    if(progress_value == 8)
                    {
                        commandSpeed = "8";
                    }
                    if(progress_value == 9)
                    {
                        commandSpeed = "9";
                    }

                    if(progress_value == 10)
                    {
                        commandSpeed = "-";
                    }

                    try {
                        output.write(commandSpeed.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
