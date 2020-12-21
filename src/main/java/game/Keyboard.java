package game;

import java.awt.event.KeyEvent;

public class Keyboard {

  // distinguish between typed events and pressed events
  public static boolean[] pressed = new boolean[256];
  public static boolean[] prev = new boolean[256];

  private Keyboard() {

  }

  public static void update() {
    // arrow keys
    prev[KeyEvent.VK_LEFT] = pressed[KeyEvent.VK_LEFT];
    prev[KeyEvent.VK_RIGHT] = pressed[KeyEvent.VK_RIGHT];
    prev[KeyEvent.VK_UP] = pressed[KeyEvent.VK_UP];
    prev[KeyEvent.VK_DOWN] = pressed[KeyEvent.VK_DOWN];

    // WASD keys
    prev[KeyEvent.VK_A] = pressed[KeyEvent.VK_A];
    prev[KeyEvent.VK_D] = pressed[KeyEvent.VK_D];
    prev[KeyEvent.VK_W] = pressed[KeyEvent.VK_W];
    prev[KeyEvent.VK_S] = pressed[KeyEvent.VK_S];
  }

  public static void keyPressed(KeyEvent e) {
    pressed[e.getKeyCode()] = true;
  }

  public static void keyReleased(KeyEvent e) {
    pressed[e.getKeyCode()] = false;
  }

  public static boolean typed(int keyEvent) {
    return !pressed[keyEvent] && prev[keyEvent];
  } 

}
