package dungeon.model;

import java.util.List;

interface Player {

  List<Treasure> getTreasureAccumulated();

  void setTreasureAccumulated(Treasure treasureAccumulated);

  int getLocation();

  void setLocation(int location);

  void modifyArrow(String op, int num);

  boolean isPlayerWon();

  void setPlayerWon(boolean playerWon);

  int getArrows();

}
