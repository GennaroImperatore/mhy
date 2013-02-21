package me.org.datacapturing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import me.org.datacapturing.UserInfo.SIDE;

import android.R.integer;
import android.os.Environment;

public class DataManager {
	
	public static class UncorrectUserInfoDataException extends Throwable
	{
		public String message = "Data in the file is incorrect";
	}
	
	
	private static final String path= Environment.getExternalStorageDirectory()+"/info.ugame";
	
	
	public static boolean createUserInfo(UserInfo info)
	{
		StringBuilder cont = new StringBuilder();
		
		cont.append(String.valueOf(info.getNOTP()) + '\r');
		cont.append(info.getEydness().toString() + '\r');
		cont.append(info.getHandedness().toString() + '\r');
		
		try 
		{
			ReadWriteTextFile.setContents(new File(path), cont.toString());
		}
		catch (IllegalArgumentException iaex)
		{
			iaex.printStackTrace();
		}
		catch (IOException ioex)
		{
		 ioex.printStackTrace();
		 
		}
		
			return false;
		
	}
	
	
	public static UserInfo loadUserInfofromFile() throws UncorrectUserInfoDataException
	{
		SIDE h,e;
		int notp=0;
		//check if file is present
		//if not tell the user
		
		String strContents = ReadWriteTextFile.getContents(new File(path));
		if (strContents==null)
			return null;
		
		String[] strUserInfo = strContents.split(System.getProperty("line.separator")); // split the string using the new line separator
		
		//if there is a file load the data from it and return it to the user
		
		//[0] is notp 
		//[1] is eydness
		//[2] is handedness
				
		
		try 
		{
			notp = Integer.parseInt(strUserInfo[0]);
		}
		catch (NumberFormatException nex)
		{
			throw new UncorrectUserInfoDataException();
		}
		finally 
		{
			if (notp <0)
				throw new UncorrectUserInfoDataException();
		}
		
		// check eyedness
		if(strUserInfo[1].equals("LEFT"))
			e= SIDE.LEFT;
		else if (strUserInfo[1].equals("RIGHT"))
			e = SIDE.RIGHT;
		else if (strUserInfo[1].equals("NONE"))
			e = SIDE.NONE;
		
		else
			throw new  UncorrectUserInfoDataException();
		
		//check handedness
		
		if(strUserInfo[2].equals("LEFT"))
			h= SIDE.LEFT;
		else if (strUserInfo[2].equals("RIGHT"))
			h = SIDE.RIGHT;
		else if (strUserInfo[2].equals("NONE"))
			h = SIDE.NONE;
		else
			throw new  UncorrectUserInfoDataException();
		
		//build new user info
	    UserInfo ui  = new UserInfo(h, e, notp);
		
		return ui;
		
	}
	
	public static void increaseNotp () // increases the number of times played
	
	{
		int notp = Integer.parseInt(  ReadWriteTextFile.getLineFromContents(new File(path), 0)); // get the number of times played
		
		UserInfo ui = new UserInfo();
		try {
			ui = loadUserInfofromFile();
		} catch (UncorrectUserInfoDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createUserInfo(new UserInfo(ui.getEydness(), ui.getHandedness(), notp+1));
		
	}
	
	
	
	
	private static boolean checkIfuserInfoPresent()
	{
		return false;
		
	}
	

}
