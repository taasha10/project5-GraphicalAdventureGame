package dungeon.model;

class FakeRandomGen implements RandomGenerator {

  @Override
  public int nextInt(int bound) {
    return (bound - 1) / 2;
  }

  @Override
  public String getRandomType() {
    return "Fake";
  }

}
