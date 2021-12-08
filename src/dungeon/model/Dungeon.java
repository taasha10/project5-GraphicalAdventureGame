package dungeon.model;

import java.util.List;

interface Dungeon {

  List<Direction> describeMoves(Player player);

  List<Integer> getValidPath();

  List<Cave> getLocationList();

  List<Edge> getRelevantPaths();

  int getTotalCaves();

  int getCavesWithTreasure();

  int getCaveWithTreasurePerc();

  int getArrows(int c);

  List<Treasure> getTreasure(int c);

  boolean hasArrows(int c);

  boolean hasTreasure(int c);

  int locationDirectionMap2(int from, Direction direction);

  Smell calculateSmell(int pos);

  boolean slayingOtyugh(int arrowLoc, int shootingDistance, Direction direction);

  boolean isGameOver(Player p);

  void reduceTreasure(int loc, Treasure t);

  void reduceArrow(int loc);

  int getStart();

  int getEnd();

  int getCavesWithArrows();

  List<Otyugh> getOtyughList();

  List<Cave> getCaveList();


}
