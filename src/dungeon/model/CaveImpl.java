package dungeon.model;

import java.util.List;

class CaveImpl implements Cave {
  private final int locId;
  private final int numOfEntrances;
  private final boolean isTunnel;
  private List<Treasure> treasureList;
  private boolean hasTreasure;
  //project 4
  private int numOfArrows;
  private boolean hasArrows;

  CaveImpl(int locId, int numOfEntrances, boolean isTunnel) {
    this.locId = locId;
    this.numOfEntrances = numOfEntrances;
    this.isTunnel = isTunnel;
    hasTreasure = false;
    hasArrows = false;
  }

  @Override
  public int getLocId() {
    return locId;
  }

  @Override
  public boolean isTunnel() {
    return isTunnel;
  }

  @Override
  public void setTreasureList(List<Treasure> treasureList) {
    this.treasureList = treasureList;
  }

  @Override
  public boolean isHasTreasure() {
    return hasTreasure;
  }

  @Override
  public void setHasTreasure(boolean hasTreasure) {
    this.hasTreasure = hasTreasure;
  }

  @Override
  public List<Treasure> getTreasure() {
    return treasureList;
  }

  //project 4
  @Override
  public int getArrows() {
    return numOfArrows;
  }

  @Override
  public void setArrows(int numOfArrows) {
    this.numOfArrows = numOfArrows;
  }

  @Override
  public boolean hasArrows() {
    return hasArrows;
  }

  @Override
  public void setHasArrows(boolean hasArrows) {
    this.hasArrows = hasArrows;
  }

  @Override
  public void reduceArrows() {
    numOfArrows--;
  }

  @Override
  public void reduceTreasure(Treasure t) {
    treasureList.remove(t);
  }

  @Override
  public String toString() {

    if (hasTreasure && hasArrows) {
      return "Cave{" + "locId=" + locId + ", numOfEntrances=" + numOfEntrances
              + ", isTunnel=" + isTunnel + ", treasureList=" + treasureList
              + ", Arrow=" + numOfArrows + '}';
    } else if (hasTreasure) {
      return "Cave{"
              + "locId=" + locId + ", numOfEntrances=" + numOfEntrances + ", isTunnel=" + isTunnel
              + ", treasureList=" + treasureList + '}';
    } else if (hasArrows) {
      return "Cave{"
              + "locId=" + locId + ", numOfEntrances=" + numOfEntrances + ", isTunnel=" + isTunnel
              + ", NumOfArrows=" + numOfArrows + '}';
    } else {
      return "Cave{"
              + "locId=" + locId + ", numOfEntrances=" + numOfEntrances + ", isTunnel=" + isTunnel
              + '}';
    }
  }

}
