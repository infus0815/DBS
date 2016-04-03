package Peer;

import java.io.IOException;
import java.net.DatagramPacket;

import Chunk.Chunk;
import Message.MessageType;
import Utils.Utils;

public class Sender extends Thread {
	
	private synchronized static void sendpacketMC(byte[] buffer) {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, Peer.mcAddress, Peer.mcPort);
		try {
			Peer.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized static void sendpacketMDB(byte[] buffer) {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, Peer.mdbAddress, Peer.mdbPort);
		try {
			Peer.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized static void sendpacketMDR(byte[] buffer) {
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, Peer.mdrAddress, Peer.mdrPort);
		try {
			Peer.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void sendPUTCHUNK(Chunk chunk) throws IOException {
		String header = MessageType.PUTCHUNK + " " + Utils.VERSION;
		
		String[] chunkidsplit = chunk.id.split("_");
		header += " " + Peer.id;
		header += " " + chunkidsplit[0];
		header += " " + chunkidsplit[1];
		header += " " + chunk.replicationDegree;
		header += " " + Utils.CRLF;
		header += Utils.CRLF;

		sendpacketMDB(Utils.joinArrays(header.getBytes(), chunk.data));
	}

	public static void sendSTORED(String chunkId) {
		String header = MessageType.STORED + " " + Utils.VERSION;
		header += " " + Peer.id;
		String[] chunkidsplit = chunkId.split("_");
		header += " " + chunkidsplit[0];
		header += " " + chunkidsplit[1];
		header += " " + Utils.CRLF;
		header += Utils.CRLF;

		sendpacketMC(header.getBytes());
	}

	public static void sendGETCHUNK(String chunkId) {
		String header = MessageType.GETCHUNK + " " + Utils.VERSION;
		header += " " + Peer.id;
		String[] chunkidsplit = chunkId.split("_");
		header += " " + chunkidsplit[0];
		header += " " + chunkidsplit[1];
		header += " " + Utils.CRLF;
		header += Utils.CRLF;

		sendpacketMC(header.getBytes());
	}

	public static void sendCHUNK(Chunk chunk) throws IOException {
		String header = MessageType.CHUNK + " " + Utils.VERSION;
		header += " " + Peer.id;
		String[] chunkidsplit = chunk.id.split("_");
		header += " " + chunkidsplit[0];
		header += " " + chunkidsplit[1];
		header += " " + Utils.CRLF;
		header += Utils.CRLF;

		sendpacketMDR(Utils.joinArrays(header.getBytes(), chunk.data));
	}

	public static void sendDELETE(String fileId) {
		String header = MessageType.DELETE + " " + Utils.VERSION;
		header += " " + Peer.id;
		header += " " + fileId;
		header += " " + Utils.CRLF;
		header += Utils.CRLF;

		sendpacketMC(header.getBytes());
	}

	public static void sendREMOVED(String chunkId) {
		String header = MessageType.REMOVED + " " + Utils.VERSION;
		header += " " + Peer.id;
		String[] chunkidsplit = chunkId.split("_");
		header += " " + chunkidsplit[0];
		header += " " + chunkidsplit[1];
		header += " " + Utils.CRLF;
		header += Utils.CRLF;

		sendpacketMC(header.getBytes());
	}


}
