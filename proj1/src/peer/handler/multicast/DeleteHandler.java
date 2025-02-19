package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import peer.ChunkManager;
import peer.Manager;
import peer.Utilities;
import peer.handler.Handler;

public class DeleteHandler extends Handler
{
	private String id;

	public DeleteHandler(byte[] message)
	{
		this.handlerType = "DeleteHandler";
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.id = elements[3];
	}
	
	@Override
	public void run()
	{
		ChunkManager manager = Manager.getInstance().getChunkManager();
		ArrayList<Integer> removed = manager.deleteAllChunksOfFile(id);
		
		if (removed.size() > 0)
			log("deleted " + removed.size() + " chunks of file with id = "+ Utilities.minifyId(id));
	}
}
