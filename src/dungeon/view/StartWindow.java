package dungeon.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dungeon.controller.GUIControllerInterface;

public class StartWindow extends JFrame implements ActionListener {

  private final JMenu menu;
  private JMenuItem newGame, restart, quit;
  private JMenuBar mb;
  private DungeonPanel dp;
  private JScrollPane scrollableGameArea;
  private GUIControllerInterface controller;


  public StartWindow(GUIControllerInterface controller){
    super("Start Game");
    setSize(1000,700);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    this.controller = controller;
    menu=new JMenu("Menu");
    newGame=new JMenuItem("New Game");
    restart=new JMenuItem("Restart");
    quit=new JMenuItem("Quit");
    mb =new JMenuBar();

    dp =  new DungeonPanel(controller);
    dp.setSize(1200,800);
    scrollableGameArea = new JScrollPane(dp);

    restart.addActionListener(this);
    quit.addActionListener(this);
    newGame.addActionListener(this);

    menu.add(restart);
    menu.add(newGame);
    menu.add(quit);
    mb.add(menu);
    setJMenuBar(mb);
    add(scrollableGameArea);
    setResizable(false);
    System.out.println("Jframe");
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if(e.getActionCommand().equals("Restart")){
      int dialogButton1 = JOptionPane.showConfirmDialog (this, "Do you want to " +
              "restart the game?",null,JOptionPane.YES_NO_OPTION);

      if(dialogButton1 == JOptionPane.YES_OPTION){
        refresh();
        controller.restart();
    }
    }

    else if(e.getActionCommand().equals("New Game")){
      int dialogButton1 = JOptionPane.showConfirmDialog (this, "Do you want to " +
              "start a new game?",null,JOptionPane.YES_NO_OPTION);

      if(dialogButton1 == JOptionPane.YES_OPTION){
        dispose();
        new DungeonGUIImpl(controller);
      }
    }

    else if(e.getActionCommand().equals("Quit")){
      int dialogButton2 = JOptionPane.showConfirmDialog (this, "Are you sure about " +
              "quitting the game?",null,JOptionPane.YES_NO_OPTION);

      if(dialogButton2 == JOptionPane.YES_OPTION){
        System.exit(0);
      }
    }
  }

//  public void addClickListener(GUIControllerInterface listener){
//
//
//  }

  public void refresh(){
    repaint();
  }
}
