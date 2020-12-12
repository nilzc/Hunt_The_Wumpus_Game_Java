package maze;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a maze {@link MazeRoom} in a maze map.
 */
public class MazeRoom implements Room {

  private Room up;
  private Room down;
  private Room left;
  private Room right;
  private final int[] location;
  private int identifier = 0;
  private boolean player = false;
  private boolean isRoom = true;
  private boolean hasBats = false;
  private boolean hasPit = false;
  private boolean hasWumpus = false;
  private String playerSymbol;
  private boolean travelled = false;

  /**
   * Constructs a maze {@link MazeRoom}.
   *
   * @param location the location of this maze {@link MazeRoom}
   */
  public MazeRoom(int[] location) {
    if (location[0] < 0 || location[1] < 0) {
      throw new IllegalArgumentException("Negative location.");
    }
    this.up = null;
    this.down = null;
    this.left = null;
    this.right = null;
    this.location = location;
  }

  @Override
  public Room copy() {
    Room newRoom = new MazeRoom(location);
    newRoom.setIdentifier(identifier);
    if (player) {
      newRoom.setPlayer();
    }
    if (!isRoom) {
      newRoom.changeToTunnel();
    }
    if (hasBats) {
      newRoom.setBats();
    }
    if (hasPit) {
      newRoom.setPit();
    }
    if (hasWumpus) {
      newRoom.setWumpus();
    }
    if (playerSymbol != null) {
      newRoom.setPlayerSymbol(playerSymbol);
    }
    if (travelled) {
      newRoom.setTravelled();
    }
    return newRoom;
  }

  @Override
  public boolean isTravelled() {
    return travelled;
  }

  @Override
  public void setTravelled() {
    travelled = true;
  }

  @Override
  public void setPlayer() {
    this.player = true;
  }

  @Override
  public void removePlayer() {
    this.player = false;
  }

  @Override
  public boolean hasPlayer() {
    return this.player;
  }

  @Override
  public String getPlayerSymbol() {
    return this.playerSymbol;
  }

  @Override
  public void setPlayerSymbol(String symbol) {
    if (symbol == null) {
      throw new IllegalArgumentException("Null symbol.");
    }
    if (symbol.length() != 1) {
      throw new IllegalArgumentException("Invalid player symbol.");
    }
    this.playerSymbol = symbol;
  }

  @Override
  public void setBats() {
    this.hasBats = true;
  }

  @Override
  public boolean hasBats() {
    return this.hasBats;
  }

  @Override
  public void setPit() {
    this.hasPit = true;
  }

  @Override
  public boolean hasPit() {
    return this.hasPit;
  }

  @Override
  public void setWumpus() {
    this.hasWumpus = true;
  }

  @Override
  public boolean hasWumpus() {
    return this.hasWumpus;
  }

  @Override
  public void changeToTunnel() {
    this.isRoom = false;
  }

  @Override
  public void removeWumpus() {
    this.hasWumpus = false;
  }

  @Override
  public boolean isRoom() {
    return this.isRoom;
  }

  @Override
  public int[] getLocation() {
    return this.location;
  }

  @Override
  public Room getUp() {
    return this.up;
  }

  @Override
  public Room getDown() {
    return this.down;
  }

  @Override
  public Room getLeft() {
    return this.left;
  }

  @Override
  public Room getRight() {
    return this.right;
  }

  @Override
  public int getIdentifier() {
    return this.identifier;
  }

  @Override
  public void setIdentifier(int newValue) {
    if (newValue < 0) {
      throw new IllegalArgumentException("Negative identifier.");
    }
    this.identifier = newValue;
  }

  @Override
  public void setUp(Room up) {
    if (up == null) {
      throw new IllegalArgumentException("Null room.");
    }
    this.up = up;
  }

  @Override
  public void setDown(Room down) {
    if (down == null) {
      throw new IllegalArgumentException("Null room.");
    }
    this.down = down;
  }

  @Override
  public void setLeft(Room left) {
    if (left == null) {
      throw new IllegalArgumentException("Null room.");
    }
    this.left = left;
  }

  @Override
  public void setRight(Room right) {
    if (right == null) {
      throw new IllegalArgumentException("Null room.");
    }
    this.right = right;
  }

  @Override
  public List<String> getOptions() {
    ArrayList<String> result = new ArrayList<>();
    if (this.up != null) {
      result.add("up");
    }
    if (this.down != null) {
      result.add("down");
    }
    if (this.left != null) {
      result.add("left");
    }
    if (this.right != null) {
      result.add("right");
    }
    return result;
  }

  @Override
  public String toString() {
    String result = "";
    if (this.up == null) {
      result += "UUUUU\n";
    } else {
      result += "U   U\n";
    }
    if (this.left == null) {
      if (this.player) {
        result += String.format("U%s%s%s",
                this.playerSymbol, this.playerSymbol, this.playerSymbol);
      } else if (this.hasWumpus && this.hasPit && this.hasBats) {
        result += "UBWP";
      } else if (this.hasBats && this.hasPit) {
        result += "UB P";
      } else if (this.hasWumpus && this.hasPit) {
        result += "U WP";
      } else if (this.hasWumpus && this.hasBats) {
        result += "UBW ";
      } else if (this.hasBats) {
        result += "U B ";
      } else if (this.hasPit) {
        result += "U P ";
      } else if (this.hasWumpus) {
        result += "U W ";
      } else {
        result += "U   ";
      }
    } else {
      if (this.player) {
        result += String.format(" %s%s%s", this.playerSymbol, this.playerSymbol, this.playerSymbol);
      } else if (this.hasWumpus && this.hasPit && this.hasBats) {
        result += " BWP";
      } else if (this.hasBats && this.hasPit) {
        result += " B P";
      } else if (this.hasWumpus && this.hasPit) {
        result += "  WP";
      } else if (this.hasWumpus && this.hasBats) {
        result += " BW ";
      } else if (this.hasBats) {
        result += "  B ";
      } else if (this.hasPit) {
        result += "  P ";
      } else if (this.hasWumpus) {
        result += "  W ";
      } else {
        result += "    ";
      }
    }
    if (this.right == null) {
      result += "U\n";
    } else {
      result += " \n";
    }
    if (this.down == null) {
      result += "UUUUU";
    } else {
      result += "U   U";
    }
    return result;
  }

}
