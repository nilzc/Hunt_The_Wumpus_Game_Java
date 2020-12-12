package controller;

import java.io.IOException;

import view.MazeView;

/**
 * Features of a maze controller.
 */
public interface Features {

  /**
   * Generate a maze game.
   *
   * @param arguments    maze configuration arguments
   * @throws IOException exception
   */
  void generateMazeGame(Arguments arguments) throws IOException;

  /**
   * Start a whole new game.
   *
   * @throws IOException exception
   */
  void newGame() throws IOException;

  /**
   * Player moves. Player moves in turn (multiplayer mode).
   *
   * @param row the target room row index
   * @param col the target room col index
   * @throws IOException exception
   */
  void playerMove(int row, int col) throws IOException;

  /**
   * Player 1 moves, for keyboard control only.
   *
   * @param direction    the moving direction
   * @throws IOException exception
   */
  void player1Move(String direction) throws IOException;

  /**
   * Player 2 moves, for keyboard control only.
   *
   * @param direction    the moving direction
   * @throws IOException exception
   */
  void player2Move(String direction) throws IOException;

  /**
   * Restart the game with the same maze map but different player locations.
   *
   * @throws IOException exception
   */
  void restart() throws IOException;

  /**
   * Player 1 shoots an arrow.
   *
   * @param shootCommandDirection shooting direction
   * @param distance              shooting distance
   * @throws IOException          exception
   */
  void player1Shoot(String shootCommandDirection, int distance) throws IOException;

  /**
   * Player 2 shoots an arrow.
   *
   * @param shootCommandDirection shooting direction
   * @param distance              shooting distance
   * @throws IOException          exception
   */
  void player2Shoot(String shootCommandDirection, int distance) throws IOException;

  /**
   * Start the maze game with the view.
   *
   * @param view          the view of the game
   * @throws IOException  exception
   */
  void execute(MazeView view) throws IOException;

}
