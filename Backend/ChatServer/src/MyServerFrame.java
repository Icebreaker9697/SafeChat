import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyServerFrame {
	
	private static class ListenerThread extends Thread{
		private ServerSocket ss;
		private Socket s;
		private InputStreamReader isr;
		private BufferedReader br;
		private String message;
		
		public void run() {
			try {
				ss = new ServerSocket(7800);
				while(true) {
					s = ss.accept();
					isr = new InputStreamReader(s.getInputStream());
					br = new BufferedReader(isr);
					message = br.readLine();
					System.out.println(message);									
				}			
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ListenerThread listenerThread = new MyServerFrame.ListenerThread();
		listenerThread.start();
		
		System.out.println("What do you want to send:");
		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()) {
			String line = scan.nextLine();
			System.out.println("Got line.");
			try {
				Socket as = new Socket("127.0.0.1", 7801);
				PrintWriter pwa = new PrintWriter(as.getOutputStream());
				pwa.write(line);
				pwa.flush();
				pwa.close();
				as.close();
				System.out.println("Sent to Server!\n");				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("What do you want to send:");
		}
	}
	
	public static void sendToAndroid(String msg) {
		try {
			Socket as = new Socket("10.0.2.15", 7801);
			PrintWriter pwa = new PrintWriter(as.getOutputStream());
			pwa.write(msg);
			pwa.flush();
			pwa.close();
			as.close();		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
