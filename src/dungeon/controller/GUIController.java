package dungeon.controller;

import java.io.IOException;
import java.util.List;

import dungeon.model.Direction;
import dungeon.model.DungeonGameModel;
import dungeon.model.DungeonGameModelImpl;
import dungeon.model.ReadOnlyDungeonGM;
import dungeon.view.DungeonGUI;

import static dungeon.controller.GameControls.A;
import static dungeon.controller.GameControls.D;
import static dungeon.controller.GameControls.H;
import static dungeon.controller.GameControls.R;

public class GUIController implements GUIControllerInterface{

  private DungeonGameModel model;
  private DungeonGUI view;
  private DungeonInput input;


  public void setView(DungeonGUI view){
    this.view = view;
  }

  public void setModel(DungeonInput inputs){
    if (inputs == null) {
      throw new IllegalArgumentException("Arguments can't be empty");
    }

    this.input = inputs;
    int rows;
    int columns;
    int interconnectivity;
    boolean isWrapping;
    int percentage;
    int otyugh;

    try {
      rows = Integer.parseInt(inputs.getRows());
      columns = Integer.parseInt(inputs.getColumns());
      interconnectivity = Integer.parseInt(inputs.getInterconnectivity());
      percentage = Integer.parseInt(inputs.getPercentage());
      otyugh = Integer.parseInt(inputs.getOtyugh());

      if (inputs.isWrapping().equalsIgnoreCase("wrapping")) {
        isWrapping = true;
      } else if (inputs.isWrapping().equalsIgnoreCase("non-wrapping")) {
        isWrapping = false;
      } else {
        throw new IllegalArgumentException("Enter 'wrapping' or 'non-wrapping'");
      }

      model = new DungeonGameModelImpl(rows,columns,interconnectivity,isWrapping,percentage,"Real",otyugh);
      System.out.println(model.getLocationList());
      model.enterPlayer();

    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

  }

  public void clickHandler(int i) {
    List<Direction> possibleMoves = model.direction();
    int currentLoc = model.getPlayerCurrentLocation();
    for (Direction pm : possibleMoves) {
        if (model.locationDirectionMap2(currentLoc, pm) == i) {
          model.movePlayer(pm);
//          return true;
        }
      }
//    return false;
  }

  @Override
  public void handleKeyMovement(Direction d) {
    List<Direction> possibleMoves = model.direction();
    if(possibleMoves.contains(d)){
      model.movePlayer(d);
    }
  }

  @Override
  public void handlePick(GameControls gc) {
    String item ="";

    switch (gc) {
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
        try {
          model.pickArrow();
        } catch (IllegalArgumentException | IllegalStateException e) {
          e.printStackTrace();
        }
        break;
      default://do nothing for now
        throw new IllegalStateException("Can't pick the item: " + item);

    }
    if(gc==R || gc==D || gc==H) {
      try {
        model.pickTreasure(item);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void handleAttack(Direction d, int distance) {
    boolean hit = false;

    try {
      hit = model.slayingOtyugh(distance, d); //add check for number of arrows left
    } catch (IllegalArgumentException | IllegalStateException e) {
      e.printStackTrace();
    }

    if (model.playerArrows() == 0) {
      return;
    }

    if (hit) {
      System.out.println("You hear a great howl in the distance. ");

    } else {
        System.out.println("\nYou shoot an arrow into the darkness. ");
    }
  }

  public ReadOnlyDungeonGM getModel(){
    return model;
  }

  public DungeonInput getInput() {
    return input;
  }

  public void restart() {
    setModel(input);
  }

}
