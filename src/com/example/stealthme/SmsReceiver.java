package com.example.stealthme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver
{
	
	// Working variables
	public static final String BUNDLE_PDU_KEY = "pdus";
	public static final String SMS_URI = "content://sms";
	public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;
    public static final int MESSAGE_UNREAD = 0;
    public static final int MESSAGE_READ = 1;
    public static final int MESSAGE_UNSEEN = 0;
    public static final int MESSAGE_SEEN = 1;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// Grab message from bundle
		Bundle bundle = intent.getExtras();
		SmsMessage[] messages = null;
		String body = "";
		
		if (bundle != null)
		{
			// Parse the received SMS
			Object[] pdus = (Object[]) bundle.get(BUNDLE_PDU_KEY);
			messages = new SmsMessage[pdus.length];
			
			// For each message, parse it and push it to the SMS database
			for (int i = 0; i < pdus.length; i++)
			{
				// Grab one message, convert from bytes
				messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				
				// Format it
				body += messages[i].getOriginatingAddress() + " : " + messages[i].getMessageBody() + "\n";
				
				// Add it to the database
				// commitMessage(cr, message);
			}
			
			// Show it in a toast
			Toast.makeText(context, body, Toast.LENGTH_LONG).show();
		}
	}

};

