package com.tt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.util.Objects;

public class TTMain {
    private JFrame jWindow = null;
    private Container c = new Container();

    public static void main(String[] args) {
        TTMain app = new TTMain();
    }

    public TTMain() {


        TTMain _this = this;

        _this.createJFrame();

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

    }

    public void createJFrame() {
        jWindow = new JFrame();
        jWindow.setType(Window.Type.UTILITY);
        jWindow.setAlwaysOnTop(true);
        jWindow.setUndecorated(true);
        //jWindow.setOpacity(0.90f);
        //jWindow.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        jWindow.setVisible(true);
        jWindow.setFocusable(true);
        jWindow.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                //mainPanel.revalidate();
                //sysPanel.revalidate();
                //jWindow.dispose();
            }
        });


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

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton() == MouseEvent.BUTTON1) {

                    jWindow.setSize(300, 200);
                    Point point1 = e.getPoint(); //system tray icon click x,y/co-ords
                    Rectangle windowSize = GraphicsEnvironment.getLocalGraphicsEnvironment()
                            .getMaximumWindowBounds();
                    String os = System.getProperty("os.name");
                    if (os.contains("Windows")) {
                        if (point1.y <= 300) {
                            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                            jWindow.setLocation(point1.x, (screenSize.height - windowSize.height) - 6);
                        } else {
                            jWindow.setLocation(point1.x, windowSize.height - 200 - 6);
                        }
                    } else if (os.contains("Mac")) {
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        jWindow.setLocation(point1.x, screenSize.height - windowSize.height);
                    }

                }

            }
        };


        icon.addMouseListener(mouseAdapter);
        icon.setImageAutoSize(true);
        tray.add(icon);

    }
}