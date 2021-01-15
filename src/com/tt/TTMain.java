package com.tt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class TTMain {

    public static void main(String[] args) {
        TTMain app = new TTMain();
    }
    
    public TTMain(){
//        JFrame f = new JFrame("Button Example");
//        JButton btn = new JButton("Click Here");
//        btn.setBounds(50,100,80,30);
//        f.add(btn);
//        f.setSize(400,400);
//        f.setLayout(null);
//        f.setVisible(true);
        
        TTMain _this = this;
        
//        btn.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
                if (SystemTray.isSupported()) {
                    try {
                        _this.displayTray();
                    } catch (AWTException awtException) {
                        awtException.printStackTrace();
                    }
                } else {
                    System.err.println("System tray is not supported!");
                    System.exit(0);
                }
//            }
//        });
    }
    
    public void displayTray() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(TTMainUI.class.getClassLoader().getResourceAsStream("resources/tt.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PopupMenu menu = new PopupMenu();

        MenuItem messageItem = new MenuItem("Show Message");
        messageItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Temperature Tray is in making!"));
        menu.add(messageItem);

        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        menu.add(closeItem);
        TrayIcon icon = new TrayIcon(Objects.requireNonNull(image), "Temperature Tray", menu);
        //icon.addMouseListener(mouseAdapter);
        icon.setImageAutoSize(true);
            tray.add(icon);
    }
}