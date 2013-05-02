package com.example.stealthme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class NewMessageActivity extends Activity
{

	// View to be instantiated
	EditText phoneNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_newmessage);
        
        // Instantiate views
        phoneNumber = (EditText)findViewById(R.id.edittext_newmessage_recipient);
	}
	
	public void sendNewMessage(View view)
	{
		// Grab entered number, go back to threads activity
		String recipient = phoneNumber.getText().toString();
		Intent intent = new Intent(this, ThreadsActivity.class);
    	intent.putExtra("recipient", recipient);
    	
        startActivity(intent);	// Launch threads activity
	}
	
};
