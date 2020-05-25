package com.alexkill536.chatroom;

import android.app.Application;

public class GlogalVariable extends Application {
    public String id_user = null;
    public String nick_user = null;
    public String id_server = null;
    public String ip_server = null;
    public String port_server = null;

    public void restVal() {
        id_user = null;
        nick_user = null;
        id_server = null;
        ip_server = null;
        port_server = null;
    }

    public void set_val_id_user(String value) {
        id_user = value;
    }

    public String get_val_id_user() {
        return id_user;
    }

    public void set_val_nick_user(String value) {
        nick_user = value;
    }

    public String get_val_nick_user() {
        return nick_user;
    }

    public void set_val_id_server(String value) {
        id_server = value;
    }

    public String get_val_id_server() {
        return id_server;
    }

    public void set_val_ip_server(String value) {
        ip_server = value;
    }

    public String get_val_ip_server() {
        return ip_server;
    }

    public void set_val_port_server(String value) {
        port_server = value;
    }

    public String get_val_port_server() {
        return port_server;
    }

}
