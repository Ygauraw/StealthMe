////////////////////////////////////////////////////////////////////////////////
//
//	StartupActivity.java
// 	Description: Launched every time application is clicked from the app drawer.
// 	This activity selects whether to launch the message activity, the password
// 	login activity or the PIN login activity. It has the LAUNCHER and MAIN
//	intent filters.
//
////////////////////////////////////////////////////////////////////////////////

package com.example.stealthme;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class StartupActivity extends Activity
{
	
	// File names and constants
	static final String PREFS_NAME = "preferences";
	static final String NO_DATA = "nodata";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		
		// Open a shared preferences editor and grab user's stored authentication type
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String storedAuthType = settings.getString("authType", NO_DATA);
        SharedPreferences.Editor editor = settings.edit();
        
        // Decide which window to open based on the stored authentication method
        if (storedAuthType.equals("none"))
        {
        	Intent intent = new Intent(this, ThreadsActivity.class);
            startActivity(intent);
        }
        else if (storedAuthType.equals("password"))
        {
        	Intent intent = new Intent(this, PasswordLoginActivity.class);
            startActivity(intent);
        }
        else if (storedAuthType.equals("pin"))
        {
        	Intent intent = new Intent(this, PinLoginActivity.class);
            startActivity(intent);
        }
        else if (storedAuthType.equals(NO_DATA))
        {
        	// No stored data, probably first time loading app; assume no authentication
        	editor.putString("authType", "none");
        	editor.commit();
        	Intent intent = new Intent(this, ThreadsActivity.class);
            startActivity(intent);
        }
        else
        {
        	Toast.makeText(getBaseContext(), "Startup error.", Toast.LENGTH_LONG).show();
        }
	}
	
};
