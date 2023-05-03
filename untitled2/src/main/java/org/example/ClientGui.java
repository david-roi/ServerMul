package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ClientGui{

    protected String[] nameChairs = {"1", "2", "3", "4", "5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27"};
    protected  int numberOfChairs = nameChairs.length;
    protected JLabel nameLabel;
    protected JFrame frame = new JFrame("buy Chairs");
    protected JTextArea messageArea = new JTextArea(8, 40);
    protected ArrayList<Chairs> chairs = new ArrayList<>();

    public  void buildListChairs(){
        int x = 600;
        int y = 50;
        int width = 50;
        int height = 50;
        chairs.add(new Chairs(x,y,width,height,1, nameChairs[0],0));
        chairs.add(new Chairs(x+55,y,width,height,1,nameChairs[1],1));
        x = x + 55;
        for(int i = 2; i < numberOfChairs; i++){
            chairs.add(new Chairs(x,y,width,height,333, nameChairs[i], i));
            x = x + 55;
            if(i % 5 == 0) {
                y = y + 55;
                x = 600;
            }
        }
    }
    public  void place(){
        for(int i = 0; i < chairs.size(); i++){
            chairs.get(i).getSendButton().setBackground(java.awt.Color.WHITE);
            frame.add(chairs.get(i).getSendButton());
        }
        ImageIcon imageIcon = new ImageIcon("C:\\ProjectServer\\src\\main\\java\\org\\example\\football.jpg");
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(resizedImageIcon);
        frame.add(imageLabel, BorderLayout.SOUTH);
    }
    public void startGui(){
        allButton();
        frame.setTitle("game ");
        frame.setVisible(true);
    }

    public void allButton(){
        buildListChairs();
        messageArea.setEditable(false);
        place();
        frame.getContentPane().add(new JScrollPane(messageArea), BorderLayout.CENTER);
        frame.pack();
    }
    public void changeColorToRed(int id){
        chairs.get(id).setColor(org.example.Color.RED);
        chairs.get(id).getSendButton().setBackground(java.awt.Color.RED);

    }
    public void changeColorToWhite(int id){
        chairs.get(id).setColor(org.example.Color.WHITE);
        chairs.get(id).getSendButton().setBackground(java.awt.Color.WHITE);
    }
    public void changeColorToGreen(int id){
        chairs.get(id).setColor(org.example.Color.GREEN);
        chairs.get(id).getSendButton().setBackground(java.awt.Color.GREEN);
    }
    public void changeColorTogray(int id){
        chairs.get(id).setColor(org.example.Color.GRAY);
        chairs.get(id).getSendButton().setBackground(java.awt.Color.GRAY);
    }


}
