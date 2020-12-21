package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import util.DrawUtils;

public class Tile {

  public static final int TILE_SIZE = 107;
  public static final int SLIDE_SPEED = 40;
  public static final int TILE_BORDER_RADIUS = 14;

  private int value;
  private BufferedImage tileImage;
  private Color tileBG;
  private Color tileText;
  private Font tileFont;
  private Point slideTo;
  private int x;
  private int y;

  private boolean canMerge = true;

  /**
   * @param value value shown on the tile
   * @param x     x coordinate of the tile
   * @param y     y coordinate of the tile
   */
  public Tile(int value, int x, int y) {
    this.value = value;
    this.x = x;
    this.y = y;
    slideTo = new Point(x, y);
    tileImage = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB); // ARGB for transparent pixels
    drawImage();
  }

  private void drawImage() {
    Graphics2D g = (Graphics2D) tileImage.getGraphics();

    switch (value) {
      // draw tile based on its value
      case 2:
        tileBG = new Color(65, 201, 184);
        tileText = new Color(223, 223, 223);
        break;
      case 4:
        tileBG = new Color(0, 178, 161);
        tileText = new Color(223, 223, 223);
        break;
      case 8:
        tileBG = new Color(237, 84, 113);
        tileText = new Color(223, 223, 223);
        break;
      case 16:
        tileBG = new Color(227, 60, 113);
        tileText = new Color(223, 223, 223);
        break;
      case 32:
        tileBG = new Color(184, 28, 100);
        tileText = new Color(223, 223, 223);
        break;
      case 64:
        tileBG = new Color(158, 0, 93);
        tileText = new Color(223, 223, 223);
        break;
      case 128:
        tileBG = new Color(255, 179, 60);
        tileText = new Color(223, 223, 223);
        break;
      case 256:
        tileBG = new Color(246, 133, 48);
        tileText = new Color(223, 223, 223);
        break;
      case 512:
        tileBG = new Color(232, 89, 56);
        tileText = new Color(223, 223, 223);
        break;
      case 1024:
        tileBG = new Color(52, 27, 90);
        tileText = new Color(223, 223, 223);
        break;
      case 2048:
        tileBG = new Color(37, 20, 64);
        tileText = new Color(223, 223, 223);
        break;
      default:
        tileBG = new Color(37, 20, 64);
        tileText = new Color(223, 223, 223);
        break;
    }

    g.setColor(new Color(0, 0, 0, 0));
    g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);

    g.setColor(tileBG);
    g.fillRoundRect(0, 0, TILE_SIZE, TILE_SIZE, TILE_BORDER_RADIUS, TILE_BORDER_RADIUS);

    g.setColor(tileText);

    tileFont = (value <= 64) ? Game.PRIMARY_FONT_PLAIN.deriveFont(36f) : Game.PRIMARY_FONT_PLAIN; // resize the font for 2-digit value
    g.setFont(tileFont);

    // center the value on the tile
    g.drawString("" + value, (TILE_SIZE - DrawUtils.getMessageWidth("" + value, tileFont, g)) / 2, (TILE_SIZE - DrawUtils.getMessageHeight("" + value, tileFont, g)) / 2);

    g.dispose();
  }

  public void update() {
    
  }

  public void render(Graphics2D g) {
    g.drawImage(tileImage, x, y, null);
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
    drawImage();
  }

  public Point getSlideTo() {
    return slideTo;
  }

  public void setSlideTo(Point slideTo) {
    this.slideTo = slideTo;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public boolean canMerge() {
    return canMerge;
  }

  public void setCanMerge(boolean canMerge) {
    this.canMerge = canMerge;
  }

}
