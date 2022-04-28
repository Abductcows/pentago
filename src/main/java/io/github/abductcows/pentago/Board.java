package io.github.abductcows.pentago;

import java.util.List;

public class Board {

    public final int sideSize = 6;
    private final List<Move> board = Move.getEmptyBoard(sideSize);

    public Move get(int index) {
        return board.get(index);
    }

    public void set(int index, Move move) {
        board.set(index, move);
    }


}
