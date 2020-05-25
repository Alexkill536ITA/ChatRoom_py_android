package com.alexkill536.chatroom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        toolbar.setTitle("ChatRoom");
        toolbar.setSubtitle("By Alexkill536ITA");
        Drawable logo = getResources().getDrawable(R.drawable.logo, null);
        Bitmap tmpBitmap = ((BitmapDrawable)logo).getBitmap();
        tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap, 100, 100, false);
        toolbar.setLogo(new BitmapDrawable(getResources(), tmpBitmap));


        Button btn_connect = (Button)findViewById(R.id.button_Atc_connect_server);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lauch_connet();
            }
        });

        Button btn_profilo = (Button)findViewById(R.id.button_Atc_profilo);
        btn_profilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalVariable.get_val_id_user() != null) {
                    lauch_profilo();
                } else {
                    lauch_login();
                }
            }
        });

        Button btn_bookmarck = (Button)findViewById(R.id.button_server_bookmarck);
        btn_bookmarck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalVariable.get_val_id_user() != null) {
                    lauch_bookmarck();
                } else {
                    lauch_login();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        MenuInflater inflater = getMenuInflater();
        if (globalVariable.get_val_id_user() == null) {
            inflater.inflate(R.menu.menu_toolbar, menu);
        } else {
            inflater.inflate(R.menu.menu_toolbar_logut, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_call) {
            lauch_login();
        } else if (id == R.id.menu_logout) {
            lauch_loout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void lauch_connet() {
        Intent intent = new Intent(this, Connesione_server.class);
        startActivity(intent);
    }

    private void lauch_profilo() {
        Intent intent = new Intent(this, Profilo.class);
        startActivity(intent);
    }

    private void lauch_login() {
        Intent intent = new Intent(this, Activity_login.class);
        startActivity(intent);
    }

    private void lauch_loout() {
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        globalVariable.restVal();
        finish();
        startActivity(getIntent());
    }

    private void lauch_bookmarck() {
        Intent intent = new Intent(this, Bookmarck_list.class);
        startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit App", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

