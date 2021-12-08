package dungeon.model;

import java.util.List;

public interface ReadOnlyDungeonGM {

  /**
   * To provide a description of the player's location that at the minimum includes a
   * description of treasure in the room and the possible moves (north, east, south, west)
   * that the player can make from their current location.
   *
   * @return String to describe that location.
   */

  String getPlayerLocation();

  /**
   * To provide a description of the player that, at a minimum, includes a description of what
   * treasure the player has collected.
   *
   * @return list of Treasure that the player has collected.
   */

  List<Treasure> playerDescription();

  /**
   * To provide valid paths with a minimum distance of 5 between start and end.
   * @return list of valid path.
   */

  List<Integer> getValidPath();

  /**
   * To provide the dungeon as a list.
   * @return list of Caves and tunnels.
   */

  List<String> getLocationList();



  /**
   * To provide caves' location.
   * @return list of Cave location.
   */

  List<Integer> getCaveLocation();

  /**
   * To provide the relevant paths that are possible after the dungeon has been created.
   * @return paths of bidirectional graph of dungeon.
   */

  List<Edge> getRelevantPaths();

  /**
   * To provide a treasure list at a location if it's present there.
   * @param location in the dungeon.
   * @return treasure list.
   */

  String getTreasureList(int location);

  /**
   * Returns total number of caves in the dungeon.
   *
   * @return int.
   */

  int getTotalCaves();

  /**
   * Returns the number of caves that has treasure in it.
   *
   * @return int.
   */
  int getCavesWithTreasure();

  /**
   * Returns the percentage of caves (all locations in case of arrows) that should at least contain
   * treasure (and arrows).
   *
   * @return int.
   */
  int getCaveWithTreasurePerc();

  /**
   * Returns the starting cave.
   *
   * @return int.
   */
  int getStart();

  /**
   * Returns the ending cave.
   *
   * @return int.
   */
  int getEnd();

  /**
   * Tells if the player's current location has arrows to pick or not.
   * @return true if arrows present else false.
   */
  boolean hasArrows();

  /**
   * Tells if the player's current location has treasure to pick or not.
   * @return true if treasure present else false.
   */
  boolean hasTreasure();

  /**
   * Gets the total number of arrows from the player's current location.
   * @return number of arrows.
   */
  int getArrows();

  /**
   * Gets the list of treasure from the player's current location.
   * @return list of treasure
   */
  List<Treasure> getTreasure();

  /**
   * Returns the next direction to move to from a location.
   * @return list of directions possible.
   */
  List<Direction> direction();

  /**
   * Method tells if the game is Over or not.
   * @return true if it is else false.
   */
  boolean isGameOver();

  /**
   * MEthod tells if the player won or lost after the game is over.
   * @return true if won, false otherwise.
   */
  boolean ifPlayerWon();

  /**
   * Returns the number of locations that has arrows in it.
   *
   * @return int.
   */
  int getCavesWithArrows();

  /**
   * Returns the list of Otyughs in the dungeon.
   * @return list of otyugh
   */
  List<Otyugh> getOtyughList();

  /**
   * Returns the locations of the otyughs in the dungeon.
   * @return list of int
   */
  List<Integer> otyughLocation();

  /**
   * Number of arrows the player holds.
   * @return number of arrows of player.
   */
  int playerArrows();

  /**
   * Calculates the smell based on the distance between the player and Otyugh(s).
   * @return 1 for strong smell, 2 for faint smell and 0 for no smell.
   */
  int calculateSmell();

  int getPlayerCurrentLocation();
}
