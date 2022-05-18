# README

"Hunt the Wumpus" is a famous game, you can find more info in here, https://en.wikipedia.org/wiki/Hunt_the_Wumpus.
This project used Java Swing to build the game, which supports both single-player and multi-player mode.

Related topics:
* Java Swing
* Maze Generator (Randomized Kruskal's algorithm)
* MVC Design Pattern

## How to use
* Go to ```res/``` directory, there should be a  ```.jar``` file. To run the ```.jar``` program, use this command:

    ```
    java -jar hw6.jar <--text | --gui>
    ```
    * ```--text``` will build a text based game view
    * ```--gui``` will build a graphic game view
    * Java version = 11.0.8

* GUI view:
    * First, configure the game by specifying the arguments below:
        * The number of rows of the maze
        * The number of columns of the maze
        * The number of walls remaining in the maze
        * The proportion of rooms with bats, from 0 to 1
        * The proportion of rooms with a pit, from 0 to 1
        * Single-player or multi-player mode
    * Second, play the game and try to kill the Wumpus:
        * Move:
            * Use Mouse (one player at a time): players take turns to move (multi-player mode) by clicking the adjacent grid.
            You can only go to the adjacent rooms that are around your current room. Nothing will happen 
            if you click any unreachable room.
            * Use keyboard (two players play at the same time): two players can move around at the same time without 
            waiting on each other.
                * Player 1 (red): use W, A, S, D to move
                * Player 2 (blue): use Arrow Keys to move
            * Note: if mouse and keyboard are using at the same time, keyboard control will affect the players' turn. After P1 moves
            using keyboard, next mouse click will be P2 moving.
        * Shooting: to shoot an arrow, a player must use a keyboard combination:
            * Player 1 (red): CTRL + W, A, S, D to shoot an arrow to the corresponding direction
            * Player 2 (blue): CTRL + Arrow Keys to shoot an arrow to the corresponding direction
            * After the key combination, a popup window will appear, and the player needs to enter the distance.
                * Note: two players cannot shoot arrows at the same time.
    * Last, the game will be over when:
        1. One of the player kills the Wumpus
        1. Two players are both dead because of falling into a pit or swallowed by the Wumpus
        1. A player is considered as dead if he/she uses up all 3 arrows
        
        After the game is over, players can choose to restart or start a new game.

* Text view:
    * First, configure the game by specifying the same arguments as above.
    * Second, play the game and try to kill the Wumpus. The players can only use keyboard to control.
        * Move: 
            1. Enter command 'm' to move
            1. Enter direction, 'w', 'a', 's', 'd' (or 'up', 'left', 'down', 'right')
        * Shooting: 
            1. Enter command 'a' to attack
            1. Enter direction, 'w', 'a', 's', 'd' (or 'up', 'left', 'down', 'right')
            1. Enter distance
        * Additional commands:
            1. Enter 'restart' to restart the game
            1. Enter 'new' to start a new game
            1. Enter 'q' to quit the game
    * Last, after the game is over, players can choose:
        1. Enter '1' to restart
        1. Enter '2' to start a new game
        1. Enter '3' to quit
       
# Features
* Easy and full customization of the maze
* Single-player and multi-player mode
* Randomly generated maze map
