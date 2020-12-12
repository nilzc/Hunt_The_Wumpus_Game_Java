package view;

import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import controller.Features;
import maze.Room;
import view.components.Game;
import view.components.GameOver;
import view.components.GameView;
import view.components.MazeComponent;
import view.components.Menu;

/**
 * A GUI view class that implements {@link MazeView}.
 */
public class GUIView extends JFrame implements MazeView {

  private final MazeComponent menu = new Menu();
  private final Game game = new GameView();
  private MazeComponent gameOver = new GameOver("", "");
  private boolean moving = true;
  private boolean shootCommandFlag = false;
  private String shootCommandDirection = "";
  private boolean player1Shoot = true;
  private boolean player1Finish = false;
  private boolean player2Finish = false;

  /**
   * Construct a GUI view.
   */
  public GUIView() {
    super();

    add(menu.getMainComponent());
    add(game.getMainComponent());

    configuration();
    setPreferredSize(new Dimension(1600, 900));
    setVisible(true);
    setLayout(new CardLayout());
    this.pack();
  }

  @Override
  public void addFeatures(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    menu.addListener(features);
    game.addListener(features);
    gameOver.addListener(features);
  }

  @Override
  public void configuration() {
    resetStatus();
    gameOver.setInvisible();
    game.setInvisible();
    menu.setVisible();
  }

  @Override
  public void startGame(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    gameOver.setInvisible();
    menu.setInvisible();
    game.setVisible();
  }

  @Override
  public void gameOver(String player1Result, String player2Result, Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    gameOver = new GameOver(player1Result, player2Result);
    gameOver.addListener(features);
    add(gameOver.getMainComponent());
    menu.setInvisible();
    game.setInvisible();
    gameOver.setVisible();
  }

  @Override
  public void initializeMaze(Room[][] map, Features features) {
    if (map == null || map.length == 0 || map[0].length == 0) {
      throw new IllegalArgumentException("Null or invalid map dimension.");
    }
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    game.initializeMaze(map, features);
    if (this.getKeyListeners().length != 0) {
      for (KeyListener k: this.getKeyListeners()) {
        this.removeKeyListener(k);
      }
    }
    addKeyBoardMovingFeature(features);
    resetFocus();
    this.pack();
  }

  private void addKeyBoardMovingFeature(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // no need to implement
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // for keyboard action
        try {
          // move
          switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
              if (moving) {
                features.player2Move("left");
                game.clearErrorMessage(false);
              }
              player1Shoot = false;
              shootCommandDirection = "left";
              break;
            case KeyEvent.VK_UP:
              if (moving) {
                features.player2Move("up");
                game.clearErrorMessage(false);
              }
              player1Shoot = false;
              shootCommandDirection = "up";
              break;
            case KeyEvent.VK_DOWN:
              if (moving) {
                features.player2Move("down");
                game.clearErrorMessage(false);
              }
              player1Shoot = false;
              shootCommandDirection = "down";
              break;
            case KeyEvent.VK_RIGHT:
              if (moving) {
                features.player2Move("right");
                game.clearErrorMessage(false);
              }
              player1Shoot = false;
              shootCommandDirection = "right";
              break;
            default:
              // no action needed
          }
        } catch (Exception error) {
          game.setErrorMessage(error.getMessage(), false);
        }

        try {
          // move
          switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
              if (moving) {
                features.player1Move("up");
                game.clearErrorMessage(true);
              }
              player1Shoot = true;
              shootCommandDirection = "up";
              break;
            case KeyEvent.VK_A:
              if (moving) {
                features.player1Move("left");
                game.clearErrorMessage(true);
              }
              player1Shoot = true;
              shootCommandDirection = "left";
              break;
            case KeyEvent.VK_S:
              if (moving) {
                features.player1Move("down");
                game.clearErrorMessage(true);
              }
              player1Shoot = true;
              shootCommandDirection = "down";
              break;
            case KeyEvent.VK_D:
              if (moving) {
                features.player1Move("right");
                game.clearErrorMessage(true);
              }
              player1Shoot = true;
              shootCommandDirection = "right";
              break;
            default:
              // no action needed
          }
        } catch (Exception error) {
          game.setErrorMessage(error.getMessage(), true);
        }

        // prepare shooting
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          moving = false;
          shootCommandFlag = true;
        }

        // for shooting
        shootingArrow(features);
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // shooting ends or aborts
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
          shootCommandFlag = false;
          moving = true;
        } else {
          shootCommandDirection = "";
        }
      }
    });
  }

  @Override
  public void setPlayerTurn(boolean player1Turn) {
    game.setPlayerTurn(player1Turn);
  }

  private void shootingArrow(Features features) {
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    if (shootCommandFlag && shootCommandDirection.length() != 0) {
      int distance;
      while (true) {
        try {
          if ((player1Finish && player1Shoot) || (player2Finish && !player1Shoot)) {
            break;
          }
          distance = Integer.parseInt(
                  JOptionPane.showInputDialog(this, "Enter Distance:"));
          if (player1Shoot) {
            features.player1Shoot(shootCommandDirection, distance);
          } else {
            features.player2Shoot(shootCommandDirection, distance);
          }
          break;
        } catch (Exception e) {
          if (e.getMessage().equals("null")) {
            break;
          }
          JOptionPane.showMessageDialog(this, "Invalid distance value.");
        }
      }
      // reset
      shootCommandFlag = false;
      moving = true;
      shootCommandDirection = "";
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
  public void updateMaze(Room[][] map, Features features) {
    if (map == null || map.length == 0 || map[0].length == 0) {
      throw new IllegalArgumentException("Null or invalid map dimension.");
    }
    if (features == null) {
      throw new IllegalArgumentException("Null controller.");
    }
    game.updateMaze(map, features);
    resetFocus();
    this.pack();
  }

  @Override
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  @Override
  public void updateInfo(Map<String, String> message, Map<String, String> hints) {
    game.updateInfo(message, hints);
  }

  @Override
  public void resetStatus() {
    moving = true;
    shootCommandFlag = false;
    shootCommandDirection = "";
    player1Shoot = true;
    player1Finish = false;
    player2Finish = false;
  }
}
