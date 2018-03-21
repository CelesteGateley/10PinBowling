package net.fluxgems.bowling;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Player {

	private String name;
	private ArrayList<Turn> turns;
	private Main instance;
	private boolean isAI;


	public Player(String name, Main instance, boolean isAI) {
	    this.name = name;
	    this.instance = instance;
	    this.isAI = isAI;

	    turns = new ArrayList<>(10);
    }

    public String getName() {return this.name;}


	public JPanel getPanel(boolean isLast) {
		JPanel tmp = new JPanel(new GridLayout(1,turns.size()+1));
		tmp.setPreferredSize(new Dimension(1300, 100));
		tmp.add(new JLabel(name));
		for (Turn t : turns) {
			JPanel p = t.getPanel();
			p.setBorder(BorderFactory.createMatteBorder(0,1,0,0,Color.BLACK));
			tmp.add(p);
		}
		if (isLast) {
            tmp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        } else {
		    tmp.setBorder(BorderFactory.createMatteBorder(1,1,0,1,Color.BLACK));
        }
		return tmp;

	}

	public void updateTurns() {
		int total = 0;
		Turn sndToLastTurn = null;
		Turn previousTurn = null;
		for (Turn t : turns) {

            if (t instanceof TurnTen) {
                if (sndToLastTurn.isStrike() && previousTurn.isStrike()) {
                    sndToLastTurn.setExtraScore(previousTurn.getFirstScore() + t.getFirstScore());
                } else if (sndToLastTurn.isStrike() && !previousTurn.isStrike()) {
                    sndToLastTurn.setExtraScore(previousTurn.getFirstScore() + previousTurn.getSecondScore());
                } else if (sndToLastTurn.isSpare()) {
                    sndToLastTurn.setExtraScore(previousTurn.getFirstScore());
                }
                if (previousTurn.isStrike()) {
                    previousTurn.setExtraScore(t.getFirstScore() + t.getSecondScore());
                } else if (previousTurn.isSpare()) {
                    previousTurn.setExtraScore(t.getFirstScore());
                }
                previousTurn.setCumulativeTotal(previousTurn.getCumulativeTotal() + previousTurn.getExtraScore());
                ((TurnTen) t).updateExtraScore();
            } else if (previousTurn != null) {
                if (sndToLastTurn == null) {
                    if (previousTurn.isStrike() && !t.isStrike()) {
                        previousTurn.setExtraScore(t.getFirstScore() + t.getSecondScore());
                    } else if (previousTurn.isSpare()) {
                        previousTurn.setExtraScore(t.getFirstScore());
                    }
                } else {
                    if (previousTurn.isStrike() && !t.isStrike()) {
                        previousTurn.setExtraScore(t.getFirstScore() + t.getSecondScore());
                    } else if (previousTurn.isSpare()) {
                        previousTurn.setExtraScore(t.getFirstScore());
                    } else if (sndToLastTurn.isStrike() && previousTurn.isStrike()) {
                        sndToLastTurn.setExtraScore(previousTurn.getFirstScore() + t.getFirstScore());
                    }
                }
            }

            t.setCumulativeTotal(total);
            total += t.getTotalScore();
            if (sndToLastTurn != null) { sndToLastTurn.refresh(); }
            if (previousTurn != null) { previousTurn.refresh(); }
            sndToLastTurn = previousTurn;
            previousTurn = t;
            t.refresh();
		}

		instance.refreshScreen();

	}

	public int getFinalScore() {
	    TurnTen t = (TurnTen) turns.get(turns.size()-1);
	    return t.getTotalScore() + t.getCumulativeTotal();
    }

    public int turnsSize() {
	    return turns.size();
    }

	public void playFrame() {
	    if (!isAI) {
            boolean complete = false;
            while (!complete) {
                try {
                    if (turns.size() < 9) {
                        Turn t = new Turn(this.instance, this);
                        int score1 = Integer.parseInt(JOptionPane.showInputDialog("What is the first score?"));
                        int score2;
                        if (score1 == 10) {
                            score2 = 0;
                        } else {
                            score2 = Integer.parseInt(JOptionPane.showInputDialog("What is the second score?"));
                        }
                        t.setFirstScore(score1);
                        t.setSecondScore(score2);
                        turns.add(t);
                        complete = true;
                    } else if (turns.size() == 9) {
                        TurnTen t = new TurnTen(this.instance, this);
                        int score1 = Integer.parseInt(JOptionPane.showInputDialog("What is the first score?"));
                        int score2 = Integer.parseInt(JOptionPane.showInputDialog("What is the second score?"));
                        t.setFirstScore(score1);
                        t.setSecondScore(score2);
                        if ((score1 != 10 && score1 + score2 < 10)) {
                            t.setThirdScore(0);
                        } else if (score1 == 10 || (score1 + score2 == 10)) {
                            int score3 = Integer.parseInt(JOptionPane.showInputDialog("What is the third score?"));
                            t.setThirdScore(score3);
                        }
                        turns.add(t);
                        complete = true;
                    } else {
                        JOptionPane.showMessageDialog(instance.frame, "No More Turns can be Played!");
                        complete = true;
                    }
                } catch (ValueException | NumberFormatException e) {
                    JOptionPane.showMessageDialog(instance.frame, "One or More of those scores were incorrect, please try again!");
                }
            }
        } else {
            if (turns.size() < 9) {
                Turn t = new Turn(this.instance, this);
                int score1 = getRandomInRange(0, 10);
                int score2 = getRandomInRange(0, 10 - score1);
                t.setFirstScore(score1);
                t.setSecondScore(score2);
                turns.add(t);
            } else if (turns.size() == 9) {
                TurnTen t = new TurnTen(this.instance, this);
                int score1 = getRandomInRange(0, 10);
                int score2;
                if (score1 == 10) {
                    score2 = getRandomInRange(0, 10);
                } else {
                    score2 = getRandomInRange(0, 10 - score1);
                }
                t.setFirstScore(score1);
                t.setSecondScore(score2);
                if (score1 + score2 < 10) {
                    System.out.println("Option 1");
                    t.setThirdScore(0);
                } else if (score1 == 10 && score2 < 10) {
                    System.out.println("Option 2");
                    t.setThirdScore(getRandomInRange(0, 10 - score2));
                } else if (score1 == 10 && score2 == 10) {
                    System.out.println("Option 3");
                    t.setThirdScore(getRandomInRange(0, 10));
                } else {
                    t.setThirdScore(0);
                }
                turns.add(t);
            }
        }
        updateTurns();
    }

	private int getRandomInRange(int x, int y) {
	    return ThreadLocalRandom.current().nextInt(x, y+1);
    }


}