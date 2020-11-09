package com.tt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrayUI {
    public static void main(String[] args) throws Exception {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            System.exit(0);
        }

        SystemTray tray = SystemTray.getSystemTray();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("src/resources/tt.png");

        PopupMenu menu = new PopupMenu();

        MenuItem messageItem = new MenuItem("Show Message");
        messageItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Temperature Tray is in making!"));
        menu.add(messageItem);

        MenuItem closeItem = new MenuItem("Close");
        closeItem.addActionListener(e -> System.exit(0));
        menu.add(closeItem);

        TrayIcon icon = new TrayIcon(image, "Temperature Tray", menu);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            JWindow jWindow = null;
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
//          icon.displayMessage("TrayIcon Demo",
//                  "This is an info message from TrayIcon demo",
//                  TrayIcon.MessageType.INFO);
                    if (jWindow == null){
                        jWindow = new JWindow();
                    }
                    JPanel mainPanel = new JPanel();
                    JLabel l = new JLabel("this is a window");
                    mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 5, true));
                    mainPanel.add(l);
                    jWindow.add(mainPanel);
                    mainPanel.setBackground(Color.WHITE);
                    jWindow.setSize(300, 200);
                    Point point1 = e.getPoint(); //system tray icon click x,y/co-ords
                    //System.out.println(point1);
                    Rectangle windowSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
                            .getMaximumWindowBounds();
                    String os = System.getProperty("os.name");
                    if (os.contains("Windows")) {
                        if (point1.y <= 300) {
                            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                            //
                            jWindow.setLocation(point1.x, (screenSize.height - windowSize.height) - 6 /*windowSize.width - 200 + 6, 6*/);
                            //System.out.println(point1.x + " " + (screenSize.height - windowSize.height));
                        } else {
                            jWindow.setLocation(/*windowSize.width - 200 - 6*/point1.x, windowSize.height - 200 - 6);
                            //System.out.println(point1.x + " " + (windowSize.height - 100 - 6));
                        }
                    } else if (os.contains("Mac")) {
                        //this solution works with windows top taskbar
                        //add logic to check taskbar location
                        //test this on Mac
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        jWindow.setLocation(point1.x, screenSize.height - windowSize.height /*windowSize.width - 200 + 6, 6*/);
                        System.out.println(point1.x + " " + (screenSize.height - windowSize.height));
                    }
                    jWindow.setAlwaysOnTop(true);
                    jWindow.setVisible(true);
                }
            }
        };

        icon.addMouseListener(mouseAdapter);
        icon.setImageAutoSize(true);
        tray.add(icon);
    }
}