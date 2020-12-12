package maze.generator;

import maze.Room;

/**
 * Represents a {@link WrappedNonPerfectMaze} generator, which generates a {@link NonPerfectMaze}
 * first then "wrap" the top border with the down border.
 */
public class WrappedNonPerfectMaze extends AbstractGenerator {

  private final int numberOfRemainWalls;
  private final boolean wrapRow;

  /**
   * Constructs a {@link WrappedNonPerfectMaze} generator.
   *
   * @param x                   the number of rows
   * @param y                   the number of columns
   * @param numberOfRemainWalls the number of walls remain
   */
  public WrappedNonPerfectMaze(int x, int y, double bats, double pits, int seed,
                               int numberOfRemainWalls, boolean wrapRow) {
    super(x, y, bats, pits, seed);
    if (numberOfRemainWalls < 0) {
      throw new IllegalArgumentException("Invalid number of remaining walls.\n");
    }
    this.wrapRow = wrapRow;
    this.numberOfRemainWalls = numberOfRemainWalls;
    this.generateMaze();
  }

  private void generateMaze() {
    int n = super.getNumberOfRows();
    int x = super.getNumberOfColumns();
    super.generateNonPerfect(this.numberOfRemainWalls);
    // wrap column
    if (!this.wrapRow) {
      for (int j = 0; j < x; j++) {
        Room up = this.getMap()[0][j];
        Room down = this.getMap()[n - 1][j];
        up.setUp(down);
        down.setDown(up);
      }
    } else {
      // wrap row
      for (int i = 0; i < n; i++) {
        Room left = this.getMap()[i][0];
        Room right = this.getMap()[i][x - 1];
        left.setLeft(right);
        right.setRight(left);
      }
    }
    super.addSpecialRooms();
  }

}
