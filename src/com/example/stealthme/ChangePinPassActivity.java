package com.example.stealthme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class ChangePinPassActivity extends Activity
{
	
	// Buttons and fields to be instantiated
	ViewSwitcher switcher;
	EditText changePin;
	EditText changePinAgain;
	EditText changePass;
	EditText changePassAgain;

	// File names
	static final String PREFS_NAME = "preferences";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Decide what type of authentication to edit
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String auth = settings.getString("authType", "");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_changepinpass);
        
        // Instantiate layouts
        switcher = (ViewSwitcher)findViewById(R.id.viewSwitcher_switch);
        
        // Switch to the right view
        if (auth.equals("password"))
        {
        	switcher.showNext();
        }
	}
	
	public void changePin(View view)
	{
		changePin = (EditText)findViewById(R.id.text_changepin);
        changePinAgain = (EditText)findViewById(R.id.text_changepin_again);
        
		// First check to see if the user's two pins matched
		String firstPin = changePin.getText().toString();
		String secondPin = changePinAgain.getText().toString();
		if (!(firstPin.equals(secondPin)))
		{
			changePin.setText("");
			changePinAgain.setText("");
			Toast.makeText(this, "Your entered PINs did not match.", Toast.LENGTH_LONG).show();
		}
		else
		{
			// Create preferences editor to store new pin
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("auth", firstPin);

            // Commit our changes
            editor.commit();
            
            changePin.setText("");
			changePinAgain.setText("");
            Toast.makeText(this, "PIN succesfully changed.", Toast.LENGTH_LONG).show();
		}
	}
	
	public void changePass(View view)
	{
        changePass = (EditText)findViewById(R.id.text_changepass);
        changePassAgain = (EditText)findViewById(R.id.text_changepass_again);
        
		// First check to see if the user's two pins matched
		String firstPass = changePass.getText().toString();
		String secondPass = changePassAgain.getText().toString();
		if (!(firstPass.equals(secondPass)))
		{
			changePass.setText("");
			changePassAgain.setText("");
			Toast.makeText(this, "Your entered passwords did not match.", Toast.LENGTH_LONG).show();
		}
		else
		{
			// Create preferences editor to store new pin
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("auth", firstPass);

            // Commit our changes
            editor.commit();
            
            changePass.setText("");
			changePassAgain.setText("");
            Toast.makeText(this, "Password succesfully changed.", Toast.LENGTH_LONG).show();
		}
	}
	
};
