import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import Utils.Utils;

public class teste {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Utils u = new Utils();
		String result = u.generateFID("lala", "1", "33333");
		
		String aA  = "lala";
		String bB  = "lala";
		
		if(aA.equals(bB))
			System.out.print("É ISSO");
		
		char a = 0xD;
		char b = 0xA;
		String ab = "" + a + b;
		System.out.print(ab);
		System.out.print(ab);
		
		System.out.println(result);
		
		String teste = "PUTCHUNK 2 3 3 3\r\n\r\n";
		
		byte[] buf = new byte[256];
		
		buf = teste.getBytes();
		
		InetAddress address = InetAddress.getByName("224.0.0.233");
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length,	address, 2201);
		
		
		
		MulticastSocket socket;
		
		socket = new MulticastSocket();
		socket.setTimeToLive(1);
		socket.joinGroup(address);
		
		socket.send(packet);
		
		
		socket.close();

	}

}
