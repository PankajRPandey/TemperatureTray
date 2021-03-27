package com.tt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;

import static com.tt.TTUtils.getDeviceTemperature;


public class TTMain {

    private JFrame jWindow = null;
    private Container c = new Container();
    private JLabel centerLabel = new JLabel("<html><body style='color:white;'>Loading..<body><html>");
    private JLabel pageStartLabel = new JLabel();
    private JLabel pageEndLabel = new JLabel();
    private JLabel sysPnlCenterLbl = new JLabel("<html><body style='color:white;font-size:20px;'>Loading..<body><html>");
    private JLabel sysPnlPageStartLbl = new JLabel();
    private JLabel sysPnlPageEndLbl= new JLabel();
    private TTJPanel sysPanel = new TTJPanel(new BorderLayout());
    private TTJPanel mainPanel = new TTJPanel(new BorderLayout());
    private CardLayout card = new CardLayout();
    private MouseAdapter mouseAdapter;
    private Image weatherImg;
    private ImageIcon weatherImgIcon;
    private Image systemImg;
    private ImageIcon systemImgIcon;

    public static void main(String[] args) {
        new TTMain();
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
        jWindow.setUndecorated(true);
        jWindow.setType(Window.Type.UTILITY);
        jWindow.setAlwaysOnTop(true);
        jWindow.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
        jWindow.setOpacity(0.80f);
        jWindow.setVisible(true);
        jWindow.setFocusable(true);

        c = jWindow.getContentPane();
        //jWindow.getContentPane().setBackground(new Color(1.0f,1.0f,1.0f,1.0f));
        c.setLayout(card);
        c.add("A", mainPanel);
        c.add("B", sysPanel);
        mainPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        sysPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
        //{"Perth"} {"Mumbai"};//
        String[] geoAPIKeyValue = TTUtils.getKeysFromAPIResponse("https://freegeoip.app/json/", "city");
        setAppFont();
        //System.out.println(APIUtils.getIPAddress());
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                while (true) {
                    // "CPU: Intel Core i7-3517U,Temp CPU Core #1: 73.0°C,Temp CPU Core #2: 77.0°C,Temp CPU Core #3: 78.0°C,Temp CPU Core #4: 79.0°C,Temp CPU Package: 100.0°C";//
                    String metaData = getDeviceTemperature();
                    metaData = metaData.replace("Temp ","");
                    String[] sysTemperatureValues;
                    sysTemperatureValues = metaData.split(",");
                    sysPnlPageStartLbl.setText("<html><center><span style='color:white;font-size:22px;'><b>System Temperature</b></span><br/><hr/><span style='color:white;font-size:12px;'><b>"+sysTemperatureValues[0].toUpperCase()+"</b></span></center></html>");
                    String corePackage = sysTemperatureValues[sysTemperatureValues.length - 1];
                    StringBuilder cores = new StringBuilder();
                    for (int i = 1; i < (sysTemperatureValues.length) - 1; i++) {
                        if(i%2 != 0){
                            cores.append(sysTemperatureValues[i]).append("&nbsp;&nbsp;|&nbsp;&nbsp;");
                        }else {
                            cores.append(sysTemperatureValues[i]).append("<br/>");
                        }

                    }
                    corePackage = corePackage.substring(corePackage.indexOf(":") + 2, corePackage.length() - 2);
                    //sysPnlCenterLbl.setText("<html><center><span style='color:white;font-size:55px;'>" +  (int)Math.ceil(Float.parseFloat(corePackage)) + "°C" + "</span></center></html>");
                    sysPnlCenterLbl.setText(" "+(int)Math.ceil(Float.parseFloat(corePackage)) + "°C");
                    sysPnlPageEndLbl.setText("<html><center><span style='color:white;font-size:12px;'><b>" + cores + "</b></span></center></html>");

                    if (!(sysPnlCenterLbl.getText()).contains("Loading..") && sysPnlCenterLbl.getIcon()==null){
                        System.out.println(sysPnlCenterLbl.getIcon());
                        sysPnlCenterLbl.setIcon(systemImgIcon);
                    }
                }
            }
        }.execute();

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (e.getButton() == MouseEvent.BUTTON1) {
                    jWindow.setVisible(true);
                    //{"20.55", "21.66", "24.77", "20.44", "50d"};//
                    String[] weatherAPIKeyValue = {"20.55", "21.66", "24.77", "20.44", "50d"};//TTUtils.getKeysFromAPIResponse("http://api.openweathermap.org/data/2.5/weather?q="+ geoAPIKeyValue[0] +"&units=metric&appid=", "main.temp", "main.feels_like", "main.temp_max", "main.temp_min", "weather.0.icon");


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

                    try {
                        weatherImg = ImageIO.read(Objects.requireNonNull(TTMainUI.class.getClassLoader().getResourceAsStream("resources/icons/weather/" + weatherAPIKeyValue[4] + ".png")));
                        weatherImgIcon = new ImageIcon(weatherImg);
                    } catch (IOException malformedURLException) {
                        weatherImgIcon = new ImageIcon("resources/icons/weather/50d.png");
                        malformedURLException.printStackTrace();
                    }

                    centerLabel.setIcon(weatherImgIcon);
                    centerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    centerLabel.setText("<html><center><span style='color:white;font-size:55px;'>&nbsp;" + (int)Math.ceil(Float.parseFloat(weatherAPIKeyValue[0])) + "°C" + "</span></center></html>");
                    mainPanel.add(centerLabel, BorderLayout.CENTER);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH) ,
                            calendar.get(Calendar.DAY_OF_MONTH));
                    int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

                    pageStartLabel.setText("<html><center><span style='color:white;font-size:22px;'><b>" + geoAPIKeyValue[0] + "</b></span><br/><hr/><span style='color:white;font-size:12px;'><b>" + LocalDate.now().getDayOfWeek() + " &nbsp;|&nbsp; WEEK OF THE MONTH: " + weekOfMonth + "</b></span></center></html>");
                    pageStartLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    mainPanel.add(pageStartLabel, BorderLayout.PAGE_START);

                    pageEndLabel.setText("<html><center><span style='color:white;font-size:12px;'><i>Feels like " + (int)Math.ceil(Float.parseFloat(weatherAPIKeyValue[1])) + "°C</i></span><br/><span style='color:white;font-size:12px;font-weight:bold;'>HIGH " + (int)Math.ceil(Float.parseFloat(weatherAPIKeyValue[2])) + "°C &nbsp;&nbsp; | &nbsp;&nbsp; LOW " + (int)Math.ceil(Float.parseFloat(weatherAPIKeyValue[3])) + "°C</span></center></html>");
                    pageEndLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    //pageEndLabel.setVerticalTextPosition(JLabel.TOP);
                    mainPanel.add(pageEndLabel, BorderLayout.PAGE_END);

                    //sysPnlPageStartLbl.setText("<html><center><span style='color:white;font-size:18px;'><b>System Temperature</b></span><br/><hr/></center></html>");
                    sysPnlPageStartLbl.setHorizontalAlignment(SwingConstants.CENTER);
                    sysPanel.add(sysPnlPageStartLbl, BorderLayout.PAGE_START);

                    try {
                        systemImg = ImageIO.read(Objects.requireNonNull(TTMainUI.class.getClassLoader().getResourceAsStream("resources/icons/system/cpu.png")));
                        systemImgIcon = new ImageIcon(systemImg);
                    } catch (IOException malformedURLException) {
                        malformedURLException.printStackTrace();
                    }

                    //sysPnlCenterLbl.setIcon(systemImgIcon);
                    sysPnlCenterLbl.setHorizontalAlignment(SwingConstants.CENTER);
                    sysPanel.add(sysPnlCenterLbl, BorderLayout.CENTER);

                    sysPnlPageEndLbl.setHorizontalAlignment(SwingConstants.CENTER);
                    sysPanel.add(sysPnlPageEndLbl, BorderLayout.PAGE_END);

                        jWindow.addWindowFocusListener(new WindowFocusListener() {
                            @Override
                            public void windowGainedFocus(WindowEvent e) {
                            }

                            @Override
                            public void windowLostFocus(WindowEvent e) {
                                    mainPanel.revalidate();
                                    sysPanel.revalidate();
                                    //jWindow.dispose();
                                jWindow.setVisible(false);
                            }
                        });



                }

            }
        };




        jWindow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                card.next(c);
            }
        });

    }

    public void displayTray() throws AWTException {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(TTMainUI.class.getClassLoader().getResourceAsStream("resources/icons/system/tt.png")));
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

        icon.addMouseListener(mouseAdapter);
        icon.setImageAutoSize(true);
        tray.add(icon);

    }

    public void setAppFont(){
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(TTMainUI.class.getClassLoader().getResourceAsStream("resources/RobotoCondensed-Bold.ttf")));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            pageStartLabel.setFont(font.deriveFont(Font.BOLD, 20f));
            pageEndLabel.setFont(font.deriveFont(Font.BOLD, 20f));
            centerLabel.setFont(font.deriveFont(Font.BOLD, 20f));
            sysPnlPageStartLbl.setFont(font.deriveFont(Font.BOLD, 20f));
            sysPnlPageEndLbl.setFont(font.deriveFont(Font.BOLD, 20f));
            sysPnlCenterLbl.setFont(font.deriveFont(Font.BOLD, 70f));
            sysPnlCenterLbl.setForeground(Color.WHITE);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}