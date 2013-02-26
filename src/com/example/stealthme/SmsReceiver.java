package com.example.stealthme;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

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
		
		if (bundle != null)
		{
			// Parse the received SMS
			Object[] pdus = (Object[]) bundle.get(BUNDLE_PDU_KEY);
			ContentResolver cr = context.getContentResolver();
			
			// For each message, parse it and push it to the SMS database
			for (int i = 0; i < pdus.length; i++)
			{
				// Grab one message, convert from bytes
				SmsMessage message = SmsMessage.createFromPdu((byte[])pdus[i]);
				
				// Add it to the database
				commitMessage(cr, message);
			}
		}
	}
	
	private void commitMessage(ContentResolver cr, SmsMessage msg)
	{
		// Create an entry in the SMS database
		// Fill in the information of the message
		ContentValues v = new ContentValues();
		v.put(ADDRESS, 	msg.getOriginatingAddress());
		v.put(DATE, 	msg.getTimestampMillis());
		v.put(BODY, 	msg.getMessageBody());
		v.put(READ, 	MESSAGE_UNREAD);
		v.put(STATUS, 	msg.getStatus());
		v.put(TYPE,  	MESSAGE_TYPE_INBOX);
		v.put(SEEN, 	MESSAGE_UNSEEN);
		
		// Add it to the database
		cr.insert(Uri.parse(SMS_URI), v);
	}

};

