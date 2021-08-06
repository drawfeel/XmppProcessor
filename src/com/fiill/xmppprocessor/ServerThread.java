package com.fiill.xmppprocessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket = null;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inStream = null;
        InputStreamReader inReader = null;
        BufferedReader buffReader= null;
        OutputStream outStream = null;
        PrintWriter writer=null;
        try {
            inStream = socket.getInputStream();
            inReader = new InputStreamReader(inStream);
            buffReader = new BufferedReader(inReader);

            String msg = null;
            int len = 0;
            //System.out.println("blcok in reading");
            while((msg = buffReader.readLine()) != null){
                ////////debug door////////////////
                if(msg.indexOf("EXIT") == 0) {
                    XmppMain.willExit();
                } else {
                ///////////////////////////////////////
                    len += msg.length();
                    //MessageProcessor.startConversation("test6@sjtu1-1", msg);
                    MessageProcessor.startDefaultGroupConversation(msg);
                }
            }
            socket.shutdownInput();
            outStream = socket.getOutputStream();
            writer = new PrintWriter(outStream);
            writer.write(len + " bytes sent");
            writer.flush();
        } catch (Exception e) {
            // TODO: handle exception
        } finally{
            try {
                if(writer!=null) writer.close();
                if(outStream!=null)outStream.close();
                if(buffReader!=null)buffReader.close();
                if(inReader!=null)inReader.close();
                if(inStream!=null)inStream.close();
                if(socket!=null) {
                    socket.close();
                    socket = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}