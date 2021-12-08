package dungeon.model;

class RandomGeneratorFactory {

  RandomGenerator randomObjectFactory(String randomType) {
    switch (randomType) {
      case "Real":
        return new RandomGen();
      case "Fake":
        return new FakeRandomGen();
      case "FakeRev":
        return new FakeRandomGenReverse();
      default:
        throw new IllegalArgumentException("Random argument can be either Fake or Real");
    }
  }

}
