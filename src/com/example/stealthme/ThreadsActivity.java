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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ThreadsActivity extends Activity
{
	
	// Global variables
	List<String> seenAddresses = new ArrayList<String>();
	ContactManager myContacts;
	
	// File names
	Uri SMS_URI = Uri.parse("content://sms/inbox");
	
	// Views and buttons to be instantiated
	ListView threads;
	Button button_Refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_threads);
		
		// Instatiate buttons and views
		threads = (ListView)findViewById(R.id.listView_Threads);
		button_Refresh = (Button)findViewById(R.id.button_Refresh);
		
		// Re-fetch contacts and numbers
		myContacts = new ContactManager(this);
		myContacts.rebuildContactsList(this);
		
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
		
		// Make refresh button clickable
		button_Refresh.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
            	refresh();
            }
        });
	}
	
	// Accesses the phone's SMS database, pulling threads sorted by date
	private void threadListUpdate()
	{	
		// Reset the seen addresses list
		seenAddresses.clear();
		
		// Set up SMS cursor
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(SMS_URI, null, null, null, null);
		
		// Create hashmap for handling multiple items in listview
		List<HashMap<String,String>> hashList = new ArrayList<HashMap<String,String>>();
		
		// Iterate through each thread in the database and add it to our list
		String[] bodies = new String[256];
		String[] names = new String[256];
		String address;
		String name;
		
		// Set up SMS cursor columns
		int addressIndex = c.getColumnIndex(SmsReceiver.ADDRESS);
		int bodyIndex = c.getColumnIndex(SmsReceiver.BODY);
		
		int count = 0;
		if(!c.moveToFirst()) return;
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
				bodies[count] = c.getString(bodyIndex);
				name = myContacts.getName(address, this);
				names[count++] = name;
				
				// Add this address to our list of seen addresses
				seenAddresses.add(address);
			}
		} while (c.moveToNext() && count < 256);
		
		// Populate our hashmap
		for(int i = 0; i < count; i++)
		{
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("name", names[i]);
            hm.put("body", bodies[i]);
            hm.put("image", Integer.toString(R.drawable.threads_list_icon));
            hashList.add(hm);
        }
		
		// Add the hashmap to our listview
		String[] from = {"image", "name", "body"};
		int[] to = {R.id.image, R.id.date, R.id.body};
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
    
    // Refreshes the contact names and numbers (time consuming)
    public void refresh()
    {
    	// This can take a second, let the user know what's going on
    	Toast.makeText(this, "Updating contacts list...", Toast.LENGTH_LONG).show();
    	myContacts.rebuildContactsList(this);
    	threadListUpdate();
    }
    
    // Opens the messaging screen
    public void openMessaging(View view, int position)
    {
    	Intent intent = new Intent(this, MessagingActivity.class);
    	
    	String address = seenAddresses.get(position);
    	String name = myContacts.getName(address, this);
    	intent.putExtra("targetAddress", address);
    	intent.putExtra("targetName", name);
    	
    	startActivity(intent);
    }
	
    // Opens new message activity
    public void newMessage(View view)
    {
    	Intent intent = new Intent(this, NewMessageActivity.class);
    	startActivity(intent);
    }
    
};

