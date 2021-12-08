package dungeon.driver;

import dungeon.controller.GUIController;
import dungeon.controller.GUIControllerInterface;
import dungeon.model.DungeonGameModel;
import dungeon.model.DungeonGameModelImpl;
import dungeon.view.DungeonGUI;
import dungeon.view.DungeonGUIImpl;

public class GUIDriver {


  public static void main(String[] args){

    GUIControllerInterface controller = new GUIController();
    DungeonGUI view = new DungeonGUIImpl(new GUIController());
    controller.setView(view);
//    DungeonGameModel model = new DungeonGameModelImpl(4,4,2,
//            true,10,"Real",3);
//    GUIControllerInterface controller = new GUIController(model,view);

//    System.out.println("here");
  }
}
