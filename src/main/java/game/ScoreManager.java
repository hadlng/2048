package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ScoreManager {

  // current score
  private int currentScore;
  private int currentTopScore;
  private long time;
  private long startingTime;
  private long bestTime;
  private int[] board = new int[GameBoard.BOARD_ROWS * GameBoard.BOARD_COLS];

  // file
  private String filePath;
  private String temp = "TEMP.tmp";
  private GameBoard gBoard; // copy of the game board

  private boolean newGame;

  public ScoreManager(GameBoard gBoard) {
    try {
      filePath = new File("").getAbsolutePath(); // get file location
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    this.gBoard = gBoard;
  }

  public void reset() {
    // reset the game
    File f = new File(filePath, temp);

    if (f.isFile()) {
      f.delete();
    }

    newGame = true;
    startingTime = 0;
    currentScore = 0;
    time = 0;
  }

  private void createFile() {
    FileWriter fw = null;
    newGame = true;

    try {
      File f = new File(filePath, temp);
      fw = new FileWriter(f);
      BufferedWriter bw = new BufferedWriter(fw);

      // set all to 0 at the beginning
      bw.write("" + 0); // current score
      bw.newLine();
      bw.write("" + 0); // current top score
      bw.newLine();
      bw.write("" + 0); // time
      bw.newLine();
      bw.write("" + 0); // best time
      bw.newLine();
      // the game board's current status
      for (int row = 0; row < GameBoard.BOARD_ROWS; row++) {
        for (int col = 0; col < GameBoard.BOARD_COLS; col++) {
          if (row == GameBoard.BOARD_ROWS - 1 && col == GameBoard.BOARD_COLS - 1) {
            bw.write("" + 0);
          } else {
            bw.write(0 + "-");
          }
        }
      }

      bw.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void saveGame() {
    FileWriter fw = null;

    if (newGame) {
      newGame = false;
    }

    try {
      File f = new File(filePath, temp);
      fw = new FileWriter(f);
      BufferedWriter bw = new BufferedWriter(fw);

      bw.write("" + currentScore);
      bw.newLine();
      bw.write("" + currentTopScore);
      bw.newLine();
      bw.write("" + time);
      bw.newLine();
      bw.write("" + bestTime);
      bw.newLine();
      for (int row = 0; row < GameBoard.BOARD_ROWS; row++) {
        for (int col = 0; col < GameBoard.BOARD_COLS; col++) {
          if (gBoard.getBoard()[row][col] != null) {
            this.board[row * GameBoard.BOARD_COLS + col] = gBoard.getBoard()[row][col].getValue();
          } else {
            this.board[row * GameBoard.BOARD_COLS + col] = 0;
          }
          if (row == GameBoard.BOARD_ROWS - 1 && col == GameBoard.BOARD_COLS - 1) {
            bw.write("" + board[row * GameBoard.BOARD_COLS + col]);
          } else {
            bw.write(board[row * GameBoard.BOARD_COLS + col] + "-");
          }
        }
      }

      bw.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void loadGame() {
    try {
      File f = new File(filePath, temp);

      if (!f.isFile()) {
        createFile();
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      currentScore = Integer.parseInt(br.readLine());
      currentTopScore = Integer.parseInt(br.readLine());
      time = Long.parseLong(br.readLine());
      startingTime = time;
      bestTime = Long.parseLong(br.readLine());

      String[] board = br.readLine().split("-");
      for (int i = 0; i < board.length; i++) {
        this.board[i] = Integer.parseInt(board[i]);
      }

      br.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public int getCurrentScore() {
    return currentScore;
  }

  public void setCurrentScore(int currentScore) {
    this.currentScore = currentScore;
  }

  public int getCurrentTopScore() {
    return currentTopScore;
  }

  public void setCurrentTopScore(int currentTopScore) {
    this.currentTopScore = currentTopScore;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time + startingTime;
  }

  public long getBestTime() {
    return bestTime;
  }

  public void setBestTime(long bestTime) {
    this.bestTime = bestTime;
  }

  public boolean newGame() {
    return newGame;
  }

  public int[] getBoard() {
    return board;
  }

}
