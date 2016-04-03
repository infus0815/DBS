package Filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Peer.Peer;

public class Filesystem {

	public static long numberOfFiles = 0;

	public static Path chunksPath;
	public static Path countfile;

	public Filesystem(String serverID) throws IOException {

		//Creating directory and count file if it doesnt exist
		Path path = Paths.get(".\\Peer_" + serverID + "_chunks");
		Path countfile = Paths.get(".\\countPeer_" + serverID + ".data");
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				System.out.println("Directory didnt exist. Generating directory..");
			} catch (IOException e) {
				//fail to create directory
				e.printStackTrace();
			}
		}
		
		Filesystem.chunksPath = path;
		Filesystem.countfile = countfile;
		loadNumberOfChunks();


	}


	public static void loadNumberOfChunks() throws IOException {

		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(chunksPath);
		int count = 0;
		for (@SuppressWarnings("unused") Path path : directoryStream) {
			count++;
		}
		numberOfFiles = count;
		System.out.println("number of files " + numberOfFiles);

	}
	
	public static final void saveChunk(String chunkId, int replicationDegree, byte[] data) throws IOException {
		
		FileOutputStream out = new FileOutputStream(chunksPath.toString() + chunkId);
		out.write(data);
		out.close();

		Peer.database.addChunk(chunkId, replicationDegree);

		loadNumberOfChunks();
	}
	
	public static boolean fileExists(String name) {
		
		File file = new File(chunksPath.toString() + name);
		return file.exists() && file.isFile();
		
	}
	
	public static final byte[] loadChunk(String chunkId) throws FileNotFoundException {
		
		File file = new File(chunksPath.toString() + chunkId);
		FileInputStream inputStream = new FileInputStream(file);

		byte[] data = new byte[(int) file.length()];

		try {
			inputStream.read(data);
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}
	
	public static final void deleteChunk(String chunkId) {
		File file = new File(chunksPath.toString() + chunkId);
		file.delete();
	}





}
