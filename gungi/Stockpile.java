package gungi;

import java.util.Map;
import java.util.TreeMap;

public class Stockpile {

    private Map<GungiPiece, Integer> reserve, captured;
    private int reserveSize, captureSize;
    
    public Stockpile() {
        reserve = new TreeMap<GungiPiece, Integer>();
        captured = new TreeMap<GungiPiece, Integer>();
        reserveSize = 0;
        captureSize = 0;
    }
    
    public void amass(GungiPiece piece, int num) {
        reserve.put(piece, num);
        reserveSize += num;
    }
    
    public void deploy(GungiPiece piece) {
        int newSize = reserve.get(piece)-1;
        if (newSize == 0) reserve.remove(piece);
        else reserve.put(piece, reserve.get(piece)-1);
        reserveSize--;
    }
    
    public void capture(GungiPiece piece) {
        int newSize = 1;
        if (captured.containsKey(piece)) newSize = captured.get(piece)+1;
        captured.put(piece, newSize);
        captureSize += newSize;
    }
    
    public Map<GungiPiece, Integer> getReserve() {
        return reserve;
    }
    
    public Map<GungiPiece, Integer> getCaptured() {
        return captured;
    }
    
    public void clear() {
        reserve.clear();
        captured.clear();
        reserveSize = 0;
        captureSize = 0;
    }
    
    public int getReserveSize() {
        return reserveSize;
    }
    
    public int getCaptureSize() {
        return captureSize;
    }
}
