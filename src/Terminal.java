import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Terminal {
	
	private final static String ADDRESS = "127.0.0.1";
	private final static int PORT = 4000;
	
	public static void main(String[] args) throws InterruptedException {
		Scanner scanner = new Scanner(System.in);
		try {
			Socket socket = new Socket(ADDRESS, PORT);
			if(socket.isConnected()) {
				System.out.println("Conectado ao servidor "+ADDRESS+":"+PORT);
				
				OutputStream outputStream = socket.getOutputStream();
				InputStream inputStream = socket.getInputStream();
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							DataInputStream in = new DataInputStream(inputStream);
							try {
								if(in.read() != -1) {
									byte[] messageByte = new byte[1024];
									String message = "";
									while(true) {
										try {
											int bytesRead = in.read(messageByte);
											message+=new String(messageByte, 0, bytesRead);
											break;
										} catch (IOException e) {}
									}
									System.err.println(message);
								}
							}catch(IOException e) {}
							
						}
					}
				}).start();
				
				while(true) {
					String text = scanner.nextLine();
					if(text.toLowerCase() == "sair") { break; }
					outputStream.write(text.getBytes());
					outputStream.flush();
				}
			}
		} catch (IOException e) {
			System.out.println("Não foi possivel encontrar o host: "+ADDRESS+":"+PORT);
		}
		scanner.close();
	}

}
