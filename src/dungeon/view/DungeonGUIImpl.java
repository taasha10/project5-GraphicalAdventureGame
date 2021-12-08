package dungeon.view;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import dungeon.controller.DungeonInput;
import dungeon.controller.GUIControllerInterface;

public class DungeonGUIImpl extends JFrame implements DungeonGUI,ActionListener {

  private final JMenu gameSettings,settings;
  private JMenuItem start,help, reset,custom;
  private StartWindow startWindow;
  private final GUIControllerInterface controller;
  private final JLabel label;

  private JMenuBar mb;

  public DungeonGUIImpl(GUIControllerInterface controller) {
    super("Dungeon Adventure Game");

    setSize(400,400);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    mb=new JMenuBar();
    this.controller = controller;
    gameSettings=new JMenu("Game Settings");
    start=new JMenuItem("Start");
    settings=new JMenu("Settings");
    help=new JMenuItem("Help");

    reset=new JMenuItem("Reset to Default");
    reset.setActionCommand("reset");
    custom=new JMenuItem("Custom Settings");
    custom.setActionCommand("custom");

    label = new JLabel("Welcome to the Game of Dungeon!",JLabel.CENTER);
    label.setFont(new Font("Serif", Font.BOLD, 25));
    start.addActionListener(this);
//    settings.addActionListener(this);
//    help.addActionListener(this);
    reset.addActionListener(this);
    custom.addActionListener(this);

    settings.add(reset);
    settings.add(custom);
    gameSettings.add(start);
    gameSettings.add(settings);
    gameSettings.add(help);
    mb.add(gameSettings);
    add(label);
    setJMenuBar(mb);
    setLocationRelativeTo(null);
    setVisible(true);


  }

    public void actionPerformed(ActionEvent e) {

      switch (e.getActionCommand()){
        case "Start":
          this.dispose();
          startWindow = new StartWindow(controller);
          startWindow.setVisible(true);
          break;

        case "reset":
          controller.setModel(new DungeonInput("4","4","2","10","3","wrapping"));
          break;

        case "custom":
          inputHandler();
          break;
        default:
      }
    }

    private void inputHandler(){
      String rows = JOptionPane.showInputDialog(this, "Enter rows");
      String columns = JOptionPane.showInputDialog(this, "Enter Columns");
      String interconnectivity = JOptionPane.showInputDialog(this, "Enter interconnectivity");
      String percentage = JOptionPane.showInputDialog(this, "Enter percentage");
      String difficulty = JOptionPane.showInputDialog(this, "Enter Difficulty Level");
      String wrapOrNot = JOptionPane.showInputDialog(this, "Choose Wrapping or Non-Wrapping");
      controller.setModel(new DungeonInput(rows,columns,interconnectivity,percentage,difficulty,wrapOrNot));
    }

}


