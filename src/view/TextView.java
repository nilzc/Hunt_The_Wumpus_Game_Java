package view;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import controller.Arguments;
import controller.Features;
import maze.Room;
import visualizer.MazeVisualizer;

/**
 * A text view class that implements {@link MazeView}.
 */
public class TextView implements MazeView {

  private Features features;
  private final String error = "\t- Error: ";
  private boolean player1Turn = true;
  private final String player1Symbol = "*";
  private final String player2Symbol = "/";
  private boolean player1Finish = false;
  private boolean player2Finish = false;
  private final String format1 = "%-50s";
  private final String format2 = "%-50s%-50s";
  private String player1Result;
  private String player2Result;
  private String separate = "=".repeat(60) + "\n";
  private final Appendable out;
  private final Scanner scanner;

  /**
   * Construct a text view.
   * @param in  the input stream
   * @param out the output stream
   */
  public TextView(Readable in, Appendable out) {
    this.out = out;
    this.scanner = new Scanner(in);
  }

  @Override
  public void addFeatures(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    this.features = features;
  }

  @Override
  public void configuration() throws IOException {
    out.append(separate).append("\n");
    out.append("- Hunt the Wumpus -").append("\n");
    out.append("- Configuration").append("\n");
    while (true) {
      try {
        out.append("\t- Number of Rows:").append("\n");
        int x = Integer.parseInt(scanner.next());
        out.append("\t- Number of Columns:").append("\n");
        int y = Integer.parseInt(scanner.next());
        out.append("\t- Number of Walls:").append("\n");
        int walls = Integer.parseInt(scanner.next());
        out.append("\t- Proportion of Bat Rooms, [0, 1):").append("\n");
        double bat = Double.parseDouble(scanner.next());
        out.append("\t- Proportion of Pit Rooms, [0, 1)").append("\n");
        double pit = Double.parseDouble(scanner.next());
        boolean singlePlayer = readSinglePlayer(scanner);
        Arguments arguments = new Arguments(x, y, walls,bat, pit, singlePlayer);
        features.generateMazeGame(arguments);
        return;
      } catch (Exception e) {
        if (e.getMessage() == null) {
          out.append(error + "invalid values.\n").append("\n");
        } else {
          out.append(error + e.getMessage()).append("\n");
        }
      }
    }
  }

  private boolean readSinglePlayer(Scanner scanner) throws IOException {
    while (true) {
      try {
        out.append("\t- Single Player? (yes/no)").append("\n");
        String command = scanner.next().toLowerCase();
        switch (command) {
          case "yes":
          case "y":
            return true;
          case "no":
          case "n":
            return false;
          default:
            throw new IllegalArgumentException(String.format("invalid command %s.\n", command));
        }
      } catch (Exception e) {
        out.append(error + e.getMessage()).append("\n");
      }
    }
  }

  @Override
  public void startGame(Features features) throws IOException {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    while (!player1Finish || !player2Finish) {
      out.append("- Enter 'm' to move, 'a' to shoot an arrow:\n"
              + "(- Or 'restart' to restart, 'new' to start a new game, 'q' to quit the game)")
              .append("\n");
      try {
        String command = scanner.next().toLowerCase();
        switch (command) {
          case "m":
            move(features, player1Turn);
            break;
          case "a":
            attack(features, player1Turn);
            break;
          case "restart":
            features.restart();
            return;
          case "new":
            features.newGame();
            return;
          case "q":
            return;
          default:
            throw new IllegalArgumentException(String.format("unknown command %s.\n", command));
        }
      } catch (Exception e) {
        out.append(error + e.getMessage()).append("\n");
      }
    }
    printResult();
    endOfGame(features);
  }

  private void endOfGame(Features features) throws IOException {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    while (true) {
      out.append("- What do you want to do next:\n"
              + "\t- 1. Restart\n"
              + "\t- 2. New Game\n"
              + "\t- 3. Quit").append("\n");
      try {
        String command = scanner.next().toLowerCase();
        switch (command) {
          case "1":
            features.restart();
            break;
          case "2":
            features.newGame();
            break;
          case "3":
            return;
          default:
            throw new IllegalArgumentException(String.format("unknown command %s.\n", command));
        }
        break;
      } catch (Exception e) {
        out.append(error + e.getMessage()).append("\n");
      }
    }
  }

  private void printResult() throws IOException {
    out.append(separate).append("\n");
    out.append("- Game Over:\n").append("\n");
    out.append(String.format(format2, "- Player1", "- Player 2")).append("\n");
    out.append(String.format(format2, player1Result, player2Result)).append("\n");
    out.append("").append("\n");
  }

  private void attack(Features features, boolean player1Turn) throws IOException {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    while (true) {
      try {
        out.append(
                "\t- Enter direction using 'up', 'left', 'down', 'right' (or 'w', 'a', 's', 'd'):")
                .append("\n");
        String direction = readDirection();
        out.append(
                "\t- Enter shooting distance:"
        ).append("\n");
        int distance = readDistance();
        if (player1Turn) {
          features.player1Shoot(direction, distance);
        } else {
          features.player2Shoot(direction, distance);
        }
        break;
      } catch (Exception e) {
        out.append(error + e.getMessage()).append("\n");
      }
    }
  }

  private int readDistance() {
    try {
      String result = scanner.next();
      return Integer.parseInt(result);
    } catch (Exception e) {
      throw new IllegalArgumentException("invalid distance.\n");
    }
  }

  private void move(Features features, boolean player1Turn) throws IOException {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    while (true) {
      out.append(
              "\t- Enter direction using 'up', 'left', 'down', 'right' (or 'w', 'a', 's', 'd'):")
              .append("\n");
      try {
        String direction = readDirection();
        if (player1Turn) {
          features.player1Move(direction);
        } else {
          features.player2Move(direction);
        }
        break;
      } catch (Exception e) {
        out.append(error + e.getMessage()).append("\n");
      }
    }
  }

  private String readDirection() {
    String command = scanner.next().toLowerCase();
    switch (command) {
      case "up":
      case "w":
        return "up";
      case "down":
      case "s":
        return "down";
      case "left":
      case "a":
        return "left";
      case "right":
      case "d":
        return "right";
      default:
        throw new IllegalArgumentException(String.format("unknown command %s.\n", command));
    }
  }

  @Override
  public void initializeMaze(Room[][] map, Features features) throws IOException {
    updateMaze(map, features);
  }

  @Override
  public void updateMaze(Room[][] map, Features features) throws IOException {
    if (map == null || map.length == 0 || map[0].length == 0) {
      throw new IllegalArgumentException("Null or invalid map dimension.");
    }
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    out.append(separate).append("\n");
    out.append("- Maze:\n").append("\n");
    out.append(MazeVisualizer.visualizer(map)).append("\n");
  }

  @Override
  public void resetFocus() {
    // no action needed
  }

  @Override
  public void updateInfo(
          Map<String, String> message, Map<String, String> hints) throws IOException {
    if (message == null || message.size() == 0) {
      throw new IllegalArgumentException("Empty message.");
    }
    if (hints == null || hints.size() == 0) {
      throw new IllegalArgumentException("Empty hints.");
    }
    // turn
    if (player1Turn) {
      out.append("- Player 1's Turn ***\n").append("\n");
    } else {
      out.append("- Player 2's Turn ///\n").append("\n");
    }
    // message
    out.append("- Message:").append("\n");
    out.append(String.format(format2, "- Player 1", "- Player 2")).append("\n");
    printMessage(message);
    // hint
    out.append("- Hints:").append("\n");
    out.append(String.format(format2, "- Player 1", "- Player 2")).append("\n");
    printMessage(hints);
  }

  private void printMessage(Map<String, String> message) throws IOException {
    if (message.size() == 1) {
      String messageP1 = message.get("player1");
      out.append(messageP1).append("\n");
    } else if (message.size() == 2) {
      String[] messageP1 = message.get("player1").split("\n");
      String[] messageP2 = message.get("player2").split("\n");
      int maxRow = Math.max(messageP1.length, messageP2.length);
      StringBuilder result = new StringBuilder();
      for (int i = 0; i < maxRow; i++) {
        if (i < messageP1.length) {
          result.append(String.format(format1, messageP1[i]));
        } else {
          result.append(String.format(format1, ""));
        }
        if (i < messageP2.length) {
          result.append(String.format(format1 + "\n", messageP2[i]));
        } else {
          result.append(String.format(format1 + "\n", ""));
        }
      }
      out.append(result.toString()).append("\n");
    } else {
      throw new IllegalArgumentException("Invalid message length.");
    }
  }

  @Override
  public void player1Finish(boolean finished) {
    player1Finish = finished;
  }

  @Override
  public void player2Finish(boolean finished) {
    player2Finish = finished;
  }

  @Override
  public void gameOver(String player1Result, String player2Result, Features features) {
    // features is not needed in text view
    this.player1Result = player1Result;
    this.player2Result = player2Result;
  }

  @Override
  public void resetStatus() {
    // no action needed
  }

  @Override
  public void setPlayerTurn(boolean player1Turn) {
    this.player1Turn = player1Turn;
  }
}
