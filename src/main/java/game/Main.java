package main.java.game;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {

  public static void main(String[] args) throws IOException { 
    Game game = new Game();

    JFrame window = new JFrame("HAI KHÔNG BỐN TÁM");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setIconImage(ImageIO.read(new File("res/drawable/2048.gif"))); // https://stackoverflow.com/a/18455062

    window.add(game);
    window.pack();

    window.setLocationRelativeTo(null); // center the window
    window.setResizable(false);
    window.setVisible(true);

    game.start();
  }

}
