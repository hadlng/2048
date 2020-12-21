package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import util.AudioManager;

public class GameBoard {

  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  public static final int UP = 2;
  public static final int DOWN = 3;

  public static final int BOARD_ROWS = 4;
  public static final int BOARD_COLS = 4;

  private final int startingTiles = 2; // start with 2 random tiles
  private Tile[][] board;
  private boolean isDead;
  private boolean isWon;
  private BufferedImage gameBoard;
  private BufferedImage finalBoard; // final board with the game board and tiles
  // where to render on the screen
  private int x;
  private int y;

  public static int MARGIN = 15; // margin between tiles
  public static int BOARD_WIDTH = (BOARD_COLS + 1) * MARGIN + BOARD_COLS * Tile.TILE_SIZE;
  public static int BOARD_HEIGHT = (BOARD_ROWS + 1) * MARGIN + BOARD_ROWS * Tile.TILE_SIZE;

  // https://stackoverflow.com/a/58983939 calculate elapsed time
  private long elapsedTime;
  private long startTime;
  private long pauseTime = 0;
  private long additionalTime = 0;

  private boolean isPaused = false;
  private boolean hasStarted;

  private ScoreManager scores;
  private Leaderboard lBoard;
  private AudioManager audio;
  private int saveCount = 0;

  /**
   * @param x x coordinate of the game board
   * @param y y coordinate of the game board
   */
  public GameBoard(int x, int y) {
    this.x = x;
    this.y = y;
    board = new Tile[BOARD_ROWS][BOARD_COLS];
    gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
    finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
    audio = new AudioManager();

    createBoardImage();

    audio.play("8-bit-nes", -1);
    lBoard = Leaderboard.getInstance();
    lBoard.loadScores();
    scores = new ScoreManager(this);
    scores.loadGame();
    scores.setBestTime(lBoard.getBestTime());
    scores.setCurrentTopScore(lBoard.getBestScore());

    if (scores.newGame()) {
      start();
      scores.saveGame();

      // reset
      pauseTime = 0;
      additionalTime = 0;
    } else {
      for (int i = 0; i < scores.getBoard().length; i++) {
        if (scores.getBoard()[i] == 0) {
          continue;
        }
        spawn(i / BOARD_ROWS, i % BOARD_COLS, scores.getBoard()[i]);
      }
      // not calling setDead because we don't want to save anything
      isDead = checkDead();
      // not coalling setWon because we don't want to save the time
      isWon = checkWon();
    }
  }

  private void createBoardImage() {
    // draw the game board
    Graphics2D g = (Graphics2D) gameBoard.getGraphics();

    g.setColor(Game.BG_COLOR);
    g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

    g.setColor(new Color(61, 41, 99));
    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLS; col++) {
        int x = MARGIN + MARGIN * col + Tile.TILE_SIZE * col;
        int y = MARGIN + MARGIN * row + Tile.TILE_SIZE * row;
        g.fillRoundRect(x, y, Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_BORDER_RADIUS, Tile.TILE_BORDER_RADIUS);
      }
    }
    g.dispose();
  }

  public void reset() {
    board = new Tile[BOARD_ROWS][BOARD_COLS];
    start();
    scores.saveGame();

    isDead = false;
    isWon= false;
    hasStarted = false;

    startTime = System.nanoTime();
    elapsedTime = 0;
    pauseTime = 0;
    additionalTime = 0;

    saveCount = 0;
  }

  private void start() {
    for (int i = 0; i < startingTiles; i++) {
      spawnRandomly();
    }
  }

  private void spawnRandomly() {
    // randomize the tiles
    Random rnd = new Random();
    boolean isValid = false;

    while (!isValid) {
      int pos = rnd.nextInt(BOARD_ROWS * BOARD_COLS);
      int row = pos / BOARD_ROWS;
      int col = pos % BOARD_COLS;
      Tile current = board[row][col];
      if (current == null) {
        int value = rnd.nextInt() < 0.9 ? 2 : 4; // 90 chances get 2, 10 chances get 4
        Tile tile = new Tile(value, getTileX(col), getTileY(row));
        board[row][col] = tile;
        isValid = true;
      }
    }
  }

  // DEBUG METHOD
  private void spawn(int row, int col, int value) {
    board[row][col] = new Tile(value, getTileX(col), getTileY(row));
  }

  public int getTileX(int col) {
    return MARGIN + col * Tile.TILE_SIZE + col * MARGIN;
  }

  public int getTileY(int row) {
    return MARGIN + row * Tile.TILE_SIZE + row * MARGIN;
  }

  public void render(Graphics2D g) {
    Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
    g2d.drawImage(gameBoard, 0, 0, null);
    
    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLS; col++) {
        Tile current = board[row][col];
        if (current == null) {
          continue;
        }
        current.render(g2d);
      }
    }

    g.drawImage(finalBoard, x, y, null);
    g2d.dispose();
  }

  public void update() {
    saveCount++;

    if (saveCount >= 120) {
      // after 2 seconds, reset saved data
      saveCount = 0;
      scores.saveGame();
    }

    if (!isDead && !isWon) {
      if (hasStarted && !isPaused) {
        if (pauseTime != 0) {
          long stopTime = System.nanoTime() - pauseTime; // the time has passed during pause
          additionalTime += stopTime;
          pauseTime = 0;
        }

        elapsedTime = (long) (System.nanoTime() - startTime - additionalTime) / 1000000000; // convert into second
        scores.setTime(elapsedTime);
      } else if (!hasStarted) {
        startTime = System.nanoTime();
      }
    }

    checkKey();

    if (scores.getCurrentScore() > scores.getCurrentTopScore()) {
      // update current top score
      scores.setCurrentTopScore(scores.getCurrentScore());
    }

    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLS; col++) {
        Tile current = board[row][col];
        if (current == null) {
          continue;
        }
        current.update();
        resetPosition(current, row, col);
        if (current.getValue() >= 2048) {
          setWon(true);
        }
      }
    }
  }

  private void resetPosition(Tile current, int row, int col) {
    if (current == null) {
      return;
    }

    int x = getTileX(col);
    int y = getTileY(row);

    // prevent animation flickering
    int distX = current.getX() - x; // horizontal distance
    int distY = current.getY() - y; // vertical distance

    if (Math.abs(distX) < Tile.SLIDE_SPEED) {
      current.setX(current.getX() - distX);
    }
    if (Math.abs(distY) < Tile.SLIDE_SPEED) {
      current.setY(current.getY() - distY);
    }
    if (distX < 0) {
      current.setX(current.getX() + Tile.SLIDE_SPEED);
    }
    if (distY < 0) {
      current.setY(current.getY() + Tile.SLIDE_SPEED);
    }
    if (distX > 0) {
      current.setX(current.getX() - Tile.SLIDE_SPEED);
    }
    if (distY > 0) {
      current.setY(current.getY() - Tile.SLIDE_SPEED);
    }
  }

  // "ALGORITHMS" OF THE GAME
  private boolean move(int row, int col, int horizontalDirection, int verticalDirection, int direction) {
    boolean canMove = false;

    Tile current = board[row][col];

    if (current == null) {
      return false; // cannot move this tile
    }

    boolean move = true;
    int newRow = row;
    int newCol = col;

    while (move) {
      newRow += verticalDirection;
      newCol += horizontalDirection;

      // while the tile is moving, perform some checks
      if (isOutOfBounds(direction, newRow, newCol)) {
        break;
      }

      if (board[newRow][newCol] == null) {
        // if there is empty space
        board[newRow][newCol] = current; // set new place to current tile
        board[newRow - verticalDirection][newCol - horizontalDirection] = null; // set old place to null
        board[newRow][newCol].setSlideTo(new Point(newRow, newCol)); // the point where it slide to
        canMove = true;
      } else if (board[newRow][newCol].getValue() == current.getValue() && board[newRow][newCol].canMerge()) {
        board[newRow][newCol].setCanMerge(false); // cannot merge anymore
        board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2); // double the value
        board[newRow - verticalDirection][newCol - horizontalDirection] = null;
        board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
        canMove = true;

        // add to current score
        scores.setCurrentScore(scores.getCurrentScore() + board[newRow][newCol].getValue());
      } else {
        move = false;
      }
    }

    return canMove;
  }

  private boolean isOutOfBounds(int direction, int row, int col) {
    // check if tile is out of the board
    if (direction == LEFT) {
      return col < 0;
    } else if (direction == RIGHT) {
      return col > BOARD_COLS - 1;
    } else if (direction == UP) {
      return row < 0;
    } else if (direction == DOWN) {
      return row > BOARD_ROWS - 1;
    }
    return false;
  }

  private void moveTiles(int direction) {
    boolean canMove = false;
    int horizontalDirection = 0;
    int verticalDirection = 0;

    if (direction == LEFT) {
      horizontalDirection = -1;
      for (int row = 0; row < BOARD_ROWS; row++) {
        for (int col = 0; col < BOARD_COLS; col++) {
          if (!canMove) {
            canMove = move(row, col, horizontalDirection, verticalDirection, direction);
          } else {
            move(row, col, horizontalDirection, verticalDirection, direction);
          }
        }
      }
    } else if (direction == RIGHT) {
      horizontalDirection = 1;
      for (int row = 0; row < BOARD_ROWS; row++) {
        for (int col = BOARD_COLS - 1; col >= 0; col--) {
          if (!canMove) {
            canMove = move(row, col, horizontalDirection, verticalDirection, direction);
          } else {
            move(row, col, horizontalDirection, verticalDirection, direction);
          }
        }
      }
    } else if (direction == UP) {
      verticalDirection = -1;
      for (int row = 0; row < BOARD_ROWS; row++) {
        for (int col = 0; col < BOARD_COLS; col++) {
          if (!canMove) {
            canMove = move(row, col, horizontalDirection, verticalDirection, direction);
          } else {
            move(row, col, horizontalDirection, verticalDirection, direction);
          }
        }
      }
    } else if (direction == DOWN) {
      verticalDirection = 1;
      for (int row = BOARD_ROWS - 1; row >= 0; row--) {
        for (int col = 0; col < BOARD_COLS; col++) {
          if (!canMove) {
            canMove = move(row, col, horizontalDirection, verticalDirection, direction);
          } else {
            move(row, col, horizontalDirection, verticalDirection, direction);
          }
        }
      }
    } else {
      System.out.println(direction + " is not a valid direction.");
    }

    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLS; col++) {
        Tile current = board[row][col];
        if (current == null) {
          continue;
        }
        current.setCanMerge(true);
      }
    }

    if (canMove) {
      // play sound
      audio.play("water-click", 0);
      spawnRandomly();
      setDead(checkDead());
    }
  }

  // handle dead and won
  private void setDead(boolean isDead) {
    if (!this.isDead && isDead) {
      lBoard.addTile(getHighestTileValue());
      lBoard.addScore(scores.getCurrentScore());
      lBoard.saveScores();
    }
    this.isDead = isDead;
  }

  private boolean checkDead() {
    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLS; col++) {
        if (board[row][col] == null) {
          return false;
        }
        boolean canMerge = !hasDiffValueSurroundingTiles(row, col, board[row][col]);
        if (canMerge) {
          return false;
        }
      }
    }
    return true;
  }

  public boolean isDead() {
    return isDead;
  }

  private void setWon(boolean isWon) {
    if (!this.isWon && isWon && !isDead) {
      lBoard.addTime(scores.getTime());
      lBoard.saveScores();
    }
    this.isWon = isWon;
  }

  private boolean checkWon() {
    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLS; col++) {
        if (board[row][col] == null) {
          continue;
        }
        if (board[row][col].getValue() >= 2048) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isWon() {
    return isWon;
  }

  private boolean hasDiffValueSurroundingTiles(int row, int col, Tile current) {
    if (row > 0) {
      Tile check = board[row - 1][col]; // the tile above the current tile
      if (check == null) {
        return false;
      }
      if (current.getValue() == check.getValue()) {
        return false;
      }
    }
    if (row < BOARD_ROWS - 1) {
      Tile check = board[row + 1][col]; // the tile below the current tile
      if (check == null) {
        return false;
      }
      if (current.getValue() == check.getValue()) {
        return false;
      }
    }
    if (col > 0) {
      Tile check = board[row][col - 1]; // the tile on the left of the current tile
      if (check == null) {
        return false;
      }
      if (current.getValue() == check.getValue()) {
        return false;
      }
    }
    if (col < BOARD_COLS - 1) {
      Tile check = board[row][col + 1]; // the tile on the right of the current tile
      if (check == null) {
        return false;
      }
      if (current.getValue() == check.getValue()) {
        return false;
      }
    }
    return true; // can merge
  }

  private void checkKey() {
    if (Keyboard.typed(KeyEvent.VK_LEFT) || Keyboard.typed(KeyEvent.VK_A)) {
      if (!isPaused) {
        // move tiles left
        moveTiles(LEFT);
        if (!hasStarted) {
          hasStarted = !isDead;
        }
      }
    }
    if (Keyboard.typed(KeyEvent.VK_RIGHT) || Keyboard.typed(KeyEvent.VK_D)) {
      if (!isPaused) {
        // move tiles right
        moveTiles(RIGHT);
        if (!hasStarted) {
          hasStarted = !isDead;
        }
      }
    }
    if (Keyboard.typed(KeyEvent.VK_UP) || Keyboard.typed(KeyEvent.VK_W)) {
      if (!isPaused) {
        // move tiles up
        moveTiles(UP);
        if (!hasStarted) {
          hasStarted = !isDead;
        }
      }
    }
    if (Keyboard.typed(KeyEvent.VK_DOWN) || Keyboard.typed(KeyEvent.VK_S)) {
      if (!isPaused) {
        // move tiles down
        moveTiles(DOWN);
        if (!hasStarted) {
          hasStarted = !isDead;
        }
      }
    }
  }

  public boolean hasStarted() {
    return hasStarted;
  }

  public void setHasStarted(boolean hasStarted) {
    this.hasStarted = hasStarted;
  }

  public int getHighestTileValue() {
    int value = 2;

    for (int row = 0; row < BOARD_ROWS; row++) {
      for (int col = 0; col < BOARD_COLS; col++) {
        if (board[row][col] == null) {
          continue;
        }
        if (board[row][col].getValue() > value) {
          value = board[row][col].getValue();
        }
      }
    }
    return value;
  }

  public Tile[][] getBoard() {
    return board;
  }

  public void setBoard(Tile[][] board) {
    this.board = board;
  }

  public ScoreManager getScores() {
    return scores;
  }

  public long getPauseTime() {
    return pauseTime;
  }

  public void setPauseTime(long pauseTime) {
    this.pauseTime = pauseTime;
  }

  public long getAdditionalTime() {
    return additionalTime;
  }

  public void setAdditionalTime(long additionalTime) {
    this.additionalTime = additionalTime;
  }

  public boolean isPaused() {
    return isPaused;
  }

  public void setPaused(boolean isPaused) {
    this.isPaused = isPaused;
  }

}
