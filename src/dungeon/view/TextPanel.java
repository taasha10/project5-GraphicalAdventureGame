package dungeon.view;

import java.util.List;

import javax.swing.*;

import dungeon.controller.GUIControllerInterface;
import dungeon.model.ReadOnlyDungeonGM;

public class TextPanel extends JPanel {

  private final ReadOnlyDungeonGM r;
  private final List<CustomLabel> customLabelList;
  private final GUIControllerInterface controller;

  public TextPanel(ReadOnlyDungeonGM r, List<CustomLabel> customLabelList, GUIControllerInterface controller) {
    this.r = r;
    this.customLabelList = customLabelList;
    this.controller = controller;

    r.getPlayerLocation();

  }


}
