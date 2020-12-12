package view.components;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.Box;
import javax.swing.border.EmptyBorder;

import controller.Features;
import maze.Room;
import view.ColorTheme;
import view.Images;

/**
 * A view for the maze game.
 */
public class GameView implements Game {

  private final JPanel main = new JPanel();
  private final JPanel game = new JPanel();
  private final JScrollPane mazeView = new JScrollPane();
  private final JTextArea mouseTurn = new JTextArea("");
  private final JTextArea logBodyP1 = new JTextArea("");
  private final JTextArea hintBodyP1 = new JTextArea("");
  private final JTextArea logBodyP2 = new JTextArea("");
  private final JTextArea hintBodyP2 = new JTextArea("");
  private final JButton restart = new JButton("Restart ");
  private final JButton newGame = new JButton("New Game");
  private final Images images = new Images();
  private final JTextArea errorP1 = new JTextArea("");
  private final JTextArea errorP2 = new JTextArea("");
  private int windowWidth = 0;
  private int windowHeight = 0;

  /**
   * Construct a game view.
   */
  public GameView() {
    constructView();
  }

  private void constructView() {
    main.setLayout(new BorderLayout());
    main.setSize(1600, 900);
    main.setBackground(Color.BLACK);
    // game view
    game.setBackground(ColorTheme.MAIN);
    main.add(game, BorderLayout.CENTER);

    // message view
    constructMessage();
  }

  private void constructMessage() {
    // use grid bag layout to construct the view
    // this is the right side bar
    int unitWidth = 265;
    JPanel message = new JPanel();
    message.setBackground(ColorTheme.SIDE);
    message.setLayout(new GridBagLayout());
    message.setBorder(new EmptyBorder(
            new Insets(10, 10, 10, 10)));
    GridBagConstraints gbc = new GridBagConstraints();

    int y = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 2;
    JLabel mouseTitle = new JLabel("- Current Mouse Player:");
    ComponentUtils.setBoldFont(mouseTitle);
    message.add(mouseTitle, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 1;
    mouseTurn.setEditable(false);
    ComponentUtils.setFont(mouseTurn);
    ComponentUtils.setEmptyBorder(mouseTurn, 10);
    message.add(mouseTurn, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    // log
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 2;
    JLabel title = new JLabel("- Message: ");
    ComponentUtils.setBoldFont(title);
    message.add(title, gbc);
    gbc.gridx = 1;
    gbc.gridy = y;
    message.add(Box.createHorizontalGlue(), gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    // log body
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.BOTH;
    ComponentUtils.setFont(logBodyP1);
    ComponentUtils.setEmptyBorder(logBodyP1, 10);
    logBodyP1.setEditable(false);
    logBodyP1.setPreferredSize(new Dimension(unitWidth, 100));
    message.add(logBodyP1, gbc);
    gbc.gridx = 1;
    gbc.gridy = y;
    logBodyP2.setEditable(false);
    logBodyP2.setPreferredSize(new Dimension(unitWidth, 100));
    ComponentUtils.setFont(logBodyP2);
    ComponentUtils.setEmptyBorder(logBodyP2, 10);
    message.add(logBodyP2, gbc);
    y += 5;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    // hints
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    JLabel hint = new JLabel("- Hints: ");
    ComponentUtils.setBoldFont(hint);
    message.add(hint, gbc);
    gbc.gridx = 1;
    gbc.gridy = y;
    message.add(Box.createHorizontalGlue(), gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    // hint body
    y++;
    ComponentUtils.setFont(hintBodyP1);
    ComponentUtils.setEmptyBorder(hintBodyP1, 10);
    ComponentUtils.setFont(hintBodyP2);
    ComponentUtils.setEmptyBorder(hintBodyP2, 10);
    hintBodyP1.setEditable(false);
    hintBodyP2.setEditable(false);
    hintBodyP1.setPreferredSize(new Dimension(unitWidth, 150));
    hintBodyP2.setPreferredSize(new Dimension(unitWidth, 150));
    gbc.gridx = 0;
    gbc.gridy = y;
    message.add(hintBodyP1, gbc);
    gbc.gridx = 1;
    gbc.gridy = y;
    message.add(hintBodyP2, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    // error
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.fill = GridBagConstraints.BOTH;
    JLabel errorTitle = new JLabel("- Error: ");
    ComponentUtils.setBoldFont(errorTitle);
    message.add(errorTitle, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0,0,0,0);
    errorP1.setEditable(false);
    errorP1.setPreferredSize(new Dimension(unitWidth, 40));
    ComponentUtils.setErrorMessage(errorP1);
    ComponentUtils.setEmptyBorder(errorP1, 10);
    message.add(errorP1, gbc);
    gbc.gridx = 1;
    gbc.gridy = y;
    errorP2.setEditable(false);
    errorP2.setPreferredSize(new Dimension(unitWidth, 40));
    ComponentUtils.setErrorMessage(errorP2);
    ComponentUtils.setEmptyBorder(errorP2, 10);
    message.add(errorP2, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    // buttons
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.insets = new Insets(0,0,0,10);
    message.add(restart, gbc);
    gbc.gridx = 1;
    gbc.gridy = y;
    gbc.insets = new Insets(0,10,0,0);
    message.add(newGame, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.insets = new Insets(0, 0,0,0);
    JLabel ruleTitle = new JLabel("- Game Rules:");
    ComponentUtils.setBoldFont(ruleTitle);
    message.add(ruleTitle, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 2;
    JTextArea rules = new JTextArea(
            "- You need to kill the Wumpus! Remember, you only have 3 arrows.\n"
            + "- You will get hints about the adjacent rooms.\n"
            + "- Bats may take you to a random location.\n"
            + "- You fall into a pit, you die."
    );
    ComponentUtils.setFont(rules);
    ComponentUtils.setEmptyBorder(rules, 10);
    rules.setPreferredSize(new Dimension(2 * unitWidth, 100));
    message.add(rules, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    // legend
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 1;
    JLabel legend = new JLabel("- Legend:");
    ComponentUtils.setBoldFont(legend);
    message.add(legend, gbc);
    y++;
    ComponentUtils.createHorizontalGap(message, gbc, y, 10);
    y++;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    JButton p1 = new JButton(
            new ImageIcon(
                    images.getPlayer1().getImage().getScaledInstance(
                            40, 40, Image.SCALE_SMOOTH)));
    p1.setMargin(new Insets(0 ,0, 0, 0));
    ComponentUtils.setEmptyBorder(p1, 10);
    p1.setText("  - Player 1: (W, A, S, D) to move, (CTRL + W, A ,S, D) to shoot an arrow");
    ComponentUtils.setFont(p1);
    message.add(p1, gbc);
    y++;
    gbc.gridx = 0;
    gbc.gridy = y;
    gbc.gridwidth = 2;
    JButton p2 = new JButton(
            new ImageIcon(
                    images.getPlayer2().getImage().getScaledInstance(
                            40, 40, Image.SCALE_SMOOTH)));
    p2.setMargin(new Insets(0 ,0, 0, 0));
    ComponentUtils.setEmptyBorder(p2, 10);
    p2.setText("  - Player 2: (Arrow Key) to move, (CTRL + Arrow Key) to shoot an arrow");
    ComponentUtils.setFont(p2);
    message.add(p2, gbc);

    main.add(message, BorderLayout.EAST);
  }

  @Override
  public void setPlayerTurn(boolean player1Turn) {
    String message = player1Turn ? "- Player 1" : "- Player 2";
    mouseTurn.setText(message);
  }

  @Override
  public void setErrorMessage(String message, boolean isP1) {
    if (isP1) {
      errorP1.setText(message);
    } else {
      errorP2.setText(message);
    }
  }

  @Override
  public void clearErrorMessage(boolean isP1) {
    if (isP1) {
      errorP1.setText("");
    } else {
      errorP2.setText("");
    }
  }

  @Override
  public void updateInfo(Map<String, String> message, Map<String, String> hints) {
    // player 1
    if (message.get("player1") != null && message.get("player1").length() > 0) {
      this.logBodyP1.setText("- Player 1\n" + message.get("player1"));
    }
    if (hints.get("player1") != null && hints.get("player1").length() > 0) {
      this.hintBodyP1.setText("- Player 1\n" + hints.get("player1"));
    }
    // player 2
    if (message.get("player2") != null && message.get("player2").length() > 0) {
      this.logBodyP2.setText("- Player 2\n" + message.get("player2"));
    }
    if (hints.get("player2") != null && hints.get("player2").length() > 0) {
      this.hintBodyP2.setText("- Player 2\n" + hints.get("player2"));
    }
  }

  private void reset() {
    logBodyP1.setText("");
    logBodyP2.setText("");
    hintBodyP2.setText("");
    hintBodyP1.setText("");
    mouseTurn.setText("");
    clearErrorMessage(true);
    clearErrorMessage(false);
  }

  @Override
  public void addListener(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    this.newGame.addActionListener(l -> {
      reset();
      try {
        features.newGame();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    this.restart.addActionListener(l -> {
      reset();
      try {
        features.restart();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  @Override
  public JComponent getMainComponent() {
    return this.main;
  }

  @Override
  public void setVisible() {
    this.main.setVisible(true);
  }

  @Override
  public void setInvisible() {
    this.main.setVisible(false);
  }

  @Override
  public void initializeMaze(Room[][] map, Features features) {
    if (map == null || map.length == 0 || map[0].length == 0) {
      throw new IllegalArgumentException("Null or invalid map dimension.");
    }
    reset();
    game.removeAll();
    int x = map.length;
    int y = map[0].length;
    if (windowHeight == 0) {
      windowHeight = (int) (game.getHeight() * 0.9);
      windowWidth = windowHeight;
    }
    JPanel maze = mazeButton(map, features, x, y);

    mazeView.setViewportView(maze);
    GridBagConstraints gbc = new GridBagConstraints();
    mazeView.setPreferredSize(new Dimension(windowWidth, windowHeight));
    mazeView.setMaximumSize(new Dimension(windowWidth, windowHeight));
    mazeView.setMinimumSize(new Dimension(windowWidth, windowHeight));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.fill = GridBagConstraints.BOTH;
    game.add(mazeView, gbc);
    game.setLayout(new GridBagLayout());
  }

  @Override
  public void updateMaze(Room[][] map, Features features) {
    int x = map.length;
    int y = map[0].length;
    JPanel maze = mazeButton(map, features, x, y);
    int h = mazeView.getHorizontalScrollBar().getValue();
    int v = mazeView.getVerticalScrollBar().getValue();
    mazeView.setViewportView(maze);
    mazeView.getHorizontalScrollBar().setValue(h);
    mazeView.getVerticalScrollBar().setValue(v);
  }

  private JPanel mazeButton(Room[][] map, Features features, int x, int y) {
    if (map == null || map.length == 0 || map[0].length == 0) {
      throw new IllegalArgumentException("Null or invalid map dimension.");
    }
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    JPanel maze = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        gbc.gridx = j;
        gbc.gridy = i;
        int sideLength = 125;
        ImageIcon img = assignImageIcon(map[i][j]);
        Icon resized_img = resizeIcon(img, sideLength, sideLength);
        JButton room = new JButton(resized_img);
        room.setBackground(Color.WHITE);
        room.setActionCommand(String.format("%d,%d", i, j));
        room.addActionListener(l -> {
          String[] command = l.getActionCommand().split(",");
          int row = Integer.parseInt(command[0]);
          int col = Integer.parseInt(command[1]);
          try {
            clearErrorMessage(mouseTurn.getText().equals("- Player 1"));
            features.playerMove(row, col);
          } catch (IllegalArgumentException | IOException e) {
            if (mouseTurn.getText().equals("- Player 1")) {
              errorP1.setText(e.getMessage());
            } else {
              errorP2.setText(e.getMessage());
            }
          }
        });
        room.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
        room.setPreferredSize(new Dimension(sideLength - 1, sideLength - 1));
        maze.add(room, gbc);
      }
    }
    return maze;
  }

  private ImageIcon assignImageIcon(Room room) {
    if (room == null) {
      throw new IllegalArgumentException("Null room.");
    }
    if (!room.isTravelled()) {
      return images.getHiddenRoom();
    }
    // empty
    if (!room.hasWumpus() && !room.hasPlayer()
            && !room.hasBats() && !room.hasPit()) {
      return emptyRoomIcon(room);
    } else if (!room.hasPlayer()
            && !room.hasBats() && !room.hasPit()) {
      // only has wumpus
      return wumpusOnlyRoom(room);
    } else if (!room.hasWumpus() && !room.hasPlayer()
            && !room.hasPit()) {
      // only has bats
      return batOnlyRoom(room);
    } else if (!room.hasWumpus() && !room.hasPlayer()
            && !room.hasBats()) {
      // only has pit
      return pitOnlyRoom(room);
    } else if (!room.hasWumpus()
            && !room.hasBats() && !room.hasPit()) {
      // only has player
      return playerOnlyRoom(room);
    } else if (!room.hasBats() && !room.hasPit()) {
      // wumpus + player
      return  playerOnlyRoom(room);
    } else if (!room.hasPlayer() && !room.hasPit()) {
      // wumpus + bats
      return wumpusBatRoom(room);
    } else if (!room.hasPlayer() && !room.hasBats()) {
      // wumpus + pit
      return wumpusPitRoom(room);
    } else if (!room.hasWumpus() && !room.hasPit()) {
      // player + bat
      return playerOnlyRoom(room);
    } else if (!room.hasWumpus() && !room.hasBats()) {
      // player + pit
      return pitOnlyRoom(room);
    } else if (!room.hasWumpus() && !room.hasPlayer()) {
      // bat + pit
      return batPitRoom(room);
    } else if (!room.hasPlayer()) {
      // wumpus + pit + bat
      return wumpusBatPitRoom(room);
    } else if (!room.hasWumpus()) {
      // bat + pit + player
      return batPitRoom(room);
    } else if (!room.hasBats()) {
      // wumpus + pit + player
      return wumpusPitRoom(room);
    } else {
      // wumpus + bat + player
      return wumpusBatPitRoom(room);
    }
  }

  /*
  Methods below are to retrieve correct image for each room
   */
  private ImageIcon wumpusBatPitRoom(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusBatPitWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomWumpusBatPitASD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusBatPitWAD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomWumpusBatPitWSD();
    } else {
      return images.getRoomWumpusBatPitWAS();
    }
  }

  private ImageIcon batPitRoom(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomBatPitWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomBatPitASD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomBatPitWAD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomBatPitWSD();
    } else {
      return images.getRoomBatPitWAS();
    }
  }

  private ImageIcon wumpusPitRoom(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusPitWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomWumpusPitASD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusPitWAD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomWumpusPitWSD();
    } else {
      return images.getRoomWumpusPitWAS();
    }
  }

  private ImageIcon wumpusBatRoom(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusBatWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomWumpusBatASD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusBatWAD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomWumpusBatWSD();
    } else {
      return images.getRoomWumpusBatWAS();
    }
  }

  private ImageIcon playerOnlyRoom(Room room) {
    if (room.getPlayerSymbol().equals("*")) {
      if (room.getUp() != null && room.getDown() != null
              && room.getLeft() != null && room.getRight() != null) {
        return images.getRoomPlayer1WASD();
      } else if (room.getUp() == null && room.getRight() != null
              && room.getLeft() != null && room.getDown() != null) {
        return images.getRoomPlayer1ASD();
      } else if (room.getLeft() == null && room.getUp() != null
              && room.getDown() != null && room.getRight() != null) {
        return images.getRoomPlayer1WSD();
      } else if (room.getDown() == null && room.getUp() != null
              && room.getLeft() != null && room.getRight() != null) {
        return images.getRoomPlayer1WAD();
      } else if (room.getRight() == null && room.getUp() != null
              && room.getLeft() != null && room.getDown() != null) {
        return images.getRoomPlayer1WAS();
      } else if (room.getUp() != null && room.getLeft() != null
              && room.getRight() == null && room.getDown() == null) {
        return images.getRoomPlayer1WA();
      } else if (room.getUp() != null && room.getDown() != null
              && room.getLeft() == null && room.getRight() == null) {
        return images.getRoomPlayer1WS();
      } else if (room.getUp() != null && room.getRight() != null
              && room.getLeft() == null && room.getDown() == null) {
        return images.getRoomPlayer1WD();
      } else if (room.getLeft() != null && room.getDown() != null
              && room.getRight() == null && room.getUp() == null) {
        return images.getRoomPlayer1AS();
      } else if (room.getLeft() != null && room.getRight() != null
              && room.getUp() == null && room.getDown() == null) {
        return images.getRoomPlayer1AD();
      } else if (room.getDown() != null && room.getRight() != null
              && room.getUp() == null && room.getLeft() == null) {
        return images.getRoomPlayer1SD();
      } else if (room.getUp() != null && room.getLeft() == null
              && room.getRight() == null && room.getDown() == null) {
        return images.getRoomPlayer1W();
      } else if (room.getLeft() != null && room.getDown() == null
              && room.getUp() == null && room.getRight() == null) {
        return images.getRoomPlayer1A();
      } else if (room.getDown() != null && room.getUp() == null
              && room.getLeft() == null && room.getRight() == null) {
        return images.getRoomPlayer1S();
      } else {
        return images.getRoomPlayer1D();
      }
    } else {
      if (room.getUp() != null && room.getDown() != null
              && room.getLeft() != null && room.getRight() != null) {
        return images.getRoomPlayer2WASD();
      } else if (room.getUp() == null && room.getRight() != null
              && room.getLeft() != null && room.getDown() != null) {
        return images.getRoomPlayer2ASD();
      } else if (room.getLeft() == null && room.getUp() != null
              && room.getDown() != null && room.getRight() != null) {
        return images.getRoomPlayer2WSD();
      } else if (room.getDown() == null && room.getUp() != null
              && room.getLeft() != null && room.getRight() != null) {
        return images.getRoomPlayer2WAD();
      } else if (room.getRight() == null && room.getUp() != null
              && room.getLeft() != null && room.getDown() != null) {
        return images.getRoomPlayer2WAS();
      } else if (room.getUp() != null && room.getLeft() != null
              && room.getRight() == null && room.getDown() == null) {
        return images.getRoomPlayer2WA();
      } else if (room.getUp() != null && room.getDown() != null
              && room.getLeft() == null && room.getRight() == null) {
        return images.getRoomPlayer2WS();
      } else if (room.getUp() != null && room.getRight() != null
              && room.getLeft() == null && room.getDown() == null) {
        return images.getRoomPlayer2WD();
      } else if (room.getLeft() != null && room.getDown() != null
              && room.getRight() == null && room.getUp() == null) {
        return images.getRoomPlayer2AS();
      } else if (room.getLeft() != null && room.getRight() != null
              && room.getUp() == null && room.getDown() == null) {
        return images.getRoomPlayer2AD();
      } else if (room.getDown() != null && room.getRight() != null
              && room.getUp() == null && room.getLeft() == null) {
        return images.getRoomPlayer2SD();
      } else if (room.getUp() != null && room.getLeft() == null
              && room.getRight() == null && room.getDown() == null) {
        return images.getRoomPlayer2W();
      } else if (room.getLeft() != null && room.getDown() == null
              && room.getUp() == null && room.getRight() == null) {
        return images.getRoomPlayer2A();
      } else if (room.getDown() != null && room.getUp() == null
              && room.getLeft() == null && room.getRight() == null) {
        return images.getRoomPlayer2S();
      } else {
        return images.getRoomPlayer2D();
      }
    }
  }

  private ImageIcon emptyRoomIcon(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomEmptyWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomEmptyASD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomEmptyWSD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomEmptyWAD();
    } else if (room.getRight() == null && room.getUp() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomEmptyWAS();
    } else if (room.getUp() != null && room.getLeft() != null
            && room.getRight() == null && room.getDown() == null) {
      return images.getRoomEmptyWA();
    } else if (room.getUp() != null && room.getDown() != null
            && room.getLeft() == null && room.getRight() == null) {
      return images.getRoomEmptyWS();
    } else if (room.getUp() != null && room.getRight() != null
            && room.getLeft() == null && room.getDown() == null) {
      return images.getRoomEmptyWD();
    } else if (room.getLeft() != null && room.getDown() != null
            && room.getRight() == null && room.getUp() == null) {
      return images.getRoomEmptyAS();
    } else if (room.getLeft() != null && room.getRight() != null
            && room.getUp() == null && room.getDown() == null) {
      return images.getRoomEmptyAD();
    } else if (room.getDown() != null && room.getRight() != null
            && room.getUp() == null && room.getLeft() == null) {
      return images.getRoomEmptySD();
    } else if (room.getUp() != null && room.getLeft() == null
            && room.getRight() == null && room.getDown() == null) {
      return images.getRoomEmptyW();
    } else if (room.getLeft() != null && room.getDown() == null
            && room.getUp() == null && room.getRight() == null) {
      return images.getRoomEmptyA();
    } else if (room.getDown() != null && room.getUp() == null
            && room.getLeft() == null && room.getRight() == null) {
      return images.getRoomEmptyS();
    } else {
      return images.getRoomEmptyD();
    }
  }

  private ImageIcon pitOnlyRoom(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomPitWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomPitASD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomPitWAD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomPitWSD();
    } else {
      return images.getRoomPitWAS();
    }
  }

  private ImageIcon wumpusOnlyRoom(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomWumpusASD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomWumpusWAD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomWumpusWSD();
    } else {
      return images.getRoomWumpusWAS();
    }
  }

  private ImageIcon batOnlyRoom(Room room) {
    if (room.getUp() != null && room.getDown() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomBatWASD();
    } else if (room.getUp() == null && room.getRight() != null
            && room.getLeft() != null && room.getDown() != null) {
      return images.getRoomBatASD();
    } else if (room.getDown() == null && room.getUp() != null
            && room.getLeft() != null && room.getRight() != null) {
      return images.getRoomBatWAD();
    } else if (room.getLeft() == null && room.getUp() != null
            && room.getDown() != null && room.getRight() != null) {
      return images.getRoomBatWSD();
    } else {
      return images.getRoomBatWAS();
    }
  }

  private Icon resizeIcon(ImageIcon icon, int width, int height) {
    if (width < 100 && height < 100) {
      width = 100;
      height = 100;
    }
    Image img = icon.getImage();
    Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(resizedImg);
  }

}
