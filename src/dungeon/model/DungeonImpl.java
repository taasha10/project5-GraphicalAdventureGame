package dungeon.model;

import static dungeon.model.Direction.E;
import static dungeon.model.Direction.N;
import static dungeon.model.Direction.S;
import static dungeon.model.Direction.W;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class DungeonImpl implements Dungeon {

  private static final int MAX_ARROWS_IN_A_CAVE = 3;
  private final int rows;
  private final int columns;
  private final int degreeOfConnectivity;
  private final boolean getIsWrapping;
  private final int cavePerc;
  private final int numOfOtyugh;
  private final int totalCaves;
  private final int cavesWithTreasure;
  private final int cavesWithArrows;
  private final int[][] dungeonMatrix;
  private final Map<Integer, List<Integer>> verticesMap;
  private final int[] parentList;
  private final List<Otyugh> otyughList;
  private final int[] sizeOfEachNode;
  private final List<Edge> relevantPaths;
  private final List<Edge> edgeList;
  private final List<Edge> leftOverEdges;
  private final List<Edge> dungeonEdges;
  private final RandomGenerator randomGen;
  private final Map<Integer, List<Integer>> adjList;
  private final List<Cave> locationList;
  private final List<Cave> caveList;
  private final Map<Integer, Cave> locationMap;
  private final List<Integer> validPath;
  private Smell smell;
  private int start;
  private int end;

  DungeonImpl(int rows, int columns, int degreeOfConnectivity, boolean getIsWrapping,
              int caveWithTreasurePerc, int source, int destination, String randomType,
              int numOfOtyugh)
          throws IllegalArgumentException {

    if (rows <= 0) {
      throw new IllegalArgumentException("Rows can only be non-negative");
    }

    if (columns <= 0) {
      throw new IllegalArgumentException("columns can only be non-negative");
    }

    if (degreeOfConnectivity < 0) {
      throw new IllegalArgumentException("Degree of Connectivity can not be negative");
    }

    if (caveWithTreasurePerc < 0) {
      throw new IllegalArgumentException("Percentage can not be negative");
    }

    if (source < 0) {
      throw new IllegalArgumentException("Source cannot  be negative");
    }

    if (destination < 0) {
      throw new IllegalArgumentException("Destination cannot  be negative");
    }

    if (randomType == null) {
      throw new IllegalArgumentException("Random type cannot be null");
    }


    this.rows = rows;
    this.columns = columns;
    this.degreeOfConnectivity = degreeOfConnectivity;
    this.getIsWrapping = getIsWrapping;
    this.cavePerc = caveWithTreasurePerc;
    this.numOfOtyugh = numOfOtyugh;
    dungeonMatrix = new int[rows][columns];
    verticesMap = new HashMap<>();
    relevantPaths = new ArrayList<>();
    edgeList = new ArrayList<>();
    leftOverEdges = new ArrayList<>();
    dungeonEdges = new ArrayList<>();
    RandomGeneratorFactory r = new RandomGeneratorFactory();
    randomGen = r.randomObjectFactory(randomType);
    adjList = new HashMap<>();
    locationList = new ArrayList<>();
    locationMap = new HashMap<>();
    caveList = new ArrayList<>();
    validPath = new ArrayList<>();
    int numOfVertices = rows * columns;
    parentList = new int[numOfVertices];
    sizeOfEachNode = new int[numOfVertices];

    createDungeon();
    kruskalsAlgorithm(numOfVertices);
    checkDegreeOfConnectivity();
    createAdjList();
    createCave();
    totalCaves = caveList.size();
    cavesWithTreasure = randomTreasureInCave();
    cavesWithArrows = randomArrowsInCave();

    if (source == 0 && destination == 0) {
      do {
        start = caveList.get(randomGen.nextInt(caveList.size())).getLocId();
        do {
          end = caveList.get(randomGen.nextInt(caveList.size())).getLocId();
        }
        while (start == end);

        findPaths();
      }
      while (validPath.size() == 0);
    } else {
      start = source;
      end = destination;
      findPaths();
    }


    //project 4 from here.
    //to validate otyugh , should be at least 1 and not more than the total caves excluding starting
    //cave as each cave can have only one Otyugh.
    if (numOfOtyugh <= 0 || numOfOtyugh > totalCaves - 1) {
      throw new IllegalArgumentException("There should at least be one Otyugh and otyughs "
              + "should not be more than the total number of caves");
    }
    otyughList = new ArrayList<>(numOfOtyugh);
    //adding mandatory otyugh at the end cave
    otyughList.add(new OtyughImpl(end, 1));
    createOtyugh(); // creates otyughList
    smell = Smell.ZERO; //initial smell being 0

  }


  private void createDungeon() {
    int node = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        dungeonMatrix[i][j] = node;
        verticesMap.put(node, asList(i, j));
        node = node + 1;
      }
    }

    for (int i = 0; i < rows - 1; i++) {
      for (int j = 0; j < columns - 1; j++) {
        createEdge(dungeonMatrix[i][j], dungeonMatrix[i][j + 1]);
        createEdge(dungeonMatrix[i][j], dungeonMatrix[i + 1][j]);
      }
    }

    for (int j = 0; j < columns - 1; j++) {
      createEdge(dungeonMatrix[rows - 1][j], dungeonMatrix[rows - 1][j + 1]);
    }

    for (int i = 0; i < rows - 1; i++) {
      createEdge(dungeonMatrix[i][columns - 1], dungeonMatrix[i + 1][columns - 1]);
    }

    if (getIsWrapping) {
      for (int j = 0; j < columns; j++) {
        createEdge(dungeonMatrix[0][j], dungeonMatrix[rows - 1][j]);
      }

      for (int i = 0; i < rows; i++) {
        createEdge(dungeonMatrix[i][0], dungeonMatrix[i][columns - 1]);
      }
    }

  }

  private void createEdge(int source, int destination) {
    Edge edge = new EdgeImpl(source, destination);
    edgeList.add(edge);
  }

  private void unionFind(int n) {
    for (int i = 0; i < n; i++) {
      parentList[i] = i;
      sizeOfEachNode[i] = 1;
    }
  }

  private int findRoot(int source) {
    int root = source;

    while (root != parentList[root]) {
      root = parentList[root];
    }
    while (source != root) {
      int temp = parentList[source];
      parentList[source] = root;
      source = temp;
    }
    return root;
  }

  private boolean isSameRoot(int source, int destination) {
    return findRoot(source) == findRoot(destination);
  }

  private void union(int source, int destination) {
    int root1 = findRoot(source);
    int root2 = findRoot(destination);
    if (root1 == root2) {
      return;
    }
    if (sizeOfEachNode[root1] < sizeOfEachNode[root2]) {
      sizeOfEachNode[root2] += sizeOfEachNode[root1];
      parentList[root1] = root2;
    } else {
      sizeOfEachNode[root1] += sizeOfEachNode[root2];
      parentList[root2] = root1;
    }
  }

  private void randomEdgeSelection() {

    for (int i = edgeList.size() - 1; i >= 1; i--) {
      int j = randomGen.nextInt(i + 1);
      Edge edge = edgeList.get(i);
      edgeList.set(i, edgeList.get(j));
      edgeList.set(j, edge);
    }

  }

  private void kruskalsAlgorithm(int numOfVertices) {

    unionFind(numOfVertices);
    randomEdgeSelection();
    for (Edge edge : edgeList) {
      if (isSameRoot(edge.getSource(), edge.getDestination())) {
        leftOverEdges.add(edge);
        continue;
      }

      union(edge.getSource(), edge.getDestination());
      dungeonEdges.add(edge);
    }

  }

  private void checkDegreeOfConnectivity() throws IllegalArgumentException {
    if (degreeOfConnectivity > leftOverEdges.size()) {
      throw new IllegalArgumentException("DegreeOfConnectivity too high!");
    } else {
      for (int i = 0; i < degreeOfConnectivity; i++) {
        Edge newEdge = leftOverEdges.get(i);
        dungeonEdges.add(newEdge);
        leftOverEdges.remove(newEdge);
      }
    }

  }

  private void createAdjList() {

    for (Edge edge : dungeonEdges) {
      relevantPaths.add(edge);
      relevantPaths.add(new EdgeImpl(edge.getDestination(), edge.getSource()));
    }

    int sizeOfRelevantPaths = relevantPaths.size();
    for (int i = 0; i < sizeOfRelevantPaths; i++) {

      List<Integer> destinationList = new ArrayList<>();
      int src = relevantPaths.get(i).getSource();

      if (adjList.containsKey(src)) {
        continue;
      }
      destinationList.add(relevantPaths.get(i).getDestination());

      if (i != sizeOfRelevantPaths - 1) {
        for (int j = i + 1; j < sizeOfRelevantPaths; j++) {
          if (src == relevantPaths.get(j).getSource()) {
            destinationList.add(relevantPaths.get(j).getDestination());
          }
        }
      }

      adjList.put(src, destinationList);
    }
  }

  private void createCave() {
    for (int i = 0; i < adjList.size(); i++) {
      CaveImpl cave;
      if (adjList.get(i).size() == 2) {
        cave = new CaveImpl(i, 2, true);
      } else {
        cave = new CaveImpl(i, adjList.get(i).size(), false);
      }
      locationList.add(cave);
      locationMap.put(cave.getLocId(), cave);

    }

    for (Cave cave : locationList) {
      if (!cave.isTunnel()) {
        caveList.add(cave);
      }
    }
  }

  private Treasure[] randomTreasure(Treasure[] treasure) throws IllegalArgumentException {

    if (treasure == null) {
      throw new IllegalArgumentException("Treasure list can't be null");
    }
    for (int i = treasure.length - 1; i >= 1; i--) {
      int j = randomGen.nextInt(i + 1);
      Treasure t = treasure[i];
      treasure[i] = treasure[j];
      treasure[j] = t;
    }
    return treasure;

  }

  private int randomTreasureInCave() {
    List<Cave> lc = new ArrayList<>(caveList);
    int actualCavesWithTreasure = checkArrowsNTreasure(lc);

    for (int i = 0; i < actualCavesWithTreasure; i++) {

      Treasure[] treasures = randomTreasure(Treasure.values());
      int numOfTreasureInACave = 1 + randomGen.nextInt(treasures.length);
      List<Treasure> treasureInCave = new ArrayList<>(asList(treasures).subList(0,
              numOfTreasureInACave));

      Cave cave = lc.get(randomGen.nextInt(lc.size()));
      while (cave.isHasTreasure()) {
        cave = lc.get(randomGen.nextInt(lc.size()));
      }
      cave.setTreasureList(treasureInCave);
      cave.setHasTreasure(true);
      lc.remove(cave);
    }
    return actualCavesWithTreasure;
  }

  //adding arrows to cave
  private int randomArrowsInCave() {
    List<Cave> lc = new ArrayList<>(locationList);
    int actualLocWithArrows = checkArrowsNTreasure(lc);

    for (int i = 0; i < actualLocWithArrows; i++) {

      int numOfArrowsAtALoc = 1 + randomGen.nextInt(MAX_ARROWS_IN_A_CAVE);
      Cave cave;
      do {
        cave = lc.get(randomGen.nextInt(lc.size()));
      }
      while (cave.hasArrows());

      if (!cave.hasArrows()) {
        cave.setArrows(numOfArrowsAtALoc);
        cave.setHasArrows(true);
        lc.remove(cave);
      }
    }
    return actualLocWithArrows;
  }

  private int checkArrowsNTreasure(List<Cave> list) {
    List<Cave> lc = new ArrayList<>(list);
    int numOfCaves = lc.size();
    int numOfCaveWithTreasureOrArrow = (int) (cavePerc * 0.01 * numOfCaves);

    //at least numOfCave With Treasure or Arrows
    int actualCaves = numOfCaveWithTreasureOrArrow + randomGen.nextInt(numOfCaves);
    if (actualCaves == 0) {
      actualCaves = 1;
    }
    return actualCaves;
  }

  private boolean isNotVisited(int x, List<Integer> path) {
    for (Integer integer : path) {
      if (integer == x) {
        return false;
      }
    }
    return true;
  }

  private boolean checkForCave(int node) {
    for (Cave cave : caveList) {
      if (cave.getLocId() == node) {
        return true;
      }
    }
    return false;
  }

  private void findPaths() throws IllegalArgumentException {

    if (!(checkForCave(start) && checkForCave(end))) {
      throw new IllegalArgumentException("Start and end must be a cave!");
    }

    List<List<Integer>> listOfPaths = new ArrayList<>();
    List<Integer> path = new ArrayList<>();

    path.add(start);
    listOfPaths.add(path);

    while (!listOfPaths.isEmpty()) {
      path = listOfPaths.remove(0);
      int last = path.get(path.size() - 1);

      if (last == end) {
        int size = path.size() - 1;
        if (size >= 5) {
          validPath.addAll(path);
        }
      }

      List<Integer> lastNode = adjList.get(last);
      for (Integer integer : lastNode) {
        if (isNotVisited(integer, path)) {
          List<Integer> newPath = new ArrayList<>(path);
          newPath.add(integer);
          listOfPaths.add(newPath);
        }
      }
    }

  }

  //project 4 from here.
  private void createOtyugh() {

    randomCaveSelectionForOtyugh();
    int j = 0;
    int location;
    for (int i = 1; i < numOfOtyugh; i++) {

      do {
        location = caveList.get(j).getLocId();
        j++;
      }
      while (location == start || location == end);
      //adding otyugh at the rest of the cave.
      otyughList.add(new OtyughImpl(location, i + 1));
    }
  }

  private void randomCaveSelectionForOtyugh() {
    for (int i = caveList.size() - 1; i >= 1; i--) {
      int j = randomGen.nextInt(i + 1);
      Cave cave = caveList.get(i);
      caveList.set(i, caveList.get(j));
      caveList.set(j, cave);
    }
  }

  private Direction locationDirectionMap(int from, int to) {
    Direction direction = null;
    if (verticesMap.get(from).get(0).equals(verticesMap.get(to).get(0))) {
      if (verticesMap.get(from).get(1) + 1 == verticesMap.get(to).get(1)
              || verticesMap.get(from).get(1) - columns + 1 == verticesMap.get(to).get(1)) {
        direction = E;
      } else if (verticesMap.get(from).get(1) - 1 == verticesMap.get(to).get(1)
              || verticesMap.get(from).get(1) + columns - 1 == verticesMap.get(to).get(1)) {
        direction = W;
      }
    } else if (verticesMap.get(from).get(1).equals(verticesMap.get(to).get(1))) {
      if (verticesMap.get(from).get(0) - 1 == verticesMap.get(to).get(0)
              || verticesMap.get(from).get(0) + rows - 1 == verticesMap.get(to).get(0)) {
        direction = N;
      } else if (verticesMap.get(from).get(0) + 1 == verticesMap.get(to).get(0)
              || verticesMap.get(from).get(0) - rows + 1 == verticesMap.get(to).get(0)) {
        direction = S;
      }
    } else {
      throw new IllegalArgumentException("Direction not found");
    }
    return direction;
  }

  @Override
  public int getStart() {
    return start;
  }

  @Override
  public int getEnd() {
    return end;
  }

  //calculates the smell dynamically from the player's location.
  @Override
  public Smell calculateSmell(int pos) throws IllegalArgumentException {
    smell = Smell.ZERO;
    if (pos < 0 || pos >= rows * columns) {
      throw new IllegalArgumentException("Invalid player position");
    }

    List<Integer> firstNeighbor = new ArrayList<>(adjList.get(pos));
    List<Integer> secondNeighbor;
    int count = 0;
    for (Integer i : firstNeighbor) {
      for (Otyugh o : otyughList) {
        if (o.getLocation() == i && !o.isDead()) {
          smell = Smell.ONE; //strong smell
          break;
        } else if (o.getLocation() == i && o.isDead()) {
          smell = Smell.ZERO; //change smell when otyugh is killed
          break;
        }
      }
      if (smell != Smell.ZERO) {
        break;
      } else {
        secondNeighbor = new ArrayList<>(adjList.get(i));
        for (Integer s : secondNeighbor) {
          for (Otyugh o : otyughList) {
            if (o.getLocation() == s && smell != Smell.TWO && !o.isDead()) {
              smell = Smell.TWO;//faint smell
              count++;
              break;
            } else if (o.getLocation() == s && smell == Smell.TWO && !o.isDead()) {
              smell = Smell.ONE; //more than 1 cave has otyugh so strong smell
              count++;
              break;
            } else if (o.getLocation() == s && smell == Smell.TWO && o.isDead()) {
              smell = Smell.ZERO; //change smell when otyugh is killed
              break;
            }
          }
          if (count == 2) {
            break;
          }
        }
      }
    }
    return smell;
  }

  @Override
  public int locationDirectionMap2(int from, Direction direction) {
    int toLoc = -1;

    if (!(direction == N || direction == S || direction == W || direction == E)
            && (from < 0 || from >= rows * columns)) {
      throw new IllegalArgumentException("Next location not found");
    }

    switch (direction) {
      case N: {
        for (Map.Entry<Integer, List<Integer>> v : verticesMap.entrySet()) {
          if (v.getValue().get(0) == verticesMap.get(from).get(0) - 1
                  && v.getValue().get(1).equals(verticesMap.get(from).get(1))) {
            toLoc = v.getKey();
            break;
          } else if (v.getValue().get(0) == verticesMap.get(from).get(0) + rows - 1
                  && v.getValue().get(1).equals(verticesMap.get(from).get(1))) {
            toLoc = v.getKey();
            break;
          }
        }
      }
      break;

      case S: {
        for (Map.Entry<Integer, List<Integer>> v : verticesMap.entrySet()) {
          if (v.getValue().get(0) == verticesMap.get(from).get(0) + 1
                  && v.getValue().get(1).equals(verticesMap.get(from).get(1))) {
            toLoc = v.getKey();
            break;
          } else if (v.getValue().get(0) == verticesMap.get(from).get(0) - rows + 1
                  && v.getValue().get(1).equals(verticesMap.get(from).get(1))) {
            toLoc = v.getKey();
            break;
          }
        }
      }
      break;

      case E: {
        for (Map.Entry<Integer, List<Integer>> v : verticesMap.entrySet()) {
          if (v.getValue().get(0).equals(verticesMap.get(from).get(0))
                  && v.getValue().get(1) == verticesMap.get(from).get(1) + 1) {
            toLoc = v.getKey();
            break;
          } else if (v.getValue().get(0).equals(verticesMap.get(from).get(0))
                  && v.getValue().get(1) == verticesMap.get(from).get(1) - columns + 1) {
            toLoc = v.getKey();
            break;
          }
        }
      }
      break;

      case W: {
        for (Map.Entry<Integer, List<Integer>> v : verticesMap.entrySet()) {
          if (v.getValue().get(0).equals(verticesMap.get(from).get(0))
                  && v.getValue().get(1).equals(verticesMap.get(from).get(1) - 1)) {
            toLoc = v.getKey();
            break;
          } else if (v.getValue().get(0).equals(verticesMap.get(from).get(0))
                  && v.getValue().get(1).equals(verticesMap.get(from).get(1) + columns - 1)) {
            toLoc = v.getKey();
            break;
          }
        }
      }
      break;
      default: throw new IllegalArgumentException("Error in converting direction to location.");
    }

    return toLoc;
  }

  @Override
  public List<Direction> describeMoves(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player can't be null.");
    }
    List<Integer> loc = adjList.get(player.getLocation());
    List<Direction> locDirection = new ArrayList<>();
    for (Integer l : loc) {
      locDirection.add(locationDirectionMap(player.getLocation(), l));
    }
    return locDirection;
  }

  @Override
  public int getCaveWithTreasurePerc() {
    return cavePerc;
  }

  @Override
  public int getTotalCaves() {
    return totalCaves;
  }

  @Override
  public int getCavesWithTreasure() {
    return cavesWithTreasure;
  }

  @Override
  public List<Edge> getRelevantPaths() {
    return relevantPaths;
  }

  @Override
  public List<Cave> getLocationList() {
    return locationList;
  }

  @Override
  public List<Integer> getValidPath() {
    return validPath;
  }


  @Override
  public boolean hasArrows(int c) {
    return locationMap.get(c).hasArrows();
  }

  @Override
  public boolean hasTreasure(int c) {
    return locationMap.get(c).isHasTreasure();
  }

  @Override
  public int getArrows(int c) {
    return locationMap.get(c).getArrows();
  }

  @Override
  public List<Treasure> getTreasure(int location) {
    if (locationList.get(location).isHasTreasure()) {
      return locationList.get(location).getTreasure();
    } else {
      return new ArrayList<>();
    }
  }

  @Override
  public void reduceArrow(int loc) {
    locationMap.get(loc).reduceArrows();
  }

  @Override
  public void reduceTreasure(int loc, Treasure t) {
    locationMap.get(loc).reduceTreasure(t);
  }

  @Override
  public boolean slayingOtyugh(int arrowLoc, int shootingDistance, Direction direction) {

    if (shootingDistance == 0) {
      for (Otyugh o : otyughList) {
        if (o.getLocation() == arrowLoc) {
          o.setHealth();
          return true;
        }
      }
      return false;
    }

    int nextLoc = locationDirectionMap2(arrowLoc, direction);
    if (nextLoc == -1) {
      return false;
    }
    Cave c = locationMap.get(nextLoc);
    if (c.isTunnel()) {
      int temp1 = adjList.get(nextLoc).get(0); // first opening of tunnel
      if (temp1 == arrowLoc) {
        temp1 = adjList.get(nextLoc).get(1);
      }
      Direction new_direction = locationDirectionMap(nextLoc, temp1);
      return slayingOtyugh(nextLoc, shootingDistance, new_direction);
    } else {
      int entries = adjList.get(nextLoc).size();
      if (entries == 1 && shootingDistance != 1) {
        return false;
      } else {
        return slayingOtyugh(nextLoc, shootingDistance - 1, direction);
      }

    }
  }

  @Override
  public boolean isGameOver(Player p) {
    if (p == null) {
      throw new IllegalArgumentException("Player and end should have valid values");
    }
    for (Otyugh o : otyughList) {
      if (o.getLocation() == p.getLocation()) {
        if (o.getHealth() == 2) {
          return true;
        } else if (o.getHealth() == 1 && !randomGen.getRandomType().equals("FakeRev")) {
          return (1 == randomGen.nextInt(2)); // game over if random = 1 else not
        }
        else if (o.getHealth() == 1 && randomGen.getRandomType().equals("FakeRev")) {
          return (1 == randomGen.nextInt(4)); // game over if random = 1 else not
        }
      }
      if (o.isDead()) {
        if (p.getLocation() == end) {
          p.setPlayerWon(true);
          return true;
        }

        return false;
      }
    }
    return false;
  }

  @Override
  public List<Otyugh> getOtyughList() {
    otyughList.removeIf(Otyugh::isDead);
    return otyughList;
  }

  @Override
  public List<Cave> getCaveList() {
    return caveList;
  }

  @Override
  public int getCavesWithArrows() {
    return cavesWithArrows;
  }

  @Override
  public String toString() {
    return "Dungeon{" + "\n adjList=" + adjList + "\n\n locationList=" + locationList
            + "\n\n otyughList=" + otyughList + '}';
  }


}
