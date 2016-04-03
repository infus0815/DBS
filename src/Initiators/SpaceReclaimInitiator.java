package Initiators;

import java.io.IOException;

import Filesystem.Filesystem;
import Peer.Peer;
import Peer.Sender;

public class SpaceReclaimInitiator extends Thread {
	
	private int newSpace;
	
	public SpaceReclaimInitiator(int newSpace) {
		this.newSpace = newSpace;
	}

	@Override
	public void run() {
		
		Peer.space = newSpace;
		try {
			Filesystem.loadNumberOfChunks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(Peer.space < Filesystem.numberOfFiles) {
			
			String chunkId = Peer.database.getBestChunktoRemove();
			
			Sender.sendREMOVED(chunkId);
			
			Peer.database.removeChunk(chunkId);
			try {
				Filesystem.deleteChunk(chunkId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	

}
