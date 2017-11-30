import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 1488;
//    public static final String HOST = "localhost";
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started, waiting for connection!");
            Socket socket = serverSocket.accept();
            System.out.println("Accepted:"+socket.getInetAddress());

            try(InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream()){

                byte[] buf = new byte[32 * 1024];
                int readBufs = in.read(buf);
                String line = new String(buf, 0, readBufs);
                System.out.println("Client: "+line);

                out.write(buf);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
