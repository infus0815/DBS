package Listeners;

import Message.Message;
import Message.MessageType;

public class ListHandler extends Thread {
	
	private final Message message;

	public ListHandler(Message message) {
		this.message = message;
	}
	
	public void run() {
		
		
		MessageType messagetype = MessageType.valueOf(message.headercontent[0]);
		
		

		switch (messagetype) {

		// 3.2 Chunk backup subprotocol

		case PUTCHUNK:
			handlePUTCHUNK();
			break;

		case STORED:
			handleSTORED();
			break;

		// 3.3 Chunk restore protocol

		case GETCHUNK:
			handleGETCHUNK();
			break;

		case CHUNK:
			handleCHUNK();
			break;

		// 3.4 File deletion subprotocol

		case DELETE:
			handleDELETE();
			break;

		// 3.5 Space reclaiming subprotocol

		case REMOVED:
			handleREMOVED();
			break;

		default:
			break;
		}
		
	}
	

	


	

	private void handlePUTCHUNK() {
		System.out.println("PUTCHUNK");
		
	}
	
	private void handleSTORED() {
		System.out.println("STORED");
		
	}
	
	
	private void handleGETCHUNK() {
		System.out.println("GETCHUNK");
		
	}
	
	private void handleCHUNK() {
		System.out.println("CHUNK");
		
	}

	
	private void handleDELETE() {
		System.out.println("DELETE");
		
	}

	private void handleREMOVED() {
		System.out.println("REMOVED");
		
	}
	
	
	
	

}
