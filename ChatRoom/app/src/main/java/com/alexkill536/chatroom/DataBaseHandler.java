package com.alexkill536.chatroom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Database.db";

    private static final String TABLE_NAME = "profile_user";
    private static final String TABLE_NAME_ser = "server_list";

    private static final String COLUM_ID = "id";
    private static final String COLUM_NICK = "nickname";
    private static final String COLUM_PASSWORD = "password";
    private static final String COLUM_DATE = "date";

    private static final String COLUM_ID_ser = "id";
    private static final String COLUM_IP_ser = "ip";
    private static final String COLUM_PORT_ser = "port";
    private static final String COLUM_ID_user_ser = "id_user";

    SQLiteDatabase db;

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COLUM_ID+" INTEGER PRIMARY KEY, "+COLUM_NICK+" TEXT, "+COLUM_PASSWORD+" TEXT, "+COLUM_DATE+" TEXT)");
        db.execSQL("CREATE TABLE "+TABLE_NAME_ser+" ("+COLUM_ID_ser+" INTEGER PRIMARY KEY, "+COLUM_IP_ser+" TEXT, "+COLUM_PORT_ser+" INTEGER, "+COLUM_ID_user_ser+" INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_ser);
        onCreate(db);
    }

    public long inserisciCliente(String nome, String pass, String date)
    {
        // creo una mappa di valori
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUM_NICK, nome);
        initialValues.put(COLUM_PASSWORD, pass);
        initialValues.put(COLUM_DATE, date);
        // applico il metodo insert
        return db.insert(TABLE_NAME, null, initialValues);
    }

    public boolean aggiornaCliente(String rigaId, String nickname, String password) {
        ContentValues args = new ContentValues();
        args.put(COLUM_NICK, nickname);
        args.put(COLUM_PASSWORD, password);
        return db.update(TABLE_NAME, args, COLUM_ID + "=" + rigaId, null) > 0;
    }

    public boolean cancellaCliente(String rigaId)
    {
        // applico il metodo delete
        return db.delete(TABLE_NAME, COLUM_ID + "=" + rigaId, null) > 0;
    }

    public Cursor ottieniTuttiClienti()
    {
        // applico il metodo query senza applicare nessuna clausola WHERE
        return db.query(TABLE_NAME, new String[] {COLUM_ID, COLUM_NICK, COLUM_PASSWORD, COLUM_DATE},
                null, null, null, null, null);
    }

    public Cursor ottieniCliente(String riganick, String rigapass) throws SQLException
    {
        // applico il metodo query filtrando per Nick e Password
        Cursor mCursore = db.query(true, TABLE_NAME, new String[] {COLUM_ID, COLUM_NICK, COLUM_PASSWORD, COLUM_DATE},
                COLUM_NICK + "='" + riganick +"' AND "+ COLUM_PASSWORD + "='" + rigapass+"'", null, null, null, null, null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return mCursore;
    }

    public long inserisciServer(String ip, int port, int id_user)
    {
        // creo una mappa di valori
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUM_IP_ser, ip);
        initialValues.put(COLUM_PORT_ser, port);
        initialValues.put(COLUM_ID_user_ser, id_user);
        // applico il metodo insert
        return db.insert(TABLE_NAME_ser, null, initialValues);
    }

    public boolean cancellaServer(long rigaId)
    {
        // applico il metodo delete
        return db.delete(TABLE_NAME_ser, COLUM_ID_ser + "=" + rigaId, null) > 0;
    }

    public boolean cancellaServer(int id_user, String ip_server, int port_server)
    {
        // applico il metodo delete
        return db.delete(TABLE_NAME_ser, COLUM_ID_user_ser + " = " + id_user +" AND "+ COLUM_IP_ser + " = '" + ip_server + "' AND " + COLUM_PORT_ser + " = " + port_server, null) > 0;

    }

    public Cursor ottieniTuttiServer()
    {
        // applico il metodo query senza applicare nessuna clausola WHERE
        return db.query(TABLE_NAME_ser, new String[] {COLUM_ID_ser, COLUM_IP_ser, COLUM_PORT_ser, COLUM_ID_user_ser},
                null, null, null, null, null);
    }

    public Cursor ottieniServer(long rigaId) throws SQLException
    {
        // applico il metodo query filtrando per ID
        Cursor mCursore = db.query(true, TABLE_NAME_ser, new String[] {COLUM_ID_ser, COLUM_IP_ser, COLUM_PORT_ser, COLUM_ID_user_ser},
                COLUM_ID_ser + "=" + rigaId, null, null, null, null, null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return mCursore;
    }

    public Cursor ottieniServerbyIdUser(int rigaId) throws SQLException
    {
        // applico il metodo query filtrando per ID
        Cursor mCursore = db.query(true, TABLE_NAME_ser, new String[] {COLUM_ID_ser, COLUM_IP_ser, COLUM_PORT_ser, COLUM_ID_user_ser},
                COLUM_ID_user_ser + "=" + rigaId, null, null, null, null, null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return mCursore;
    }

}
