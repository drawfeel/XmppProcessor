package com.fiill.xmppprocessor;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.muc.MultiUserChat;

public class Conversation {
    private String mDest;
    private Chat mChat;
    private MultiUserChat mMultChat;
    private boolean isMult;

    Conversation (String dest,  Chat chat) {
        mDest = dest;
        mChat = chat;
        isMult = false;
    }

     Conversation (String dest,  MultiUserChat chat) {
         mDest = dest;
         mMultChat = chat;
         isMult = true;
    }

    public String getDest() {
        return mDest;
    }

    public Chat getChat() {
        if (isMult) return null;
        return mChat;
    }

    public MultiUserChat getMultiChat() {
        return mMultChat;
    }
}
