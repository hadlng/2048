package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import game.Game;
import game.Leaderboard;
import util.DrawUtils;

@SuppressWarnings("serial")
public class LeaderboardPanel extends Panel {
  
  private Leaderboard lBoard;
  private final int MARGIN = 20;
  private final int BUTTON_WIDTH = 140;
  private final int BACK_BUTTON_WIDTH = BUTTON_WIDTH * 3 + MARGIN * 2;
  private final int BUTTON_HEIGHT = 60;
  private final int BUTTON_Y = 160;
  private final int LEADERBOARD_X = 200;
  private final int LEADERBOARD_Y = BUTTON_Y + BUTTON_HEIGHT + 120;

  private String title = "BẢNG XẾP HẠNG";
  private Font titleFont = Game.PRIMARY_FONT_BOLD.deriveFont(48f);
  private Font scoreFont = Game.PRIMARY_FONT_PLAIN.deriveFont(30f);
  private State currentState = State.SCORE;

  public LeaderboardPanel(){
		super();
		lBoard = Leaderboard.getInstance();
		lBoard.loadScores();
    
    Button tileButton = new Button(Game.WIDTH / 2 - BUTTON_WIDTH / 2, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    tileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        currentState = State.TILE;
      }
    });
    tileButton.setText("GIÁ TRỊ");
    add(tileButton);

    Button scoreButton = new Button(Game.WIDTH / 2 - BUTTON_WIDTH / 2 - tileButton.getWidth() - MARGIN, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
    scoreButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        currentState = State.SCORE;
      }
    });
    scoreButton.setText("ĐIỂM");
    add(scoreButton);
		
		Button timeButton = new Button(Game.WIDTH / 2 + BUTTON_WIDTH / 2 + MARGIN, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		timeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentState = State.TIME;
			}
		});
		timeButton.setText("THỜI GIAN");
		add(timeButton);
		
		Button backButton = new Button(Game.WIDTH / 2 - BACK_BUTTON_WIDTH / 2, 600, BACK_BUTTON_WIDTH, BUTTON_HEIGHT);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Screen.getInstance().setCurrentPanel("Main Menu");
			}
		});
		backButton.setText("TRỞ VỀ MÀN HÌNH CHÍNH");
		add(backButton);
	}

  private void drawLeaderboards(Graphics2D g) {
    ArrayList<String> strings = new ArrayList<String>();
    if (currentState == State.SCORE) {
      strings = convertToStrings(lBoard.getTopScores());
    } else if (currentState == State.TILE) {
      strings = convertToStrings(lBoard.getTopTiles());
    } else {
      for (Long l : lBoard.getTopTimes()) {
        strings.add(DrawUtils.formatTime(l));
      }
    }

    g.setColor(new Color(223, 223, 223));
    g.setFont(scoreFont);

    for (int i = 0; i < strings.size(); i++) {
      String s = (i + 1) + ". " + strings.get(i);
      g.drawString(s, LEADERBOARD_X, LEADERBOARD_Y + i * 40);
    }
  }

  private ArrayList<String> convertToStrings(ArrayList<? extends Number> list) {
    ArrayList<String> res = new ArrayList<String>();
    for (Number n : list) {
      res.add(n.toString());
    }
    return res;
  }

  @Override
  public void update() {

  }

  @Override
  public void render(Graphics2D g) {
    super.render(g);

    g.setColor(new Color(223, 223, 223));
    g.drawString(title, (Game.WIDTH - DrawUtils.getMessageWidth(title, titleFont, g)) / 2, 100);

    drawLeaderboards(g);
  }

  private enum State {
    SCORE, TILE, TIME
  }

}
