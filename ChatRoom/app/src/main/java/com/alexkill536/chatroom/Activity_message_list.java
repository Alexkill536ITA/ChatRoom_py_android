package com.alexkill536.chatroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Activity_message_list extends AppCompatActivity {

    private  Client_TCP clientTcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ChatRoom");
        toolbar.setSubtitle("By Alexkill536ITA");
        Drawable logo = getResources().getDrawable(R.drawable.logo, null);
        Bitmap tmpBitmap = ((BitmapDrawable)logo).getBitmap();
        tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap, 50, 50, false);
        toolbar.setLogo(new BitmapDrawable(getResources(), tmpBitmap));

        final LinearLayout Loadr_buf = (LinearLayout)findViewById(R.id.page_buffer);
        final Animation slideleft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);

        final EditText int_message = (EditText)findViewById(R.id.edittext_chatbox);
        final List<MessageListAdapter.BaseMessage> messageList = new ArrayList<>();
        final MessageListAdapter adapter = new MessageListAdapter(this, messageList);

        final RecyclerView view_message = (RecyclerView)findViewById(R.id.recyclerview_message_list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        view_message.setLayoutManager(layoutManager);
        view_message.setAdapter(adapter);

        final String nickname = this.getIntent().getStringExtra("Nickname");
        final String ipServer = this.getIntent().getStringExtra("ServerIP");
        final int portServer = this.getIntent().getIntExtra("PortServer", -1);

        clientTcp = new Client_TCP();
        clientTcp.setReceiveMessageEventObservers(new Client_TCP.ReceiveMessageEvent() {
            @Override
            public void receiveMessage(MessageListAdapter.UserMessage message) {
                adapter.addItem(message);
                view_message.smoothScrollToPosition(messageList.size() - 1);
                adapter.notifyDataSetChanged();
            }
        });

        final AsyncTask<Object, String, Boolean> runner = new AsyncTask<Object, String, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... datas) {
                try {
                    clientTcp.Connect((String)datas[0], (String)datas[1], (int)datas[2]);
                    return clientTcp.getstatusconnect();
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | IOException | BadPaddingException | InvalidKeyException | InvalidKeySpecException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (!result) {
                    Activity_message_list.super.onBackPressed();
                }
                Loadr_buf.startAnimation(slideleft);
                Loadr_buf.setVisibility(View.INVISIBLE);
                super.onPostExecute(result);
            }
        };

        Loadr_buf.setVisibility(View.VISIBLE);
        runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] {nickname, ipServer, portServer});

        Button btn_send = (Button)findViewById(R.id.button_chatbox_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (int_message.getText().length() > 0) {

                    AsyncTask<String, String, Integer> sendTask = new AsyncTask<String, String, Integer>() {
                        @Override
                        protected Integer doInBackground(String... messages) {
                            return clientTcp.SendMessage(messages[0]);
                        }

                        @Override
                        protected void onPostExecute(Integer result) {
                            messageList.add(new MessageListAdapter.UserMessage(String.format(int_message.getText().toString())));
                            view_message.smoothScrollToPosition(messageList.size()-1);
                            int_message.setText("");
                            adapter.notifyDataSetChanged();
                            super.onPostExecute(result);
                        }
                    };
                    sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, int_message.getText().toString());
                }
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                // some code depending on keyboard visiblity status
                if (isOpen) {
                    if (messageList.size() != 0) {
                        view_message.smoothScrollToPosition(messageList.size() - 1);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            try {
                clientTcp.Disonnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Activity_message_list.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
