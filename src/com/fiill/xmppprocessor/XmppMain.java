package com.fiill.xmppprocessor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class XmppMain {
    private static boolean cmd_quit = false;
    static ServerSocket serverSocket;

    public static void main(String[] args) throws Exception {
        MyXMPPConnection.onLine(); // online
        int port = MyXMPPConnection.SOCKET_PORT;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            Socket socket = null;
            System.out.println("Server Started on " + port);
            while(!cmd_quit){
                // listen server
                try {
                    socket = serverSocket.accept();
                } catch(SocketException e) {
                    if(!cmd_quit) {
                        Thread.sleep(500);
                        continue;
                    }
                }
                ServerThread thread = new ServerThread(socket);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized public static void willExit() throws IOException {
        System.out.println("process exit");
        cmd_quit = true;
        if(null != serverSocket)serverSocket.close();
        MyXMPPConnection.closeConnection();
    }
}
