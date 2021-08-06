package com.fiill.xmppprocessor;

import java.io.IOException;
import java.util.Vector;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection.FromMode;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

/**
 * This class create connection for send/receive messages. The connection is a since instance
 * 
 */

public class MyXMPPConnection {
    public static final int SOCKET_PORT = 7779;
    public static final String SERVER_DOMAIN = "sjtu1-1";
    public static final String USER_ID = "test5";
    public static final String PASSWORD = "test5";
    public static final String HOST_ADDRESS = "172.16.5.211";
    public static final SecurityMode SECURITY_MODE = SecurityMode.disabled;
    
    public static final String GROUP_ID = "group1";
    public static final String NICK_NAME = "nickName5";

    static Vector<Conversation> converstions = new Vector<Conversation>();
    private static AbstractXMPPConnection instance;

    public static void onLine()  throws SmackException, IOException, XMPPException{
         makeConnection();
    }
    
    public static AbstractXMPPConnection  getConnection() throws SmackException, IOException, XMPPException{
        if(null == instance || !instance.isConnected()) {
            makeConnection();
        }
        return instance;
    }

    private static void  makeConnection() throws SmackException, IOException, XMPPException {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        XMPPTCPConnectionConfiguration config = builder
                .setSecurityMode(SECURITY_MODE)
                .setUsernameAndPassword(USER_ID, PASSWORD)
                .setServiceName(SERVER_DOMAIN)
                .setHost(HOST_ADDRESS)
                .setPort(SECURITY_MODE == SecurityMode.disabled ? 5222 : 5223)
                .build();
        instance = new XMPPTCPConnection(config);
        instance.setFromMode(FromMode.USER);
        instance.connect();
        instance.login();
    }

    public static Chat getChat(String to,
            ChatMessageListener msgListener) throws SmackException, IOException, XMPPException {
        Chat c = null;
        for(int i = 0; i < converstions.size(); i ++) {
            Conversation conver = converstions.elementAt(i);
            if(to.equals(conver.getDest())) {
                c = conver.getChat();
                break;
            }
        }
        if(null == c) {
            ChatManager chatmanager = ChatManager.getInstanceFor(getConnection());
            c = chatmanager.createChat(to);
            c.addMessageListener(msgListener);
            converstions.add(new Conversation(to,c));
        }
        return c;
    }

    public static MultiUserChat getGroupChat(String to, String nickName,
                MessageListener msgListener)
                throws SmackException, IOException, XMPPException {
        MultiUserChat c = null;
        for(int i = 0; i < converstions.size(); i ++) {
            Conversation conver = converstions.elementAt(i);
            if(conver.getDest().indexOf(GROUP_ID) == 0) {
                c = conver.getMultiChat();
                break;
            }
        }
        if(null == c) {
            MultiUserChatManager multiUserChatManager = MultiUserChatManager.getInstanceFor(getConnection());
            String jid = to + "@conference." + getConnection().getServiceName();
            c = multiUserChatManager.getMultiUserChat(jid);
            c.join(nickName);
            //c.addMessageListener(msgListener);
            converstions.add(new Conversation(to,c));
        }
        return c;
    }

    public static void closeConnection(){
        if (instance != null) {
            try {
                instance.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
