package com.codepath.apps.mysimpletweets.models;

import com.codepath.apps.mysimpletweets.utils.Helper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Tugce on 11/7/2016.
 */
@Parcel
public class Message {

    public Message(){

    }
    private long messageId;
    private User sender;
    private User receiver;
    private String text;
    private String createdAt;

    public boolean bReceived;

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getMessageId() {
        return messageId;
    }

    public static Message fromJson(JSONObject jsonObject) {
        Message message = new Message();
        try {
            message.text = jsonObject.getString("text");
            message.createdAt = Helper.getRelativeTimeAgo(jsonObject.getString("created_at"));
            message.sender = User.fromJson(jsonObject.getJSONObject("sender"));
            message.receiver = User.fromJson(jsonObject.getJSONObject("recipient"));
            message.messageId=jsonObject.getLong("id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ArrayList<Message> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Message> messages = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++)
                messages.add(Message.fromJson(jsonArray.getJSONObject(i)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }
}
