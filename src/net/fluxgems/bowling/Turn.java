package net.fluxgems.bowling;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Turn {

	private int firstScore = 0;
	private int secondScore = 0;
	private int extraScore = 0;
	private int cumulativeTotal = 0;
	private JLabel firstScoreField;
	private JLabel secondScoreField;
	private JLabel totalScoreLabel = new JLabel();
	private Main instance;
	private Player player;
	private boolean isSpare;
	private boolean isStrike;

	public Turn(Main instance, Player player) {
		firstScoreField = new JLabel("", SwingConstants.CENTER);
		secondScoreField = new JLabel("", SwingConstants.CENTER);
		totalScoreLabel = new JLabel("", SwingConstants.CENTER);
		isSpare = false;
		isStrike = false;
		this.player = player;
		this.instance = instance;
	}




    public Turn() {
    }

    public int getFirstScore() {
		return this.firstScore;
	}

	public void setFirstScore(int score) throws ValueException {
		if (score < 0 || score > 10 ) { throw new ValueException("Scores out of Range"); }
		if (score + secondScore > 10) { throw new ValueException("Scores out of range"); }
		this.firstScore = score;
		if (score == 0) {
			firstScoreField.setText("-");
		} else if (score == 10) {
			firstScoreField.setText("X");
			secondScore = 0;
			secondScoreField.setText("-");
			isStrike = true;
		} else {
		    firstScoreField.setText("" + firstScore);
        }
	}

	public int getSecondScore() {
		return this.secondScore;
	}

	public void setSecondScore(int score) throws ValueException {
		if ((score < 0 || score > 10) || firstScore + score > 10) {
			throw new ValueException("Scores out of range");
		}
        this.secondScore = score;
		if (firstScore + secondScore == 10 && firstScore != 10) {
            secondScoreField.setText("/");
            isSpare = true;
        } else if (secondScore == 0) {
		    secondScoreField.setText("-");
        } else {
		    secondScoreField.setText("" + secondScore);
        }
	}

	public int getCumulativeTotal() { return cumulativeTotal; }

	public void setCumulativeTotal(int i) {
		this.cumulativeTotal = i;
	}

	public JPanel getPanel() {
		JPanel main = new JPanel(new GridLayout(2,1));
		JPanel top = new JPanel(new GridLayout(1,2));

		firstScoreField.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
		secondScoreField.setBorder(BorderFactory.createMatteBorder(0,1,1,0,Color.BLACK));

		top.add(firstScoreField);
		top.add(secondScoreField);
		main.add(top);
		main.add(totalScoreLabel);

		return main;
	}

	public boolean isSpare() {
		return this.isSpare;
	}

	public boolean isStrike() {
		return this.isStrike;
	}

	public void setExtraScore(int extraScore) {
	    this.extraScore = extraScore;
    }

    public int getExtraScore() { return this.extraScore; }

    public void refresh() {
	    int value = 0;
	    value += cumulativeTotal;
	    value += firstScore;
	    value += secondScore;
	    value += extraScore;
        totalScoreLabel.setText("" + (value));
    }
    
    public int getTotalScore() {
        return firstScore + secondScore + extraScore;
    }

}