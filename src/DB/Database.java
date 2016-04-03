package DB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import Peer.Peer;

public class Database implements Serializable {

	private static final long serialVersionUID = 1L;

	private volatile HashMap<String, ChunkInfo> chunkDB;

	public Database() {
		
		chunkDB = new HashMap<String, ChunkInfo>();
		
	}

	public synchronized boolean containsChunk(String chunkId) {
		
		return chunkDB.containsKey(chunkId);
		
	}

	public synchronized void addChunk(String chunkId, int replicationDegree) {
		
		if (!containsChunk(chunkId)) {
			chunkDB.put(chunkId, new ChunkInfo(replicationDegree,new ArrayList<String>()));
			Peer.saveDatabase();
		}
		
	}

	public synchronized void removeChunk(String chunkID) {
		
		chunkDB.remove(chunkID);
		Peer.saveDatabase();
		
	}

	public synchronized void addChunkMirror(String chunkID, String peerID) {
		if (containsChunk(chunkID)) {
			if (!chunkDB.get(chunkID).mirrors.contains(peerID)) {
				chunkDB.get(chunkID).mirrors.add(peerID);
				Peer.saveDatabase();
			}
		}
	}

	
	public synchronized void removeChunkMirror(String chunkID, String peerID) {
		chunkDB.get(chunkID).removeMirror(peerID);
		
	}

	public synchronized int getChunkReplicationDegree(String chunkID) {
		
		return chunkDB.get(chunkID).replicationDegree;
		
	}

	public synchronized int getChunkMirrorsSize(String chunkID) {
		
		return chunkDB.get(chunkID).mirrors.size();
		
	}

	public synchronized String getBestChunktoRemove() {
		
		String best = null;
		for (String chunkID : chunkDB.keySet()) {
			if (best == null || getChunkMirrorsSize(chunkID) - getChunkReplicationDegree(chunkID) > getChunkMirrorsSize(best) - getChunkReplicationDegree(best)) {
				best = chunkID;
			}
		}
		
		return best;
		
	}
	
	public synchronized ArrayList<String> getChunkIDsOfFile(String fileId) {
		ArrayList<String> chunkIDs = new ArrayList<String>();

		for (String chunkID : chunkDB.keySet()) {
			String currentId = chunkID.split("_")[0];
			if (currentId.equals(fileId))
				chunkIDs.add(chunkID);
		}
		return chunkIDs;
	}



}
