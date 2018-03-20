package net.fluxgems.bowling;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TurnTen extends Turn {

	private int firstScore;
	private int secondScore;
    private int thirdScore;
    private int cumulativeTotal;
    private int extraScore;

	private JLabel firstScoreField;
	private JLabel secondScoreField;
	private JLabel thirdScoreField;
	private JLabel totalScoreLabel;

	private Main instance;
    private Player player;

	TurnTen(Main instance, Player player) {
        firstScoreField = new JLabel("", SwingConstants.CENTER);
        secondScoreField = new JLabel("", SwingConstants.CENTER);
        thirdScoreField = new JLabel("", SwingConstants.CENTER);
        totalScoreLabel = new JLabel("", SwingConstants.CENTER);
        this.player = player;
        this.instance = instance;

    }

    @Override
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
        } else {
            firstScoreField.setText("" + firstScore);
        }
    }

    @Override
    public void setSecondScore(int score) throws ValueException {
        if ((score < 0 || score > 10) || (firstScore + score > 10 && firstScore < 10)) {
            throw new ValueException("Scores out of range");
        }
        this.secondScore = score;
        if (firstScore + secondScore == 10 && firstScore != 10) {
            secondScoreField.setText("/");
        } else if (secondScore == 0) {
            secondScoreField.setText("-");
        } else if (firstScore == 10 && secondScore == 10) {
            secondScoreField.setText("X");
        } else {
            secondScoreField.setText("" + secondScore);
        }
    }


	public int getThirdScore() {
		return thirdScore;
	}

	public void setThirdScore(int score) throws ValueException {
        if ((score < 0 || score > 10) || (firstScore + secondScore < 10 && score != 0) ||
                ((firstScore == 10 && secondScore < 10) && secondScore + thirdScore > 10)) {
            throw new ValueException("Score out of Range");
        }
        this.thirdScore = score;
        if (secondScore + score == 10) {
            thirdScoreField.setText("/");
        } else if (firstScore == 10 && secondScore == 10 && score == 10) {
            thirdScoreField.setText("X");
        } else if (score == 0) {
            thirdScoreField.setText("-");
        } else {
            thirdScoreField.setText("" + thirdScore);
        }
	}

	@Override
	public JPanel getPanel() {
		JPanel tmp = new JPanel(new GridLayout(2,1));
		JPanel top = new JPanel(new GridLayout(1,3));

        firstScoreField.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK));
        secondScoreField.setBorder(BorderFactory.createMatteBorder(0,1,1,0,Color.BLACK));
        thirdScoreField.setBorder(BorderFactory.createMatteBorder(0,1,1,1,Color.BLACK));

		top.add(firstScoreField);
		top.add(secondScoreField);
		top.add(thirdScoreField);

		tmp.add(top);
		tmp.add(totalScoreLabel);

		return tmp;
	}
    
    @Override
    public int getTotalScore() {
        return firstScore + secondScore + thirdScore + extraScore;
    }

    public int getExtraScore() {
        return extraScore;
    }

    @Override
    public void setExtraScore(int extraScore) {
        this.extraScore = extraScore;
    }

    public void updateExtraScore() {
	    if (firstScore == 10 && secondScore == 10) {
	        extraScore = secondScore + thirdScore*2;
        } else if (firstScore == 10) {
	        extraScore = secondScore + thirdScore;
        } else if (firstScore + secondScore == 10) {
	        extraScore = thirdScore;
        }
    }

    @Override
    public void refresh() {
        int value = 0;
        value += cumulativeTotal;
        value += firstScore;
        value += secondScore;
        value += thirdScore;
        value += extraScore;
        totalScoreLabel.setText("" + (value));
    }

    @Override
    public void setCumulativeTotal(int total) {
	    this.cumulativeTotal = total;
    }


    @Override
    public int getCumulativeTotal() {
	    return this.cumulativeTotal;
    }
}