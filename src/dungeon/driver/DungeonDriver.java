package dungeon.driver;

import dungeon.controller.DungeonGameControllerImpl;
import dungeon.model.DungeonGameModelImpl;
import dungeon.view.DungeonGUI;
import dungeon.view.DungeonGUIImpl;

import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Driver class to demonstrate the Model of the Dungeon game along with different functionalities
 * by a making mock of the Random Generator. The class shows dungeon creation,
 * valid path for the player to move, player movement, player picking treasure, player location
 * description on each move and player moving in the four directions.
 */

public class DungeonDriver {

  /**
   * Main method that takes argument from command line and perform above-mentioned
   * operations in a dungeon. Wrapping dungeon with zero interconnectivity.
   *
   * @param args size of the dungeon, its interconnectivity, whether it is wrapping or not, and the
   *             percentage of the caves that have treasure.Put this as cli args
   *             {5x6 0 wrapping 20}.
   */

  public static void main(String[] args) {

    if (args.length == 0) {
      throw new IllegalArgumentException("Arguments can't be empty");
    }

    String[] size = args[0].split("x");
    int rows;
    int columns;
    int interconnectivity;
    boolean isWrapping;
    int percentage;
    int otyugh;
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;

    if (args[2].equalsIgnoreCase("wrapping")) {
      isWrapping = true;
    } else if (args[2].equalsIgnoreCase("non-wrapping")) {
      isWrapping = false;
    } else {
      throw new IllegalArgumentException("Enter 'wrapping' or 'non-wrapping'");
    }

    try {
      rows = Integer.parseInt(size[0]);
      columns = Integer.parseInt(size[1]);
      interconnectivity = Integer.parseInt(args[1]);
      percentage = Integer.parseInt(args[3]);
      otyugh = Integer.parseInt(args[4]);
      new DungeonGameControllerImpl(input, output).playGame(new DungeonGameModelImpl(rows, columns,
              interconnectivity, isWrapping, percentage,
              "Real", otyugh));
      //    dungeonGameModel = new DungeonGameModelImpl(3, 3, 2,
      //            false, 10, "Fake", 3, 6,2);
    } catch (NumberFormatException numberFormatException) {
      try {
        output.append("Interconnectivity, Percentage and number of Otyughs must be an integer");
      } catch (IOException e) {
        throw new IllegalStateException("Append failed");
      }
    }



  }
}
