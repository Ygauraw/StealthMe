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
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ThreadsActivity extends Activity
{
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
		threads.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
	          public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
	          {
	        	  // Parse out address and body
	        	  String message = ((TextView)view).getText().toString();
	        	  String address, body;
	        	  int line = 0;
	        	  while (line < message.length() && message.charAt(line) != '\n')
	        		  line++;
	        	  address = message.subSequence(0, line).toString();
	        	  body = message.subSequence(line + 1, message.length()).toString();
	        	  
	        	  // When clicked, show a toast with the TextView text
	        	  Toast.makeText(getApplicationContext(), "Body: " + body, Toast.LENGTH_SHORT).show();
	        	  Toast.makeText(getApplicationContext(), "Address: " + address, Toast.LENGTH_SHORT).show();
	          }
	    });
	}
	
	// Accesses the phone's SMS database, pulling threads sorted by date
	private void threadListUpdate()
	{
		ArrayList<String> messages = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(SMS_URI, null, null, null, null);
		
		// Iterate through each thread in the database and add it to our list
		String address;
		String body;
		int addressIndex = c.getColumnIndex(SmsReceiver.ADDRESS);
		int bodyIndex = c.getColumnIndex(SmsReceiver.BODY);
		List<String> seenAddresses = new ArrayList<String>();
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
				body = c.getString(bodyIndex);
				messages.add(address + "\n" + body);
				
				// Add this address to our list of seen addresses
				seenAddresses.add(address);
			}
		} while (c.moveToNext());
		
		// Add the list to our listView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
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
	
};

