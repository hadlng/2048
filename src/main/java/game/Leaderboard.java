package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Leaderboard {

  private static Leaderboard lBoard;
  private String filePath;
  private String leaderboard = "Leaderboard.txt";

  // all time leaderboard
  private ArrayList<Integer> topScores;
  private ArrayList<Integer> topTiles;
  private ArrayList<Long> topTimes;

  private Leaderboard() {
    // read-only ==> private
    try {
      filePath = new File("").getAbsolutePath(); // get file location
      // System.out.println(filePath);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    topScores = new ArrayList<Integer>();
    topTiles = new ArrayList<Integer>();
    topTimes = new ArrayList<Long>();
  }

  public static Leaderboard getInstance() {
    if (lBoard == null) {
      lBoard = new Leaderboard();
    }
    return lBoard;
  }

  public void addScore(int score) {
    for (int i = 0; i < topScores.size(); i++) {
      if (score >= topScores.get(i)) {
        topScores.add(i, score); // add to leaderboard
        topScores.remove(topScores.size() - 1); // remove last one
        return; // only do this once
      }
    }

    while (topScores.size() > 5) {
      topScores.remove(topScores.size() - 1);
    }
  }

  public void addTile(int tileValue) {
    for (int i = 0; i < topTiles.size(); i++) {
      if (tileValue >= topTiles.get(i)) {
        topTiles.add(i, tileValue);
        topTiles.remove(topTiles.size() - 1);
        return;
      }
    }

    while (topTiles.size() > 5) {
      topTiles.remove(topTiles.size() - 1);
    }
  }

  public void addTime(long second) {
    for (int i = 0; i < topTimes.size(); i++) {
      if (second <= topTimes.get(i)) {
        topTimes.add(i, second);
        topTimes.remove(topTimes.size() - 1);
        return;
      }
    }

    while (topTimes.size() > 5) {
      topTimes.remove(topTimes.size() - 1);
    }
  }

  public void loadScores() {
    try {
      File f = new File(filePath, leaderboard);
      if (!f.isFile()) {
        createSaveData();
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

      topScores.clear();
      topTiles.clear();
      topTimes.clear();

      String[] scores = br.readLine().split("-");
      int scoresLen = scores.length;
      String[] tiles = br.readLine().split("-");
      int tilesLen = tiles.length;
      String[] times = br.readLine().split("-");
      int timesLen = times.length;

      for (int i = 0; i < scoresLen; i++) {
        topScores.add(Integer.parseInt(scores[i]));
      }
      for (int i = 0; i < tilesLen; i++) {
        topTiles.add(Integer.parseInt(tiles[i]));
      }
      for (int i = 0; i < timesLen; i++) {
        topTimes.add(Long.parseLong(times[i]));
      }

      br.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void saveScores() {
    FileWriter fw = null;

    try {
      File f = new File(filePath, leaderboard);
      fw = new FileWriter(f);
      BufferedWriter bw = new BufferedWriter(fw);

      bw.write(topScores.get(0) + "-" + topScores.get(1) + "-" + topScores.get(2) + "-" + topScores.get(3) + "-" + topScores.get(4));
      bw.newLine();
      bw.write(topTiles.get(0) + "-" + topTiles.get(1) + "-" + topTiles.get(2) + "-" + topTiles.get(3) + "-" + topTiles.get(4));
      bw.newLine();
      bw.write(topTimes.get(0) + "-" + topTimes.get(1) + "-" + topTimes.get(2) + "-" + topTimes.get(3) + "-" + topTimes.get(4));

      bw.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void createSaveData() {
    try {
      File f = new File(filePath, leaderboard);
      FileWriter fw = new FileWriter(f);
      BufferedWriter bw = new BufferedWriter(fw);

      bw.write("0-0-0-0-0"); // initial top score
      bw.newLine();
      bw.write("0-0-0-0-0"); // initial top tile
      bw.newLine();
      bw.write("1200-2400-5100-12000-24000"); // initial top time
      
      bw.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public int getBestScore() {
    return topScores.get(0);
  }

  public long getBestTile() {
    return topTiles.get(0);
  }

  public long getBestTime() {
    return topTimes.get(0);
  }

  public ArrayList<Integer> getTopScores() {
    return topScores;
  }

  public ArrayList<Integer> getTopTiles() {
    return topTiles;
  }

  public ArrayList<Long> getTopTimes() {
    return topTimes;
  }

}
