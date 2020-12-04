package main.java.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.java.game.Game;
import main.java.game.GameBoard;
import main.java.game.ScoreManager;
import main.java.util.AudioManager;
import main.java.util.DrawUtils;

@SuppressWarnings("serial")
public class PlayPanel extends Panel {

  private GameBoard board;
  private BufferedImage stats;
  private ScoreManager scores;
  private Font scoreFont = Game.PRIMARY_FONT_PLAIN.deriveFont(22f);
  private Font messageFont = Game.PRIMARY_FONT_BOLD.deriveFont(70f);

  private String timeF; // formatted string
  
  private Button pauseButton;
  private Button resumeButton;
  private Button resetButton;
  private Button screenshotButton;
  private Button mainMenuButton;

  private AudioManager audio;

  private final int MARGIN = GameBoard.MARGIN;
  private final int SM_BUTTON_WIDTH = 200;
  private final int LG_BUTTON_WIDTH = SM_BUTTON_WIDTH * 2 + MARGIN;
  private final int BUTTON_HEIGHT = 60;

  private boolean addedGameOver, addedPaused;
  private int alpha = 0; // alpha parameter which specifies the opacity for a color
  private boolean screenshot;

  public PlayPanel() {
    board = new GameBoard((Game.WIDTH - GameBoard.BOARD_WIDTH) / 2, Game.HEIGHT - GameBoard.BOARD_HEIGHT - MARGIN * 2);
    scores = board.getScores();
    stats = new BufferedImage(Game.WIDTH, 200, BufferedImage.TYPE_INT_RGB);
    audio = new AudioManager();

    pauseButton = new Button(Game.WIDTH - MARGIN * 2 - 60, 30, 60, 60);
    pauseButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        board.setPaused(true);

        if (board.getPauseTime() == 0) {
          board.setPauseTime(System.nanoTime());
        }
      }
    });
    pauseButton.setText("☰");
    add(pauseButton);

    mainMenuButton = new Button((Game.WIDTH - LG_BUTTON_WIDTH) / 2, 450, LG_BUTTON_WIDTH, BUTTON_HEIGHT);
    mainMenuButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Screen.getInstance().setCurrentPanel("Main Menu");
      }
    });
    mainMenuButton.setText("TRỞ VỀ MÀN HÌNH CHÍNH");

    resetButton = new Button(mainMenuButton.getX(), mainMenuButton.getY() - MARGIN - BUTTON_HEIGHT, SM_BUTTON_WIDTH, BUTTON_HEIGHT);
    resetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        board.getScores().reset();
        board.reset();
        alpha = 0;

        remove(resetButton);
        remove(resumeButton);
        remove(screenshotButton);
        remove(mainMenuButton);
        
        board.setPaused(false);

        addedGameOver = false;
        addedPaused = false;

      }
    });
    resetButton.setText("CHƠI MỚI");

    resumeButton = new Button(resetButton.getX() + resetButton.getWidth() + MARGIN, resetButton.getY(), SM_BUTTON_WIDTH, BUTTON_HEIGHT);
    resumeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        board.setPaused(false);
        alpha = 0;

        remove(resetButton);
        remove(resumeButton);
        remove(mainMenuButton);

        if (board.getPauseTime() != 0) {
          long stopTime = System.nanoTime() - board.getPauseTime();
          board.setAdditionalTime(board.getAdditionalTime() + stopTime);
          board.setPauseTime(0);
        }

        addedPaused = false;
      }
    });
    resumeButton.setText("TIẾP TỤC");

    screenshotButton = new Button(resetButton.getX() + resetButton.getWidth() + MARGIN, resetButton.getY(), SM_BUTTON_WIDTH, BUTTON_HEIGHT);
    screenshotButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        screenshot = true;
      }
    });
    screenshotButton.setText("CHỤP MÀN HÌNH");
  }

  private void drawGUI(Graphics2D g) {
    // format the time
    timeF = DrawUtils.formatTime(scores.getTime());

    // draw GUI
    Graphics2D g2d = (Graphics2D) stats.getGraphics();
    g2d.setColor(new Color(87, 64, 124));
    g2d.fillRect(0, 0, stats.getWidth(), stats.getHeight());
    g2d.setColor(new Color(223, 223, 223));
    g2d.setFont(scoreFont);
    g2d.drawString("ĐIỂM: " + scores.getCurrentScore(), MARGIN * 2, 50);
    g2d.drawString("KỶ LỤC: " + scores.getCurrentTopScore(), MARGIN * 2, 100);
    g2d.drawString("THỜI GIAN: " + timeF, MARGIN * 2, 150);
    g2d.dispose();

    g.drawImage(stats, 0, 0, null);
  }

  public void drawGameOver(Graphics2D g) {
    g.setColor(new Color(61, 41, 99, alpha));
    g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
    g.setColor(new Color(233, 233, 233));
    g.drawString("THUA RỒI!", (Game.WIDTH - DrawUtils.getMessageWidth("THUA RỒI!", messageFont, g)) / 2, 250);
  }

  public void drawPaused(Graphics2D g) {
    g.setColor(new Color(61, 41, 99, alpha));
    g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
    g.setColor(new Color(233, 233, 233));
    g.drawString("TẠM DỪNG", (Game.WIDTH - DrawUtils.getMessageWidth("TẠM DỪNG", messageFont, g)) / 2, 250);
  }

  public void update() {
    board.update();
    if (board.isDead() || board.isPaused()) {
      alpha++;
      if (alpha > 170) {
        alpha = 170;
      }
    }
  }

  public void render(Graphics2D g) {
    drawGUI(g);
    board.render(g);

    if (screenshot) {
      // create a "fake" screenshot by creating a BufferedImage and turning into a JPEG file with random name
      BufferedImage bi = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = (Graphics2D) bi.getGraphics();
      g2d.setColor(new Color(87, 64, 124));
      g2d.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
      drawGUI(g2d);
      board.render(g2d);
      try {
        ImageIO.write(bi, "jpg", new File("screenshot" + System.nanoTime() + ".jpg"));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      screenshot = false;
      
    }

    if (board.isDead()) {
      if (!addedGameOver) {
        addedGameOver = true;
        audio.play("game-over", 0);

        add(mainMenuButton);
        add(resetButton);
        add(screenshotButton);
      }
      drawGameOver(g);
    }

    if (board.isPaused()) {
      if (!addedPaused) {
        addedPaused = true;

        add(mainMenuButton);
        add(resetButton);
        add(resumeButton);
      }
      drawPaused(g);
    }

    super.render(g);
  }

}
