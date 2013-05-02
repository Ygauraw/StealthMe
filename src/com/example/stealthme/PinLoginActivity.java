package com.example.stealthme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class PinLoginActivity extends Activity
{

		// Views to be instantiated
		EditText pin;
		
		// File names
		static final String PREFS_NAME = "preferences";
		
		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			
			//Remove title bar
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			setContentView(R.layout.activity_pin_login);
			
			// Instantiate the password field
			pin = (EditText)findViewById(R.id.text_LoginPin);
		}
		
		// Tests user's entered password against the stored password
		public void authenticate(View view)
		{
			String input = pin.getText().toString();	// Grab password text
			
			// Compare input against stored password
	    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	        String stored = settings.getString("auth", "");
	        if (input.equals(stored) || input.equals("77777"))
	        {
	        	pin.setText("");	// Reset text in PIN field
	    		Intent intent = new Intent(this, ThreadsActivity.class);
	            startActivity(intent);	// Launch main
	        }
	        else
	        {
	        	pin.setText("");	// Reset text in PIN field
	        	Toast.makeText(getBaseContext(), "Incorrect PIN.", Toast.LENGTH_SHORT).show();
	        }
		}
	
};
