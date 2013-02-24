package com.example.stealthme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends Activity
{
	
	// Buttons and fields to be instantiated
	Button button_clearHistory;
	
	// File names
	static final String PREFS_NAME = "preferences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Instantiate buttons and fields
        button_clearHistory = (Button)findViewById(R.id.button_clearHistory);
        
        // Handle sending the message when the user hits 'Send'
        button_clearHistory.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {                
                // Create preferences editor to clear history
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("messageHistory", "");

                // Commit our changes
                editor.commit();
            }
        });
    }
	
};

