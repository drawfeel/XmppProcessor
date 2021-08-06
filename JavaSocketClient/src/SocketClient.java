import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {
    static final int SOCKET_PORT = 7779;

    public static void main(String[] args) throws InterruptedException {
        try {
            Socket socket = new Socket("localhost", SOCKET_PORT);

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.write("EXIT");
            pw.flush();

            socket.shutdownOutput();

            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String msg = null;
            while((msg = br.readLine()) != null){
                System.out.println("messgae received from server:"+ msg);
            }

            br.close();
            is.close();
            os.close();
            pw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}