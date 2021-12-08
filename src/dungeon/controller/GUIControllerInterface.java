package dungeon.controller;

import dungeon.model.Direction;
import dungeon.model.ReadOnlyDungeonGM;
import dungeon.view.DungeonGUI;

public interface GUIControllerInterface {

  void setView(DungeonGUI view);

  void setModel(DungeonInput inputs);

  ReadOnlyDungeonGM getModel();

  DungeonInput getInput();

  void restart();

  void clickHandler(int i);

  void handleKeyMovement(Direction d);

  void handlePick(GameControls gc);

  void handleAttack(Direction d,int distance);
}
