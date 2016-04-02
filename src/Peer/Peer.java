package Peer;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Listeners.MClistener;



public class Peer {

	public static InetAddress mcAddress = null;
	public static int mcPort = 0;

	public static InetAddress mdbAddress = null;
	public static int mdbPort = 0;

	public static InetAddress mdrAddress = null;
	public static int mdrPort = 0;
	
	public static String id = null;
	public static int space = 0;
	public static MulticastSocket socket;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		/*
		 * ARGS
		 * 0 - server id
		 * 1 - MC Address
		 * 2 - MC Port
		 * 3 - MDB Address
		 * 4 - MDB Port
		 * 5 - MDR Address
		 * 6 - MDR Port
		 * 7 - Number of Chunks(Space available)
		 */

		//Creating directory and count file if it doesnt exist
		
		
		Path path = Paths.get(".\\Peer_" + args[0]);
		Path countfile = Paths.get(".\\countPeer_" + args[0] + ".txt");
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				System.out.println("Directory didnt exist. Generating directory..");
			} catch (IOException e) {
				//fail to create directory
				e.printStackTrace();
			}
		}

		if(!Files.exists(countfile)) {
			try {
				Files.createFile(countfile);
				System.out.println("Count file didnt exist. Generating file..");
			} catch (IOException e) {
				//fail to create file
				e.printStackTrace();
			}
		}

		//initiating stuff
		
		id = args[0];
		

		//mc
		mcAddress = InetAddress.getByName(args[1]);
		mcPort = Integer.parseInt(args[2]);

		//mdb
		mdbAddress = InetAddress.getByName(args[3]);
		mdbPort = Integer.parseInt(args[4]);

		//mdr
		mdrAddress = InetAddress.getByName(args[5]);
		mdrPort = Integer.parseInt(args[6]);
		
		space = Integer.parseInt(args[7]);
		socket = new MulticastSocket();
		
		
		MClistener mcList = new MClistener(mcAddress,mcPort);
		mcList.start();
		
		






	}




}
