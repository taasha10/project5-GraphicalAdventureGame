package dungeon.model;

import java.util.List;

interface Cave {

  int getLocId();

  boolean isTunnel();

  void setTreasureList(List<Treasure> treasureList);

  boolean isHasTreasure();

  void setHasTreasure(boolean hasTreasure);

  List<Treasure> getTreasure();

  //project 4
  int getArrows();

  void setArrows(int numOfArrows);

  boolean hasArrows();

  void setHasArrows(boolean hasArrows);

  void reduceArrows();

  void reduceTreasure(Treasure t);

}
