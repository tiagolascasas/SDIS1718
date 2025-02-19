package server.handlers;

import java.net.Socket;

import server.Notifier;
import server.ServerManager;
import server.Utils;

public class HandlerLogin extends Handler
{
	private int peerPort;

	public HandlerLogin(Socket socket, String username, String password, String p2pPort)
	{
		super("LOGIN", username, password, socket);
		this.peerPort = Integer.parseInt(p2pPort);
	}

	@Override
	public void run()
	{
		log("Starting");
		
		ServerManager manager = ServerManager.getInstance();
		
		StringBuilder build = new StringBuilder();
		build.append("RES_LOGIN ");
		if (!verifyCredentials())
		{
			String message = Utils.encode("Error: the username and/or password are incorrect");
			build.append(0).append(" ").append(message);
		}
		else if (!manager.loginUser(username, password, socket, peerPort))
		{
			String message = Utils.encode("Error: you are already logged in");
			build.append(0).append(" ").append(message);
		}
		else
		{
			String message = Utils.encode("Successfully logged in");
			build.append(1).append(" ").append(message);
			
			String notify = "Client " + username + " is now online";
			Notifier notif = new Notifier(username, notify);
			notif.start();
		}
		build.append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}
}
