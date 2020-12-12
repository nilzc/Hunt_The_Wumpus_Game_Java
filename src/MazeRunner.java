import java.io.IOException;
import java.io.InputStreamReader;

import controller.Controller;
import controller.Features;
import view.GUIView;
import view.MazeView;
import view.TextView;

/**
 * A driver to run the game.
 */
public class MazeRunner {

  /**
   * Main method.
   *
   * @param args start arguments
   * @throws IOException exception
   */
  public static void main(String[] args) throws IOException {
    boolean isGui;
    try {
      isGui = readViewOption(args);
    } catch (Exception e) {
      System.out.println("Usage: java -jar hw6.jar <--text | --gui>\n");
      return;
    }
    Features controller = new Controller();
    MazeView view;
    if (isGui) {
      view = new GUIView();
    } else {
      view = new TextView(new InputStreamReader(System.in), System.out);
    }
    controller.execute(view);
  }

  private static boolean readViewOption(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("Invalid command.");
    }
    switch (args[0]) {
      case "--text":
        return false;
      case "--gui":
        return true;
      default:
        throw new IllegalArgumentException(String.format("Unknown command %s.", args[0]));
    }
  }

}
