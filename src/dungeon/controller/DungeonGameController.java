package dungeon.controller;

import dungeon.model.DungeonGameModel;

/**
 * Represents a Controller for a Text Based Adventure Game of Dungeon and Monsters: handles user
 * moves by executing them using the model and conveys outcomes to the user in some form.
 */
public interface DungeonGameController {

  /**
   * Execute a single game of dungeon given a dungeon Model. When the game is over,
   * the playGame method ends.
   *
   * @param d a non-null Dungeon Game Model
   */
  void playGame(DungeonGameModel d);
}
