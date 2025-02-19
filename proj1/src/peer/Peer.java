package peer;

import java.net.MulticastSocket;
import peer.handler.Handler;
import peer.message.MessageOnline;

public class Peer extends Thread
{
	private String rmiMethodName;
	private McastID[] connections;
	private MulticastSocket mcSocket, mdbSocket, mbrSocket;
	static boolean running = true;
	
	Thread mc;
	Thread mdb;
	Thread mdr;
	Thread client;
	
	public Peer(String version, int serverID, String rmi, McastID[] connections, String ip) 
	{
		System.out.println("----------------------------------------------");
		Manager.getInstance().init(version, serverID, connections, ip);
		this.rmiMethodName = rmi;
		this.connections = connections;
		
		try
		{
			System.out.println("----------------------------------------------");
			System.out.println("MC  Channel: multicast group " + this.connections[0].addr + ":" + this.connections[0].port);
			this.mcSocket = new MulticastSocket(this.connections[0].port);
			this.mcSocket.joinGroup(connections[0].addr);
			
			System.out.println("MDB Channel: multicast group " + this.connections[1].addr + ":" + this.connections[1].port);
			this.mdbSocket = new MulticastSocket(this.connections[1].port);
			this.mdbSocket.joinGroup(connections[1].addr);
			
			System.out.println("MDR Channel: multicast group " + this.connections[2].addr + ":" + this.connections[2].port);
			this.mbrSocket = new MulticastSocket(this.connections[2].port);
			this.mbrSocket.joinGroup(connections[2].addr);
			
			Manager.getInstance().setSockets(mcSocket, mbrSocket, mdbSocket);
			System.out.println("----------------------------------------------");
			System.out.println("Format of log messages:");
			System.out.println("<Thread id>-<handler type>: <message>");
			System.out.println("\n");
		}
		catch (Exception e)
		{
			System.out.println("Error creating and joining multicast socket(s): " + e.getMessage());
			System.exit(-1);
		}
		
		mc = new DispatcherMC();
		mdb = new DispatcherMDB();
		mdr = new DispatcherMDR();
		client = new DispatcherRMI(this.rmiMethodName);
	}
	
	@Override
	public void run()
	{
		mc.start();
		mdb.start();
		mdr.start();
		client.start();
		
		//Enhancement: send ONLINE message upon start
		MessageOnline message = new MessageOnline();
		Handler.send(Channels.MC, message.getMessageBytes());
		
		while (true)
		{
			try
			{
				Thread.sleep(2000);
				Manager.getInstance().saveState();
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
