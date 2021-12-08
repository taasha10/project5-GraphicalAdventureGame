package dungeon.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import dungeon.model.Direction;
import dungeon.model.ReadOnlyDungeonGM;

public class CustomLabel extends JLabel {

  private final int id;
//  private int flag = 0;
  ReadOnlyDungeonGM r;
  ImageRepository ir;

  public CustomLabel(int id, ReadOnlyDungeonGM r, ImageRepository ir) {
    this.id = id;
    this.r = r;
    this.ir=ir;
  }

  public int getId() {
    return id;
  }

  BufferedImage addingImg() {
    //show that location img
    BufferedImage location = showLocation();
//    System.out.println(location.getHeight()+":"+location.getWidth());
    location = resize(location,200,200);

    //show treasure
    List<BufferedImage> treasureList = showTreasure(); //check for empty
    List<BufferedImage> insideLocation = new ArrayList<>();
    if(!treasureList.isEmpty()){
      insideLocation.addAll(treasureList);
    }

    //show arrows
    List<BufferedImage> arrowsList = showArrows(); //check for empty
    if(!arrowsList.isEmpty()){
      insideLocation.addAll(arrowsList);
    }

    //show player
    BufferedImage player = showPlayer();
    if(player!=null) {
      insideLocation.add(player);
    }

    //show Otyugh
    BufferedImage otyugh = showOtyugh();
    if(otyugh!=null) {
      insideLocation.add(otyugh);
    }


    //show smell
    BufferedImage smell = showSmell();
    if(smell!=null) {
      insideLocation.add(smell);
    }

    BufferedImage combined = overlay(location, insideLocation.get(0), 50);

    for(int i=1;i<insideLocation.size();i++){
      combined = overlay(combined,insideLocation.get(i),30+(10*i));
    }
    return combined;
  }

  private BufferedImage overlay(BufferedImage starting, BufferedImage overlay, int offset) {

    int w = Math.max(starting.getWidth(), overlay.getWidth());
    int h = Math.max(starting.getHeight(), overlay.getHeight());
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(starting, 0, 0, null);
    g.drawImage(overlay, offset, offset, null);
    return combined;
  }

  private BufferedImage showLocation() {
    List<Direction> dirs = r.direction();
    HashMap<String, BufferedImage> loc = ir.getLocations();
    if (dirs.contains(Direction.W) && dirs.contains(Direction.S) && dirs.contains(Direction.E) && dirs.contains(Direction.N))
      return loc.get("NSEW.png");
    else if (dirs.contains(Direction.N) && dirs.contains(Direction.S) && dirs.contains(Direction.E))
      return loc.get("NSE.png");
    else if (dirs.contains(Direction.W) && dirs.contains(Direction.S) && dirs.contains(Direction.E))
      return loc.get("SEW.png");
    else if (dirs.contains(Direction.W) && dirs.contains(Direction.S) && dirs.contains(Direction.N))
      return loc.get("NSW.png");
    else if (dirs.contains(Direction.W) && dirs.contains(Direction.N) && dirs.contains(Direction.E))
      return loc.get("NEW.png");
    else if (dirs.contains(Direction.N) && dirs.contains(Direction.S))
      return loc.get("NS.png");
    else if (dirs.contains(Direction.W) && dirs.contains(Direction.E))
      return loc.get("EW.png");
    else if (dirs.contains(Direction.N) && dirs.contains(Direction.E))
      return loc.get("NE.png");
    else if (dirs.contains(Direction.N) && dirs.contains(Direction.W))
      return loc.get("NW.png");
    else if (dirs.contains(Direction.S) && dirs.contains(Direction.E))
      return loc.get("SE.png");
    else if (dirs.contains(Direction.S) && dirs.contains(Direction.W))
      return loc.get("SW.png");
    else if (dirs.contains(Direction.N))
      return loc.get("N.png");
    else if (dirs.contains(Direction.S))
      return loc.get("S.png");
    else if (dirs.contains(Direction.W))
      return loc.get("W.png");
    else
      return loc.get("E.png");
  }

  private List<BufferedImage> showTreasure() {
    List<BufferedImage> b = new ArrayList<>();
    if (r.hasTreasure()) {
      int n = r.getTreasure().size();
      for (int i = 0; i < n; i++) {
        String treasure = String.valueOf(r.getTreasure().get(i));
        if (treasure.equalsIgnoreCase("Ruby")) {
          b.add(ir.getBufferedImages().get(4));
        } else if (treasure.equalsIgnoreCase("Diamond")) {
          b.add(ir.getBufferedImages().get(2));
        } else {
          b.add(ir.getBufferedImages().get(3));
        }
      }
    }
    return b;
  }

  private List<BufferedImage> showArrows() {
    List<BufferedImage> b = new ArrayList<>();
    if (r.hasArrows()) {
      int arrows = r.getArrows();
      for (int i = 0; i < arrows; i++) {
        b.add(ir.getBufferedImages().get(0));
      }
    }
    return b;
  }

  private BufferedImage showPlayer() {
    int currentLoc = r.getPlayerCurrentLocation();
    if(currentLoc == id){
//      System.out.println("player");
      return ir.getBufferedImages().get(8);
    }
//    System.out.println("no player");
    return null;
  }

  private BufferedImage showOtyugh() {
    List<Integer> oLoc = r.otyughLocation();
    if(oLoc.contains(id)){
//      System.out.println("otyugh");
      return ir.getBufferedImages().get(5);
    }
//    System.out.println("no otyugh");
    return null;
  }

  private BufferedImage showSmell() {

    int smell = r.calculateSmell();
    if (smell == 1) {
     return ir.getBufferedImages().get(6);
    }
    else if (smell == 2) {
      return ir.getBufferedImages().get(7);
    }
    return null;
  }

  BufferedImage resize(BufferedImage img, int newW, int newH) {
    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, null);
    g2d.dispose();

    return dimg;
  }
}
