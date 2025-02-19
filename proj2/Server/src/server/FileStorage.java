package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileStorage implements Serializable
{
	private static final long serialVersionUID = 4832308080638012451L;
	private String storageDir = "storage_";
	private ArrayList<String> storedFiles;

	public FileStorage()
	{
		this.storedFiles = new ArrayList<>();	
	}
	
	public void getExistantFilesInStorage() {
		String path = storageDir + ServerManager.getInstance().getId();
		
	    File directory = new File(path);
	    if (!directory.exists())
	        directory.mkdir();
	    else {
	    	System.out.println(directory.listFiles());
	    }
	}
	
	public int save(String title, byte[] data)
	{	
		String path = storageDir + ServerManager.getInstance().getId();
	    File directory = new File(path);
	    if (!directory.exists())
	        directory.mkdir();
	    
	    path += "/" + title;
		try
		{		
			File file = new File(path);
			if (file.exists() || this.storedFiles.contains(file.getName()))
				return -1;
			
			if (data != null) 
			{
				DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
				stream.write(data, 0, data.length);
				stream.close();
			}
			else
				return 0;
			
			this.storedFiles.add(title);
			return 1;
		} 
		catch (IOException e)
		{
			System.out.println("Error while writting to " + path);
			return 0;
		}
	}

	public ArrayList<String> search(String searchTerm)
	{	
		String regex = Pattern.quote(searchTerm);
		
		String pattern = "(.*)" + searchTerm + "(.*)";
		Pattern regex2 = Pattern.compile(pattern);
		
		ArrayList<String> list = new ArrayList<>();
		
		
		for (String file : this.storedFiles)
		{
			if (file.matches(regex))
				list.add(file);
			else
			{
				Matcher m = regex2.matcher(file);
				if(m.lookingAt())
					list.add(file);
			}
			
		}
		return list;
	}

	public byte[] retrieve(String title)
	{
		title = Utils.decode(title);
		String path = storageDir + ServerManager.getInstance().getId() + "/" + title;

		if (storedFiles.indexOf(title) == -1)
			return null;

		byte[] data = null;
		
		try {
			File file = new File(path);	
			if(!file.exists())
				System.out.println("File doesn't Exist on storage!");
			else if(!file.isFile())
				System.out.println("File is not File on storage!");
			data = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
}
