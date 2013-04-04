////////////////////////////////////////////////////////////////////////////////
//
//	ThreadsActivity.java
// 	Description: Manages conversation threads between multiple recipients.
//	Allows the user to select which conversation to view, which is then taken
//	to the MessagingActivity.
//
////////////////////////////////////////////////////////////////////////////////

package com.example.stealthme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadsActivity extends Activity
{
	
	// Global variables
	List<String> seenAddresses = new ArrayList<String>();
	
	// File names
	Uri SMS_URI = Uri.parse("content://sms/inbox");
	
	// Views and buttons to be instantiated
	ListView threads;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_threads);
		
		// Instatiate buttons and views
		threads = (ListView)findViewById(R.id.listView_Threads);
		
		// Query phone SMS database, refresh threads list
		threadListUpdate();
		
		// Make each list item clickable
		threads.setOnItemClickListener(new OnItemClickListener()
		{
			  public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			  {
				  // Open messaging activity with the selected address
				  openMessaging(view, position);
			  }
		});
	}
	
	// Accesses the phone's SMS database, pulling threads sorted by date
	private void threadListUpdate()
	{
		// Reset the seen addresses list
		seenAddresses.clear();
		
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(SMS_URI, null, null, null, null);
		
		// Create hashmap for handling multiple items in listview
		List<HashMap<String,String>> hashList = new ArrayList<HashMap<String,String>>();
		
		// Iterate through each thread in the database and add it to our list
		String[] addresses  = new String[256];
		String[] bodies = new String[256];
		String address;
		int addressIndex = c.getColumnIndex(SmsReceiver.ADDRESS);
		int bodyIndex = c.getColumnIndex(SmsReceiver.BODY);
		int count = 0;
		c.moveToFirst();
		do
		{
			// First grab the address
			address = c.getString(addressIndex);
			address = StringManager.removeSpecialCharacters(address);	// Remove special characters
			
			// Now check to see if we've seen it already (for organizing conversations)
			// If not, process it
			if (!seenAddresses.contains(address))
			{
				// Save the address and body
				addresses[count] = c.getString(addressIndex);
				bodies[count++] = c.getString(bodyIndex);
				
				// Add this address to our list of seen addresses
				seenAddresses.add(address);
			}
		} while (c.moveToNext() && count < 256);
		
		// Populate our hashmap
		for(int i = 0; i < count; i++)
		{
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("address", addresses[i]);
            hm.put("body", bodies[i]);
            hm.put("image", Integer.toString(R.drawable.threads_list_icon_png));
            hashList.add(hm);
        }
		
		// Add the hashmap to our listview
		String[] from = {"image", "address", "body"};
		int[] to = {R.id.image, R.id.address, R.id.body};
		SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), hashList, R.layout.image_listview_item, from, to);
		threads.setAdapter(adapter);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		threadListUpdate();
	}
	
    // Opens the settings menu screen - invoked by the settings button
    public void openSettings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    // Opens the messaging screen
    public void openMessaging(View view, int position)
    {
    	Intent intent = new Intent(this, MessagingActivity.class);
    	
    	String address = seenAddresses.get(position);
    	address = StringManager.removeSpecialCharacters(address);
    	intent.putExtra("targetAddress", address);
    	
    	startActivity(intent);
    }
	
};

