package com.fiill.xmppprocessor;

import java.io.IOException;
import java.util.Vector;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class MessageProcessor {
    static Vector<Conversation> converstions = new Vector<Conversation>();

    static MessageListener mMessageListener = new MessageListener(){
        @Override
        public void processMessage(Message message) {
            if(Message.Type.groupchat  == message.getType() && message.getBody() != null) {
                System.out.println("RECEIVE GROUP: " + message.getFrom() + ":" + message.getBody());
            }
        }
    };

    static ChatMessageListener mChatMessageListener = new ChatMessageListener() {
        public void processMessage(Chat arg0, Message arg1) {
            System.out.println("RECEIVE CHAT: " + arg1.getFrom() + ":" + arg1.getBody()); 
        }
    };

    public static void startConversation(String to, String msg) {
        try {
            Chat c = MyXMPPConnection.getChat(to, mChatMessageListener);
            c.sendMessage(msg);
        } catch (SmackException | IOException | XMPPException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
       }
    }
    
    public static void startGroupConversation(String group, String nickName, String body) throws Exception {
        MultiUserChat multiUserChat =MyXMPPConnection.getGroupChat(
                group, nickName,
                mMessageListener);
        multiUserChat.sendMessage(body);
    }

    public static void startDefaultGroupConversation(String body) throws Exception {
        MultiUserChat multiUserChat =MyXMPPConnection.getGroupChat(
                MyXMPPConnection.GROUP_ID, MyXMPPConnection.NICK_NAME,
                mMessageListener);
        multiUserChat.sendMessage(body);
    }
}
