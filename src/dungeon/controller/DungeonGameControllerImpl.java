package dungeon.controller;

import static dungeon.controller.GameControls.A;
import static dungeon.controller.GameControls.K;
import static dungeon.controller.GameControls.M;
import static dungeon.controller.GameControls.P;

import dungeon.model.Direction;
import dungeon.model.DungeonGameModel;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The controller class implements DungeonGameController interface for the Text based Adventure
 * Game. Includes several private methods for readability.
 */
public class DungeonGameControllerImpl implements DungeonGameController {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructor for the controller.
   *
   * @param in  the source to read from
   * @param out the target to print to
   */
  public DungeonGameControllerImpl(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  @Override
  public void playGame(DungeonGameModel dungeonGameModel) {

    if (dungeonGameModel == null) {
      throw new IllegalArgumentException("Model can't be null");
    }

    try {
      out.append("\n---- Dungeon is ready to enter! ----\n");
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    }

    dungeonGameModel.enterPlayer();

    do {
      List<Direction> directionList = dungeonGameModel.direction();
      smellCheck(dungeonGameModel);

      if (directionList.size() == 2) {
        try {
          out.append("\nYou are in a tunnel! ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      } else {
        try {
          out.append("\nYou are in a cave! ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }

      //to find treasure and arrows.
      findItems(dungeonGameModel);

      try {
        out.append("\nDoors lead to the ").append(directionList.stream().map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .append(".\n\n< Move(M), Pick(P), Attack(K) > ");
      } catch (IOException e) {
        throw new IllegalStateException("Append failed", e);
      }

      boolean actionFound = false;
      while (!actionFound && scan.hasNext()) {
        String action = scan.next();

        //validating action
        if (!(action.equalsIgnoreCase(M.name()) || action.equalsIgnoreCase(P.name())
                || action.equalsIgnoreCase(K.name()))) {
          try {
            out.append("\nNot a valid action! Press (M-move, P-pick, K-attack) ");
          } catch (IOException e) {
            throw new IllegalStateException("Append failed", e);
          }
        } else {
          actionFound = true;
          switch (GameControls.valueOf(action.toUpperCase())) {
            case M:
              move(dungeonGameModel,directionList);//E, W, N, S.
              break;
            case P:
              pickItem(dungeonGameModel);
              break;
            case K:
              validateAttack(dungeonGameModel);
              break;
            default: {
              try {
                out.append("\n Error in action. ");
              } catch (IOException e) {
                throw new IllegalStateException("Append failed", e);
              }
            }
          }
        }
      }
    }
    while (!dungeonGameModel.isGameOver());

    if (dungeonGameModel.isGameOver()) {
      if (dungeonGameModel.ifPlayerWon()) {
        try {
          out.append("\n\n***** Congratulations, you won! *****");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      } else {
        try {
          out.append("\n\nChomp, chomp, chomp, you are eaten by an Otyugh!"
                  + "\nBetter luck next time :(");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }
    }
  }

  private void smellCheck(DungeonGameModel d) {
    int smell = d.calculateSmell();
    if (smell == 1) {
      try {
        out.append("\nYou smell something terrible nearby ");
      } catch (IOException e) {
        throw new IllegalStateException("Append failed", e);
      }
    } else if (smell == 2) {
      try {
        out.append("\nYou feel some weird smell nearby ");
      } catch (IOException e) {
        throw new IllegalStateException("Append failed", e);
      }
    }
  }

  private void pickItem(DungeonGameModel d) {
    GameControls pick = validatePick();
    String item;
    switch (pick) {
      case R:
        item = "RUBY";
        break;
      case D:
        item = "DIAMOND";
        break;
      case H:
        item = "SAPPHIRE";
        break;
      case A:
        item = "ARROW";
        break;
      default:
        throw new IllegalStateException("Can't pick the item: " + pick);
    }

    if (pick == A) {
      try {
        d.pickArrow();
      } catch (IllegalArgumentException | IllegalStateException e) {
        try {
          out.append("\nError in picking up Arrows: ").append(String.valueOf(e.getMessage()));
        } catch (IOException ex) {
          throw new IllegalStateException("Append failed", ex);
        }
      }
    } else {
      try {
        d.pickTreasure(item);
      } catch (IllegalArgumentException e) {
        try {
          out.append("\n Error in picking up treasure: ").append(String.valueOf(e.getMessage()));
        } catch (IOException ex) {
          throw new IllegalStateException("Append failed", ex);
        }
      }

    }
  }

  private Direction validateDirection() {
    boolean moveFound = false;
    String move = "";
    try {
      out.append("\nWhere to? ");
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    }
    do {
      move = scan.next();
      if (move.equalsIgnoreCase(String.valueOf(Direction.N))
              || move.equalsIgnoreCase(String.valueOf(Direction.W))
              || move.equalsIgnoreCase(String.valueOf(Direction.E))
              || move.equalsIgnoreCase(String.valueOf(Direction.S))) {
        moveFound = true;
      } else {
        try {
          out.append("\nInvalid direction! Enter a valid direction(N-S-E-W) ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }
    }
    while (!moveFound && scan.hasNext());

    if (moveFound) {
      return Direction.valueOf(move.toUpperCase());
    } else {
      throw new IllegalArgumentException("Direction not found");
    }

  }

  private void move(DungeonGameModel d, List<Direction> directionList) {
    try {
      Direction dir = validateDirection();
      if (directionList.contains(dir)){
        d.movePlayer(dir);
      }
      else{
        throw new IllegalArgumentException("Not a valid direction.");
      }

    } catch (IllegalArgumentException e) {
      try {
        out.append("\nError in moving the player: ").append(String.valueOf(e.getMessage()));
      } catch (IOException ex) {
        throw new IllegalStateException("Append failed", ex);
      }
    }
  }

  private GameControls validatePick() {

    boolean pickFound = false;
    String pick = "";

    try {
      out.append("\nWhat?\nEnter R for ruby, D for Diamond, H for Sapphire and A for Arrow : ");
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    }

    while (!pickFound && scan.hasNext()) {
      pick = scan.next();
      if (pick.equalsIgnoreCase(GameControls.R.name())
              || pick.equalsIgnoreCase(GameControls.D.name())
              || pick.equalsIgnoreCase(GameControls.H.name()) || pick.equalsIgnoreCase(A.name())) {
        pickFound = true;
      } else {
        try {
          out.append("\nInvalid pick! Enter a valid pick(R-D-H-A) ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }
    }

    if (pickFound) {
      try {
        out.append("\nYou picked up a/an ").append(pick);
      } catch (IOException e) {
        throw new IllegalStateException("Append failed", e);
      }
      return GameControls.valueOf(pick.toUpperCase());
    } else {
      throw new IllegalArgumentException("Pick up not successful");
    }
  }

  private void validateAttack(DungeonGameModel d) {
    boolean caveFound = false;
    int numOfCave = 0;
    Direction direction = null;

    try {
      out.append("\nNumber of Caves (1-5) : ");
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    }

    while (!caveFound && scan.hasNext()) {
      String numOfCaveString = scan.next();
      try {
        numOfCave = Integer.parseInt(numOfCaveString);
      } catch (NumberFormatException numberFormatException) {
        try {
          out.append("\nInvalid number!");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }

      if (numOfCave >= 1 && numOfCave <= 5) {
        direction = validateDirection();
        caveFound = true;
      } else {
        try {
          out.append("\nEnter a valid distance(1-5): ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }
    }

    boolean hit = false;

    if (caveFound) {
      try {
        hit = d.slayingOtyugh(numOfCave, direction); //add check for number of arrows left
      } catch (IllegalArgumentException | IllegalStateException e) {
        try {
          out.append(String.valueOf(e.getMessage()));
        } catch (IOException ex) {
          throw new IllegalStateException("Append failed", ex);
        }
      }

      if (d.playerArrows() == 0) {
        return;
      }

      if (hit) {
        try {
          out.append("\nYou hear a great howl in the distance. ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      } else {
        try {
          out.append("\nYou shoot an arrow into the darkness. ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }

    }
  }

  private void findItems(DungeonGameModel d) {
    if (d.hasArrows()) {
      int arrows = d.getArrows();
      if (arrows == 1) {
        try {
          out.append("\nYou found 1 Arrow here. ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      } else if (arrows != 0) {
        try {
          out.append("\nYou found ").append(String.valueOf(arrows)).append(" Arrows here. ");
        } catch (IOException e) {
          throw new IllegalStateException("Append failed", e);
        }
      }
    }

    if (d.hasTreasure()) {
      int n = d.getTreasure().size();
      for (int i = 0; i < n; i++) {
        String treasure = String.valueOf(d.getTreasure().get(i));
        if (treasure.equalsIgnoreCase("Ruby")) {
          try {
            out.append("\nYou found a Ruby here. ");
          } catch (IOException e) {
            throw new IllegalStateException("Append failed", e);
          }
        } else if (treasure.equalsIgnoreCase("Diamond")) {
          try {
            out.append("\nYou found a Diamond here. ");
          } catch (IOException e) {
            throw new IllegalStateException("Append failed", e);
          }
        } else {
          try {
            out.append("\nYou found a Sapphire here. ");
          } catch (IOException e) {
            throw new IllegalStateException("Append failed", e);
          }
        }
      }
    }
  }
}
