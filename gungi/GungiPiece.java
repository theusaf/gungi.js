package gungi;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GungiPiece implements Comparable<GungiPiece> {

    protected enum Title { 
        marshall, pawn, spy, cannon, fortress, samurai, captain, 
        musketeer, knight, archer, general, lieutenant_general, major_general 
    };
    
    private Title title;
    private boolean isWhite;
    private BufferedImage tier1, tier2, tier3, small, movement;
    
    public GungiPiece(Title title, boolean isWhite) {
        this.title = title;
        this.isWhite = isWhite;
        try {
            String color = (isWhite)? "white_pieces/" : "black_pieces/";
            tier1 = ImageIO.read(getClass().getResource("assets/" + color + title.name() + "_tier1.png"));
            small = ImageIO.read(getClass().getResource("assets/" + color + "small/" + title.name() + ".png"));
            movement = ImageIO.read(getClass().getResource("assets/movement/" + title.name() + ".png"));
            if (title != Title.fortress) {
                tier2 = ImageIO.read(getClass().getResource("assets/" + color + title.name() + "_tier2.png"));
                tier3 = ImageIO.read(getClass().getResource("assets/" + color + title.name() + "_tier3.png"));
            }
        } catch(IOException e) {
            System.out.println(title.name() + " : " + isWhite);
            e.printStackTrace();
        }
    }
    
    public Title getTitle() {
        return title;
    }
    
    public BufferedImage getTier1Image() {
        return tier1;
    }
    public BufferedImage getTier2Image() {
        return tier2;
    }
    public BufferedImage getTier3Image() {
        return tier3;
    }
    public BufferedImage getSmallImage() {
        return small;
    }
    public BufferedImage getMovementImage() {
        return movement;
    }
    
    public boolean isWhite() {
        return isWhite;
    }
    
    @Override
    public String toString() {
        if (isWhite) return "white " + title.name();
        return "black " + title.name();
    }
    
    @Override
    public boolean equals(Object other) {
        if (!other.getClass().getSimpleName().equals("GamePiece")) return false;
        return this.toString().equals(((GungiPiece) other).toString());
    }

    @Override
    public int compareTo(GungiPiece other) {
        return this.toString().compareTo(other.toString());
    }

}
