package net.fluxgems.bowling;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class Main {

    JFrame frame = new JFrame("10 Pin Bowling");
    private ArrayList<Player> players = new ArrayList<>();
    private JButton runTurnButton;
    private JMenuBar topMenu;

    public Main() {

        frame.setBounds(100,100,1300,250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("Width: " + frame.getWidth());
                System.out.println("Height: " + frame.getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        topMenu = new JMenuBar();
        JMenu newGame = new JMenu("New Game");
        JMenuItem playVsPlayer = new JMenuItem("Player VS Player");
        JMenuItem playVsAi = new JMenuItem("Player VS AI");

        playVsPlayer.addActionListener((ActionEvent e) -> {
            players = new ArrayList<>();
            String player1Name = JOptionPane.showInputDialog("First players name?");
            String player2Name = JOptionPane.showInputDialog("Second players name?");
            Player player1 = new Player(player1Name, this, false);
            Player player2 = new Player(player2Name, this, false);
            players.add(player1);
            players.add(player2);
            refreshScreen();
        });

        playVsAi.addActionListener((ActionEvent e) -> {
            players = new ArrayList<>();
            String player1Name = JOptionPane.showInputDialog("First players name?");
            Player player1 = new Player(player1Name, this, false);
            Player player2 = new Player("Computer", this, true);
            players.add(player1);
            players.add(player2);
            refreshScreen();
        });

        newGame.add(playVsPlayer);
        newGame.add(playVsAi);
        topMenu.add(newGame);
        frame.setJMenuBar(topMenu);


        runTurnButton = new JButton("Play Frame");
        runTurnButton.setPreferredSize(new Dimension(200,50));

        runTurnButton.addActionListener((ActionEvent e) -> playRound());

        refreshScreen();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main window = new Main();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void repaint()  {
        frame.repaint();
        frame.setVisible(true);
    }

    public void refreshScreen() {
        frame.setJMenuBar(topMenu);
        if (players.size() > 0) {
            frame.getContentPane().removeAll();
            frame.getRootPane().setDefaultButton(runTurnButton);
            frame.setJMenuBar(topMenu);

            frame.setLayout(new GridLayout(players.size()+1, 1));

            int counter = 1;
            for (Player p : players) {
                JPanel pl;
                if (counter < players.size()) {
                    pl = p.getPanel(false);
                } else {
                    pl = p.getPanel(true);
                }
                frame.add(pl);
                counter++;
            }

            JPanel run = new JPanel(new BorderLayout());
            run.add(runTurnButton, BorderLayout.EAST);
            frame.add(run);
            frame.validate();
            frame.repaint();
            frame.setVisible(true);
        }
    }

    private void playRound() {
        for (Player p : players) {
            p.playFrame();
        }
        refreshScreen();
    }

}
