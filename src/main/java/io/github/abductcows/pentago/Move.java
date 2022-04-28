package io.github.abductcows.pentago;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Move {

    B, W, Empty;

    public static List<Move> getEmptyBoard(int sideSize) {
        return Stream.generate(() -> Empty)
                .limit((long) sideSize * sideSize)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Move getNextMove() {
        return switch (this) {
            case B -> W;
            case W -> B;
            case Empty -> throw new IllegalStateException("next move called on Empty move");
        };
    }

    public Winner asWinner() {
        return switch (this) {

            case B -> Winner.B;
            case W -> Winner.W;
            case Empty -> Winner.Undecided;
        };
    }
}
