package com.tt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.RoundRectangle2D;

public class TrayUI {
    public static void main(String[] args) {
        String[] geoAPIKeyValue = APIUtils.getKeysFromAPIResponse("https://freegeoip.app/json/", "city", "ip");
        //System.out.println(APIUtils.getIPAddress());
        EventQueue.invokeLater(() -> {

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
                JFrame jWindow = null;

                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
//          icon.displayMessage("TrayIcon Demo",
//                  "This is an info message from TrayIcon demo",
//                  TrayIcon.MessageType.INFO);
                        String[] weatherAPIKeyValue = APIUtils.getKeysFromAPIResponse("http://api.openweathermap.org/data/2.5/weather?q="+ geoAPIKeyValue[0] +"&units=metric&appid=", "main.temp", "main.feels_like");

                        if (jWindow == null) {
                            jWindow = new JFrame();
                        }

                        JPanel mainPanel = new JPanel(new BorderLayout());
                        JLabel l = new JLabel("<html><span style='color:white;font-size:80px;'>" + weatherAPIKeyValue[0] + "°C" + "</span><i><center style='color:white;font-size:12px;'>Feels like "+ weatherAPIKeyValue[1] +"°C</center></i></html>", SwingConstants.CENTER);
                        //mainPanel.setBorder(BorderFactory.createLineBorder(Color.black, 5, true));
                        mainPanel.add(l, BorderLayout.CENTER);
                        //mainPanel.add(new JLabel("Testing", SwingConstants.CENTER), BorderLayout.LINE_START);
                        //mainPanel.add(new JLabel("Testing", SwingConstants.CENTER), BorderLayout.LINE_END);
                        mainPanel.add(new JLabel("<html><span style='color:white;font-size:20px;'>" + geoAPIKeyValue[0] + "</span></html>", SwingConstants.CENTER), BorderLayout.PAGE_START);
                        JLabel pageEnd = new JLabel("<html><span style='color:white;'>IP:" + geoAPIKeyValue[1] + "</span></html>", SwingConstants.CENTER);
                        pageEnd.setVerticalTextPosition(JLabel.TOP);
                        mainPanel.add(pageEnd, BorderLayout.PAGE_END);

                        jWindow.add(mainPanel);
                        mainPanel.setBackground(new Color(0.0f,0.0f,0.0f,0.20f));
                        //mainPanel.setBackground(Color.WHITE);
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
                        jWindow.setType(Window.Type.UTILITY);
                        jWindow.setAlwaysOnTop(true);
                        jWindow.setUndecorated(true);
                        jWindow.setShape(new RoundRectangle2D.Double(0, 0, 300, 200, 20, 20));
                        jWindow.setOpacity(0.55f);
                        //l.setBackground(new Color(0,0,0,1));
                        jWindow.setBackground(new Color(0.0f,0.0f,0.0f));
                        jWindow.setVisible(true);
                        jWindow.setFocusable(true);
                        jWindow.addWindowFocusListener(new WindowFocusListener() {
                            @Override
                            public void windowGainedFocus(WindowEvent e) {

                            }
                            @Override
                            public void windowLostFocus(WindowEvent e) {
                                jWindow.dispose();
                            }
                        });
                    }
                }
            };


            icon.addMouseListener(mouseAdapter);
            icon.setImageAutoSize(true);
            try {
                tray.add(icon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
    }
}