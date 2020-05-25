package com.alexkill536.chatroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Activity_Registry_profile extends AppCompatActivity {
    EditText nickname, pass, passr;
    TextView date;
    TextInputLayout floatingUsernameLabel;
    TextInputLayout floatingPasswordLabel;
    TextInputLayout floatingPasswordRWLabel;
    Button btn_sbumit;
    DataBaseHandler mydb_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__registry_profile);

        mydb_profile = new DataBaseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ChatRoom Add Profile");
        toolbar.setSubtitle("By Alexkill536ITA");
        Drawable logo = getResources().getDrawable(R.drawable.logo, null);
        Bitmap tmpBitmap = ((BitmapDrawable)logo).getBitmap();
        tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap, 50, 50, false);
        toolbar.setLogo(new BitmapDrawable(getResources(), tmpBitmap));

        nickname = (EditText)findViewById(R.id.editText_nick);
        pass = (EditText)findViewById(R.id.editText_pws);
        passr = (EditText)findViewById(R.id.editText_pssr);
        date = (TextView)findViewById(R.id.textView_data_ora_out);
        date.setText(new SimpleDateFormat("dd/MM/YYYY HH:mm", Locale.getDefault()).format(new Date()));

        floatingUsernameLabel = (TextInputLayout) findViewById(R.id.username_text_input_layout);
        floatingPasswordLabel = (TextInputLayout) findViewById(R.id.pass_text_input_layout);
        floatingPasswordRWLabel = (TextInputLayout) findViewById(R.id.passr_text_input_layout);

        nickname.addTextChangedListener(fill_text);
        pass.addTextChangedListener(fill_text);
        passr.addTextChangedListener(fill_text);


        Button btn_cancell = (Button)findViewById(R.id.button_cancel);
        btn_cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_sbumit = (Button)findViewById(R.id.button_submit_reg);
        btn_sbumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }

    private void submit() {
        String nickname_out = nickname.getText().toString();
        String password_out = passr.getText().toString();
        String data_out = date.getText().toString();
        if (nickname_out.length() > 0 && password_out.length() > 0 && data_out.length() > 0) {
            mydb_profile.inserisciCliente(nickname_out, password_out, data_out);
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("ERROR");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setMessage("Riempire i campi Corretamente");
            builder.show();
            setupFloatingLabelError();
        }
    }

    private void setupFloatingLabelError() {

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

        floatingPasswordRWLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() == 0) {
                    floatingPasswordRWLabel.setError("                 Rinserire Password");
                    floatingPasswordRWLabel.setErrorEnabled(true);

                } else {
                    if (passr.getText().toString().equals(pass.getText().toString())) {
                        floatingPasswordRWLabel.setErrorEnabled(false);
                    } else {
                        floatingPasswordRWLabel.setError("                 Password Non Coincidono");
                        floatingPasswordRWLabel.setErrorEnabled(true);
                    }
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
            String nickInput = nickname.getText().toString().trim();
            String passInput = pass.getText().toString().trim();
            String passRInput = passr.getText().toString().trim();
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
