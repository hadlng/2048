package main.java.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import main.java.gui.LeaderboardPanel;
import main.java.gui.MainMenuPanel;
import main.java.gui.PlayPanel;
import main.java.gui.Screen;

@SuppressWarnings("serial")
public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

  public static final int WIDTH = GameBoard.BOARD_WIDTH + GameBoard.MARGIN * 2;
  public static final int HEIGHT = GameBoard.BOARD_HEIGHT + GameBoard.MARGIN * 2 + 200;
  public static final Color BG_COLOR = new Color(87, 64, 124);
  public static final Font PRIMARY_FONT_PLAIN = new Font("Bebas Neue Regular", Font.PLAIN, 28);
  public static final Font PRIMARY_FONT_BOLD = new Font("Bebas Neue Regular", Font.BOLD, 28);

  private Thread game;
  private boolean isRunning;
  private BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

  private Screen screen;

  public Game() {
    setFocusable(true);
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    addKeyListener(this);
    addMouseListener(this);
    addMouseMotionListener(this);

    screen = Screen.getInstance();
    screen.add("Main Menu", new MainMenuPanel());
    screen.add("Play", new PlayPanel());
    screen.add("Leaderboard", new LeaderboardPanel());
    screen.setCurrentPanel("Main Menu");
  }

  private void update() {
    screen.update();
    Keyboard.update();
  }

  private void render() {
    Graphics2D g = (Graphics2D) img.getGraphics();
    g.setColor(BG_COLOR);
    g.fillRect(0, 0, WIDTH, HEIGHT);

    screen.render(g);
    g.dispose();

    Graphics2D g2d = (Graphics2D) getGraphics();
    g2d.drawImage(img, 0, 0, null);
    g2d.dispose();
  }

  @Override
  public void run() {
    int fps = 0, updates = 0;

    long fpsTimer = System.currentTimeMillis();
    double nsPerUpdate = 1000000000.0 / 60; // last update time in nanoseconds
    double then = System.nanoTime();
    double unprocessed = 0;

    while (isRunning) {
      boolean shouldRender = false;

      double now = System.nanoTime();
      unprocessed += (now - then) / nsPerUpdate;
      then = now;

      // update queue
      while (unprocessed >= 1) {
        // update
        updates++;
        update();
        unprocessed--;
        shouldRender = true;
      }

      // render
      if (shouldRender) {
        fps++;
        render();
        shouldRender = false;
      } else {
        try {
          Thread.sleep(1);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      // FPS timer
      if (System.currentTimeMillis() - fpsTimer > 1000) {
        System.out.printf("%d fps %d updates", fps, updates);
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b"); // clear screen
        fps = 0;
        updates = 0;
        fpsTimer += 1000;
      }
    }
  }

  public synchronized void start() {
    if (isRunning)
      return;
    isRunning = true;
    game = new Thread(this, "game");
    game.start();
  }

  public synchronized void stop() {
    if (!isRunning)
      return;
    isRunning = false;
    System.exit(0);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    screen.mouseDragged(e);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    screen.mouseMoved(e);
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    screen.mousePressed(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    screen.mouseReleased(e);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    Keyboard.keyPressed(e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    Keyboard.keyReleased(e);
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

}
