package dungeon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing DungeonGameModel. Takes in package-private classes' objects and returns
 * their outcomes as a single interface.
 */
public class DungeonGameModelImpl implements DungeonGameModel {
  private final Dungeon dungeon;
  private final Player player;
  private final int start;
  private final int end;


  /**
   * Constructor to be used when we are randomly doing functions on the dungeon. Randomly
   * generated start and end in the constructor.
   *
   * @param maxRows              maximum number of rows input by user.
   * @param maxColumns           maximum number of columns input by user.
   * @param interconnectivity    degree of interconnectivity input by user.
   * @param isWrapping           whether dungeon is wrappable or not.
   * @param caveWithTreasurePerc minimum percentage of caves with treasure.
   * @param randomType           "Real" for this constructor.
   * @param otyugh               number of Otyugh required to be present in the dungeon.
   */

  public DungeonGameModelImpl(int maxRows, int maxColumns, int interconnectivity,
                              boolean isWrapping, int caveWithTreasurePerc, String randomType,
                              int otyugh) {

    player = new PlayerImpl();
    dungeon = new DungeonImpl(maxRows, maxColumns, interconnectivity, isWrapping,
            caveWithTreasurePerc, 0, 0, randomType, otyugh);
    start = dungeon.getStart();
    end = dungeon.getEnd();
  }

  /**
   * Constructor to be used when we are mocking the random Generator and providing our own
   * fixed values for various functions on the dungeon for testing purposes.
   *
   * @param maxRows              maximum number of rows input by user.
   * @param maxColumns           maximum number of columns input by user.
   * @param interconnectivity    degree of interconnectivity input by user.
   * @param isWrapping           whether dungeon is wrappable or not.
   * @param caveWithTreasurePerc minimum percentage of caves with treasure.
   * @param randomType           "Fake" for this constructor.
   * @param start                Starting point of the dungeon given by the user.
   * @param end                  Ending point of the dungeon given by the user.
    *@param otyugh               number of Otyugh required to be present in the dungeon.
   */

  public DungeonGameModelImpl(int maxRows, int maxColumns, int interconnectivity,
                              boolean isWrapping, int caveWithTreasurePerc, String randomType,
                              int start, int end, int otyugh) {

    player = new PlayerImpl();
    this.start = start;
    this.end = end;
    dungeon = new DungeonImpl(maxRows, maxColumns, interconnectivity, isWrapping,
            caveWithTreasurePerc, start, end, randomType, otyugh);

  }

  @Override
  public void enterPlayer() {
    player.setLocation(start);
  }

  @Override
  public int getStart() {
    return start;
  }

  @Override
  public int getEnd() {
    return end;
  }

  @Override
  public void pickArrow() {

    if (dungeon.hasArrows(player.getLocation())) {
      player.modifyArrow("+", 1);
      dungeon.reduceArrow(player.getLocation());

    } else {
      throw new IllegalArgumentException("Location doesn't have an arrow.");
    }
  }

  //picking all treasure
  @Override
  public void pickTreasure(String treasure) {
    if (treasure == null || !(treasure.equalsIgnoreCase("Diamond")
            || treasure.equalsIgnoreCase("Ruby")
            || treasure.equalsIgnoreCase("Sapphire"))) {
      throw new IllegalArgumentException("Illegal value for treasure");
    }

    if (dungeon.hasTreasure(player.getLocation())) {
      player.setTreasureAccumulated(Treasure.valueOf(treasure.toUpperCase()));
      dungeon.reduceTreasure(player.getLocation(), Treasure.valueOf(treasure.toUpperCase()));
    } else {
      throw new IllegalArgumentException("Location doesn't have a treasure.");
    }
  }

  @Override
  public void movePlayer(Direction locationDirection) {
    if (locationDirection == null) {
      throw new IllegalArgumentException("Direction can't be null.");
    }
    player.setLocation(dungeon.locationDirectionMap2(player.getLocation(), locationDirection));
  }

  @Override
  public String getPlayerLocation() {
    int pl = player.getLocation();

    if (pl == end) {
      return "Location:" + pl + getTreasureList(pl);
    } else {
      return "Location:" + pl + "| Possible Moves:" + dungeon.describeMoves(player)
              + getTreasureList(pl);
    }
  }

  @Override
  public List<Treasure> playerDescription() {
    return player.getTreasureAccumulated();
  }

  @Override
  public List<Integer> getValidPath() {
    return dungeon.getValidPath();
  }

  @Override
  public List<String> getLocationList() {
    List<String> locations = new ArrayList<>();
    List<Cave> caves = dungeon.getLocationList();
    for (Cave c : caves) {
      locations.add(c.toString());
    }
    return locations;
  }

  @Override
  public List<Integer> getCaveLocation() {
    List<Integer> caveLoc = new ArrayList<>();
    List<Cave> caves = dungeon.getCaveList();
    for (Cave c : caves) {
      caveLoc.add(c.getLocId());
    }
    return caveLoc;
  }

  @Override
  public List<Edge> getRelevantPaths() {
    return dungeon.getRelevantPaths();
  }

  @Override
  public String getTreasureList(int location) {
    if (location < 0 || location > getLocationList().size()) {
      throw new IllegalArgumentException("Invalid location.");
    }
    List<Treasure> tl = dungeon.getTreasure(location);
    if (tl.isEmpty()) {
      return " | No treasure to pick!" + "| Arrows at this location: " + getArrows();
    } else {
      return " | Treasure found : " + dungeon.getTreasure(location) + "| Arrows at this location: "
              + getArrows();
    }
  }

  @Override
  public int getTotalCaves() {
    return dungeon.getTotalCaves();
  }

  @Override
  public int getCavesWithTreasure() {
    return dungeon.getCavesWithTreasure();
  }

  @Override
  public int getCaveWithTreasurePerc() {
    return dungeon.getCaveWithTreasurePerc();
  }

  @Override
  public String toString() {
    return dungeon.toString();
  }

  @Override
  public List<Direction> direction() {
    return dungeon.describeMoves(player);
  }

  @Override
  public boolean hasArrows() {
    return dungeon.hasArrows(player.getLocation());
  }

  @Override
  public boolean hasTreasure() {
    return dungeon.hasTreasure(player.getLocation());
  }

  @Override
  public int getArrows() {
    return dungeon.getArrows(player.getLocation());
  }

  @Override
  public List<Treasure> getTreasure() {
    return dungeon.getTreasure(player.getLocation());
  }

  @Override
  public int calculateSmell() {
    Smell s = dungeon.calculateSmell(player.getLocation());
    if (s == Smell.ONE) {
      return 1;
    } else if (s == Smell.TWO) {
      return 2;
    }
    return 0;
  }

  @Override
  public boolean slayingOtyugh(int shootingDistance, Direction direction) {
    if (shootingDistance < 0 || direction == null) {
      throw new IllegalArgumentException("Invalid input for slaying Otyugh!");
    }

    player.modifyArrow("-", 1);
    return dungeon.slayingOtyugh(player.getLocation(), shootingDistance, direction);
  }

  @Override
  public boolean isGameOver() {
    return dungeon.isGameOver(player);
  }

  @Override
  public boolean ifPlayerWon() {
    return player.isPlayerWon();
  }

  @Override
  public int getCavesWithArrows() {
    return dungeon.getCavesWithArrows();
  }

  @Override
  public List<Otyugh> getOtyughList() {
    return dungeon.getOtyughList();
  }

  @Override
  public List<Integer> otyughLocation() {
    List<Integer> otyughLoc = new ArrayList<>();
    List<Otyugh> otyughs = getOtyughList();
    for (Otyugh o : otyughs) {
      otyughLoc.add(o.getLocation());
    }
    return otyughLoc;
  }

  @Override
  public int playerArrows() {
    return player.getArrows();
  }

  @Override
  public int locationDirectionMap2(int from, Direction direction){
    return dungeon.locationDirectionMap2(from,direction);
  }

  @Override
  public int getPlayerCurrentLocation(){
    return player.getLocation();
  }
}
