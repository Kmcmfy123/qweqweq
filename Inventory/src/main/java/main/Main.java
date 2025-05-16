package main;

import javax.swing.*;
import java.awt.CardLayout;
import Dashboard.dashboardPanel;
import Dashboard.SalesHistory.salesHistoryPanel;

public class Main {
    private static final JFrame window = new JFrame();
    private static final CardLayout cardLayout = new CardLayout();
    private static final JPanel mainPanel = new JPanel(cardLayout);

    private static final salesHistoryPanel salesHistory = new salesHistoryPanel();

    private static final dashboardPanel dashboardPanel = new dashboardPanel();

    public static void main(String[] args) {
        initializeWindow();
        initializeModules();
        changeCard("SalesHistory");
        window.setVisible(true);
    }

    public static void addCard(JPanel card, String card_name) {
        mainPanel.add(card, card_name);
    }

    public static void changeCard(String card_name) {
        cardLayout.show(mainPanel, card_name);
    }

    private static void initializeWindow() {
        window.setSize(880, 660);
        window.setTitle("Atlas Inventory");
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setLayout(new CardLayout());
        window.add(mainPanel);
    }

    private static void initializeModules(){
        addCard(dashboardPanel, "Dashboard");
        addCard(salesHistory, "SalesHistory");
    }
}