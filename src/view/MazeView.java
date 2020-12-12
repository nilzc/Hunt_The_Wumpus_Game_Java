package view;

import java.io.IOException;
import java.util.Map;

import controller.Features;
import maze.Room;

/**
 * A maze view interface.
 */
public interface MazeView {

  /**
   * Add the controller.
   *
   * @param features the controller
   */
  void addFeatures(Features features);

  /**
   * Configure the maze.
   *
   * @throws IOException exception
   */
  void configuration() throws IOException;

  /**
   * Start the maze game.
   *
   * @param features the controller
   * @throws IOException exception
   */
  void startGame(Features features) throws IOException;

  /**
   * Initialize the maze game.
   *
   * @param map      the maze map
   * @param features the controller
   * @throws IOException exception
   */
  void initializeMaze(Room[][] map, Features features) throws IOException;

  /**
   * Update the maze.
   *
   * @param map      the maze map
   * @param features the controller
   * @throws IOException exception
   */
  void updateMaze(Room[][] map, Features features) throws IOException;

  /**
   * Reset focus.
   */
  void resetFocus();

  /**
   * Update the message and hints of the maze game.
   *
   * @param message  the message
   * @param hints    the hints
   * @throws IOException exception
   */
  void updateInfo(Map<String, String> message, Map<String, String> hints) throws IOException;

  /**
   * Set player 1's game status.
   *
   * @param finished whether it's over
   */
  void player1Finish(boolean finished);

  /**
   * Set player 2's game status.
   *
   * @param finished whether it's over
   */
  void player2Finish(boolean finished);

  /**
   * The whole game is over.
   *
   * @param player1Result the result of player 1
   * @param player2Result the result of player 2
   * @param features the controller
   */
  void gameOver(String player1Result, String player2Result, Features features);

  /**
   * Reset game status including shooting, moving, game over status and so on.
   */
  void resetStatus();

  /**
   * Set the current player in control.
   *
   * @param player1Turn whether it's player 1's turn
   */
  void setPlayerTurn(boolean player1Turn);
}
