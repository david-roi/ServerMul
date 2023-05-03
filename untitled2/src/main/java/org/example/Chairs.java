package org.example;

import javax.swing.*;

public class Chairs {
    protected int id;
    private int x;
    private int y;
    private int width;
    private int height;

    private double price;
    private JButton sendButton ;
    Color color;
    public Chairs(int x, int y, int width,int height, double price, String nameChairs, int id){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.price = price;
        color = Color.WHITE;
        this.id = id;
        sendButton = new JButton(nameChairs);
        sendButton.setBounds(x,  y, width, height);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}