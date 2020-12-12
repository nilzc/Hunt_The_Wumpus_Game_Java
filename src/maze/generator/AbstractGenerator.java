package maze.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import maze.MazeRoom;
import maze.Room;

/**
 * Abstract class to implement {@link MazeGenerator}.
 */
abstract class AbstractGenerator implements MazeGenerator {

  private final int numberOfRows;
  private final int numberOfColumns;
  private final Room[][] map;
  private final ArrayList<int[]> allEdges = new ArrayList<>();
  private final int randomSeed;
  private final List<int[]> rooms = new ArrayList<>();
  private final double batsPercent;
  private final double pitsPercent;

  /**
   * Constructs an abstract maze generator class.
   *
   * @param x the number of rows
   * @param y the number of columns
   */
  public AbstractGenerator(int x, int y, double bats, double pits, int seed) {
    if (x <= 0 || y <= 0) {
      throw new IllegalArgumentException("Non-positive maze dimensions.\n");
    }
    if (x < 2 || y < 2) {
      throw new IllegalArgumentException("Maze is too small.\n");
    }
    if (bats < 0 || bats >= 1) {
      throw new IllegalArgumentException("Invalid bats percent.\n");
    }
    if (pits < 0 || pits >= 1) {
      throw new IllegalArgumentException("Invalid pits percent.\n");
    }
    this.numberOfRows = x;
    this.numberOfColumns = y;
    this.map = new MazeRoom[x][y];
    this.randomSeed = seed;
    this.batsPercent = bats;
    this.pitsPercent = pits;
    this.initialize(x, y);
  }

  private void initialize(int n, int x) {
    int identifier = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < x; j++) {
        Room temp = new MazeRoom(new int[]{i, j});
        temp.setIdentifier(identifier);
        identifier++;
        this.map[i][j] = temp;
      }
    }
    // add edges
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < x - 1; j++) {
        this.allEdges.add(new int[]{i, j, i, j + 1});
      }
    }
    for (int j = 0; j < x; j++) {
      for (int i = 0; i < n - 1; i++) {
        this.allEdges.add(new int[]{i, j, i + 1, j});
      }
    }

  }

  @Override
  public Room[][] getMap() {
    return this.map;
  }

  /**
   * Gets the number of rows.
   *
   * @return the number of rows
   */
  protected int getNumberOfRows() {
    return this.numberOfRows;
  }

  /**
   * Gets the number of columns.
   *
   * @return the number of columns
   */
  protected int getNumberOfColumns() {
    return this.numberOfColumns;
  }

  /**
   * Gets all the edges in the initial maze before generating.
   *
   * @return all edges
   */
  protected ArrayList<int[]> getAllEdges() {
    return this.allEdges;
  }

  /**
   * Generates a perfect maze.
   *
   * @param allEdges all the edges in the initial map
   */
  protected void generatePerfect(ArrayList<int[]> allEdges) {
    Random randomNum = new Random();
    randomNum.setSeed(this.randomSeed);
    int numberOfCells = numberOfRows * numberOfColumns;
    int removeCount = 0;
    List<HashSet<Integer>> edgeSets = new ArrayList<HashSet<Integer>>();
    prepareEdgeSet(edgeSets);

    while (removeCount < numberOfCells - 1) {
      int[] edge = allEdges.get(randomNum.nextInt(allEdges.size()));
      int[] indexOne = new int[]{
              edge[0], edge[1]
      };
      int[] indexTwo = new int[]{
              edge[2], edge[3]
      };
      Room one = map[indexOne[0]][indexOne[1]];
      Room two = map[indexTwo[0]][indexTwo[1]];
      HashSet<Integer> oneSet = edgeSets.get(one.getIdentifier());
      HashSet<Integer> twoSet = edgeSets.get(two.getIdentifier());
      if (!(oneSet.equals(twoSet))) {
        // left and right
        if (indexOne[1] < indexTwo[1]) {
          one.setRight(two);
          two.setLeft(one);
        } else if (indexOne[0] < indexTwo[0]) {
          // up and down
          one.setDown(two);
          two.setUp(one);
        } else if (indexOne[1] > indexTwo[1]) {
          // left and right border
          one.setRight(two);
          two.setLeft(one);
        } else if (indexOne[0] > indexTwo[0]) {
          // up and down border
          one.setDown(two);
          two.setUp(one);
        }
        // update sets
        oneSet.addAll(twoSet);
        for (Integer item: oneSet) {
          edgeSets.set(item, oneSet);
        }
        allEdges.remove(edge);
        removeCount++;
      } else {
        allEdges.remove(edge);
      }
    }
    this.addSpecialRooms();
  }

  private void prepareEdgeSet(List<HashSet<Integer>> edgeSets) {
    for (int i = 0; i < numberOfRows; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        HashSet<Integer> temp = new HashSet<>();
        temp.add(this.getMap()[i][j].getIdentifier());
        edgeSets.add(temp);
      }
    }
  }

  /**
   * Generates a non perfect maze.
   *
   * @param numberOfRemainWalls the number of walls remain
   */
  protected void generateNonPerfect(int numberOfRemainWalls) {
    ArrayList<int[]> remainWalls = new ArrayList<>();
    Random randomNum = new Random();
    randomNum.setSeed(this.randomSeed);
    int numberOfCells = numberOfRows * numberOfColumns;
    int removeCount = 0;
    final int numberOfAllEdges = allEdges.size();
    List<HashSet<Integer>> edgeSets = new ArrayList<HashSet<Integer>>();
    prepareEdgeSet(edgeSets);

    while (removeCount < numberOfCells - 1) {
      int[] edge = allEdges.get(randomNum.nextInt(allEdges.size()));
      int[] indexOne = new int[]{
              edge[0], edge[1]
      };
      int[] indexTwo = new int[]{
              edge[2], edge[3]
      };
      Room one = map[indexOne[0]][indexOne[1]];
      Room two = map[indexTwo[0]][indexTwo[1]];
      HashSet<Integer> oneSet = edgeSets.get(one.getIdentifier());
      HashSet<Integer> twoSet = edgeSets.get(two.getIdentifier());
      if (!(oneSet.equals(twoSet))) {
        // left and right
        if (indexOne[1] < indexTwo[1]) {
          one.setRight(two);
          two.setLeft(one);
        } else if (indexOne[0] < indexTwo[0]) {
          // up and down
          one.setDown(two);
          two.setUp(one);
        }
        // update sets
        oneSet.addAll(twoSet);
        for (Integer item: oneSet) {
          edgeSets.set(item, oneSet);
        }
        allEdges.remove(edge);
        removeCount++;
      } else {
        remainWalls.add(edge);
        allEdges.remove(edge);
      }
    }
    remainWalls.addAll(allEdges);

    int remainWallCount = numberOfAllEdges - numberOfCells + 1;
    while (remainWallCount > numberOfRemainWalls) {
      int[] edge = remainWalls.get(randomNum.nextInt(remainWalls.size()));
      int[] indexOne = new int[]{
              edge[0], edge[1]
      };
      int[] indexTwo = new int[]{
              edge[2], edge[3]
      };
      Room one = map[indexOne[0]][indexOne[1]];
      Room two = map[indexTwo[0]][indexTwo[1]];
      if (indexOne[1] < indexTwo[1]) {
        one.setRight(two);
        two.setLeft(one);
      } else if (indexOne[0] < indexTwo[0]) {
        // up and down
        one.setDown(two);
        two.setUp(one);
      }
      remainWalls.remove(edge);
      remainWallCount--;
    }
  }

  /*
  Add bats, pits, and wumpus to the generated maze.
  Call this after the maze is generated
   */
  protected void addSpecialRooms() {
    this.identifyRoom();
    this.addBats();
    this.addPits();
    this.addWumpus();
  }

  private void identifyRoom() {
    for (int i = 0; i < this.numberOfRows; i++) {
      for (int j = 0; j < this.numberOfColumns; j++) {
        int doorCount = 0;
        Room curr = this.map[i][j];
        if (curr.getUp() != null) {
          doorCount++;
        }
        if (curr.getDown() != null) {
          doorCount++;
        }
        if (curr.getLeft() != null) {
          doorCount++;
        }
        if (curr.getRight() != null) {
          doorCount++;
        }
        if (doorCount == 2) {
          curr.changeToTunnel();
        }
        if (curr.isRoom()) {
          this.rooms.add(new int[]{i, j});
        }
      }
    }
  }

  private void addBats() {
    int numberOfRooms = this.rooms.size();
    int numberOfBatRooms = (int) (numberOfRooms * this.batsPercent);
    Random randomNum = new Random(this.randomSeed);
    // shuffle the rooms, pick first n rooms as the bat rooms
    List<int[]> batRooms = shuffleRooms(numberOfBatRooms, randomNum);
    for (int[] r: batRooms) {
      this.map[r[0]][r[1]].setBats();
    }
  }

  private void addPits() {
    int numberOfRooms = this.rooms.size();
    int numberOfPitRooms = (int) (numberOfRooms * this.pitsPercent);
    Random randomNum = new Random(this.randomSeed + 1);
    // shuffle the rooms, pick first n rooms as the bat rooms
    List<int[]> pitRooms = shuffleRooms(numberOfPitRooms, randomNum);
    for (int[] r: pitRooms) {
      this.map[r[0]][r[1]].setPit();
    }
  }

  private void addWumpus() {
    Random randomNum = new Random(this.randomSeed + 2);
    // shuffle the rooms, pick first 1 rooms as the bat rooms
    List<int[]> wumpusRoom = shuffleRooms(1, randomNum);
    for (int[] r: wumpusRoom) {
      this.map[r[0]][r[1]].setWumpus();
    }
  }

  private List<int[]> shuffleRooms(int numberOfBatRooms, Random randomNum) {
    List<int[]> copyRooms = new ArrayList<>();
    for (int[] r: this.rooms) {
      copyRooms.add(new int[]{r[0], r[1]});
    }
    Collections.shuffle(copyRooms, randomNum);

    List<int[]> itemRooms = new ArrayList<>();
    for (int i = 0; i < numberOfBatRooms; i++) {
      itemRooms.add(copyRooms.get(i));
    }
    return itemRooms;
  }

}
