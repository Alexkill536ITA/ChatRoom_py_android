package com.alexkill536.chatroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<BaseMessage> mMessageList;
    private User thisUser;

    public MessageListAdapter(Context context, List<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
        thisUser = new User("ME", 0);
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        UserMessage message = (UserMessage) mMessageList.get(position);
        if (message.getSender().getUserId() == thisUser.getUserId()) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserMessage message = (UserMessage) mMessageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    public void addItem(BaseMessage message) {
        if (mMessageList.add(message))
            notifyItemInserted(mMessageList.indexOf(message));
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }
        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getCreatedAt());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        }

        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getCreatedAt());
            nameText.setText(message.getSender().getNickname());
            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl());
        }
    }

    public static class BaseMessage {
        protected String message;
        protected String createdAt;
        protected User sender;

        public String getMessage() { return message; }
        public String getCreatedAt() { return createdAt; }
        public User getSender() { return sender; }
    }
    public static class UserMessage extends BaseMessage {
        public UserMessage() { super(); }
        public UserMessage(String message) {
            this.message = message;
            this.createdAt = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            this.sender = new User("altro", 0);
        }
        public UserMessage(String message, int UserID) {
            this.message = message;
            this.createdAt = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            this.sender = new User("altro", UserID);
        }
        public UserMessage(String nickname, String message, int UserID) {
            this.message = message;
            this.createdAt = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            this.sender = new User(nickname, UserID);
        }
    }

    private static class User {
        private String nickname;
        private int userId;

        public User(String user, int id) {
            nickname = user;
            userId = id;
        }

        public String getNickname() { return nickname; }
        public String getProfileUrl() { return "http://stocazzo.it/"; }
        public int getUserId() { return userId; }
    }

    public static class Utils {
        public static String formatDateTime(Date input)
        {
            return input.toString();
        }
    }
}
