package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

import game.Game;

@SuppressWarnings("serial")
public class Toast extends JFrame {

  String message;
  JWindow window;
  private Font font = Game.PRIMARY_FONT_PLAIN.deriveFont(18f);
  private float alpha = 0.3f;
  private int duration = 2000;

  public Toast(String message) {
    window = new JWindow();
    window.setBackground(new Color(0, 0, 0, alpha));

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
        drawString(g, message, 25, 27);
      }
    };

    window.add(panel);
    window.setSize(Game.WIDTH - 100, 100);
    window.setAlwaysOnTop(true);
    window.setLocationRelativeTo(null);
  }

  public void showToast() {
    try {
      window.setOpacity(1);
      window.setVisible(true);

      // wait for some time then remove toast
      Thread.sleep(duration);
      window.setVisible(false);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

}
