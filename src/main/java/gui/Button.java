package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;

import game.Game;
import util.AudioManager;
import util.DrawUtils;

@SuppressWarnings("serial")
public class Button extends JButton {

  private State currentState = State.RELEASED;
  private Rectangle clickBox;
  private ArrayList<ActionListener> actionListeners;
  private String text = ""; // button text

  private Color released;
  private Color hover;
  private Color pressed;
  private Font font = Game.PRIMARY_FONT_PLAIN.deriveFont(22f);
  private AudioManager audio;

  public Button(int x, int y, int width, int height) {
    clickBox = new Rectangle(x, y, width, height);
    actionListeners = new ArrayList<ActionListener>();
    audio = new AudioManager();

    released = new Color(61, 41, 99);
    hover = new Color(211, 56, 106);
    pressed = new Color(211, 56, 106);
  }

  public void update() {

  }

  public void render(Graphics2D g) {
    // color the button based on its state
    if (currentState == State.RELEASED) {
      g.setColor(released);
      g.fill(clickBox);
    } else if (currentState == State.HOVER) {
      g.setColor(hover);
      g.fill(clickBox);
    } else if (currentState == State.PRESSED) {
      g.setColor(pressed);
      g.fill(clickBox);
    }

    // button text's color
    g.setColor(new Color(223, 223, 223));
    // center the text of the button
    final int w = DrawUtils.getMessageWidth(text, font, g);
    final int h = DrawUtils.getMessageHeight(text, font, g);
    g.drawString(text, clickBox.x + (clickBox.width - w) / 2, clickBox.y + (clickBox.height - h) / 2);
  }

  public void addActionListener(ActionListener listener) {
    actionListeners.add(listener);
  }

  public void mousePressed(MouseEvent e) {
    if (clickBox.contains(e.getPoint())) {
      // if the mouse is on the button
      currentState = State.PRESSED;
    }
  }

  public void mouseReleased(MouseEvent e) {
    if (clickBox.contains(e.getPoint())) {
      for (ActionListener al: actionListeners) {
        al.actionPerformed(null); 
      }
      
      audio.play("button-select", 0);
    }
    currentState = State.RELEASED;
  }

  public void mouseDragged(MouseEvent e) {
    if (clickBox.contains(e.getPoint())) {
      currentState = State.PRESSED;
    } else {
      currentState = State.RELEASED;
    }
  }

  public void mouseMoved(MouseEvent e) {
    if (clickBox.contains(e.getPoint())) {
      currentState = State.HOVER;
    } else {
      currentState = State.RELEASED;
    }
  }

  public int getX() {
    return clickBox.x;
  }

  public int getY() {
    return clickBox.y;
  }

  public int getWidth() {
    return clickBox.width;
  }

  public int getHeight() {
    return clickBox.height;
  }

  public void setText(String text) {
    this.text = text;
  }

  private enum State {
    RELEASED, HOVER, PRESSED
  }

}
