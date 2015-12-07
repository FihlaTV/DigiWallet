package com.example.mjvyas.test1;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;


public class MainActivity extends Activity {

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayAdapter<String> BTArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private ListView myListView;

    Button btnDiscovery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set bluetooth On at the start of application
        boolean isEnabled = BTAdapter.isEnabled();
        if (!isEnabled) {

            // prompt the user to turn BlueTooth on
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            //Toast.makeText(getApplicationContext(), "Bluetooth turned on",
            //Toast.LENGTH_LONG).show();
        }
        else {
            //return bluetoothAdapter.disable();
        }
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        btnDiscovery = (Button) findViewById(R.id.button1);
//        btnDiscovery.setOnClickListener(new OnClickListener(){
//            public void onClick(View v) {
//                BTAdapter.startDiscovery();
//            }
//        });

        btnDiscovery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                find(v);
            }
        });

        myListView = (ListView)findViewById(R.id.listView1);

        // create the arrayAdapter that contains the BTDevices, and set it to the ListView
        BTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        myListView.setAdapter(BTArrayAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name and the MAC address of the object to the arrayAdapter
                BTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                BTArrayAdapter.notifyDataSetChanged();
            }
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                // Device is now connected
                // BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(getApplicationContext(),"Device is now connected",
                        Toast.LENGTH_SHORT).show();
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // Done searching
                Toast.makeText(getApplicationContext(),"Done searching",
                        Toast.LENGTH_SHORT).show();
            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                // Device is about to disconnect
                Toast.makeText(getApplicationContext(),"Device is about to disconnect",
                        Toast.LENGTH_SHORT).show();
            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                // Device has disconnected
                Toast.makeText(getApplicationContext(),"Device has disconnected",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onStop()
    {
        unregisterReceiver(receiver);
        super.onStop();
    }

    public void find(View view) {
        if (BTAdapter.isDiscovering()) {
            // the button is pressed when it discovers, so cancel the discovery
            BTAdapter.cancelDiscovery();
        }
        else {
            BTArrayAdapter.clear();
            BTAdapter.startDiscovery();

            registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }
}