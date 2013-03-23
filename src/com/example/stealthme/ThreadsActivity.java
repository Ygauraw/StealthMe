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
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
		c.moveToFirst();
		do
		{
			address = c.getString(addressIndex);
			body = c.getString(bodyIndex);
			messages.add(address + "\n" + body);
		} while (c.moveToNext());
		
		// Add the list to our listView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);
		threads.setAdapter(adapter);
	}
	
	@Override
	protected void onResume()
	{
		threadListUpdate();
	}
	
};

