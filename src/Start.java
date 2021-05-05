import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Start {
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket server = new ServerSocket(4000);
		System.out.println("Servidor iniciado na porta 4000");
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
			        Socket cliente;
					try {
						cliente = server.accept();
						createConnection(cliente);
					} catch (IOException e) {
						e.printStackTrace();
					}   
				}
			}
		}).start();
		
	}
	
	public static void createConnection(Socket clientConnected) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				OutputStream outputStream;
				try {
					outputStream = clientConnected.getOutputStream();
					outputStream.write(Response.CONNECTED.getMessage().getBytes());
					outputStream.flush();
					while(clientConnected.isConnected()) {
						outputStream.write(Response.WAITING_DATA.getMessage().getBytes());
				        outputStream.flush();
				        Thread.sleep(5000);
					}
				} catch (IOException | InterruptedException e) { e.printStackTrace(); }
				
			}
		}).start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStream inputStream = clientConnected.getInputStream();
					DataInputStream in = new DataInputStream(inputStream);
					while(clientConnected.isConnected()) {
						try {
							if(in.read() != -1) {
								byte[] messageByte = new byte[1024];
								String message = "";
								while(true) {
									try {
										int bytesRead = in.read(messageByte);
										message+=new String(messageByte, 0, bytesRead);
										
										if(message == Resquest.PRINT.getMessage()) { System.out.println("Imprimindo");}
										
										if(message == Resquest.CLOSE.getMessage()) {
											 System.out.println("Fechando o socket");
											 clientConnected.close();
										}
										break;
									} catch (IOException e) {}
								}
								System.err.println("MENSAGEM: "+message);
							}
						}catch(IOException e) {}
					}
				} catch (IOException e) { e.printStackTrace(); }
			}
		}).start();
	}
	
	
	public enum Response{
		CONNECTED("Você está conectado ao servidor"),
		WAITING_DATA("Aguardando dados");

		private String message;
		
		Response(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
	
	public enum Resquest{
		PRINT("IMPRIMIR"),
		CLOSE("FECHAR_SOCKET");
		
		private String message;
		
		Resquest(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
	

}
