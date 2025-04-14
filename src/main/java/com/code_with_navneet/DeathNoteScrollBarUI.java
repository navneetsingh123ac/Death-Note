package com.code_with_navneet;

import javax.swing.*;
import java.awt.*;

class DeathNoteScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
    private static final Color THUMB_COLOR = new Color(54, 29, 205);
    private static final Color THUMB_HOVER_COLOR = new Color(0, 39, 180);
    private static final Color TRACK_COLOR = new Color(50, 50, 55);

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createInvisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createInvisibleButton();
    }

    private JButton createInvisibleButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(TRACK_COLOR);
        g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isDragging) {
            g2.setColor(THUMB_HOVER_COLOR);
        } else if (isThumbRollover()) {
            g2.setColor(THUMB_HOVER_COLOR);
        } else {
            g2.setColor(THUMB_COLOR);
        }

        g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y,
                thumbBounds.width - 4, thumbBounds.height, 10, 10);
    }
}