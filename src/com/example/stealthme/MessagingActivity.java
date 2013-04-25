////////////////////////////////////////////////////////////////////////////////
//
//	MessagingActivity.java
// 	Description: Handles sending and displaying messages between the user and
//	one recipient.
//
////////////////////////////////////////////////////////////////////////////////

package com.example.stealthme;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.database.Cursor;
import android.telephony.SmsManager;
//import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MessagingActivity extends Activity
{

	// Views to be instantiated
	Button button_SendMessage;
	EditText text_PhoneNumber;
	EditText text_Message;
	ListView messages;		// Used to store message history
	
	// Working variables
	String history = "";	// Used to store message history
	SimpleAdapter adapter;
	List<HashMap<String, String>> hashList;
	
	// File names
	static final String PREFS_NAME = "preferences";	// Shared prefs filename
	Uri SMS_URI = Uri.parse("content://sms/inbox");	// SMS inbox filename
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        
        // Grab extra data
        Intent intent = getIntent();
        String recipient = intent.getStringExtra("targetAddress");
        
        // Instantiate buttons and fields
        button_SendMessage = (Button)findViewById(R.id.button_SendMessage);
        text_PhoneNumber = (EditText)findViewById(R.id.text_PhoneNumber);
        text_Message = (EditText)findViewById(R.id.text_Message);
        messages = (ListView)findViewById(R.id.listView_MessageHistory);
        
        // Set properties of the listView
        messages.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messages.setStackFromBottom(true);
        
        // Retrieve message history
        updateMessageHistory(recipient);
        
        // Add the hashmap to our listview
     	String[] from = {"date", "body"};
     	int[] to = {R.id.message_history_date, R.id.message_history_body};
     	adapter = new SimpleAdapter(getBaseContext(), hashList, R.layout.message_history_listview_item, from, to);
     	messages.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        
        // Set phone number
        if (recipient != null) text_PhoneNumber.setText("[" + recipient + "]");
        
        // Handle sending the message when the user hits 'Send'
        button_SendMessage.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
            	// Grab the data from each field
                String phoneNumber = text_PhoneNumber.getText().toString();
                String message = text_Message.getText().toString();       
                
                // Check if the user entered data into both fields, send the text if they did
                if (phoneNumber.length() > 0 && message.length() > 0) sendMessage(phoneNumber, message);                
                else			// Display error message; there was an empty field
                {
                    Toast.makeText(getBaseContext(), "Either the phone number or message field was left blank.", Toast.LENGTH_SHORT).show();
                }
                
                // Reset the message box
                text_Message.setText("");
            }
        });
    }
    
    // On activity resume
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	// Reload chat history
        Intent intent = getIntent();
        String recipient = intent.getStringExtra("targetAddress");
        updateMessageHistory(recipient);
    }
    
    // sendMessage method: sends a given message to another device via SMS
    private void sendMessage(String targetNumber, String targetMessage)
    {
    	// Create a pending intent; can be used to display another activity after sending the message,
    	// but in our case it will just send it back to MainActivity       
        SmsManager messageManager = SmsManager.getDefault();
        messageManager.sendTextMessage(targetNumber, null, targetMessage, null, null);
    }
    
    // Opens the settings menu screen - invoked by the settings button
    public void openSettings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    // Opens the threads activity
    public void openThreads(View view)
    {
        Intent intent = new Intent(this, ThreadsActivity.class);
        startActivity(intent);
    }
    
    // Grabs all messages from the recipient from the sms database and displays them in the text_History box
    public void updateMessageHistory(String address)
    {
    	// Working variables
    	String thisAddress;	// For comparing target address against stored address
    	long[] dates = new long[256];
    	String[] bodies = new String[256];
    	int count = 0;
    	ContentResolver cr = getContentResolver();
		Cursor c = cr.query(SMS_URI, null, null, null, null);
		int addressIndex = c.getColumnIndex(SmsReceiver.ADDRESS);
		int bodyIndex = c.getColumnIndex(SmsReceiver.BODY);
		int dateIndex = c.getColumnIndex(SmsReceiver.DATE);
		
		// Deque acting as a stack to reverse the order of the messages
		Deque<HashMap<String, String>> messageStack = new ArrayDeque<HashMap<String, String>>();
		
		// Create hashmap for handling multiple items in listview
		hashList = new ArrayList<HashMap<String,String>>();
		
		// Date formatter
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mma, MMM dd", Locale.US);
		String formattedDate = "";
		
		// Scroll through the SMS database
		if(c.moveToFirst())
		{
			do
			{
				// First grab the address
				thisAddress = StringManager.removeSpecialCharacters(c.getString(addressIndex));
				
				// Compare it against our target address
				if (thisAddress.equals(address))
				{
					dates[count] = c.getLong(dateIndex);		// Grab body and date
					bodies[count++] = c.getString(bodyIndex);
				}
			} while (c.moveToNext() && count < 256);
		}
		
		// Populate our message stack
		for(int i = 0; i < count; i++)
		{
			HashMap<String, String> hm = new HashMap<String,String>();
		    hm.put("body", bodies[i]);
		    
		    // Convert date to readable format
		    formattedDate = formatter.format(dates[i]);
		    hm.put("date", formattedDate);
		    
		    // Push to message stack
		    messageStack.push(hm);
		}
		
		// Populate our hashmap from the message stack to obtain reversed order of messages
		while (!messageStack.isEmpty()) hashList.add(messageStack.pop());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
};
