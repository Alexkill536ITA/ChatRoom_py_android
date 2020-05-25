package com.alexkill536.chatroom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class boockmarckAdapter extends RecyclerView.Adapter {

    private Context bContext;
    private List<Bookmarck_item.Bookmarck_item_data> BookmarckList;
    private Bookmarck_item.ClickListener onPositionClickListener;
    private GlogalVariable globalVariable;
    private Cursor dbCursor;
    private boolean confimed;

    public boockmarckAdapter(
            Context context,
            List<Bookmarck_item.Bookmarck_item_data> messageList,
            Bookmarck_item.ClickListener onPositionClicklistener) {
        bContext = context;
        BookmarckList = messageList;
        this.onPositionClickListener = onPositionClicklistener;
        this.globalVariable = (GlogalVariable)context.getApplicationContext();
        this.dbCursor = null;
    }

    @Override
    public int getItemCount() {
        return BookmarckList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view;
        boolean set_color = true;
        if (set_color) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(Bookmarck_item.getItemLayout(viewType), parent, false);
            final Bookmarck_item holder = new Bookmarck_item(view, onPositionClickListener);
            final int pos = viewType;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textViewIpBook = (TextView)v.findViewById(R.id.textView_ip_adpter);
                    TextView textViewPortBook = (TextView)v.findViewById(R.id.textView_port_adpter);

                    Intent intent = new Intent(bContext, Activity_message_list.class);
                    intent.putExtra("Nickname", globalVariable.get_val_nick_user());
                    intent.putExtra("ServerIP", textViewIpBook.getText().toString());
                    intent.putExtra("PortServer", Integer.parseInt(textViewPortBook.getText().toString()));
                    bContext.startActivity(intent);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DataBaseHandler dbHandler = new DataBaseHandler(v.getContext());
                    TextView textViewIpBook = (TextView)v.findViewById(R.id.textView_ip_adpter);
                    TextView textViewPortBook = (TextView)v.findViewById(R.id.textView_port_adpter);
                    Opendialog(view, holder);
                    //    if (dbHandler.cancellaServer(Integer.parseInt(globalVariable.get_val_id_user()), textViewIpBook.getText().toString(), Integer.parseInt(textViewPortBook.getText().toString()))) {
                    //          holder.onLongClick(v);
                    //    }
                    return true;
                }
            });
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Bookmarck_item.Bookmarck_item_data item = BookmarckList.get(position);
        ((Bookmarck_item) holder).bind(item);
    }

    private void Opendialog(final View v, final Bookmarck_item holder) {
        AlertDialog.Builder allert = new AlertDialog.Builder(bContext);
        allert.setTitle("AVVISO");
        allert.setMessage("Sei sicuro di volere eliminare il profilo?");
        allert.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataBaseHandler dbHandler = new DataBaseHandler(v.getContext());
                TextView textViewIpBook = (TextView)v.findViewById(R.id.textView_ip_adpter);
                TextView textViewPortBook = (TextView)v.findViewById(R.id.textView_port_adpter);
                if (dbHandler.cancellaServer(Integer.parseInt(globalVariable.get_val_id_user()), textViewIpBook.getText().toString(), Integer.parseInt(textViewPortBook.getText().toString()))) {
                    holder.onLongClick(v);
                }
            }
        });
        allert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confimed = false;
            }
        });
        allert.create().show();
    }
}
