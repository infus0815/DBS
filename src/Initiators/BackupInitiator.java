package Initiators;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import Filesystem.Filesystem;
import Listeners.ListHandler;
import Peer.Peer;
import Peer.Sender;
import Utils.Utils;
import Chunk.Chunk;

public class BackupInitiator extends Thread {
	
	private File file;
	private int replicationDegree;

	public BackupInitiator(File file, int replicationDegree) {
		this.file = file;
		this.replicationDegree = replicationDegree;
	}

	@Override
	public void run() {
		
		

		String fileID = Utils.generateFID(file.getName(),Peer.id,String.valueOf(file.lastModified()));

		try {
			byte[] fileData = Filesystem.loadFile(file);

			int numChunks = fileData.length / Chunk.MAXSIZE + 1;

			ByteArrayInputStream stream = new ByteArrayInputStream(fileData);
			byte[] streamConsumer = new byte[Chunk.MAXSIZE];

			for (int i = 0; i < numChunks; i++) {
				
				byte[] chunkData;

				if (i == numChunks - 1 && fileData.length % Chunk.MAXSIZE == 0) {
					chunkData = new byte[0];
				} else {
					int numBytesRead = stream.read(streamConsumer, 0,
							streamConsumer.length);

					chunkData = Arrays.copyOfRange(streamConsumer, 0,
							numBytesRead);
				}

				Chunk chunk = new Chunk(fileID, i, replicationDegree, chunkData);

				ChunkBackup(chunk);

			}

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void ChunkBackup(Chunk chunk) throws IOException {
		
		long waitingTime = 1000;
		int attempt = 0;
		
		

		boolean done = false;
		while (!done) {
			ListHandler.storedReceived.clear();
			Sender.sendPUTCHUNK(chunk);
			try {
				System.out.println("Waiting " + waitingTime	+ "ms");
				Thread.sleep(waitingTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int confirms = Collections.frequency(ListHandler.storedReceived, chunk.id);


			if (confirms < chunk.replicationDegree) {
				attempt++;

				if (attempt > 5) {
					done = true;
				} else {
					waitingTime *= 2;
				}
			} else {
				done = true;
			}
		}

	}
		

}
