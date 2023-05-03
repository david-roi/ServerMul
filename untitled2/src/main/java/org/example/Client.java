package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends ClientGui implements Runnable {
    private double mony = 1000;
    private String UserName ;
    private ArrayList<Chairs> yourChairs = new ArrayList<>();
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frameDetails = new JFrame("Details");
    private JPanel panelDetails = new JPanel();
    private  JFrame frame1 = new JFrame("Error Window Example");
    private JPanel panel1 = new JPanel();

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 8080);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            start();
            startGui();
            //here i build Thread that run and listen to server
            InputHandler inputHandler = new InputHandler();
            Thread t = new Thread(inputHandler);
            t.start();
            String inMessage;
            while ((inMessage = in.readLine()) != null) {
                if(inMessage.contains("RED")){
                    for(Chairs ch: chairs){
                        String numberId = String.valueOf(ch.id);

                        String numStr = inMessage.replaceAll("[^\\d.]", "");
                        if(numStr.equals(numberId)){
                            changeColorToRed(ch.id);
                        }
                    }
                }else if(inMessage.contains("White")){
                    for(Chairs ch: chairs){
                        String numberId = String.valueOf(ch.id);
                        String numStr = inMessage.replaceAll("[^\\d.]", "");
                        if(numStr.equals(numberId)){
                            changeColorToWhite(ch.id);
                        }
                    }
                }else if(inMessage.contains("gray")){

                    for(Chairs ch: chairs) {
                        String numberId = String.valueOf(ch.id);
                        String numStr = inMessage.replaceAll("[^\\d.]", "");
                        if (numStr.equals(numberId)) {
                            changeColorTogray(ch.id);
                        }
                    }
                }
                System.out.println(inMessage);
            }

        } catch (IOException e) {
            shutdown();
        }
    }
    //this i ask for the user: name
    public void start() throws IOException {
        while (true) {
            String input = JOptionPane.showInputDialog(frame, "Enter a username:", "Username", JOptionPane.PLAIN_MESSAGE);
            if (input != null && input.length() > 0) {
                UserName = input;
                nameLabel = new JLabel("user Name " + UserName + "  " +"price  = " + mony);
                nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
                frame.add(nameLabel, BorderLayout.NORTH);
                out.println(input);
                break;
            }
        }
    }

    public synchronized void shutdown() {
        try {
            in.close();
            out.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }

    public void errorWindow(String message) {
        frame1.getContentPane().add(panel1);
        frame1.getContentPane().add(panel1);
        frame1.setSize(300, 200);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JOptionPane.showMessageDialog(frame1, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void addToYourChairs(Chairs chairs, double mony) {
        this.mony = this.mony - mony;
        nameLabel.setText("User Name: " + UserName + "  " +"price  = " + this.mony);
        out.println("RED" + chairs.id );
        yourChairs.add(chairs);
        changeColorToGreen(chairs.id);
        System.out.println(this.mony);
    }

    public void details(int price, Chairs chairs) {
        frameDetails.getContentPane().add(panelDetails);
        frameDetails.getContentPane().add(panelDetails);
        frameDetails.setSize(300, 200);
        frameDetails.setLocationRelativeTo(null);
        frameDetails.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameDetails.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

            }
        });

        JLabel priceLabel = new JLabel("Price: " + price);
        panelDetails.add(priceLabel);

        JButton buttonExit = new JButton("Cancel");

        JButton button = new JButton("you want buy");

        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeColorToWhite(chairs.id);
                out.println("White" + chairs.id);
                panelDetails.removeAll();
                frameDetails.dispose();
            }
        });

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Update the price based on the value entered in the text field
                try {
                    addToYourChairs(chairs, chairs.getPrice());


                } catch (NumberFormatException ex) {
                    // Display an error message if the value entered in the text field is not a valid integer
                    JOptionPane.showMessageDialog(frameDetails, "Invalid price");
                    return;
                }
                panelDetails.removeAll();
                frameDetails.dispose();
            }
        });
        panelDetails.add(buttonExit);
        panelDetails.add(button);
        frameDetails.setVisible(true);

    }

    class InputHandler implements Runnable {
        public void run() {
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    out.println("/quit");
                    shutdown();
                }
            });
            for (Chairs ch : chairs) {
                ch.getSendButton().addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(ch.color == Color.GRAY){
                            errorWindow("Someone is buying it already");
                        }else if(ch.color == Color.GREEN){
                            errorWindow("this Is Your Chairs");
                        }else if(ch.color == Color.RED){
                            errorWindow("The place is occupied");
                        }
                        else{
                            if(ch.getPrice() > mony){
                                errorWindow("you need more mony");
                            }
                            else{
                                out.println("gray " + ch.id);
                                changeColorTogray(ch.id);
                                details((int) ch.getPrice(), ch);
                            }
                        }
                    }
                });
            }
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
                System.exit(0);
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static void main(String[] args) {
        Client client1 = new Client();
        client1.run();

    }
}
