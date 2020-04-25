package gungi;

public class Gungi {

    public static void main(String[] args) {
        Stockpile whiteStockpile = new Stockpile();
        Stockpile blackStockpile = new Stockpile();
        setup(whiteStockpile, blackStockpile);
        new GungiGUI(whiteStockpile, blackStockpile);
    }

    public static void setup(Stockpile whiteStockpile, Stockpile blackStockpile) {
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.major_general, true), 4);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.lieutenant_general, true), 4);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.general, true), 6);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.archer, true), 2);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.knight, true), 2);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.musketeer, true), 1);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.captain, true), 1);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.samurai, true), 2);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.fortress, true), 2);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.cannon, true), 2);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.spy, true), 2);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.pawn, true), 9);
        whiteStockpile.amass(new GungiPiece(GungiPiece.Title.marshall, true), 1);
        
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.major_general, false), 4);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.lieutenant_general, false), 4);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.general, false), 6);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.archer, false), 2);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.knight, false), 2);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.musketeer, false), 1);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.captain, false), 1);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.samurai, false), 2);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.fortress, false), 2);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.cannon, false), 2);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.spy, false), 2);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.pawn, false), 9);
        blackStockpile.amass(new GungiPiece(GungiPiece.Title.marshall, false), 1);
    }

}
