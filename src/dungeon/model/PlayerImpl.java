package dungeon.model;

import java.util.ArrayList;
import java.util.List;


class PlayerImpl implements Player {

  private final List<Treasure> treasureAccumulated;
  private int location;
  private int arrows;
  private boolean playerWon;

  PlayerImpl() {
    this.treasureAccumulated = new ArrayList<>();
    this.location = 0;
    this.arrows = 3;
    playerWon = false;
  }

  @Override
  public List<Treasure> getTreasureAccumulated() {
    return treasureAccumulated;
  }

  @Override
  public void setTreasureAccumulated(Treasure treasureAccumulated) {
    this.treasureAccumulated.add(treasureAccumulated);
  }

  @Override
  public int getLocation() {
    return location;
  }

  @Override
  public void setLocation(int location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return "Player{" + "treasureAccumulated=" + treasureAccumulated + ", location=" + location
            + '}';
  }

  @Override
  public int getArrows() {
    return arrows;
  }

  @Override
  public boolean isPlayerWon() {
    return playerWon;
  }

  @Override
  public void setPlayerWon(boolean playerWon) {
    this.playerWon = playerWon;
  }

  @Override
  public void modifyArrow(String op, int num) {
    if (op.equals("+") && arrows <= 10) {
      arrows += num;
    } else if (op.equals("-") && arrows > 0) {
      arrows -= num;
    } else if (arrows == 0) {
      throw new IllegalStateException("You have exhausted your arrows.Explore more");
    } else if (arrows > 10) {
      throw new IllegalStateException("You can't hold more than 10 arrows. Shoot some first");
    } else {
      throw new IllegalArgumentException("Wrong operator.");
    }
  }
}
