package com.alexkill536.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

public class Activity_login extends AppCompatActivity {
    EditText nick, pass;
    DataBaseHandler mydb_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mydb_profile = new DataBaseHandler(this);
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ChatRoom Login");
        toolbar.setSubtitle("By Alexkill536ITA");
        Drawable logo = getResources().getDrawable(R.drawable.logo, null);
        Bitmap tmpBitmap = ((BitmapDrawable)logo).getBitmap();
        tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap, 50, 50, false);
        toolbar.setLogo(new BitmapDrawable(getResources(), tmpBitmap));

        nick = (EditText)findViewById(R.id.intText_nick);
        pass = (EditText)findViewById(R.id.intText_pws);

        nick.addTextChangedListener(fill_text);
        pass.addTextChangedListener(fill_text);

        Button btn_cancell = (Button)findViewById(R.id.button_cancel);
        btn_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btn_regestry = (Button)findViewById(R.id.button_regestry);
        btn_regestry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch_regestry();
            }
        });

        Button btn_submit = (Button)findViewById(R.id.button_Login);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if  (nick.getText().toString().length() > 0 && pass.getText().toString().length() > 0) {
                    Cursor valore = mydb_profile.ottieniCliente(nick.getText().toString(), pass.getText().toString());
                    if (valore != null && valore.getCount() > 0) {
                        String valore_id = valore.getString(valore.getColumnIndex("id"));
                        String valore_nick = valore.getString(valore.getColumnIndex("nickname"));
                        globalVariable.set_val_id_user(valore_id);
                        globalVariable.set_val_nick_user(valore_nick);
                        Intent intent = new Intent(Activity_login.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Activity_login.this, "Login Fallito", Toast.LENGTH_SHORT).show();
                        pass.setText("");
                    }
                }
            }
        });

        setupFloatingLabelError();
    }

    private void launch_regestry() {
        Intent intent = new Intent(this, Activity_Registry_profile.class);
        startActivity(intent);
    }

    private void setupFloatingLabelError() {
        final TextInputLayout floatingUsernameLabel = (TextInputLayout) findViewById(R.id.intText_nick_layout);
        floatingUsernameLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() == 0) {
                    floatingUsernameLabel.setError("                 Inserire Nick");
                    floatingUsernameLabel.setErrorEnabled(true);
                } else {
                    floatingUsernameLabel.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final TextInputLayout floatingPasswordLabel = (TextInputLayout) findViewById(R.id.intText_pws_layout);
        floatingPasswordLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() == 0) {
                    floatingPasswordLabel.setError("                 Inserire Password");
                    floatingPasswordLabel.setErrorEnabled(true);
                } else {
                    floatingPasswordLabel.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private TextWatcher fill_text = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setupFloatingLabelError();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }


    private void setSupportActionBar(Toolbar toolbar) {
    }
}
