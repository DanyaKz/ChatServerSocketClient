package MyApp;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Client {

    static boolean status = true;
    static BufferedWriter out;
    static BufferedReader inputUser;
    static BufferedReader in;


    public static void main(String[] args) throws IOException {
        new ThreadActions();
    }

}


class ThreadActions {

    boolean status = true;
    BufferedReader in;
    BufferedWriter out;
    BufferedReader inputUser;

    public ThreadActions() {
        try (Socket socket = new Socket("127.0.0.1", 8081)) {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.print("Text your name: ");

            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException ignored) {
        }

    }

    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (status) {
                    str = in.readLine();
                    if (str != null) System.out.println(str);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private class WriteMsg extends Thread {

        @Override
        public void run() {
            while (status) {
                String userWord;
                try {
                    userWord = inputUser.readLine();
                    out.write(userWord + "\n");
                    out.flush();
                    if (userWord.equals("/leave")) {
                        status = false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

}