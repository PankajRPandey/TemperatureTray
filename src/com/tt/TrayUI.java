package com.tt;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

public class TrayUI {
  public static void main(String[] args)throws Exception {
    if (!SystemTray.isSupported()) {
      System.out.println("SystemTray is not supported");
      return;
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
          icon.displayMessage("TrayIcon Demo",
                  "This is an info message from TrayIcon demo",
                  TrayIcon.MessageType.INFO);
        }
      }
    };

    icon.addMouseListener(mouseAdapter);

    icon.setImageAutoSize(true);

    tray.add(icon);
  }
}