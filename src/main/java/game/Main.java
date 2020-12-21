package game;

import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {

  public static void main(String[] args) throws IOException {
    Game game = new Game();

    JFrame window = new JFrame("HAI KHÔNG BỐN TÁM");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // better to not use a file, but a class resource (can be packed in a jar)
    // window.setIconImage(ImageIO.read(new File("src/main/resources/drawable/2048.gif"))); // https://stackoverflow.com/a/18455062
    window.setIconImage(ImageIO.read(Main.class.getResourceAsStream("/images/2048.gif")));

    window.add(game);
    window.pack();

    window.setLocationRelativeTo(null); // center the window
    window.setResizable(false);
    window.setVisible(true);

    game.start();
  }

}