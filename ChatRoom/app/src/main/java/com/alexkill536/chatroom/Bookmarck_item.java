package com.alexkill536.chatroom;

import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

public class Bookmarck_item extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {
    private TextView textViewNumBook, textViewIpBook, textViewPortBook, btn_connect;
    private WeakReference<ClickListener> onPositionClickListenerWeakRef;

    public Bookmarck_item(View view, ClickListener onPositionClickListenerWeakRef) {
        super(view);
        this.onPositionClickListenerWeakRef = new WeakReference<>(onPositionClickListenerWeakRef);

        textViewNumBook = (TextView)view.findViewById(R.id.textView_name_book);
        textViewIpBook = (TextView)view.findViewById(R.id.textView_ip_adpter);
        textViewPortBook = (TextView)view.findViewById(R.id.textView_port_adpter);
        btn_connect = (TextView) view.findViewById(R.id.button_join_adapeter);

    }

    void bind(Bookmarck_item_data data) {
        textViewNumBook.setText(String.format("Bookmark %d", data.getNum_book()));
        textViewIpBook.setText(data.getIp_book());
        textViewPortBook.setText(String.valueOf(data.getPort_book()));
    }

    public static @LayoutRes int getItemLayout(int position) {
        return position % 2 == 0 ? R.layout.item_bookmarck_server : R.layout.item_bookmarck_server_light;
    }

    @Override
    public void onClick(View v) {
        onPositionClickListenerWeakRef.get().onPositionClicked(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        onPositionClickListenerWeakRef.get().onLongClicked(getAdapterPosition());
        return true;
    }

    public static class Bookmarck_item_data {
        private int num_book, port_book;
        private String ip_book;

        public Bookmarck_item_data(int num_book, int port_book, String ip_book) {
            this.num_book = num_book;
            this.port_book = port_book;
            this.ip_book = ip_book;
        }

        public int getPort_book() {
            return port_book;
        }
        public int getNum_book() {
            return num_book;
        }
        public String getIp_book() {
            return ip_book;
        }
    }

    public interface ClickListener {
        void onPositionClicked(int position);
        void onLongClicked(int position);
    }
}
