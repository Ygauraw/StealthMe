package com.example.stealthme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends Activity
{
	
	// Buttons and fields to be instantiated
	Button button_clearHistory;
	RadioGroup radioGroup_authType;
	
	// File names
	static final String PREFS_NAME = "preferences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Instantiate buttons and fields
        button_clearHistory = (Button)findViewById(R.id.button_clearHistory);
        radioGroup_authType = (RadioGroup)findViewById(R.id.radioGroup_AuthType);
        
        // Load previous setting on the radio group that the user checked
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String radioSelection = settings.getString("authType", "");
        if (radioSelection.equals("password"))
        	radioGroup_authType.check(R.id.radio_AuthType_Pass);
        else if (radioSelection.equals("pin"))
        	radioGroup_authType.check(R.id.radio_AuthType_Pin);

        else if (radioSelection.equals("none"))
        	radioGroup_authType.check(R.id.radio_AuthType_None);
        else
        	radioGroup_authType.check(R.id.radio_AuthType_None);
        
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
	
	// Finds which radio button was clicked and decides what to do about it
	// Triggered upon clicking one of the radio buttons
	public void processRadioClick(View view)
	{
		boolean checked = ((RadioButton)view).isChecked();	// Is the radio button checked?
		
		// Create preferences editor to save data
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
		
		// Figure out which radio button was clicked
		switch(view.getId())
		{
		case R.id.radio_AuthType_None:
			if (checked)
			{
				editor.putString("authType", "none");		// User doesn't want any authentication
				editor.commit();
			}
			break;
		case R.id.radio_AuthType_Pass:
			if (checked)
			{
				editor.putString("authType", "password");	// User wants password authentication
				editor.commit();
			}
			break;
		case R.id.radio_AuthType_Pin:
			if (checked)
			{
				editor.putString("authType", "pin");		// User wants PIN authentication
				editor.commit();
			}
			break;
		default:
			// Should never reach this
			Toast.makeText(getBaseContext(), "Form Error", Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
};

