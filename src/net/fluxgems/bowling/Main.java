package net.fluxgems.bowling;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class Main implements Serializable{

    transient JFrame frame = new JFrame("10 Pin Bowling");
    private ArrayList<Player> players = new ArrayList<>();
    private transient JButton runTurnButton;
    private transient JButton getWinnerButton;
    private transient JMenuBar topMenu;
    private static transient FileNameExtensionFilter extFilter
            = new FileNameExtensionFilter("Bowling File", "bwl");

    public Main() {

        frame.setBounds(100,100,1000,250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);



        getWinnerButton = new JButton("Who won?");
        getWinnerButton.setPreferredSize(new Dimension(200, 50));

        getWinnerButton.addActionListener((ActionEvent e) -> {
            int highScore = 0;
            Player winner = null;

            for (Player p : players) {
                if (p.getFinalScore() > highScore) {
                    highScore = p.getFinalScore();
                    winner = p;
                }
            }

            JOptionPane.showMessageDialog(null, "The winner is " + winner.getName() + " with a score of " + highScore + "!");
        });

        implementJMenu();

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
        if (!players.isEmpty()) {
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

            if (players.get(0).turnsSize() == 10) {
                run.add(getWinnerButton, BorderLayout.EAST);
            } else {
                run.add(runTurnButton, BorderLayout.EAST);
            }



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

    public void implementJMenu() {
        topMenu = new JMenuBar();
        JMenu file = new JMenu("File...");

        JMenuItem playVsPlayer = new JMenuItem("Player VS Player");
        JMenuItem playVsAi = new JMenuItem("Player VS Computer");
        JMenuItem aiVsAi = new JMenuItem("Computer VS Computer");

        JMenuItem saveGame = new JMenuItem("Save Game");
        JMenuItem loadGame = new JMenuItem("Load Game");

        JMenuItem about = new JMenuItem("About");
        JMenuItem close = new JMenuItem("Close Program");

        playVsPlayer.addActionListener((ActionEvent e) -> {
            boolean confirmed;
            if (!players.isEmpty()) {
                confirmed = JOptionPane.showConfirmDialog(frame, "This will wipe your current game, are you sure?")
                        == JOptionPane.OK_OPTION;
            } else {
                confirmed = true;
            }

            if (confirmed) {
                try {
                    int playerCount = Integer.parseInt(JOptionPane.showInputDialog(frame, "How Many Players?"));
                    if (playerCount > 8 || playerCount < 1) {
                        throw new NumberFormatException();
                    }
                    frame.setBounds(frame.getX(), frame.getY(), frame.getWidth(),(playerCount*100)+50);
                    players = new ArrayList<>();
                    for (int c = 1; c <= playerCount; c++) {
                        String name = JOptionPane.showInputDialog(frame, "Enter Player " + c + "'s name!");
                        Player p = new Player(name, this, false);
                        players.add(p);
                    }
                    refreshScreen();
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(frame, "Something was wrong with that value. Remember, the game can only have between 1 and 8 players.");
                }
            }
        });

        playVsAi.addActionListener((ActionEvent e) -> {
            boolean confirmed;
            if (!players.isEmpty()) {
                confirmed = JOptionPane.showConfirmDialog(frame, "This will wipe your current game, are you sure?")
                        == JOptionPane.OK_OPTION;
            } else {
                confirmed = true;
            }

            if (confirmed) {
                try {
                    int playerCount = Integer.parseInt(JOptionPane.showInputDialog(frame, "How many Players?"));
                    int compCount = Integer.parseInt(JOptionPane.showInputDialog(frame, "How many Computers?"));
                    if (playerCount+compCount > 8 || playerCount+compCount < 1) {
                        throw new NumberFormatException();
                    }
                    frame.setBounds(frame.getX(), frame.getY(), frame.getWidth(),((playerCount+compCount)*100)+50);
                    players = new ArrayList<>();
                    for (int c = 1; c <= playerCount; c++) {
                        String name = JOptionPane.showInputDialog(frame, "Enter Player " + c + "'s name!");
                        Player p = new Player(name, this, false);
                        players.add(p);
                    }
                    for (int c = 1; c <= compCount; c++) {
                        Player p = new Player("Computer "+c, this, true);
                        players.add(p);
                    }
                    refreshScreen();
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(frame, "Something was wrong with that value. Remember, the game can only have between 1 and 8 players.");
                }
            }
        });

        aiVsAi.addActionListener((ActionEvent e) -> {
            boolean confirmed;
            if (!players.isEmpty()) {
                confirmed = JOptionPane.showConfirmDialog(frame, "This will wipe your current game, are you sure?")
                        == JOptionPane.OK_OPTION;
            } else {
                confirmed = true;
            }

            if (confirmed) {
                try {
                    int playerCount = Integer.parseInt(JOptionPane.showInputDialog(frame, "How Many Computers?"));
                    if (playerCount > 8 || playerCount < 1) {
                        throw new NumberFormatException();
                    }
                    frame.setBounds(frame.getX(), frame.getY(), frame.getWidth(),(playerCount*100)+50);
                    players = new ArrayList<>();
                    for (int c = 1; c <= playerCount; c++) {
                        Player p = new Player("Computer " + c, this, true);
                        players.add(p);
                    }
                    refreshScreen();
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(frame, "Something was wrong with that value. Remember, the game can only have between 1 and 8 players.");
                }
            }
        });

        saveGame.addActionListener((ActionEvent e) -> {
            JFileChooser saveDialogue = new JFileChooser();
            saveDialogue.setCurrentDirectory(new File(System.getProperty("user.dir")));
            saveDialogue.setAcceptAllFileFilterUsed(false);
            saveDialogue.setFileFilter(extFilter);
            int retrival = saveDialogue.showSaveDialog(frame);
            if (retrival == JFileChooser.APPROVE_OPTION) {
                try {
                    FileOutputStream out = new FileOutputStream(saveDialogue.getSelectedFile() + ".bwl");
                    ObjectOutputStream objOut = new ObjectOutputStream(out);
                    objOut.writeObject(players);
                    objOut.close();
                    out.close();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(frame, "An error occurred whilst saving the file!");
                }
            }
        });

        loadGame.addActionListener((ActionEvent e) -> {
            JFileChooser loadDialogue = new JFileChooser();
            loadDialogue.setCurrentDirectory(new File(System.getProperty("user.dir")));
            loadDialogue.setDialogType(JFileChooser.OPEN_DIALOG);
            loadDialogue.setDialogTitle("Open File");
            loadDialogue.setApproveButtonText("Open");
            loadDialogue.setAcceptAllFileFilterUsed(false);
            loadDialogue.setFileFilter(extFilter);
            int retrival = loadDialogue.showOpenDialog(frame);
            if (retrival == JFileChooser.APPROVE_OPTION) {
                try {
                    FileInputStream in = new FileInputStream(loadDialogue.getSelectedFile());
                    ObjectInputStream objIn = new ObjectInputStream(in);
                    players = (ArrayList<Player>) objIn.readObject();
                    objIn.close();
                    in.close();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(frame, "An error occurred whilst loading the file!");
                }
            }
            refreshScreen();
        });

        close.addActionListener((ActionEvent e) -> {
            boolean confirmClose = JOptionPane.showConfirmDialog(frame, "Are you sure you wish to close the program?") == JOptionPane.OK_OPTION;
            if (confirmClose) {
                System.exit(0);
            }
        });

        about.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(frame, "System Created by Kieran Gateley\n" +
                    "\nStudent: U1755082");
        });

        file.add(playVsPlayer);
        file.add(playVsAi);
        file.add(aiVsAi);
        file.add(new JSeparator());
        file.add(saveGame);
        file.add(loadGame);
        file.add(new JSeparator());
        file.add(about);
        file.add(close);

        topMenu.add(file);
        frame.setJMenuBar(topMenu);
    }
}
