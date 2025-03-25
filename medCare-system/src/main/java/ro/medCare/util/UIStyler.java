package ro.medCare.util;

import javax.swing.*;
import java.awt.*;

public class UIStyler {

    public static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Blue
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Lighter blue
    public static final Color ACCENT_COLOR = new Color(231, 76, 60); // Red
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light gray
    public static final Color TEXT_COLOR = new Color(44, 62, 80); // Dark blue
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
    public static final Color WARNING_COLOR = new Color(241, 196, 15); // Yellow

    public static final Color BUTTON_DEFAULT_BG = new Color(52, 152, 219); // Blue
    public static final Color BUTTON_DEFAULT_FG = Color.WHITE;
    public static final Color BUTTON_SUCCESS_BG = new Color(46, 204, 113); // Green
    public static final Color BUTTON_SUCCESS_FG = Color.WHITE;
    public static final Color BUTTON_DANGER_BG = new Color(231, 76, 60); // Red
    public static final Color BUTTON_DANGER_FG = Color.WHITE;
    public static final Color BUTTON_WARNING_BG = new Color(241, 196, 15); // Yellow
    public static final Color BUTTON_WARNING_FG = Color.WHITE;

    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);

    public static final int BORDER_RADIUS = 10;
    public static final int PANEL_PADDING = 15;

    public static JButton styleButton(JButton button) {
        button.setBackground(BUTTON_DEFAULT_BG);
        button.setForeground(BUTTON_DEFAULT_FG);
        button.setFont(REGULAR_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }


    public static JButton styleSuccessButton(JButton button) {
        button.setBackground(BUTTON_SUCCESS_BG);
        button.setForeground(BUTTON_SUCCESS_FG);
        button.setFont(REGULAR_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }


    public static JButton styleDangerButton(JButton button) {
        button.setBackground(BUTTON_DANGER_BG);
        button.setForeground(BUTTON_DANGER_FG);
        button.setFont(REGULAR_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }


    public static JButton styleWarningButton(JButton button) {
        button.setBackground(BUTTON_WARNING_BG);
        button.setForeground(BUTTON_WARNING_FG);
        button.setFont(REGULAR_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }


    public static JPanel stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING, PANEL_PADDING));
        return panel;
    }


    public static JLabel styleLabel(JLabel label) {
        label.setForeground(TEXT_COLOR);
        label.setFont(REGULAR_FONT);
        return label;
    }


    public static JLabel styleTitleLabel(JLabel label) {
        label.setForeground(TEXT_COLOR);
        label.setFont(TITLE_FONT);
        return label;
    }


    public static JTable styleTable(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(new Color(189, 195, 199));
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(Color.WHITE);

        // Style the header
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setOpaque(true);

        return table;
    }


    public static JPanel createTitlePanel(String titleText) {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(PRIMARY_COLOR);

        JLabel titleLabel = new JLabel(titleText, SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }


    public static void applyHoverEffect(JButton button) {
        Color originalColor = button.getBackground();
        Color hoverColor = lightenColor(originalColor, 0.2f);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
    }


    private static Color lightenColor(Color color, float amount) {
        int red = (int) Math.min(255, color.getRed() + amount * 255);
        int green = (int) Math.min(255, color.getGreen() + amount * 255);
        int blue = (int) Math.min(255, color.getBlue() + amount * 255);

        return new Color(red, green, blue);
    }
}