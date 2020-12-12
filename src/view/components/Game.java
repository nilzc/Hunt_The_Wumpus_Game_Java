package view.components;

import java.util.Map;

import controller.Features;
import maze.Room;

/**
 * Game Interface.
 */
public interface Game extends MazeComponent {

  /**
   * Load the maze map for the first time.
   *
   * @param map      maze map
   * @param features controller
   */
  void initializeMaze(Room[][] map, Features features);

  /**
   * Load the updated maze map.
   *
   * @param map       maze map
   * @param features  controller
   */
  void updateMaze(Room[][] map, Features features);

  /**
   * Load the message and hints.
   *
   * @param message message
   * @param hints   hints
   */
  void updateInfo(Map<String, String> message, Map<String, String> hints);

  /**
   * Load the error message.
   *
   * @param message error message
   * @param isP1    whether is for Player 1
   */
  void setErrorMessage(String message, boolean isP1);

  /**
   * Clear the error message.
   *
   * @param isP1 whether is for Player 1
   */
  void clearErrorMessage(boolean isP1);

  /**
   * Set the current player.
   *
   * @param player1Turn whether it's player 1' turn
   */
  void setPlayerTurn(boolean player1Turn);

}
