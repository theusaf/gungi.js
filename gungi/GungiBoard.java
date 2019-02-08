package gungi;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GungiBoard {

    private GungiPiece[][][] board;
    private BufferedImage image;
    private int numWhitePieces, numBlackPieces;
    
    public GungiBoard() {
        board = new GungiPiece[9][9][3];
        try {
            image = ImageIO.read(getClass().getResource("assets/gungi_board.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int getHeight(int x, int y) {
        if (board[x][y][2] != null) return 3;
        if (board[x][y][1] != null) return 2;
        if (board[x][y][0] != null) return 1;
        return 0;
    }
    
    public GungiPiece getTop(int x, int y) {
        int h = getHeight(x, y);
        if (h > 0) return board[x][y][h-1];
        return null;
    }
    
    public void removeTop(int x, int y) {
        int h = getHeight(x, y);
        if (h > 0) {
            if (board[x][y][h-1].isWhite()) numWhitePieces--;
            else numBlackPieces--;
            board[x][y][h-1] = null;
        }
    }
    
    public GungiPiece get(int x, int y, int z) {
        return board[x][y][z];
    }
    
    public void set(int x, int y, int z, GungiPiece piece) {
        if(piece.isWhite()) numWhitePieces++;
        else numBlackPieces++;
        board[x][y][z] = piece;
    }
    
    public boolean determineCheck(boolean isWhite, boolean whiteTurn) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        if (isWhite == whiteTurn) this.boardFlip();
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                GungiPiece piece = this.getTop(i, j);
                int h = this.getHeight(i, j);
                if (piece != null && piece.isWhite() == !isWhite) 
                    moves.addAll(LegalMoveGenerator.getMoves(i, j, h, piece, this));
            }
        }
        
        boolean checked = (isWhite) ? LegalMoveGenerator.contains(moves, getMarshallPos(true)) : 
            LegalMoveGenerator.contains(moves, getMarshallPos(false));
        
        if (isWhite == whiteTurn) this.boardFlip();
        return checked;
    }
    
    public boolean determineMate(boolean isWhite) {
        return false;
    }
    
    public int[] getMarshallPos(boolean isWhite) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                GungiPiece piece = this.getTop(i, j);
                if (piece != null && piece.getTitle() == GungiPiece.Title.marshall && piece.isWhite() == isWhite) {
                    int[] pos = { i, j };
                    return pos;
                }
            }
        }
        return null;
    }
    
    public int getNumPieces(boolean isWhite) {
        if (isWhite) return numWhitePieces;
        return numBlackPieces;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    private void transpose() {
        for (int i = 0; i < 9; i++) { 
            for (int j = i; j < 9; j++) { 
                GungiPiece temp[] = board[i][j]; 
                board[i][j] = board[j][i]; 
                board[j][i] = temp; 
            } 
        } 
    }
    
    private void reverseCols() {
        for (int i = 0; i < 9; i++) { 
            for (int j = 0, k = 8; j < k; j++, k--) { 
                GungiPiece temp[] = board[j][i]; 
                board[j][i] = board[k][i]; 
                board[k][i] = temp; 
            } 
        } 
    }
    
    public void boardFlip() {
        transpose();
        reverseCols();
        transpose();
        reverseCols();
    }
    
    public void clear() {
        numWhitePieces = 0;
        numBlackPieces = 0;
        board = new GungiPiece[9][9][3];
    }
}
