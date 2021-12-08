package dungeon.model;

import java.util.Random;

class RandomGen extends Random implements RandomGenerator {

  Random r ;
  public RandomGen() {
    r = new Random();
  }

  public RandomGen(int seed) {
    r = new Random(seed);

  }

  @Override
  public String getRandomType() {
    return "Real";
  }

  @Override
  public int nextInt(int bound) {
    return r.nextInt(bound);
  }
}
