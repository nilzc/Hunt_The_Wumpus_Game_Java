package maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import visualizer.MazeVisualizer;

/**
 * Abstract maze class to implement {@link MazeGame}.
 */
public abstract class AbstractMazeGame implements MazeGame {

  private final Player player;
  private int[] playerLocation;
  private final Room[][] map;
  private boolean finished = false;
  private final Random random;
  private final StringBuilder message = new StringBuilder();
  private final String playerSymbol;
  private boolean singlePlayer;

  /**
   * Constructs an abstract maze class, with seed.
   *
   * @param map   the generated maze map
   */
  public AbstractMazeGame(Room[][] map, boolean singlePlayer, String playerSymbol, Random random) {
    if (map == null || map.length == 0) {
      throw new IllegalArgumentException("Null or empty map.");
    }
    this.player = new Player();
    this.playerSymbol = playerSymbol;
    this.random = random;
    this.singlePlayer = singlePlayer;
    this.playerLocation = generatePlayerLocation(map);
    this.map = map;
    this.map[this.playerLocation[0]][this.playerLocation[1]].setPlayer();
    this.updateStatus();
  }

  private int[] generatePlayerLocation(Room[][] map) {
    int n = map.length;
    int x = map[0].length;
    List<int[]> possibleLocations = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < x; j++) {
        Room curr = map[i][j];
        if (!curr.hasWumpus() && !curr.hasPit() && !curr.hasBats()
                && curr.isRoom() && !curr.hasPlayer()) {
          possibleLocations.add(new int[]{i, j});
        }
      }
    }
    if (possibleLocations.size() == 0) {
      throw new IllegalArgumentException("No possible start location.");
    }
    int index = random.nextInt(possibleLocations.size());
    return possibleLocations.get(index);
  }

  @Override
  public int[] getPlayerLocation() {
    return this.playerLocation;
  }

  @Override
  public List<String> getPlayerMoveOptions() {
    int[] location = this.getPlayerLocation();
    Room curr = this.map[location[0]][location[1]];
    return curr.getOptions();
  }

  @Override
  public void shootArrowTo(String option, int distance) {
    if (this.isFinished()) {
      throw new IllegalStateException("Game is already over.");
    }
    if (option == null) {
      throw new IllegalArgumentException("Null shooting option");
    }
    if (!option.equals("up") && !option.equals("down")
            && !option.equals("left") && !option.equals("right")) {
      throw new IllegalArgumentException("Invalid shooting options.");
    }
    if (distance <= 0) {
      throw new IllegalArgumentException("Non-positive distance.");
    }
    try {
      this.player.shootArrow();
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
    Room curr = this.map[this.playerLocation[0]][this.playerLocation[1]];
    while (distance > 0) {
      try {
        RoomWithOption temp = this.arrowToNextRoom(curr, option);
        curr = temp.room;
        option = temp.option;
        distance--;
      } catch (Exception e) {
        this.message.append(e.getMessage());
        // hits the wall
        if (this.player.getArrowCount() == 0) {
          this.message.append("- You don't have any arrow left.\n");
          // remove the current player
          int[] location = this.getPlayerLocation();
          Room currRoom = this.map[location[0]][location[1]];
          removeRurrentPlayer(currRoom);
          this.finished = true;
        } else {
          this.message.append(
                  String.format("- You have %d arrows left.\n", this.player.getArrowCount()));
        }
        return;
      }
    }
    this.message.append(String.format("- You have %d arrows left.\n", this.player.getArrowCount()));
    // travel through the distancewwws
    if (curr.hasWumpus()) {
      curr.removeWumpus();
      this.message.append("- Great! You killed the wumpus.\n");
      this.finished = true;
    } else {
      // missed the wumpus
      this.message.append("- Your arrow missed the wumpus.\n");
      if (this.player.getArrowCount() == 0) {
        this.message.append("- You don't have any arrow left.\n");
        // remove the current player
        int[] location = this.getPlayerLocation();
        Room currRoom = this.map[location[0]][location[1]];
        removeRurrentPlayer(currRoom);
        this.finished = true;
      }
    }
  }

  @Override
  public void setFinished() {
    this.finished = true;
  }

  /*
    a temporary class to store the next room and the arrow flying direction
     */
  private static class RoomWithOption {
    private final Room room;
    private final String option;

    protected RoomWithOption(Room room, String option) {
      this.room = room;
      this.option = option;
    }
  }

  /*
  arrow flies to the next room, return the next room and the arrow flying direction
   */
  private RoomWithOption arrowToNextRoom(Room curr, String option) {
    List<String> possibleOptions = curr.getOptions();
    boolean valid = false;
    for (String p: possibleOptions) {
      if (p.equals(option)) {
        valid = true;
        break;
      }
    }
    if (!valid) {
      throw new IllegalStateException("- Your arrow hits the wall.\n");
    }
    Room next = getNextRoom(curr, option);
    String nextOption = option;
    // travel through the tunnel
    while (!next.isRoom()) {
      // in the tunnel, find the next room
      List<String> nextOptions = next.getOptions();
      String prev;
      switch (nextOption) {
        case "up":
          prev = "down";
          break;
        case "down":
          prev = "up";
          break;
        case "left":
          prev = "right";
          break;
        default:
          prev = "left";
      }
      for (String n: nextOptions) {
        if (!n.equals(prev)) {
          nextOption = n;
        }
      }
      // get the next room, maybe still in the tunnel
      next = getNextRoom(next, nextOption);
    }
    return new RoomWithOption(next, nextOption);
  }

  /*
  get the next room in the specified direction
   */
  private Room getNextRoom(Room curr, String option) {
    switch (option) {
      case "up":
        return curr.getUp();
      case "down":
        return curr.getDown();
      case "left":
        return curr.getLeft();
      default:
        return curr.getRight();
    }
  }

  @Override
  public void playerMove(String option) {
    if (this.isFinished()) {
      throw new IllegalStateException("- Game is already over.\n");
    }
    List<String> options = this.getPlayerMoveOptions();
    boolean valid = false;
    for (String o : options) {
      if (o.equals(option)) {
        valid = true;
        break;
      }
    }
    if (!valid) {
      throw new IllegalArgumentException(String.format("- You cannot move %s.\n", option));
    }

    // option is valid
    int[] location = this.getPlayerLocation();
    Room currRoom = this.map[location[0]][location[1]];
    Room newRoom;
    switch (option) {
      case "up":
        newRoom = currRoom.getUp();
        break;
      case "down":
        newRoom = currRoom.getDown();
        break;
      case "left":
        newRoom = currRoom.getLeft();
        break;
      default:
        newRoom = currRoom.getRight();
    }
    // remove current player
    removeRurrentPlayer(currRoom);
    newRoom.setPlayer();
    // update player symbol
    newRoom.setPlayerSymbol(this.playerSymbol);
    // newRoom.setTouched();
    this.playerLocation = newRoom.getLocation();
    this.updateStatus();
  }

  private void removeRurrentPlayer(Room currRoom) {
    if (twoPlayersSameLocation()) {
      if (currRoom.getPlayerSymbol().equals(this.playerSymbol)) {
        currRoom.setPlayerSymbol(currRoom.getPlayerSymbol().equals("*") ? "/" : "*");
      }
    } else {
      currRoom.removePlayer();
    }
  }

  private boolean twoPlayersSameLocation() {
    if (this.singlePlayer) {
      return false;
    } else {
      int count = 0;
      for (Room[] rooms : map) {
        for (Room r: rooms) {
          if (r.hasPlayer()) {
            count++;
          }
        }
      }
      return count == 1;
    }
  }

  /*
  update the game status based on the player's current position
   */
  private void updateStatus() {
    Room curr = this.map[this.playerLocation[0]][this.playerLocation[1]];
    // update the player symbol
    curr.setPlayerSymbol(this.playerSymbol);
    // mark as travelled
    curr.setTravelled();
    // in tunnel
    if (!curr.isRoom()) {
      return;
    }
    // in a room
    int[] newLocation;
    if (curr.hasBats()) {
      newLocation = this.meetBats();
      // meet bats, and is carried to a new location
      if (!Arrays.equals(newLocation, this.playerLocation)) {
        this.message.append(
                String.format("- Super bats carried you to [%d, %d].\n",
                newLocation[0], newLocation[1]));
        this.playerLocation = newLocation;
        if (twoPlayersSameLocation()) {
          if (curr.getPlayerSymbol().equals(this.playerSymbol)) {
            curr.setPlayerSymbol(curr.getPlayerSymbol().equals("*") ? "/" : "*");
          }
        } else {
          curr.removePlayer();
        }
        this.map[newLocation[0]][newLocation[1]].setPlayer();
        this.updateStatus();
        return;
      }
      // if not, continue
      this.message.append("- Super bats missed you.\n");
    }

    if (curr.hasPit()) {
      this.finished = true;
      this.message.append("- You fall into the bottomless pit.\n");
      curr.removePlayer();
      return;
    }

    if (curr.hasWumpus()) {
      this.finished = true;
      curr.removePlayer();
      this.message.append("- You are swallowed by the wumpus.\n");
    }
  }

  @Override
  public String getHints() {
    if (isFinished()) {
      return "- Game over.\n";
    }
    Room curr = this.map[this.playerLocation[0]][this.playerLocation[1]];
    Room adjacent;
    List<String> options = new ArrayList<>(Arrays.asList("up", "down", "left", "right"));
    boolean bat = false;
    boolean pit = false;
    boolean wumpus = false;
    for (String o: options) {
      try {
        adjacent = arrowToNextRoom(curr, o).room;
      } catch (Exception e) {
        continue;
      }

      if (adjacent.hasWumpus()) {
        wumpus = true;
      }
      if (adjacent.hasPit()) {
        pit = true;
      }
      if (adjacent.hasBats()) {
        bat = true;
      }
    }
    // get hints
    StringBuilder hints = new StringBuilder();
    // add possible directions
    List<String> possibleOptions = getPlayerMoveOptions();
    hints.append("- You can move to [");
    for (String o: possibleOptions) {
      hints.append(o).append(", ");
    }
    hints.delete(hints.length() - 2, hints.length());
    hints.append("].\n");
    if (bat) {
      hints.append("- You hear flapping nearby.\n");
    }
    if (pit) {
      hints.append("- You feel a breeze nearby.\n");
    }
    if (wumpus) {
      hints.append("- You smell a wumpus nearby.\n");
    }
    return hints.toString();
  }

  /*
  when entering a room with bats, 50% the player will be carried away
   */
  private int[] meetBats() {
    // 1 means get carried to a new location
    // 2 means stay in the same position
    if (random.nextInt(2) == 1) {
      List<int[]> allLocations = new ArrayList<>();
      for (int i = 0; i < this.map.length; i++) {
        for (int j = 0; j < this.map[0].length; j++) {
          allLocations.add(new int[]{i, j});
        }
      }
      return allLocations.get(random.nextInt(allLocations.size()));
    }
    return this.playerLocation;
  }

  @Override
  public void refreshPlayer() {
    Room curr = this.map[this.playerLocation[0]][this.playerLocation[1]];
    curr.setPlayer();
    curr.setPlayerSymbol(this.playerSymbol);
  }

  @Override
  public String toString() {
    this.refreshPlayer();
    return MazeVisualizer.visualizer(map);
  }

  @Override
  public boolean isFinished() {
    return this.finished;
  }

  @Override
  public String getMessage() {
    String result = this.message.toString();
    this.message.setLength(0);
    return result;
  }

  @Override
  public void setSinglePlayer(boolean flag) {
    this.singlePlayer = flag;
  }

  @Override
  public Room[][] copyMaze() {
    int x = map.length;
    int y = map[0].length;
    Room[][] newMaze = new Room[x][y];
    // get a copy of each room
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        Room originalRoom = map[i][j];
        newMaze[i][j] = originalRoom.copy();
      }
    }
    // connect each room
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        Room originalRoom = map[i][j];
        Room newRoom = newMaze[i][j];
        if (originalRoom.getUp() != null) {
          int[] location = originalRoom.getUp().getLocation();
          newRoom.setUp(newMaze[location[0]][location[1]]);
        }
        if (originalRoom.getDown() != null) {
          int[] location = originalRoom.getDown().getLocation();
          newRoom.setDown(newMaze[location[0]][location[1]]);
        }
        if (originalRoom.getLeft() != null) {
          int[] location = originalRoom.getLeft().getLocation();
          newRoom.setLeft(newMaze[location[0]][location[1]]);
        }
        if (originalRoom.getRight() != null) {
          int[] location = originalRoom.getRight().getLocation();
          newRoom.setRight(newMaze[location[0]][location[1]]);
        }
      }
    }
    return newMaze;
  }
}
