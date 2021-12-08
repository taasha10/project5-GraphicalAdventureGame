package dungeon.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageRepository {

    public final byte arrow = 0;
    public final byte blank = 1;
    public final byte diamond = 2;
    public final byte sapphire = 3;
    public final byte ruby = 4;
    public final byte otyugh = 5;
    public final byte stench1 = 6;
    public final byte stench2 = 7;
    public final byte player = 8;

    public final HashMap<String, BufferedImage> locations = new HashMap<>();
    public final ArrayList<BufferedImage> bufferedImages = new ArrayList<>();


    public void load(){
//        File imagesFolder = new File("./res/dungeon-images");
        File locationsFolder = new File("./res/dungeon-images/color-cells");
        try {
            for (File imgFile : locationsFolder.listFiles()) {
                locations.put(imgFile.getName(), ImageIO.read(imgFile));
            }
            bufferedImages.add(arrow,ImageIO.read(new File("./res/dungeon-images/arrow-white.png")));
            bufferedImages.add(blank,ImageIO.read(new File("./res/dungeon-images/blank.png")));
            bufferedImages.add(diamond,ImageIO.read(new File("./res/dungeon-images/diamond.png")));
            bufferedImages.add(sapphire,ImageIO.read(new File("./res/dungeon-images/sapphire.png")));
            bufferedImages.add(ruby,ImageIO.read(new File("./res/dungeon-images/ruby.png")));
            bufferedImages.add(otyugh,ImageIO.read(new File("./res/dungeon-images/otyugh.png")));
            bufferedImages.add(stench1,ImageIO.read(new File("./res/dungeon-images/stench1.png")));
            bufferedImages.add(stench2,ImageIO.read(new File("./res/dungeon-images/stench2.png")));
            bufferedImages.add(player,ImageIO.read(new File("./res/dungeon-images/player.png")));

        }
        catch (NullPointerException | IOException e){
            e.printStackTrace();
        }
    }

    public HashMap<String, BufferedImage> getLocations() {
        return locations;
    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }
}
