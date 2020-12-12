package maze;

import java.util.List;

/**
 * An interface for maze room.
 */
public interface Room {

  /**
   * Sets the identifier to be true when a {@link Player} is currently in this {@link MazeRoom}.
   */
  void setPlayer();

  /**
   * Sets the identifier to be false when a {@link Player} is no longer in this {@link MazeRoom}.
   */
  void removePlayer();

  /**
   * Return whether this room has a player.
   *
   * @return whether there is a player
   */
  boolean hasPlayer();

  /**
   * Put bats into the room.
   */
  void setBats();

  /**
   * Return whether this room has bats.
   *
   * @return whether the room has bats
   */
  boolean hasBats();

  /**
   * Return whether this room has been travelled.
   *
   * @return whether this room has been travelled
   */
  boolean isTravelled();

  /**
   * Mark this room as travelled.
   */
  void setTravelled();

  /**
   * Set the player symbol.
   *
   * @param symbol the symbol
   */
  void setPlayerSymbol(String symbol);

  /**
   * Get the player symbol, "*" or "/".
   *
   * @return the player symbol
   */
  String getPlayerSymbol();

  /**
   * Put a pit into the room.
   */
  void setPit();

  /**
   * Return whether the room has a pit.
   *
   * @return whether the room has a pit
   */
  boolean hasPit();

  /**
   * Put a wumpus into the room.
   */
  void setWumpus();

  /**
   * The wumpus is killed.
   */
  void removeWumpus();

  /**
   * Return whether the room has a wumpus.
   *
   * @return whether the room has a wumpus
   */
  boolean hasWumpus();

  /**
   * Make the room a tunnel.
   */
  void changeToTunnel();

  /**
   * Return whether the room is truly a room, not a tunnel.
   *
   * @return whether the room is truly a room, not a tunnel
   */
  boolean isRoom();

  /**
   * Gets the {@link Room} location.
   *
   * @return the location of this {@link Room}
   */
  int[] getLocation();

  /**
   * Gets the neighbor {@link Room} that is located "up" to this {@link Room}.
   * Null means there is a wall.
   *
   * @return the "up" {@link Room}
   */
  Room getUp();

  /**
   * Gets the neighbor {@link Room} that is located "down" to this {@link Room}.
   * Null means there is a wall.
   *
   * @return the "down" {@link Room}
   */
  Room getDown();

  /**
   * Gets the neighbor {@link Room} that is located "left" to this {@link Room}.
   * Null means there is a wall.
   *
   * @return the "left" {@link Room}
   */
  Room getLeft();

  /**
   * Gets the neighbor {@link Room} that is located "right" to this {@link Room}.
   * Null means there is a wall.
   *
   * @return the "right" {@link Room}
   */
  Room getRight();

  /**
   * Gets the identifier of this {@link Room}. An identifier is used to distinguish a {@link Room},
   * which is created to help generate a maze. See {@link maze.generator.MazeGenerator} for
   * more info.
   *
   * @return the identifier of this maze {@link Room}
   */
  int getIdentifier();

  /**
   * Sets this {@link Room}'s identifier.
   *
   * @param newValue the identifier
   */
  void setIdentifier(int newValue);

  /**
   * Sets the neighbor {@link Room} that is located "up" to this {@link Room}.
   *
   * @param up the neighbor {@link Room}
   */
  void setUp(Room up);

  /**
   * Sets the neighbor {@link Room} that is located "down" to this {@link Room}.
   *
   * @param down the neighbor {@link Room}
   */
  void setDown(Room down);

  /**
   * Sets the neighbor {@link Room} that is located "left" to this {@link Room}.
   *
   * @param left the neighbor {@link Room}
   */
  void setLeft(Room left);

  /**
   * Sets the neighbor {@link Room} that is located "right" to this {@link Room}.
   *
   * @param right the neighbor {@link Room}
   */
  void setRight(Room right);

  /**
   * Gets the possible move options for the {@link Player}.
   *
   * @return the possible move options
   */
  List<String> getOptions();

  /**
   * Get a copy of the current room, with walls around.
   *
   * @return a copy of the room
   */
  Room copy();
}
