package peer;

import java.net.MulticastSocket;

public class DispatcherMDB implements Runnable 
{
	MulticastSocket socket;
	String version;
	int serverID;
	
	public DispatcherMDB(MulticastSocket mdbSocket, String version, int serverID) 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub

	}

}