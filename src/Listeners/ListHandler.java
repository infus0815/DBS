package Listeners;

import Message.Message;
import Message.MessageType;
import Peer.Peer;
import Peer.Sender;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Chunk.Chunk;
import Filesystem.Filesystem;
import Utils.Utils;

public class ListHandler extends Thread {

	private final Message message;
	
	private static ArrayList<String> CHUNKSsent = new ArrayList<String>();
	

	public ListHandler(Message message) {
		this.message = message;
	}

	public void run() {

		MessageType messagetype = MessageType.valueOf(message.headercontent[0]);

		switch (messagetype) {

		case PUTCHUNK:
			handlePUTCHUNK();
			break;

		case STORED:
			handleSTORED();
			break;

		case GETCHUNK:
			try {
				handleGETCHUNK();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case CHUNK:
			handleCHUNK();
			break;

		case DELETE:
			handleDELETE();
			break;

		case REMOVED:
			handleREMOVED();
			break;

		default:
			break;
		}

	}


	private void handlePUTCHUNK() {
		String chunkId = message.headercontent[3] + "_" + message.headercontent[4];

		int replicationDeg = Integer.parseInt(message.headercontent[5]);

		message.extractBody();

		if (Filesystem.numberOfFiles < Peer.space) {
			try {
				Thread.sleep(Utils.random.nextInt(400));
				if (Filesystem.fileExists(chunkId.toString())) {
					Sender.sendSTORED(chunkId);
				}
				else {
					Sender.sendSTORED(chunkId);
					Filesystem.saveChunk(chunkId, replicationDeg, message.body);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

	private void handleSTORED() {
		
		String chunkId = message.headercontent[3] + "_" + message.headercontent[4];
		String senderId = message.headercontent[2];

		Peer.database.addChunkMirror(chunkId, senderId);

	}


	private void handleGETCHUNK() throws IOException {
		String chunkId = message.headercontent[3] + "_" + message.headercontent[4];
		CHUNKSsent.clear();

		if (Peer.database.containsChunk(chunkId)) {
			try {
				Thread.sleep(Utils.random.nextInt(400));
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}

			boolean chunkAlreadySent = CHUNKSsent.contains(chunkId);

			if (!chunkAlreadySent) {
				try {
					byte[] data = Filesystem.loadChunk(chunkId);
					
					Chunk chunk = new Chunk(chunkId, 1, data);

					Sender.sendCHUNK(chunk);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			CHUNKSsent.remove(chunkId);
		}

	}

	private void handleCHUNK() {
		String chunkId = message.headercontent[3] + "_" + message.headercontent[4];
		CHUNKSsent.add(chunkId);

	}


	private void handleDELETE() {
		String fileId = message.headercontent[3];

		ArrayList<String> chunksToBeDeleted = Peer.database.getChunkIDsOfFile(fileId);

		while (!chunksToBeDeleted.isEmpty()) {
			String chunkID = chunksToBeDeleted.remove(0);

			Filesystem.deleteChunk(chunkID);
		}

	}

	private void handleREMOVED() {
		System.out.println("REMOVED");
		//TODO

	}





}
