package Initiators;

import java.io.File;

import Peer.Peer;
import Peer.Sender;

public class DeleteInitiator extends Thread{
	
	private String fileName;

	public DeleteInitiator(File file) {
		fileName = file.getName();
	}

	public void run() {
	
		if (Peer.database.fileHasBeenBackedUp(fileName)) {
		
			String fileID = Peer.database.getFileInfo(fileName).fileId;

			Sender.sendDELETE(fileID);

			Peer.database.removeBackupFile(fileName);
		} else {
			System.out.println("no chunks found");
		}
	}

}
