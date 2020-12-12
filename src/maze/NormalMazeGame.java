package maze;

import java.util.Random;

/**
 * A concrete class that extends {@link AbstractMazeGame} class.
 */
public class NormalMazeGame extends AbstractMazeGame {

  /**
   * Constructs a normal maze game, with seed.
   *
   * @param map the generated maze map
   */
  public NormalMazeGame(Room[][] map, boolean singlePlayer, String symbol, Random random) {
    super(map, singlePlayer, symbol, random);
  }

}
