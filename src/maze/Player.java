package maze;

/**
 * Represents a player that is going to move in a maze and collect gold.
 */
public class Player {

  private int arrowCount;

  /**
   * Construct a player.
   */
  public Player() {
    this.arrowCount = 3;
  }

  /**
   * Decrement the player's arrow count.
   */
  public void shootArrow() {
    if (this.arrowCount <= 0) {
      throw new IllegalStateException("The player has no arrow.");
    }
    this.arrowCount--;
  }

  /**
   * Get the player's current arrow count.
   *
   * @return the arrow count
   */
  public int getArrowCount() {
    return this.arrowCount;
  }

}
