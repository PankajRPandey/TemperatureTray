package com.tt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

public class TrayUI {
  public static void main(String[] args)throws Exception {
    if (!SystemTray.isSupported()) {
      System.out.println("SystemTray is not supported");
      System.exit(0);
    }

    SystemTray tray = SystemTray.getSystemTray();
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image image = toolkit.getImage("src/resources/tt.png");

    PopupMenu menu = new PopupMenu();

    MenuItem messageItem = new MenuItem("Show Message");
    messageItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "www.java2s.com"));
    menu.add(messageItem);

    MenuItem closeItem = new MenuItem("Close");
    closeItem.addActionListener(e -> System.exit(0));
    menu.add(closeItem);

    TrayIcon icon = new TrayIcon(image, "Temperature Tray", menu);

    MouseAdapter mouseAdapter = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
//          icon.displayMessage("TrayIcon Demo",
//                  "This is an info message from TrayIcon demo",
//                  TrayIcon.MessageType.INFO);
          JWindow jWindow = new JWindow();
          JPanel p = new JPanel();
          JLabel l = new JLabel("this is a window");
          p.setBorder(BorderFactory.createLineBorder(Color.black));
          p.add(l);
          jWindow.add(p);
          //p.setBackground(Color.red);
          jWindow.setSize(200, 100);

//          Rectangle bounds = TestTaskIcon.getSafeScreenBounds(e.getPoint());
//          Point point = e.getPoint();
//
//          int x = point.x;
//          int y = point.y;
//          if (y < bounds.y) {
//            y = bounds.y;
//          } else if (y > bounds.y + bounds.height) {
//            y = bounds.y + bounds.height;
//          }
//          if (x < bounds.x) {
//            x = bounds.x;
//          } else if (x > bounds.x + bounds.width) {
//            x = bounds.x + bounds.width;
//          }
//
//          if (x + jWindow.getPreferredSize().width > bounds.x + bounds.width) {
//            x = (bounds.x + bounds.width) - jWindow.getPreferredSize().width;
//          }
//          if (y + jWindow.getPreferredSize().height > bounds.y + bounds.height) {
//            y = (bounds.y + bounds.height) - jWindow.getPreferredSize().height;
//          }
//          jWindow.setLocation(x, y);
          Point point1 = e.getPoint();
          System.out.println(point1);
          Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment()
                  .getMaximumWindowBounds();
          String os = System.getProperty("os.name");
          if (os.contains("Windows")) {
            jWindow.setLocation(/*screen.width - 200 - 6*/point1.x, screen.height - 100 - 6);
          } else if (os.contains("Mac")) {
            //jWindow.setLocation(screen.width - windowSize.width + 6, 6);
          }
          jWindow.setVisible(true);

          //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
//          GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
//          Rectangle bounds1 = gd.getDefaultConfiguration().getBounds();
//          Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
//
//          Rectangle safeBounds = new Rectangle(bounds1);
//          safeBounds.x += insets.left;
//          safeBounds.y += insets.top;
//          safeBounds.width -= (insets.left + insets.right);
//          safeBounds.height -= (insets.top + insets.bottom);
//
//          System.out.println("Bounds = " + bounds1);
//          System.out.println("SafeBounds = " + safeBounds);
//
//          Area area = new Area(bounds1);
//          area.subtract(new Area(safeBounds));
//          System.out.println("Area = " + area.getBounds());
          //\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

        }
      }
    };

    icon.addMouseListener(mouseAdapter);

    icon.setImageAutoSize(true);

    tray.add(icon);
  }
}