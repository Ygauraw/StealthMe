package com.example.stealthme;

import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity
{

	// Views to be instantiated
	Button button_SendMessage;
	Button button_OpenSettings;
	EditText text_PhoneNumber;
	EditText text_Message;
	EditText text_History;
	
	// Working variables
	String history = "";	// Used to store message history
	
	// File names
	static final String PREFS_NAME = "preferences";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Instantiate buttons and fields
        button_SendMessage = (Button)findViewById(R.id.button_SendMessage);
        button_OpenSettings = (Button)findViewById(R.id.button_OpenSettings);
        text_PhoneNumber = (EditText)findViewById(R.id.text_PhoneNumber);
        text_Message = (EditText)findViewById(R.id.text_Message);
        text_History = (EditText)findViewById(R.id.text_History);
        
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
        
        // Handle opening settings menu
        button_OpenSettings.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
            	// Send an intent to open the settings activity
            	openSettings(v);
            }
        });
        
        // Load the message history
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        history = settings.getString("messageHistory", "");
        text_History.setText(history);	// Display the loaded history
    }
    
    // On activity resume
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	// Reload chat history
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        history = settings.getString("messageHistory", "");
        text_History.setText(history);	// Display the loaded history
    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
    	
        // Create preferences editor to store history
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("messageHistory", history);

        // Commit our changes
        editor.commit();
    }
    
    // sendMessage method: sends a given message to another device via SMS
    private void sendMessage(String targetNumber, String targetMessage)
    {
    	// Create a pending intent; can be used to display another activity after sending the message,
    	// but in our case it will just send it back to MainActivity       
        SmsManager messageManager = SmsManager.getDefault();
        messageManager.sendTextMessage(targetNumber, null, targetMessage, null, null);
        
        // Update the history text field to reflect the newly sent message
        updateHistory(targetMessage);
    }
    
    // Adds the desired message to the message history
    private void updateHistory(String storeMessage)
    {
    	// Append the sent message to history storage
    	if (history != "") history += "\n\n" + "Me> " + storeMessage;
    	else history += "Me> " + storeMessage;
    	text_History.setText(history);
    }
    
    // Opens the settings menu screen - invoked by the settings button
    public void openSettings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
};

