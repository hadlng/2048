package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.Game;
import util.DrawUtils;

@SuppressWarnings("serial")
public class MainMenuPanel extends Panel {

  private Font titleFont = Game.PRIMARY_FONT_BOLD.deriveFont(130f);
  private String title = "2048";
  private final int BUTTON_WIDTH = 220;
  private final int BUTTON_HEIGHT = 60;
  private final int MARGIN = 90;

  
  public MainMenuPanel() {
    super();

    Button playButton = new Button(Game.WIDTH / 2 - BUTTON_WIDTH / 2, 350, BUTTON_WIDTH, BUTTON_HEIGHT);
    playButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Screen.getInstance().setCurrentPanel("Play");
      }
    });
    playButton.setText("BẮT ĐẦU");
    add(playButton);

    Button leaderboardButton = new Button(Game.WIDTH / 2 - BUTTON_WIDTH / 2, playButton.getY() + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
    leaderboardButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Screen.getInstance().setCurrentPanel("Leaderboard");
      }
    });
    leaderboardButton.setText("BẢNG XẾP HẠNG");
    add(leaderboardButton);

    Button quitButton = new Button(Game.WIDTH / 2 - BUTTON_WIDTH / 2, leaderboardButton.getY() + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT);
    quitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0); // return 0 ==> exit successfully
      }
    });
    quitButton.setText("THOÁT");
    add(quitButton);
  }

  @Override
  public void render(Graphics2D g) {
    super.render(g); // render all the buttons

    g.setColor(new Color(223, 223, 223));
    g.drawString(title, (Game.WIDTH - DrawUtils.getMessageWidth(title, titleFont, g)) / 2, 220);
  }

}
