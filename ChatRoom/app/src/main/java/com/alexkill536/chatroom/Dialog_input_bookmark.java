package com.alexkill536.chatroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog_input_bookmark extends AppCompatDialogFragment {
    private EditText editText_ip, editText_port;
    private intText_port_dialog_Listener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_bookmarck, null);
        builder.setView(view).setTitle("Bookmark ADD").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ip = editText_ip.getText().toString();
                int port = Integer.parseInt(editText_port.getText().toString());
                listener.applyText(ip, port);
            }
        });
        editText_ip = view.findViewById(R.id.intText_ip_dialog);
        editText_port = view.findViewById(R.id.intText_port_dialog);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (intText_port_dialog_Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "implement intText_port_dialog_Listener");
        }
    }

    public interface intText_port_dialog_Listener {
        void applyText(String ip, int port);
    }
}
