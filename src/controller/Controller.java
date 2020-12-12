package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maze.MazeGame;
import maze.Room;
import maze.generator.MazeGenerator;
import maze.generator.WrappedNonPerfectMaze;
import view.MazeView;

/**
 * A controller for the maze game, which can be combined with a GUI view or a TEXT view.
 */
public class Controller implements Features {
  protected MazeView view;
  protected Room[][] map;
  protected List<MazeGame> mazeGames = new ArrayList<>();
  protected int randomSeedMap = 1;
  protected int randomSeedGame = 2;
  protected Arguments arguments;
  protected boolean player1Turn;
  protected String player1Result = "";
  protected String player2Result = "";

  @Override
  public void generateMazeGame(Arguments arguments) throws IOException {
    if (arguments == null) {
      throw new IllegalArgumentException("Null argument.");
    }
    view.resetStatus();
    // reset
    this.mazeGames.clear();
    this.player1Turn = true;
    this.map = null;
    player1Result = "";
    player2Result = "";

    // generate map
    this.arguments = arguments;
    MazeGenerator mazeGenerator = new WrappedNonPerfectMaze(
            arguments.x, arguments.y, arguments.bat, arguments.pit,
            this.randomSeedMap, arguments.wall, true
    );
    map = mazeGenerator.getMap();
    // generate game
    mazeGames = ControllerUtils.generateMazeGame(arguments, mazeGenerator, randomSeedGame);
    if (arguments.singlePlayer) {
      view.player1Finish(false);
      view.player2Finish(true);
    } else {
      view.player1Finish(false);
      view.player2Finish(false);
    }
    view.initializeMaze(mazeGames.get(0).copyMaze(), this);
    updateInfo();
    view.startGame(this);
  }

  @Override
  public void player1Shoot(String shootCommandDirection, int distance) throws IOException {
    mazeGames.get(0).shootArrowTo(shootCommandDirection, distance);
    checkWumpusKilled();
    if (mazeGames.get(0).isFinished()) {
      ControllerUtils.onePlayerDies(arguments, mazeGames);
      Room curr = map[
              mazeGames.get(0).getPlayerLocation()[0]][mazeGames.get(0).getPlayerLocation()[1]];
      if (!arguments.singlePlayer) {
        if (mazeGames.get(0).getPlayerLocation() == mazeGames.get(1).getPlayerLocation()) {
          curr.setPlayerSymbol("/");
        }
      }
    }
    // update view
    view.player1Finish(mazeGames.get(0).isFinished());
    if (!arguments.singlePlayer) {
      view.player2Finish(mazeGames.get(1).isFinished());
    }
    view.updateMaze(mazeGames.get(0).copyMaze(), this);
    updateInfo();
  }

  @Override
  public void player2Shoot(String shootCommandDirection, int distance) throws IOException {
    mazeGames.get(1).shootArrowTo(shootCommandDirection, distance);
    checkWumpusKilled();
    if (mazeGames.get(1).isFinished()) {
      ControllerUtils.onePlayerDies(arguments, mazeGames);
      Room curr = map[
              mazeGames.get(1).getPlayerLocation()[0]][mazeGames.get(1).getPlayerLocation()[1]];
      if (mazeGames.get(0).getPlayerLocation() == mazeGames.get(1).getPlayerLocation()) {
        curr.setPlayerSymbol("*");
      }
    }
    // update view
    view.player1Finish(mazeGames.get(0).isFinished());
    if (!arguments.singlePlayer) {
      view.player2Finish(mazeGames.get(1).isFinished());
    }
    view.updateMaze(mazeGames.get(0).copyMaze(), this);
    updateInfo();
  }

  private void checkWumpusKilled() {
    boolean killed = true;
    for (Room[] rooms: map) {
      for (Room r: rooms) {
        if (r.hasWumpus()) {
          killed = false;
        }
      }
    }
    if (killed) {
      for (MazeGame m: mazeGames) {
        m.setFinished();
      }
    }
  }

  @Override
  public void restart() throws IOException {
    // same map, new player location
    randomSeedGame++;
    // regenerate
    generateMazeGame(arguments);
    view.resetFocus();
  }

  @Override
  public void player1Move(String direction) throws IOException {
    mazeGames.get(0).playerMove(direction);
    if (mazeGames.get(0).isFinished()) {
      view.player1Finish(true);
    } else {
      view.player1Finish(false);
    }
    view.updateMaze(mazeGames.get(0).copyMaze(), this);
    view.resetFocus();
    if (!arguments.singlePlayer) {
      player1Turn = false;
      ControllerUtils.onePlayerDies(arguments, mazeGames);
    }
    updateInfo();
  }

  @Override
  public void player2Move(String direction) throws IOException {
    if (arguments.singlePlayer) {
      throw new IllegalArgumentException("\t- Player2 does not exist.");
    }
    mazeGames.get(1).playerMove(direction);
    if (mazeGames.get(1).isFinished()) {
      view.player2Finish(true);
    } else {
      view.player2Finish(false);
    }
    view.updateMaze(mazeGames.get(0).copyMaze(), this);
    view.resetFocus();
    player1Turn = true;
    ControllerUtils.onePlayerDies(arguments, mazeGames);
    updateInfo();
  }

  @Override
  public void playerMove(int row, int col) throws IOException {
    view.resetFocus();
    Room curr = map[row][col];
    boolean twoPlayerAlive = true;
    for (MazeGame m: mazeGames) {
      twoPlayerAlive = twoPlayerAlive && !m.isFinished();
    }
    String direction;
    if (twoPlayerAlive) {
      direction = findDirectionTwoPlayerAlive(curr);
      if (direction.length() == 0) {
        return;
      }
      if (player1Turn) {
        mazeGames.get(0).playerMove(direction);
        if (mazeGames.get(0).isFinished()) {
          view.player1Finish(true);
        } else {
          view.player1Finish(false);
        }
      } else {
        mazeGames.get(1).playerMove(direction);
        if (mazeGames.get(1).isFinished()) {
          view.player2Finish(true);
        } else {
          view.player2Finish(false);
        }
      }
      // don't switch turn if in single player mode
      player1Turn = arguments.singlePlayer ? player1Turn : !player1Turn;
    } else {
      direction = findDirectionOnePlayerAlive(curr);
      if (!mazeGames.get(0).isFinished()) {
        mazeGames.get(0).playerMove(direction);
        if (mazeGames.get(0).isFinished()) {
          view.player1Finish(true);
        } else {
          view.player1Finish(false);
        }
      }
      if (mazeGames.size() > 1 && !mazeGames.get(1).isFinished()) {
        mazeGames.get(1).playerMove(direction);
        if (mazeGames.get(1).isFinished()) {
          view.player2Finish(true);
        } else {
          view.player2Finish(false);
        }
      }
    }
    ControllerUtils.onePlayerDies(arguments, mazeGames);
    view.updateMaze(mazeGames.get(0).copyMaze(), this);
    updateInfo();
  }

  protected void updateInfo() throws IOException {
    Map<String, String> message = new HashMap<>();
    Map<String, String> hints = new HashMap<>();
    // mouse player
    if (player1Turn && !mazeGames.get(0).isFinished()) {
      view.setPlayerTurn(true);
    } else {
      if (mazeGames.size() > 1 && !mazeGames.get(1).isFinished()) {
        view.setPlayerTurn(false);
      }
    }
    // message
    if (!arguments.singlePlayer) {
      String m = mazeGames.get(1).getMessage();
      message.put("player2", m);
      hints.put("player2", mazeGames.get(1).getHints());
      if (m.length() != 0) {
        player2Result = m.split("\n")[m.split("\n").length - 1];
      }
    }
    String m = mazeGames.get(0).getMessage();
    message.put("player1", m);
    // hints
    hints.put("player1", mazeGames.get(0).getHints());
    if (m.length() != 0) {
      player1Result = m.split("\n")[m.split("\n").length - 1];
    }
    view.updateInfo(message, hints);
    // check if game is over
    if (!arguments.singlePlayer) {
      if (mazeGames.get(0).isFinished() && mazeGames.get(1).isFinished()) {
        view.gameOver(player1Result, player2Result, this);
      }
    } else {
      if (mazeGames.get(0).isFinished()) {
        view.gameOver(player1Result, player2Result, this);
      }
    }
  }

  private String findDirectionOnePlayerAlive(Room curr) {
    if (curr.getUp() != null) {
      if (curr.getUp().hasPlayer()) {
        return "down";
      }
    }
    if (curr.getDown() != null) {
      if (curr.getDown().hasPlayer()) {
        return "up";
      }
    }
    if (curr.getLeft() != null) {
      if (curr.getLeft().hasPlayer()) {
        return "right";
      }
    }
    if (curr.getRight() != null) {
      if (curr.getRight().hasPlayer()) {
        return "left";
      }
    }
    return "";
  }

  private String findDirectionTwoPlayerAlive(Room curr) {
    String symbol;
    if (player1Turn) {
      symbol = "*";
    } else {
      symbol = "/";
    }
    // when 2 players are in the same location
    if (!arguments.singlePlayer
            && Arrays.equals(
            mazeGames.get(0).getPlayerLocation(), mazeGames.get(1).getPlayerLocation())) {
      if (player1Turn) {
        symbol = "/";
      } else {
        symbol = "*";
      }
    }
    if (curr.getUp() != null) {
      if (curr.getUp().hasPlayer() && curr.getUp().getPlayerSymbol().equals(symbol)) {
        return "down";
      }
    }
    if (curr.getDown() != null) {
      if (curr.getDown().hasPlayer() && curr.getDown().getPlayerSymbol().equals(symbol)) {
        return "up";
      }
    }
    if (curr.getLeft() != null) {
      if (curr.getLeft().hasPlayer() && curr.getLeft().getPlayerSymbol().equals(symbol)) {
        return "right";
      }
    }
    if (curr.getRight() != null) {
      if (curr.getRight().hasPlayer() && curr.getRight().getPlayerSymbol().equals(symbol)) {
        return "left";
      }
    }
    throw new IllegalArgumentException("- You cannot move to there.");
  }

  @Override
  public void newGame() throws IOException {
    // new map and player locations
    randomSeedGame++;
    randomSeedMap++;
    view.configuration();
    view.resetFocus();
  }

  @Override
  public void execute(MazeView view) throws IOException {
    this.view = view;
    this.view.addFeatures(this);
    this.player1Turn = true;
    this.view.configuration();
  }
}
