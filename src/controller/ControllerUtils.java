package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import maze.MazeGame;
import maze.NormalMazeGame;
import maze.generator.MazeGenerator;

/**
 * A utility class to provide some common methods for {@link Controller}.
 */
public class ControllerUtils {

  /**
   * Generate maze games based on the number of players.
   *
   * @param arguments     maze configuration arguments
   * @param mazeGenerator {@link MazeGenerator}
   * @param randomSeed    the random seed
   * @return              a list of {@link MazeGame}
   */
  public static List<MazeGame> generateMazeGame(
          Arguments arguments, MazeGenerator mazeGenerator, int randomSeed) {
    Random random = new Random(randomSeed);
    List<MazeGame> mazeGames = new ArrayList<>();
    if (!arguments.singlePlayer) {
      // player 1
      mazeGames.add(new NormalMazeGame(
              mazeGenerator.getMap(), false,"*", random));
      // player 2
      mazeGames.add(new NormalMazeGame(
              mazeGenerator.getMap(), false,"/", random
      ));
    } else {
      // player 1
      mazeGames.add(new NormalMazeGame(
              mazeGenerator.getMap(),true, "*", random));
    }
    return mazeGames;
  }

  /**
   * In multiplayer mode, when one player dies, the other game will be in single player mode.
   *
   * @param arguments maze configuration arguments
   * @param mazeGames a list of {@link MazeGame}
   */
  public static void onePlayerDies(Arguments arguments, List<MazeGame> mazeGames) {
    // when one game ends, the other one will be single player
    if (!arguments.singlePlayer) {
      if (mazeGames.get(0).isFinished()) {
        mazeGames.get(1).setSinglePlayer(true);
      }
      if (mazeGames.get(1).isFinished()) {
        mazeGames.get(0).setSinglePlayer(true);
      }
    }
  }
}
