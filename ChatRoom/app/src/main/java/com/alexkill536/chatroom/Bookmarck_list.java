package com.alexkill536.chatroom;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Bookmarck_list extends AppCompatActivity implements  Dialog_input_bookmark.intText_port_dialog_Listener{

    private Toolbar toolbar;
    private DataBaseHandler mydb_server;
    private List<Bookmarck_item.Bookmarck_item_data> bookmarck_itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarck_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ChatRoom Chat Preferite");
        toolbar.setSubtitle("By Alexkill536ITA");
        Drawable logo = getResources().getDrawable(R.drawable.logo, null);
        Bitmap tmpBitmap = ((BitmapDrawable)logo).getBitmap();
        tmpBitmap = Bitmap.createScaledBitmap(tmpBitmap, 50, 50, false);
        toolbar.setLogo(new BitmapDrawable(getResources(), tmpBitmap));

        final RecyclerView listView = (RecyclerView)findViewById(R.id.recyclerview_bookmarck_list);
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        mydb_server = new DataBaseHandler(this);
        Cursor serversCursor = mydb_server.ottieniTuttiServer();
        bookmarck_itemList = new ArrayList<>();
        int indexCol_ip = serversCursor.getColumnIndex("ip");
        int indexCol_port = serversCursor.getColumnIndex("port");
        int bookmarkIndex = 0;
        if (serversCursor.getCount() > 0) {
            for (serversCursor.moveToFirst(); serversCursor.isAfterLast(); serversCursor.moveToNext()) {
                bookmarck_itemList.add(
                    new Bookmarck_item.Bookmarck_item_data(
                        ++bookmarkIndex,
                        serversCursor.getInt(indexCol_port),
                        serversCursor.getString(indexCol_ip)
                    )
                );
            }
        }
        boockmarckAdapter adapter = new boockmarckAdapter(
                this,
                bookmarck_itemList,
                new Bookmarck_item.ClickListener() {
                    @Override
                    public void onPositionClicked(int position) {
                    }

                    @Override
                    public void onLongClicked(int position) {
                        bookmarck_itemList.remove(position);
                        listView.getAdapter().notifyItemRemoved(position);
                    }
                });
        listView.setAdapter(adapter);

        loadbookmarck();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toobar_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            lauch_loout();
        } else if (id == R.id.menu_add_bookmarck) {
            add_bookmark();
        }
        return super.onOptionsItemSelected(item);
    }

    private void lauch_loout() {
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        globalVariable.restVal();
        Intent intent = new Intent(Bookmarck_list.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void add_bookmark() {
        Dialog_input_bookmark dialog_input_bookmark = new Dialog_input_bookmark();
        dialog_input_bookmark.show(getSupportFragmentManager(), "Input");
        /*
        RecyclerView listView = (RecyclerView)findViewById(R.id.recyclerview_bookmarck_list);
        @LayoutRes int layoutId = Bookmarck_item.getItemLayout(listView.getAdapter().getItemCount());
        bookmarck_itemList.add(new Bookmarck_item.Bookmarck_item_data(0, 1234, "0.0.0.1"));
        listView.getAdapter().notifyDataSetChanged();
         */

    }

    private void  loadbookmarck(){
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        RecyclerView listView = (RecyclerView) findViewById(R.id.recyclerview_bookmarck_list);
        Cursor cursor = mydb_server.ottieniServerbyIdUser(Integer.parseInt(globalVariable.get_val_id_user()));
        if (cursor.getCount() > 0) {
            bookmarck_itemList.clear();
            do {
                String ip_temp = cursor.getString(cursor.getColumnIndex("ip"));
                int port_temp = Integer.parseInt(cursor.getString(cursor.getColumnIndex("port")));

                @LayoutRes int layoutId = Bookmarck_item.getItemLayout(listView.getAdapter().getItemCount());
                bookmarck_itemList.add(new Bookmarck_item.Bookmarck_item_data(cursor.getPosition()+1, port_temp, ip_temp));
            } while (cursor.moveToNext());
            listView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void applyText(String ip, int port) {
        final GlogalVariable globalVariable = (GlogalVariable) getApplicationContext();
        mydb_server.inserisciServer(ip, port, Integer.parseInt(globalVariable.get_val_id_user()));
        loadbookmarck();
    }
}
