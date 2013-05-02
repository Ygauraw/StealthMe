package com.example.stealthme;

import java.util.ArrayList;

import android.util.Base64;

public class Sms
{
	public String text;
	public char headerValue;
	public char saltIndex;
	public static EncryptionSuite encrypt = null;
	
	public Sms(String Text, char HeaderValue, char SaltIndex, String password)
	{
		text = Text;
		headerValue = HeaderValue;
		saltIndex = SaltIndex;
		encrypt = new EncryptionSuite(password);
	}
	
	public String serialize(byte[] key)
	{
		try {
		byte[] temp = {(byte) headerValue};
		temp = concatenate(temp, text.getBytes("UTF-8"));
		return "" + (char) 29 + saltIndex + Base64.encodeToString(temp, Base64.DEFAULT);
		} catch (Exception e) { return null; }
	}
	
	private byte[] concatenate(byte[] a, byte[] b)
	{
		byte rtrn[] = new byte[a.length + b.length];
		int i = 0;
		for (; i < a.length; i++)
			rtrn[i] = a[i];
		for (int j = 0; j < b.length; j++)
			rtrn[i + j] = b[j];
		return rtrn;
	}
	
}