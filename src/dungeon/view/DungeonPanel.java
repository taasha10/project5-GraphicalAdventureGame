package dungeon.view;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import dungeon.controller.GUIControllerInterface;
import dungeon.controller.GameControls;
import dungeon.model.Direction;
import dungeon.model.ReadOnlyDungeonGM;

public class DungeonPanel extends JPanel  implements KeyListener, MouseListener{

  private final ImageRepository ir;
  private final ReadOnlyDungeonGM r;
  private final List<CustomLabel> customLabelList;
  private final GUIControllerInterface controller;
  boolean shiftPressed;
  int distance;

  public DungeonPanel(GUIControllerInterface controller) {

    int rows = Integer.parseInt(controller.getInput().getRows());
    int columns = Integer.parseInt(controller.getInput().getColumns());
    LayoutManager grid = new GridLayout(rows,columns);
    ir = new ImageRepository();
    ir.load();
    setLayout(grid);
    setPreferredSize(new Dimension(800,800));
    setBackground(Color.black);
    r = controller.getModel();
    customLabelList = new ArrayList<>();
    this.controller = controller;
    shiftPressed = false;
    distance = 0;

    for (int i=0;i<rows*columns;i++){
      CustomLabel label = new CustomLabel(i,r,ir);
      label.addMouseListener(this);
      label.addKeyListener(this);
      label.setFocusable(true);
      customLabelList.add(label);
      add(label);
      System.out.println(i);
    }

    if (r.isGameOver()) {
      if(r.ifPlayerWon()){
        JLabel l = new JLabel("Game Over! You won!");
        l.setSize(200,200);
        l.setForeground(Color.white);
        add(l);
      }
      else{
        JLabel l = new JLabel("Game Over! You lost!");
        l.setSize(200,200);
        add(l);
      }
      removeKeyListener(this);
      removeMouseListener(this);
    }

  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    CustomLabel c = customLabelList.get(r.getPlayerCurrentLocation());
    BufferedImage combined = c.addingImg();
    c.setIcon(new ImageIcon(combined));
//    System.out.println("here");
  }

  private int enterDistance(){
    int distanceDialog =0;
    try{
      distanceDialog = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter distance"));
    }catch (NumberFormatException e){
      e.printStackTrace();
    }
      if(distanceDialog>=1 && distanceDialog<=5){
        System.out.println("Exit enter distance");
        return distanceDialog;
      }
      else{
        JOptionPane.showMessageDialog(this,"Enter a valid distance(1-5)","Alert",JOptionPane.WARNING_MESSAGE);
        return enterDistance();

      }
  }


  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
      shiftPressed = true;
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_LEFT){
      if(shiftPressed){
        distance = enterDistance();
        controller.handleAttack(Direction.W,distance);
        shiftPressed = false;
      }
      else{
        controller.handleKeyMovement(Direction.W);
      }

    }
    else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
      if(shiftPressed){
        distance = enterDistance();
        controller.handleAttack(Direction.E,distance);
        shiftPressed = false;
      }
      else{
        controller.handleKeyMovement(Direction.E);
      }

    }
    else if(e.getKeyCode() == KeyEvent.VK_UP){
      if(shiftPressed){
        distance = enterDistance();
        controller.handleAttack(Direction.N,distance);
        shiftPressed = false;
      }
      else{
        controller.handleKeyMovement(Direction.N);
      }

    }
    else if(e.getKeyCode() == KeyEvent.VK_DOWN){
      System.out.println(shiftPressed);
      if(shiftPressed){
        distance = enterDistance();
        controller.handleAttack(Direction.S,distance);
        shiftPressed = false;
      }
      else{
        controller.handleKeyMovement(Direction.S);
      }
    }
    else if(e.getKeyCode() == KeyEvent.VK_A){
      controller.handlePick(GameControls.A);
    }
    else if(e.getKeyCode() == KeyEvent.VK_R){
      controller.handlePick(GameControls.R);
    }
    else if(e.getKeyCode() == KeyEvent.VK_D){
      controller.handlePick(GameControls.D);
    }
    else if(e.getKeyCode() == KeyEvent.VK_H){
      controller.handlePick(GameControls.H);
    }


  }

  @Override
  public void mouseClicked(MouseEvent e) {
    CustomLabel l = (CustomLabel)e.getSource();
    controller.clickHandler(l.getId());
  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }
}
