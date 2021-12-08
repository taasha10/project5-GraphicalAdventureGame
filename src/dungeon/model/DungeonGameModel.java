package dungeon.model;

import java.util.List;

/**
 * Main interface that interacts with the driver and returns values based on different methods.
 * It takes both Player and Dungeon classes' objects and perform functions like moving the player,
 * picking the treasure etc.
 */
public interface DungeonGameModel extends ReadOnlyDungeonGM {

  /**
   * Method that moves the player to the start location in the dungeon.
   */
  void enterPlayer();

  /**
   * Method to move a player within the dungeon.
   *
   * @param locationDirection direction the player has to move to.
   */

  void movePlayer(Direction locationDirection);

  /**
   * Method pick the treasure specified treasure from the cave.
   * @param treasure is one of the Sapphire, Diamond and Ruby.
   */

  void pickTreasure(String treasure);


  /**
   * Picks an arrow from the player's current location, when player asks for it.
   */
  void pickArrow();



  /**
   * Method tries to slay an Otyugh from a location.
   * @param shootingDistance from the player's current location.
   * @param direction direction to shoot the arrow.
   * @return true if hit; false if it's miss.
   */
  boolean slayingOtyugh(int shootingDistance, Direction direction);

  int locationDirectionMap2(int from, Direction direction);



}
