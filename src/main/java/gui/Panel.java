package gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Panel extends JPanel {

  private ArrayList<Button> buttons;

  public Panel() {
    buttons = new ArrayList<Button>(); // array list of all the buttons
  }

  public void update() {
    // not using for-each with ArrayList because it might cause "ConcurrentModificationException"

    // for (Button b: buttons) {
    //   b.update();
    // }
    for (int i = 0; i < buttons.size(); i++) {
      Button b = buttons.get(i);
      b.update();
    }
  }

  public void render(Graphics2D g) {
    // for (Button b: buttons) {
    //   b.render(g);
    // }
    for (int i = 0; i < buttons.size(); i++) {
      Button b = buttons.get(i);
      b.render(g);
    }
  }

  public void add(Button button) {
    buttons.add(button);
  }

  public void remove(Button button) {
    buttons.remove(button);
  }

  public void mousePressed(MouseEvent e) {
    // for (Button b : buttons) {
    //   b.mousePressed(e);
    // }
    for (int i = 0; i < buttons.size(); i++) {
      Button b = buttons.get(i);
      b.mousePressed(e);
    }
  }

  public void mouseReleased(MouseEvent e) {
    // for (Button b : buttons) {
    //   b.mouseReleased(e);
    // }
    for (int i = 0; i < buttons.size(); i++) {
      Button b = buttons.get(i);
      b.mouseReleased(e);
    }
  }

  public void mouseDragged(MouseEvent e) {
    // for (Button b : buttons) {
    //   b.mouseDragged(e);
    // }
    for (int i = 0; i < buttons.size(); i++) {
      Button b = buttons.get(i);
      b.mouseDragged(e);
    }
  }

  public void mouseMoved(MouseEvent e) {
    // for (Button b : buttons) {
    //   b.mouseMoved(e);
    // }
    for (int i = 0; i < buttons.size(); i++) {
      Button b = buttons.get(i);
      b.mouseMoved(e);
    }
  }

}
