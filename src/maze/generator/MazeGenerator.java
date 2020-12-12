package maze.generator;

import maze.Room;

/**
 * Represents a maze generator.
 */
public interface MazeGenerator {

  /**
   * Gets the maze map.
   *
   * @return the maze map
   */
  Room[][] getMap();

}
