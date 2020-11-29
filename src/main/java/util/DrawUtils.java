package main.java.util;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class DrawUtils {

  final static Canvas c = new Canvas();
  
  public static int getMessageWidth(String message, Font font, Graphics2D g) {
    g.setFont(font);
    final FontMetrics fm = c.getFontMetrics(font);
    final int w = fm.stringWidth(message);
    return w;
  }

  public static int getMessageHeight(String message, Font font, Graphics2D g) {
    g.setFont(font);
    final FontMetrics fm = c.getFontMetrics(font);
    final int h = (int) fm.getLineMetrics(message, g).getBaselineOffsets()[2];
    return h;
  }

  public static String formatTime(long second) {
    String formattedTime = "";

    formattedTime = "" + second + "s";
    return formattedTime;
  }

}
