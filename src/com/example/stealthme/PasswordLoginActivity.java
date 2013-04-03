package com.example.stealthme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordLoginActivity extends Activity
{

	// Views to be instantiated
	EditText password;
	
	// File names
	static final String PREFS_NAME = "preferences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_login);
		
		// Instantiate the password field
		password = (EditText)findViewById(R.id.text_LoginPassword);
	}
	
	// Tests user's entered password against the stored password
	public void authenticate(View view)
	{
		String input = password.getText().toString();	// Grab password text
		
		// Compare input against stored password
    	//SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //String stored = settings.getString("password", "");
		String stored = "pass";
        if (input.equals(stored))
        {
        	password.setText("");	// Reset text in password field
    		Intent intent = new Intent(this, ThreadsActivity.class);
            startActivity(intent);	// Launch main
        }
        else
        {
        	password.setText("");	// Reset text in password field
        	Toast.makeText(getBaseContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
        }
	}
	
};
