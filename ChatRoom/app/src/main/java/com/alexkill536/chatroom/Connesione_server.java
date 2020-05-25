package com.alexkill536.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

public class Connesione_server extends AppCompatActivity {

    String nickname = "Anonymus";
    String ipsever = null;
    int portsever = 0;
    EditText edt_nickname;
    EditText edt_severip;
    EditText edt_serverport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connesione_server);

        final GlogalVariable glogalVariable = (GlogalVariable) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ChatRoom");
        toolbar.setSubtitle("By Alexkill536ITA");
        Drawable logo = getResources().getDrawable(R.drawable.logo, null);
        Bitmap tmpBitmap = ((BitmapDrawable) logo).getBitmap();
        tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap, 100, 100, false);
        toolbar.setLogo(new BitmapDrawable(getResources(), tmpBitmap));

        edt_nickname = (EditText) findViewById(R.id.editText_name);
        edt_severip = (EditText) findViewById(R.id.editText_serverip);
        edt_serverport = (EditText) findViewById(R.id.editText_severport);

        if (glogalVariable.get_val_nick_user() != null) {
            edt_nickname.setText(glogalVariable.get_val_nick_user());
        }
        if (glogalVariable.get_val_ip_server() != null && glogalVariable.get_val_port_server() != null) {
            edt_severip.setText(glogalVariable.get_val_ip_server());
            edt_serverport.setText(glogalVariable.get_val_port_server());
        }

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join();
            }
        });
    }

    private void join() {
        final GlogalVariable glogalVariable = (GlogalVariable) getApplicationContext();
        UIUtil.hideKeyboard(this);
        if (edt_nickname.getText().length() > 0) {
            if (glogalVariable.get_val_nick_user() == null) {
                nickname = edt_nickname.getText().toString();
            } else {
                if(nickname.equals(glogalVariable.get_val_nick_user())) {
                    nickname = glogalVariable.get_val_nick_user();
                } else {
                    nickname = edt_nickname.getText().toString();
                }
            }
        } else {
            nickname = "Anonymus";
        }
        ipsever = edt_severip.getText().toString();
        portsever = Integer.parseInt(String.valueOf(edt_serverport.getText()));
        Intent intent = new Intent(this, Activity_message_list.class);
        intent.putExtra("Nickname", nickname);
        intent.putExtra("ServerIP", ipsever);
        intent.putExtra("PortServer", portsever);
        startActivity(intent);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
}

