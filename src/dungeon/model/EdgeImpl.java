package dungeon.model;

class EdgeImpl implements Edge {
  private final int source;
  private final int destination;

  EdgeImpl(int source, int destination) {
    this.source = source;
    this.destination = destination;
  }

  @Override
  public int getSource() {
    return source;
  }

  @Override
  public int getDestination() {
    return destination;
  }

  @Override
  public String toString() {
    return source + " -> " + destination;
  }
}
