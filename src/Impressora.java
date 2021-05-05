import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Impressora {
	
	private String name;
	private int port;
	private Socket socket;
	
	public Impressora(String name, int port) {
		this.name = name;
		this.port = port;
	}
	
	public void initImpressora() throws IOException {
		ServerSocket serverSocket = new ServerSocket(this.port);
		while(true) {
			this.socket = serverSocket.accept();
			OutputStream outputStream = this.socket.getOutputStream();
			InputStream inputStream = this.socket.getInputStream();
		}
	}
	
	public boolean available() {
		return !this.socket.isConnected();
	}

	public int getPort() {
		return port;
	}

	public String getName() {
		return name;
	}
	

}
