package Initiators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Chunk.Chunk;
import DB.Fileinfo;
import Filesystem.Filesystem;
import Listeners.ListHandler;
import Peer.Peer;
import Peer.Sender;
import Utils.Utils;

public class RestoreInitiator extends Thread{

	private File file;

	public RestoreInitiator(File file) {
		this.file = file;
	}
	public void run() {
		if (Peer.database.fileHasBeenBackedUp(file.getName())) {
			Fileinfo fileinfo = Peer.database.getFileInfo(file.getName());
			ListHandler.chunksReceived.clear();

			for (int i = 0; i < fileinfo.nChunks; i++) {
				String chunkId = fileinfo.fileId + "_" + i;
				Sender.sendGETCHUNK(chunkId);
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			while(ListHandler.chunksReceived.size() < fileinfo.nChunks) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			//restoring file
			byte[] fileData = new byte[0];
			
			System.out.println(ListHandler.chunksReceived.toString());

			for (int i = 0; i < fileinfo.nChunks; i++) {
				Chunk rightChunk = null;

				for (Chunk chunk : ListHandler.chunksReceived) {
					String[] ids = chunk.id.split("_");
					int chunkn = Integer.parseInt(ids[1]);
					if (chunkn == i && ids[0].equals(fileinfo.fileId)) {
						rightChunk = chunk;
						System.out.println(rightChunk.id);
						break;
					}
				}

				if (rightChunk == null)
					System.out.println("Missing file chunk.");

				try {
					fileData = Utils.joinArrays(fileData, rightChunk.data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				Filesystem.saveFile(file.getName(), fileData);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("The requested file can not be restored.");
		}
	}

}
