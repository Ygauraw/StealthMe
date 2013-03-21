package com.example.stealthme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
			setContentView(R.layout.activity_pin_login);
			
			// Instantiate the password field
			pin = (EditText)findViewById(R.id.text_LoginPin);
		}
		
		// Tests user's entered password against the stored password
		public void authenticate(View view)
		{
			String input = pin.getText().toString();	// Grab password text
			
			// Compare input against stored password
	    	//SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	        //String stored = settings.getString("password", "");
			String stored = "2388";
	        if (input.equals(stored))
	        {
	        	pin.setText("");	// Reset text in PIN field
	    		Intent intent = new Intent(this, MainActivity.class);
	            startActivity(intent);	// Launch main
	        }
	        else
	        {
	        	pin.setText("");	// Reset text in PIN field
	        	Toast.makeText(getBaseContext(), "Incorrect PIN.", Toast.LENGTH_SHORT).show();
	        }
		}
	
};
