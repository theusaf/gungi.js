package gungi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class LegalMoveGenerator {

    public static List<int[]> getMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        switch (piece.getTitle()) {
        case marshall:
        case fortress:
            return marshallMoves(x, y, h, piece, board);
        case pawn:
            return pawnMoves(x, y, h, piece, board);
        case spy:
            return spyMoves(x, y, h, piece, board);
        case cannon:
            return cannonMoves(x, y, h, piece, board);
        case samurai:
            return samuraiMoves(x, y, h, piece, board);
        case captain:
            return captainMoves(x, y, h, piece, board);
        case musketeer:
            return musketeerMoves(x, y, h, piece, board);
        case knight:
            return knightMoves(x, y, h, piece, board);
        case archer:
            return archerMoves(x, y, h, piece, board);
        case general:
            return generalMoves(x, y, h, piece, board);
        case lieutenant_general:
            return lieutenantGeneralMoves(x, y, h, piece, board);
        case major_general:
            return majorGeneralMoves(x, y, h, piece, board);
        }
        
        return null;
    }
    
    public static boolean contains(List<int[]> list, int[] move) {
        for (int[] i : list) {
            if (Arrays.equals(i, move)) return true;
        }
        return false;
    }
    
    private static boolean withinBoundary(int[] pos) {
        int x = pos[0], y = pos[1];
        if (x > 8 || x < 0 || y > 8 || y < 0) return false;
        return true;
    }
    
    private static List<int[]> marshallMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                int[] pos = { i, j };
                if (withinBoundary(pos) && (i != x || j != y)) moves.add(pos);
            }
        }
        return moves;
    }
    
    private static List<int[]> pawnMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        switch (h) {
        case 3:
        case 2:
            for (int i = x-1; i <= x+1; i++) {
                int[] pos = { i, y-1 };
                if (withinBoundary(pos)) moves.add(pos);
            }
            break;
        default:
            int[] pos = { x, y-1 };
            if (withinBoundary(pos)) moves.add(pos);
        }
        return moves;
    }
    
    private static List<int[]> spyMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        boolean[] toProbe = { true, true, true, true, true, true, true, true };
        
        switch (h) {
        case 3:
            probePaths(moves, x, y, board, 8, toProbe);
            break;
        case 2:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (withinBoundary(pos) && i != x && j != y) moves.add(pos);
                }
            }
            break;
        default:
            int[] pos = { x, y-1 };
            if (withinBoundary(pos)) moves.add(pos);
        }
        return moves;
    }
    
    private static List<int[]> cannonMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        boolean[] toProbe = { true, true, true, true, false, false, false, false };
        
        switch (h) {
        case 3:
            probePaths(moves, x, y, board, 8, toProbe);
            break;
        case 2:
            probePaths(moves, x, y, board, 2, toProbe);
            break;
        default:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (i == x && j == y) continue;
                    if (withinBoundary(pos) && i == x || j == y) moves.add(pos);
                }
            }
        }
        return moves;
    }
    
    private static List<int[]> samuraiMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        boolean[] toProbe = { false, false, false, false, true, true, true, true };
        
        switch (h) {
        case 3:
            probePaths(moves, x, y, board, 8, toProbe);
            break;
        case 2:
            int[] move1 = { x-2, y-2 }, move2 = { x+2, y-2 }, move3 = { x-2, y+2 }, move4 = { x+2, y+2 };
            if (withinBoundary(move1)) moves.add(move1);
            if (withinBoundary(move2)) moves.add(move2);
            if (withinBoundary(move3)) moves.add(move3);
            if (withinBoundary(move4)) moves.add(move4);
            break;
        default:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (withinBoundary(pos) && i != x && j != y) moves.add(pos);
                }
            }
        }
        return moves;
    }
    
    private static List<int[]> captainMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        switch (h) {
        case 3:
        case 2:
            return getMoves(x, y, h-1, board.get(x, y, h-2), board);
        default:
            return marshallMoves(x, y, h, piece, board);
        }
    }
    
    private static List<int[]> musketeerMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        boolean[] toProbe = { false, false, true, false, false, false, false, false };
        
        switch (h) {
        case 3:
            probePaths(moves, x, y, board, 8, toProbe);
            break;
        case 2:
            probePaths(moves, x, y, board, 2, toProbe);
            break;
        default:
            int[] pos = { x, y-1 };
            if (withinBoundary(pos)) moves.add(pos);
        }
        return moves;
    }
    
    private static List<int[]> knightMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] moveA = { x-2, y-1 }, moveB = { x+2, y-1 }, moveC = { x-1, y-2 }, moveD = { x+1, y-2 };
        int[] moveE = { x+2, y+1 }, moveF = { x-2, y+1 }, moveG = { x+1, y+2 }, moveH = { x-1, y+2 };
        int[] move1 = { x-1, y }, move2 = { x+1, y }, move3 = { x-1, y-2 }, move4 = { x+1, y-2 };
        
        switch (h) {
        case 3:
            if (withinBoundary(moveA)) moves.add(moveA);
            if (withinBoundary(moveB)) moves.add(moveB);
            if (withinBoundary(moveC)) moves.add(moveC);
            if (withinBoundary(moveD)) moves.add(moveD);
            if (withinBoundary(moveE)) moves.add(moveE);
            if (withinBoundary(moveF)) moves.add(moveF);
            if (withinBoundary(moveG)) moves.add(moveG);
            if (withinBoundary(moveH)) moves.add(moveH);
            break;
        case 2:
            if (withinBoundary(moveA)) moves.add(moveA);
            if (withinBoundary(moveB)) moves.add(moveB);
            if (withinBoundary(moveC)) moves.add(moveC);
            if (withinBoundary(moveD)) moves.add(moveD);
            break;
        default:
            
            if (withinBoundary(move1)) moves.add(move1);
            if (withinBoundary(move2)) moves.add(move2);
            if (withinBoundary(move3)) moves.add(move3);
            if (withinBoundary(move4)) moves.add(move4);
        }
        return moves;
    }
    
    private static List<int[]> archerMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        
        switch (h) {
        case 3:
            for (int j = 0; j < 6; j++) {
                int[] moveA = { x-3, y+3 - j }, moveB = { x-3 + j, y-3 }, moveC = { x+3, y-3+j }, moveD = { x+3-j, y+3 };
                if (withinBoundary(moveA)) moves.add(moveA);
                if (withinBoundary(moveB)) moves.add(moveB); 
                if (withinBoundary(moveC)) moves.add(moveC);
                if (withinBoundary(moveD)) moves.add(moveD);
            }
            break;
        case 2:
            for (int j = 0; j < 4; j++) {
                int[] moveA = { x-2, y+2 - j }, moveB = { x-2 + j, y-2 }, moveC = { x+2, y-2+j }, moveD = { x+2-j, y+2 };
                if (withinBoundary(moveA)) moves.add(moveA);
                if (withinBoundary(moveB)) moves.add(moveB); 
                if (withinBoundary(moveC)) moves.add(moveC);
                if (withinBoundary(moveD)) moves.add(moveD);
            }
            break;
        default:
            return marshallMoves(x, y, h, piece, board);
        }
        return moves;
    }
    
    private static List<int[]> generalMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        int[] moveA = { x, y-2 }, moveB = { x-1, y-2 }, moveC = { x+1, y-2 };
        
        switch (h) {
        case 3:
            // revise movement in future?
            moves.addAll(marshallMoves(x, y, h, piece, board));
            if (withinBoundary(moveA) && board.get(moveA[0], moveA[1]+1, 0) == null) moves.add(moveA);
            if (withinBoundary(moveB) && board.get(moveB[0], moveB[1]+1, 0) == null) moves.add(moveB);
            if (withinBoundary(moveC) && board.get(moveC[0], moveC[1]+1, 0) == null) moves.add(moveC);
            break;
        case 2:
            return marshallMoves(x, y, h, piece, board);
        default:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (withinBoundary(pos) && (i != x || j != y) && (i != x-1 || j != y+1) && (i != x+1 || j != y+1))
                        moves.add(pos);
                }
            }
        }
        return moves;
    }
    
    private static List<int[]> lieutenantGeneralMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        
        switch (h) {
        case 3:
            return marshallMoves(x, y, h, piece, board);
        case 2:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (withinBoundary(pos) && (i != x || j != y) && (i != x-1 || j != y) && (i != x+1 || j != y))
                        moves.add(pos);
                }
            }
            break;
        default:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (withinBoundary(pos) && (i != x || j != y) && (i != x-1 || j != y) && (i != x+1 || j != y) && (i != x || j != y+1))
                        moves.add(pos);
                }
            }
        }
        return moves;
    }
    
    private static List<int[]> majorGeneralMoves(int x, int y, int h, GungiPiece piece, GungiBoard board) {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        
        switch (h) {
        case 3:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (withinBoundary(pos) && (i != x || j != y) && (i != x-1 || j != y+1) && (i != x+1 || j != y+1))
                        moves.add(pos);
                }
            }
            break;
        case 2:
            for (int i = x-1; i <= x+1; i++) {
                for (int j = y-1; j <= y+1; j++) {
                    int[] pos = { i, j };
                    if (withinBoundary(pos) && (i != x || j != y) && (i != x-1 || j != y) && (i != x+1 || j != y) && (i != x || j != y+1))
                        moves.add(pos);
                }
            }
            break;
        default:
            int[] moveA = { x+1, y-1 }, moveB = { x-1, y-1 };
            if (withinBoundary(moveA)) moves.add(moveA);
            if (withinBoundary(moveB)) moves.add(moveB);
        }
        return moves;
    }
    
    /**
     * @param depth: how far to probe each path
     * @param toProbe:
     * 0: right path
     * 1: left path
     * 2: up path
     * 3: down path
     * 4: up-right path
     * 5: up-left path
     * 6: down-right path
     * 7: down-left path
     */
    private static void probePaths(List<int[]> moves, int x, int y, GungiBoard board, int depth, boolean[] toProbe) {
        boolean[] found = new boolean[8];   
        int[][] probes = new int[8][2];
        
        for (int i = 1; i <= depth; i++) {
            int[] rightProbe = { x + i, y }, leftProbe = { x - i, y };
            int[] upProbe = { x, y - i }, downProbe = { x, y + i };
            int[] upRightProbe = { x + i, y - i }, upLeftProbe = { x - i, y - i };
            int[] downRightProbe = { x + i, y + i }, downLeftProbe = { x - i, y + i };
            
            probes[0] = rightProbe;
            probes[1] = leftProbe;
            probes[2] = upProbe;
            probes[3] = downProbe;
            probes[4] = upRightProbe;
            probes[5] = upLeftProbe;
            probes[6] = downRightProbe;
            probes[7] = downLeftProbe;
            
            for (int j = 0; j < 8; j++) {
                if (toProbe[j] && withinBoundary(probes[j]) && !found[j]) {
                    moves.add(probes[j]);
                    found[j] = board.get(probes[j][0], probes[j][1], 0) != null;
                }
            }
        }
    }
    
}
