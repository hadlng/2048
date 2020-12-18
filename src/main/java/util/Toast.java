package main.java.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

import main.java.game.Game;

@SuppressWarnings("serial")
public class Toast extends JFrame {

  String str;
  JWindow window;
  private Font font = Game.PRIMARY_FONT_PLAIN.deriveFont(18f);

  public Toast(String str, int x, int y) {
    window = new JWindow();
    window.setBackground(new Color(0, 0, 0, 0)); // make the background transparent

    JPanel panel = new JPanel() {
      // default `drawString` method doesn't handle new lines
      // https://stackoverflow.com/a/4413153
      private void drawString(Graphics g, String str, int x, int y) {
        for (String line: str.split("\n")) {
          g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }
      }

      @Override
      public void paintComponent(Graphics g) {
        g.setFont(font);
        g.setColor(new Color(223, 223, 223));
        drawString(g, str, 25, 27);
      }
    };

    window.add(panel);
    window.setLocation(x, y);
    window.setSize(Game.WIDTH, 100);
  }

  public void showToast() {
    try {
      window.setOpacity(1);
      window.setVisible(true);

      // wait for 2 seconds then remove toast
      Thread.sleep(2000);
      window.setVisible(false);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

}
