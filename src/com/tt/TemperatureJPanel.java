package com.tt;

import javax.swing.*;
import java.awt.*;

public class TemperatureJPanel extends JPanel {
    public TemperatureJPanel(LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(15, 15);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Draws the rounded opaque panel
        graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 0.80f));
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);//paint background
    }

    @Override
    public void setForeground(Color fg) {
        super.setForeground(fg);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }
}
