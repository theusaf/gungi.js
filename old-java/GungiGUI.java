package gungi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GungiGUI extends JFrame {

    private static final long serialVersionUID = 818935534467986466L;
    private final int WIDTH, HEIGHT, WIDTH_INSET = 6, HEIGHT_INSET = 32, SIDE_PANEL_WIDTH = 500;
    private static final boolean DEBUG = true;
    private enum GameState { draft, game };
    
    private GungiBoard board = new GungiBoard();
    private Stockpile whiteStockpile, blackStockpile;
    private Selector selector = new Selector();
    private boolean whiteTurn = false;
    private boolean whiteReadied = false, blackReadied = false;
    
    private Renderer renderer;
    private GameState state;        

    public GungiGUI(Stockpile whiteStockpile, Stockpile blackStockpile) {
        this.whiteStockpile = whiteStockpile;
        this.blackStockpile = blackStockpile;
       
        WIDTH = board.getImage().getWidth();
        HEIGHT = board.getImage().getHeight();
        
        this.buildFrame();
        renderer = new Renderer();
        this.add(renderer);
        renderer.repaint();
        this.addMouseListener(mouseInput);
        this.addKeyListener(keyInput);
        this.setVisible(true);
        
        state = GameState.draft;
    }

    private void buildFrame() {
        this.setTitle("Gungi");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH + WIDTH_INSET + SIDE_PANEL_WIDTH, HEIGHT + HEIGHT_INSET);
    }
    
    MouseListener mouseInput = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX(), y = e.getY();
            checkSelection(x, y);
            
            int cellX = selector.getCellX(), cellY = selector.getCellY();
            GungiPiece selected = selector.getSelection();
            
            // placement action
            if (selector.getSelected() == Selector.Selected.board && selected != null) {
                if (!place(cellX, cellY, selected, false)) return;
                
                if (!whiteTurn) blackStockpile.deploy(selected);
                else whiteStockpile.deploy(selected);
                selector.deselect();
                if (state == GameState.game || (!blackReadied || !whiteTurn) && (!whiteReadied || whiteTurn)) {
                    whiteTurn = !whiteTurn;
                    board.boardFlip();
                }
            }
            
            // movement | attack | stack action
            int[] prevSelected = selector.getPrevSelectedCell();
            if (state == GameState.game && selector.getSelected() == Selector.Selected.board && prevSelected != null) {
                GungiPiece piece = board.getTop(prevSelected[0], prevSelected[1]);
                if (piece != null && piece.isWhite() == whiteTurn) {
                    if (!move(cellX, cellY, prevSelected[0], prevSelected[1], piece)) {
                        selector.deselect();
                        GungiGUI.this.repaint();
                        return;
                    }
                    
                    selector.deselect();
                    whiteTurn = !whiteTurn;
                    board.boardFlip();
                }
            }
            
            String format = String.format("%4d:%4d", x, y);
            String selectedCellStr = Arrays.toString(selector.getSelectedCell());
            String prevSelectedStr = Arrays.toString(prevSelected);
            if (DEBUG) System.out.println(format + "\tselected: " + selectedCellStr + "\tprev: " + prevSelectedStr 
                    + "\t" + selector.getSelected().name());
            GungiGUI.this.repaint();
        }
        
        public boolean move(int x, int y, int prevX, int prevY, GungiPiece piece) {
            int h = board.getHeight(prevX, prevY);
            List<int[]> legalMoves = LegalMoveGenerator.getMoves(prevX, prevY, h, piece, board);
            int[] move = { x, y };
            
            if (!LegalMoveGenerator.contains(legalMoves, move)) return false;

            if (board.getTop(x, y) == null || board.getTop(x, y).isWhite() == whiteTurn) {
                // move to empty square or stack on friendly piece
                board.removeTop(prevX, prevY);
                if (!place(x, y, piece, false)) {
                    place(prevX, prevY, piece, true);    
                    return false;
                }
            } else {
                // enemy piece => options: could attack or stack
                String[] options = { "attack", "stack", "none" };
                switch (JOptionPane.showOptionDialog(GungiGUI.this, "Select which action to take?", "Action Type",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0])) {
                case 0:
                    // attack
                    GungiPiece target = board.getTop(x, y);
                    board.removeTop(x, y);
                    board.removeTop(prevX, prevY);
                    if (!place(x, y, piece, false)) {
                        place(prevX, prevY, piece, true);
                        place(x, y, target, true);
                        return false;
                    }
                    if (whiteTurn) whiteStockpile.capture(target);
                    else blackStockpile.capture(target);
                    break;
                case 1: 
                    board.removeTop(prevX, prevY);
                    if (!place(x, y, piece, false)) {
                        place(prevX, prevY, piece, true);       
                        return false;
                    }
                    break;
                default:
                    return false;
                }
            }
            
            return true;
        }
        
        public boolean place(int x, int y, GungiPiece selected, boolean undo) {
            int h = board.getHeight(x, y);
            
            if (whiteTurn && board.getNumPieces(true) == 26 || !whiteTurn && board.getNumPieces(false) == 26) {
                JOptionPane.showMessageDialog(GungiGUI.this, "Max number of pieces on the board for any player at anytime is 26!", 
                        "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                return false;
            }
            if (h > 0 && board.get(x, y, h-1).getTitle() == GungiPiece.Title.marshall) {
                JOptionPane.showMessageDialog(GungiGUI.this, "Pieces can't be stacked on the marshall!", 
                        "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                return false;
            }
            if (h > 0 && selected.getTitle() == GungiPiece.Title.fortress) {
                JOptionPane.showMessageDialog(GungiGUI.this, "Fortresses cannot stack on other pieces!", 
                        "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                return false;
            }
            
            if (state == GameState.draft) {
                if (y < 6) {
                    JOptionPane.showMessageDialog(GungiGUI.this, "During draft phase, you may only place pieces within your first 3 ranks!", 
                            "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                    return false;
                }
                if (selected.getTitle() != GungiPiece.Title.marshall) {
                    if (!whiteTurn && board.getMarshallPos(false) == null || whiteTurn && board.getMarshallPos(true) == null) {
                        JOptionPane.showMessageDialog(GungiGUI.this, "Must place marshall first!", "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                        return false;
                    }
                }
            } else {
                // pawns can't be placed in same file [stacking diff piece on pawn break this]
                if (selector.getSelection() != null) {
                    for (int i = 0; i < 9; i++) {
                        GungiPiece piece = board.getTop(x, i);
                        if (piece != null && piece.getTitle() == GungiPiece.Title.pawn && 
                                selector.getSelection().getTitle() == GungiPiece.Title.pawn) {
                            if ((piece.isWhite() && whiteTurn) || (!piece.isWhite() && !whiteTurn)) {
                                JOptionPane.showMessageDialog(GungiGUI.this, "Multiple pawns can't be placed in the same file!", 
                                        "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                                return false;
                            }
                        }
                    }
                }
                
                
                // pawns can't place opposing marshall into mate
                
                
                // pieces can't be placed within first 3 ranks of opposing side
                if (selector.getSelection() != null && y < 3) {
                    JOptionPane.showMessageDialog(GungiGUI.this, "Pieces cannot be placed within the first 3 ranks of opposing side!", 
                            "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                    return false;
                }
            }
            
            if (h == 3) {
                JOptionPane.showMessageDialog(GungiGUI.this, "Cannot stack past tier 3!", "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                return false;
            } else board.set(x, y, h, selected);      
            
            // can't place unless it gets you out of check
            if (!undo && ((whiteTurn && board.determineCheck(true, whiteTurn)) || (!whiteTurn && board.determineCheck(false, whiteTurn)))) {
                JOptionPane.showMessageDialog(GungiGUI.this, "Check!", "Illegal Move", JOptionPane.ERROR_MESSAGE, null);
                board.removeTop(x, y);
                return false;
            }
            
            return true;
        }
        
        public void checkSelection(int x, int y) {
            int cellX = (x - WIDTH_INSET)/100, cellY = (y - HEIGHT_INSET)/100;
            if (x > WIDTH+WIDTH_INSET) {
                if (whiteTurn) {
                    int col = (x-WIDTH-WIDTH_INSET-25)/50, row = (y-HEIGHT_INSET-87)/50;
                    int index = 9 * row + col;
                    if (col < 0 || col > 8 || row < 0 || row > 8 || index >= whiteStockpile.getReserveSize() ||
                            (y-HEIGHT_INSET-87) < 0 || (x-WIDTH-WIDTH_INSET-25) < 0) {
                        selector.deselect();
                    } else {
                        int[] selectedCell = { col, row };
                        selector.setSelectedCell(Selector.Selected.white_reserve, selectedCell);
                    }
                } else {
                    int col = (x-WIDTH-WIDTH_INSET-25)/50, row = (y-HEIGHT_INSET-620)/50;
                    int index = 9 * row + col;
                    if (col < 0 || col > 8 || row < 0 || row > 8 || index >= blackStockpile.getReserveSize()  || 
                            (y-HEIGHT_INSET-620) < 0 || (x-WIDTH-WIDTH_INSET-25) < 0) {
                        selector.deselect();
                    } else {
                        int[] selectedCell = { col, row };
                        selector.setSelectedCell(Selector.Selected.black_reserve, selectedCell);
                    }
                }
            } else {
                int[] selectedCell = { cellX, cellY };
                selector.setSelectedCell(Selector.Selected.board, selectedCell);
            }
        }

        @Override
        public void mouseEntered(MouseEvent arg0) { }
        @Override
        public void mouseExited(MouseEvent arg0) { }
        @Override
        public void mousePressed(MouseEvent arg0) { }
        @Override
        public void mouseReleased(MouseEvent arg0) { }
        
    };
    
    KeyListener keyInput = new KeyListener() {

        @Override
        public void keyPressed(KeyEvent e) {
            String info = "";
            
            switch (e.getKeyCode()) {
            case KeyEvent.VK_R:
                info = "ready";
                ready();
                break;
            case KeyEvent.VK_ESCAPE:
                info = "deselect";
                selector.deselect();
                break;
            case KeyEvent.VK_S:
                info = "save";
                // serialization || file IO?
                break;
            case KeyEvent.VK_H:
                info = "help";
                break;
            case KeyEvent.VK_F:
                info = "forfeit";
                int choice = JOptionPane.showConfirmDialog(GungiGUI.this, "forfeit?");
                if (choice == JOptionPane.YES_OPTION) {
                    String winner = (whiteTurn) ? "black" : "white";
                    JOptionPane.showMessageDialog(GungiGUI.this, winner + " wins!", "Game Concluded", JOptionPane.INFORMATION_MESSAGE, null);
                    reset();
                }
            }
            
            if (DEBUG) System.out.println(info);
            GungiGUI.this.repaint();
        }
        
        private void reset() {
            whiteStockpile.clear();
            blackStockpile.clear();
            board.clear();
            Gungi.setup(whiteStockpile, blackStockpile);
            whiteReadied = false;
            blackReadied = false;
            state = GameState.draft;
        }
        
        private void ready() {
            if (!blackReadied && !whiteTurn || !whiteReadied && whiteTurn) {
                if (state == GameState.draft && ((whiteTurn && board.getNumPieces(true) > 0) || (!whiteTurn && board.getNumPieces(false) > 0))) {
                    int choice = JOptionPane.showConfirmDialog(GungiGUI.this, "satisfied with this setup?");
                    blackReadied |= (choice == JOptionPane.YES_OPTION && !whiteTurn);
                    whiteReadied |= (choice == JOptionPane.YES_OPTION && whiteTurn);
                    if (choice == JOptionPane.YES_OPTION) {
                        whiteTurn = !whiteTurn;
                        board.boardFlip();
                    }
                }
                if (blackReadied == whiteReadied && whiteReadied == true) {
                    state = GameState.game;
                    if (!whiteTurn) {
                        whiteTurn = !whiteTurn;
                        board.boardFlip();
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent arg0) { }

        @Override
        public void keyTyped(KeyEvent arg0) { }
        
    };
    
    private void render(Graphics2D g) {
        g.drawImage(board.getImage(), 0, 0, null);
        boolean whiteChecked = board.determineCheck(true, whiteTurn), blackChecked = board.determineCheck(false, whiteTurn);
        
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board.get(x, y, 2) != null) {
                   g.drawImage(board.get(x, y, 2).getTier3Image(), x*101, y*101, null);
                } else if (board.get(x, y, 1) != null) {
                    g.drawImage(board.get(x, y, 1).getTier2Image(), x*101, y*101, null);
                } else if (board.get(x, y, 0) != null) {
                    g.drawImage(board.get(x, y, 0).getTier1Image(), x*101, y*101, null);
                }
                
                GungiPiece piece = board.getTop(x, y);
                
                if (piece != null && piece.getTitle() == GungiPiece.Title.marshall &&
                        (whiteChecked && piece.isWhite() || blackChecked && !piece.isWhite())) {
                    g.setColor(new Color(.9f, 0, 0, .25f));
                    g.fill(new Ellipse2D.Double(x*101, y*101, 100, 100));
                }
            }
        }
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String info = state.name() + " phase: ";
        if (whiteTurn) info += "white's turn";
        else info += "black's turn";
        g.drawString(info, WIDTH+WIDTH_INSET+255, HEIGHT_INSET-5);
        
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("white's stockpile:", WIDTH+WIDTH_INSET+60, HEIGHT_INSET+30);
        renderStockpile(g, whiteStockpile, 55, true, Selector.Selected.white_reserve);
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("black's stockpile:", WIDTH+WIDTH_INSET+60, HEIGHT_INSET+562);
        renderStockpile(g, blackStockpile, 590, false, Selector.Selected.black_reserve);
        
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("piece/tower info:", WIDTH+WIDTH_INSET+30, HEIGHT_INSET+352);
        g.setFont(new Font("Arial", Font.PLAIN, 25));
        if (selector.getSelected() == Selector.Selected.board) {
            int x = selector.getCellX(), y = selector.getCellY();
            
            // tower info
            for (int h = 0; h < 3; h++) {
                GungiPiece piece = board.get(x, y, h);
                if (piece != null) {
                    g.drawImage(piece.getSmallImage(), WIDTH+WIDTH_INSET+90, HEIGHT_INSET+325 + (3-h)*50, null);
                    g.drawString("tier " + (h+1) + " " + piece.getTitle().name(), WIDTH+WIDTH_INSET+145, HEIGHT_INSET+355 + (3-h)*50);
                }
            }
            
            if (state == GameState.game) {
                int h = board.getHeight(x, y);
                GungiPiece piece = null;
                if (h > 0) piece = board.get(x, y, h-1);
                if (piece != null && piece.isWhite() == whiteTurn) {
                    // highlight possible moves
                    for (int[] cell : LegalMoveGenerator.getMoves(x, y, h, piece, board)) {
                        g.setColor(new Color(0, .4f, .9f, .25f));
                        g.fill(new Ellipse2D.Double(cell[0]*101, cell[1]*101, 100, 100));
                    }
                }
            }
        }
    }
    
    private void renderStockpile(Graphics2D g, Stockpile stockpile, int inset, boolean whiteTurn, Selector.Selected selected) {
      int x = WIDTH+WIDTH_INSET+25, y = HEIGHT_INSET+inset;
      int j = 0;
      for (Map.Entry<GungiPiece, Integer> entry : stockpile.getReserve().entrySet()) {
          for (int i = 0; i < entry.getValue(); i++) {
              g.drawImage(entry.getKey().getSmallImage(), x, y, null);
              
              if (this.whiteTurn == whiteTurn && selector.getSelected() == selected && j == selector.getIndex()) {
                  g.setColor(new Color(0, .4f, .9f, .25f));
                  g.fill(new Ellipse2D.Double(x-1, y-1, 48, 48));
                  
                  selector.setSelection(entry.getKey());
                  
                  // piece info
                  g.setColor(Color.BLACK);
                  g.setFont(new Font("Arial", Font.PLAIN, 25));
                  g.drawString(entry.getKey().getTitle().name(), WIDTH+WIDTH_INSET+280, HEIGHT_INSET+352);
                  g.drawImage(entry.getKey().getMovementImage(), WIDTH+WIDTH_INSET+5, HEIGHT_INSET+365, null);
              }
              j++;
              x += 50;
              if (x > WIDTH+WIDTH_INSET+25+(8*50)) {
                  x = WIDTH+WIDTH_INSET+25;
                  y += 50;
              }
          }
      }
      
      // captured list
      for (Map.Entry<GungiPiece, Integer> entry : stockpile.getCaptured().entrySet()) {
          for (int i = 0; i < entry.getValue(); i++) {
              g.drawImage(entry.getKey().getSmallImage(), x, y, null);
              
              x += 50;
              if (x > WIDTH+WIDTH_INSET+25+(8*50)) {
                  x = WIDTH+WIDTH_INSET+25;
                  y += 50;
              }
          }
      }
    }
    
    public class Renderer extends JPanel {
        
        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            GungiGUI.this.render((Graphics2D) g);
        }
    }
}
