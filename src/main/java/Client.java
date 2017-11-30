import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.Socket;


public class Client {
    public static final int PORT = 1488;
    public static final String HOST = "localhost";

    public static void main(String[] args) {
        Socket socket = null;

        try{
            socket = new Socket(HOST, PORT);

            try (InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream();){
                BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Client started. Input the field and rule:");
                String field = bf.readLine();
                String line = "";
                out.write(field.getBytes());
                out.flush();

                byte[] data = new byte[32 * 1024];
                int readBytes = in.read(data);

                System.out.printf("Server's answer> %s",new String(data, 0, readBytes));

            }

        }catch (IOException e){
            e.printStackTrace();
        }
//        finally {
//            IOUtil.(socket);
//        }
    }
}
