package Peer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import DB.Database;
import Filesystem.Filesystem;
import Initiators.BackupInitiator;
import Initiators.RestoreInitiator;
import Listeners.ListHandler;
import Listeners.Listener;
import Listeners.MClistener;
import RMI.RMInterface;



public class Peer implements RMInterface {
	

	public static InetAddress mcAddress = null;
	public static int mcPort = 0;

	public static InetAddress mdbAddress = null;
	public static int mdbPort = 0;

	public static InetAddress mdrAddress = null;
	public static int mdrPort = 0;

	public static String id = null;
	public static int space = 0;
	public static MulticastSocket socket;
	
	
	public static volatile Database database;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
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

		//rmi
		Peer peer = new Peer();
		RMInterface stub = (RMInterface) UnicastRemoteObject.exportObject(peer, 0);
		// Bind the remote object's stub in the registry
		Registry registry = LocateRegistry.getRegistry();
		try {
			/*
			 * The name to which the remote object shall be bound (<remote_object_name>) 
			 * is passed to the server as a command line argument
			 */
			registry.bind(args[0], stub);				// args[0] = <remote_object_name>
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Filesystem f = new Filesystem(args[0]);
		
		database = new Database();
		loadDatabase();


		//listeners
		Listener mcList = new Listener(mcAddress,mcPort);
		mcList.start();
		Listener mdbList = new Listener(mdbAddress,mdbPort);
		mdbList.start();
		Listener mdrList = new Listener(mdrAddress,mdrPort);
		mdrList.start();
		
		while(true);



	}

	private static void createDatabase() {
		database = new Database();
		saveDatabase();
	}

	private static void loadDatabase() throws ClassNotFoundException,IOException {
		try {
			FileInputStream fileInputStream = new FileInputStream(Filesystem.countfile.toString());

			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			database = (Database) objectInputStream.readObject();
			objectInputStream.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Database not found. Creating...");
			createDatabase();
		}
	}

	public static void saveDatabase() {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(Filesystem.countfile.toString());

			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

			objectOutputStream.writeObject(database);
			objectOutputStream.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Database not found. Creating...");
			createDatabase();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public String tarefa(String message) {
		String str;
		File file;

		String[] splitmessage = message.split(" ");

		switch(splitmessage[0]) {

		case "BACKUP":
			file = new File(splitmessage[1]);
			int repDeg = Integer.parseInt(splitmessage[2]);
			BackupInitiator bi = new BackupInitiator(file,repDeg);
			bi.start();

			break;
		case "RESTORE":
			file = new File(splitmessage[1]);
			RestoreInitiator ri = new RestoreInitiator(file);
			ri.start();
			break;
		case "DELETE":
			break;
		case "RECLAIM":
			break;
		default:
			break;

		}

		System.out.println("RECEBEU: "+ message);
		str = processa(message);
		System.out.println("RESPOSTA: "+ str);

		return str;
	}


	String processa (String message){	// funcao eco
		return message;
	}




}
