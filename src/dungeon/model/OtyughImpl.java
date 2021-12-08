package dungeon.model;

class OtyughImpl implements Otyugh {
  private final int location;
  private final int otyughId;
  private int health;

  OtyughImpl(int location, int otyughId) {
    this.location = location;
    this.otyughId = otyughId;
    this.health = 2;
  }

  @Override
  public int getLocation() {
    return location;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public void setHealth() {
    this.health--;
  }

  @Override
  public boolean isDead() {
    return health == 0;
  }

  @Override
  public String toString() {
    return "{ Location= " + location + ", Health= " + health + ", OtyughId= " + otyughId + "}";
  }
}
