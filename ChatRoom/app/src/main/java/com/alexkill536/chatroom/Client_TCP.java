package com.alexkill536.chatroom;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Client_TCP {
    //public static String NameServer = "192.168.1.102";
    //public static int ServerPort = 1234;
    public static Socket MySocket;
    //public static String StrUser = "Anonymus\n";
    public static String StrSever;
    public static OutputStreamWriter out;
    public static PrintWriter outServer;
    public static BufferedReader IntServer;
    public static byte[] ks;
    public static String publicKey = null;
    public static Key pvt = null;
    public static Key pubServer = null;
    public static int HEADER_LENGTH = 10;
    public String publicKeyStr = null;

    public Client_TCP() {
        receiveMessageEventObservers = new ArrayList<>();
    }

    ArrayList<ReceiveMessageEvent> receiveMessageEventObservers;
    public interface ReceiveMessageEvent {
        void receiveMessage(MessageListAdapter.UserMessage message);
    }

    public void setReceiveMessageEventObservers(ReceiveMessageEvent observer) {
        if (receiveMessageEventObservers != null)
            receiveMessageEventObservers.add(observer);
    }

    private void fireReceiveMessageEvent(MessageListAdapter.UserMessage message) {
        if (receiveMessageEventObservers != null) {
            for (ReceiveMessageEvent observer : receiveMessageEventObservers) {
                observer.receiveMessage(message);
            }
        }
    }

    /*------------------------------------------------------------------------------------------------*/

    // Gen Key
    public static void Key_rsa_gen() throws NoSuchAlgorithmException {
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        final KeyPair kp = kpg.generateKeyPair();
        pvt = kp.getPrivate();
        publicKey = new String("-----BEGIN RSA PUBLIC KEY-----\n"+ Base64.getMimeEncoder().encodeToString(kp.getPublic().getEncoded())+"\n-----END RSA PUBLIC KEY-----\n");
        System.err.println("Public key format: " + kp.getPublic().getFormat());
        System.out.println(publicKey);
    }

    /*------------------------------------------------------------------------------------------------*/

    // encrypt
    public static String Cripter(String message) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubServer);
        return Base64.getMimeEncoder().encodeToString(cipher.doFinal(message.getBytes()));
    }

    // decrypt
    public String Decripter(String message) throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pvt);
        return new String(cipher.doFinal(Base64.getMimeDecoder().decode(message)));
    }

    /*------------------------------------------------------------------------------------------------*/

    // Send Message
    public int SendMessage(String intstr)  {
        //final String message = Cripter(int_str);
        final byte[] message_b = intstr.getBytes(StandardCharsets.UTF_8);
        final int message_lengh_int = message_b.length;
        String header = "";
        for (int i = String.valueOf(message_lengh_int).length(); i < 10; i++) {
            header += '0';
        }
        header += String.valueOf(message_lengh_int);
        outServer.write(header);
        outServer.write(intstr);
        outServer.flush();
        return 0;
    }

    private boolean isDataToReceive() throws IOException {
        return IntServer.ready();
    }

    // Recive Message
    public String[] RecivMessage() throws IOException {
        int messageLength = -1;
        int charsRead = -1;
        byte[] strprint;
        String vt;
        final char[] cbuf = new char[1024];
        charsRead = IntServer.read(cbuf, 0, HEADER_LENGTH);
        StrSever = new String(cbuf);
        ks = trim(StrSever.getBytes(StandardCharsets.UTF_8));
        vt = new String(ks, StandardCharsets.UTF_8);
        //System.out.println(vt);
        messageLength = Integer.parseInt(StrSever.trim());
        charsRead = IntServer.read(cbuf, 0, messageLength);
        String user_temp = new String(cbuf).trim();
        String user = user_temp.substring(0, messageLength);

        charsRead = IntServer.read(cbuf, 0, HEADER_LENGTH);
        StrSever = new String(cbuf);
        ks = trim(StrSever.getBytes(StandardCharsets.UTF_8));
        vt = new String(ks, StandardCharsets.UTF_8);
        //System.out.println(vt);
        messageLength = Integer.parseInt(StrSever.trim());
        charsRead = IntServer.read(cbuf, 0, messageLength);
        String message = new String(cbuf).trim();

        return new String[] { user, message};
    }

    /*------------------------------------------------------------------------------------------------*/

    static byte[] trim(byte[] bytes)
    {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0)
        {
            --i;
        }

        return Arrays.copyOf(bytes, i + 1);
    }

    /*------------------------------------------------------------------------------------------------*/

    public void Handshake(String StrUser) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        final byte[] message_b = StrUser.getBytes(StandardCharsets.UTF_8);
        final int message_lengh_int = message_b.length;
        String header = "";
        for (int i = String.valueOf(message_lengh_int).length(); i < 10; i++) {
            header += '0';
        }
        header += String.valueOf(message_lengh_int);
        outServer.write(header);
        outServer.write(StrUser);
        outServer.flush();

        int messageLength = -1;
        int charsRead = -1;
        byte[] strprint;
        String vt;
        final char[] cbuf = new char[1024];
        //ptk1
        charsRead = IntServer.read(cbuf, 0, HEADER_LENGTH);
        StrSever = new String(cbuf);
        strprint = trim(StrSever.getBytes(StandardCharsets.UTF_8));
        vt = new String(strprint, StandardCharsets.UTF_8);
        System.out.println(vt);
        messageLength = Integer.parseInt(StrSever.substring(0, HEADER_LENGTH));

        charsRead = IntServer.read(cbuf, 0, messageLength);
        StrSever = new String(cbuf);
        strprint = trim(StrSever.getBytes(StandardCharsets.UTF_8));
        vt = new String(strprint, StandardCharsets.UTF_8);
        System.out.println(vt);

        //ptk2
        charsRead = IntServer.read(cbuf, 0, HEADER_LENGTH);
        StrSever = new String(cbuf);
        ks = trim(StrSever.getBytes(StandardCharsets.UTF_8));
        vt = new String(ks, StandardCharsets.UTF_8);
        System.out.println(vt);
        messageLength = Integer.parseInt(StrSever.substring(0, StrSever.indexOf(" ")));
        charsRead = IntServer.read(cbuf, 0, messageLength);
        StrSever = new String(cbuf);

        //key import
        String keyString = StrSever.replace("-----BEGIN PUBLIC KEY-----\n", "").replace("\n-----END PUBLIC KEY-----\n", "").replace("-----BEGIN RSA PUBLIC KEY-----\n", "").replace("\n-----END RSA PUBLIC KEY-----\n", "");
        byte[] keyBytes = Base64.getMimeDecoder().decode(keyString);
        X509EncodedKeySpec ks = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        pubServer = kf.generatePublic(ks);

        final byte[] message_key = publicKey.getBytes(StandardCharsets.UTF_8);
        final int message_lengh_int_key = message_key.length;
        String header_2 = "";
        for (int i = String.valueOf(message_lengh_int_key).length(); i < 10; i++) {
            header_2 += '0';
        }
        header_2 += String.valueOf(message_lengh_int_key);
        outServer.write(header_2);
        outServer.write(publicKey);
        outServer.flush();

        AsyncTask<String, String, String> reciveTasck = new AsyncTask<String, String, String>() {
            private boolean runThread = false;
            @Override
            protected String doInBackground(String... inputs) {
                try {
                    runThread = true;
                    while (runThread) {
                        if (isDataToReceive()) {
                            this.publishProgress(RecivMessage());
                        }
                        Thread.sleep(500);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                return "";
            }

            @Override
            protected void onProgressUpdate(String... values) {
                fireReceiveMessageEvent(new MessageListAdapter.UserMessage(values[0], values[1], 99));
            }

            @Override
            protected void onCancelled() {
                runThread = false;
                super.onCancelled();
            }
        };
        reciveTasck.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

    }

    /*------------------------------------------------------------------------------------------------*/

    public void communicate() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, IOException {
        String int_str = "Hello JAVA";
        SendMessage(int_str);
    }

    /*------------------------------------------------------------------------------------------------*/

    public Socket Connect(String StrUser, String NameServer, int ServerPort) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        try {
            Key_rsa_gen();
            InetAddress serverAddr = InetAddress.getByName(NameServer);
            MySocket = new Socket();
            MySocket.connect(new InetSocketAddress(serverAddr, ServerPort), 15000);
            //MySocket = new Socket(serverAddr, ServerPort);

            out = new OutputStreamWriter(MySocket.getOutputStream());
            outServer = new PrintWriter(out, false);

            IntServer = new BufferedReader(new InputStreamReader(MySocket.getInputStream()));
            Handshake(StrUser);
        } catch (final UnknownHostException e) {
            System.err.println("Host sconosciuto");
            throw e;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            System.out.println("Errore durante la connessione!");
//            System.exit(1);
            throw e;
        }
        return MySocket;
    }

    public boolean getstatusconnect() {
          boolean status = MySocket.isConnected();
         return status;
    }

    public void Disonnect() throws IOException {
        MySocket.close();
    }
}
