package com.alexkill536.chatroom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class Profilo extends AppCompatActivity {

    EditText nickname, pass, passr, passold;
    TextInputLayout floatingUsernameLabel;
    TextInputLayout floatingPasswordOLDLabel;
    TextInputLayout floatingPasswordNEWLabel;
    TextInputLayout floatingPasswordRNEWLabel;
    DataBaseHandler mydb_profile;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        mydb_profile = new DataBaseHandler(this);
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ChatRoom");
        toolbar.setSubtitle("By Alexkill536ITA");
        Drawable logo = getResources().getDrawable(R.drawable.logo, null);
        Bitmap tmpBitmap = ((BitmapDrawable) logo).getBitmap();
        tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap, 50, 50, false);
        toolbar.setLogo(new BitmapDrawable(getResources(), tmpBitmap));

        nickname = (EditText)findViewById(R.id.intText_nick_edit);
        passold = (EditText)findViewById(R.id.intText_pws_edit);
        pass = (EditText)findViewById(R.id.intText_pwsn_edit);
        passr = (EditText)findViewById(R.id.intText_pwsnr_edit);

        floatingUsernameLabel = (TextInputLayout) findViewById(R.id.intText_nick_layout_edit);
        floatingPasswordOLDLabel = (TextInputLayout) findViewById(R.id.intText_pws_layout_edit);
        floatingPasswordNEWLabel = (TextInputLayout) findViewById(R.id.intText_pwsn_layout_edit);
        floatingPasswordRNEWLabel = (TextInputLayout) findViewById(R.id.intText_pwsnr_layout_edit);

        nickname.addTextChangedListener(fill_text);
        passold.addTextChangedListener(fill_text);
        pass.addTextChangedListener(fill_text);
        passr.addTextChangedListener(fill_text);

        nickname.setText(globalVariable.get_val_nick_user());

        Button btn_delete = (Button)findViewById(R.id.button_delate_profile);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_profile();
            }
        });

        Button btn_cancel = (Button)findViewById(R.id.button_cancel_profilo);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button btn_save = (Button)findViewById(R.id.button_salva_profilo);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar_logut, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            lauch_loout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void lauch_loout() {
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        globalVariable.restVal();
        Intent intent = new Intent(Profilo.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void submit() {
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        String nick = nickname.getText().toString();
        String oldpass = passold.getText().toString();
        String newpass = pass.getText().toString();
        String newpassr = passr.getText().toString();
        if(nick.length() > 0 && oldpass.length() > 0 &&  newpass.length() > 0 && newpassr.length() > 0) {

            Cursor valore = mydb_profile.ottieniCliente(globalVariable.get_val_nick_user(), passold.getText().toString());

            if (valore != null && valore.getCount() > 0) {
                String valore_id = valore.getString(valore.getColumnIndex("id"));
                String valore_nick = valore.getString(valore.getColumnIndex("nickname"));
                String valore_pass = valore.getString(valore.getColumnIndex("password"));

                if (newpassr.equals(newpass) && oldpass.equals(valore_pass) && globalVariable.get_val_id_user().equals(valore_id) && globalVariable.get_val_nick_user().equals(valore_nick)) {
                    mydb_profile.aggiornaCliente(globalVariable.get_val_id_user(), nick, newpass);
                    globalVariable.restVal();
                    globalVariable.set_val_id_user(valore_id);
                    globalVariable.set_val_nick_user(nick);
                    finish();
                    startActivity(getIntent());
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(true);
                    builder.setTitle("ERROR");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setMessage("La vecchia Password non Coincide");
                    builder.show();
                    setupFloatingLabelError();
                }
            }
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

    private void delete_profile() {
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        AlertDialog.Builder allert = new AlertDialog.Builder(this);
        allert.setTitle("AVVISO");
        allert.setMessage("Sei sicuro di volere eliminare il profilo?");
        allert.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mydb_profile.cancellaCliente(globalVariable.get_val_id_user());
                globalVariable.restVal();
                Intent intent = new Intent(Profilo.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        allert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        allert.create().show();
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

        floatingPasswordOLDLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() == 0) {
                    floatingPasswordOLDLabel.setError("                 Inserire la vecchia Password");
                    floatingPasswordOLDLabel.setErrorEnabled(true);
                } else {
                    floatingPasswordOLDLabel.setErrorEnabled(false);
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

        floatingPasswordNEWLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() == 0) {
                    floatingPasswordNEWLabel.setError("                 Rinserire Nuova Password");
                    floatingPasswordNEWLabel.setErrorEnabled(true);

                } else {
                    floatingPasswordNEWLabel.setErrorEnabled(false);
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

        floatingPasswordRNEWLabel.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() == 0) {
                    floatingPasswordRNEWLabel.setError("                 Rinserire Nuova Password");
                    floatingPasswordRNEWLabel.setErrorEnabled(true);

                } else {
                    if (passr.getText().toString().equals(pass.getText().toString())) {
                        floatingPasswordRNEWLabel.setErrorEnabled(false);
                    } else {
                        floatingPasswordRNEWLabel.setError("                 Password Non Coincidono");
                        floatingPasswordRNEWLabel.setErrorEnabled(true);
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
}
