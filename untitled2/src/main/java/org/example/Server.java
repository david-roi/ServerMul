package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


    public class Server implements Runnable {

        private ArrayList<ConnectionHandler> connections;
        private ArrayList<String> chairsChach = new ArrayList<>();
        private ArrayList<String> nowChairsChach = new ArrayList<>();
        private ServerSocket server;
        private boolean done;
        private ExecutorService pool;

        public Server() {
            connections = new ArrayList<>();
            done = false;
        }

        @Override
        public void run() {
            try {
                server = new ServerSocket(8080);
                pool = Executors.newCachedThreadPool();
                while (!done) {
                    Socket client = server.accept();
                    ConnectionHandler handler = new ConnectionHandler(client);
                    connections.add(handler);
                    pool.execute(handler);
                }
            } catch (Exception e) {
                shutdown();
            }
        }

        // This method broadcasts a message to all connected clients
        public synchronized void broadcast(String message) {
            for (ConnectionHandler ch : connections) {
                if (ch != null) {
                    ch.sendMessage(message);
                }
            }
        }

        public synchronized void changeBuyMessage(String color, String name) {
            for (ConnectionHandler ch : connections) {
                if (ch != null && !ch.nickname.equals(name)) {
                    ch.sendMessage(color);
                }
            }
        }

        public void shutdown() {
            try {
                done = true;
                pool.shutdown();
                if (!server.isClosed()) {
                    server.close();
                }
                for (ConnectionHandler ch : connections) {
                    ch.shutdown();
                }
            } catch (IOException e) {

            }

        }


        class ConnectionHandler implements Runnable {
            private Socket client;

            private BufferedReader in;
            private PrintWriter out;
            private String nickname;
            public ConnectionHandler(Socket client) {
                this.client = client;
            }

            @Override
            public void run() {
                try {
                    out = new PrintWriter(client.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    // send prompt for nickname to client
                    out.println("Please enter a nickname: ");
                    // read nickname from client
                    nickname = in.readLine();
                    System.out.println(nickname + " connected!");
                    if(!chairsChach.isEmpty()){
                        for(int i = 0 ; i < chairsChach.size();i++){
                            out.println("RED" + chairsChach.get(i));
                        }
                    }
                    if(!nowChairsChach.isEmpty()){
                        for(int i = 0; i < nowChairsChach.size();i++){
                            out.println("gray" + nowChairsChach.get(i));
                        }
                    }
                    broadcast(nickname + " joined the chat!");
                    getFromUser();
                } catch (IOException e) {
                    shutdown();
                }
            }

            public synchronized void getFromUser() throws IOException {
                String message = null;
                while ((message = in.readLine()) != null) {

                    if (message.startsWith("/quit")) {
                        broadcast(nickname + " left the chat! ");
                        shutdown();
                    }

                    changeBuyMessage(message, this.nickname);
                    if (message.contains("gray")) {
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            String extractedNumber = matcher.group();
                            nowChairsChach.add(extractedNumber);
                        }
                    }
                    if (message.contains("White")) {
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            String extractedNumber = matcher.group();
                            nowChairsChach.remove(extractedNumber);
                        }
                    }
                    if (message.contains("RED")) {
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(message);
                        if (matcher.find()) {
                            String extractedNumber = matcher.group();
                            nowChairsChach.remove(extractedNumber);
                            chairsChach.add(extractedNumber);
                        }
                    }

                }

            }

            public void sendMessage(String message) {
                System.out.println(message);
                out.println(message);
            }

            public void shutdown() {
                try {
                    in.close();
                    out.close();
                    if (!client.isClosed()) {
                        client.close();
                    }

                    connections.remove(this);
                } catch (IOException e) {
                }
            }

        }

        public static void main(String[] args) {
            org.example.Server server = new org.example.Server();
            Thread t = new Thread(server);
            t.start();
        }
    }
