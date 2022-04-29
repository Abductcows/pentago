package io.github.abductcows.pentago;

import java.util.Collections;
import java.util.List;

public class Board {

    public final int sideSize = 6;
    public final int winTarget = 5;
    private final List<Move> board = Move.getEmptyBoard(sideSize);

    public Move get(int index) {
        return board.get(index);
    }

    public void set(int index, Move move) {
        board.set(index, move);
    }

    public Winner checkWinner() {
        var emptyCount = board.stream().filter(Move.Empty::equals).count();
        if (emptyCount > board.size() - winTarget) return Winner.Undecided;

        for (var row = 0; row < sideSize; row++) {
            var maybeWinner = tryGetTargetInARow(row * sideSize, 1);
            if (maybeWinner != Winner.Undecided) return maybeWinner;
            maybeWinner = tryGetTargetInARow(row * sideSize + 1, 1);
            if (maybeWinner != Winner.Undecided) return maybeWinner;
        }

        for (var col = 0; col < sideSize; col++) {
            var maybeWinner = tryGetTargetInARow(col, sideSize);
            if (maybeWinner != Winner.Undecided) return maybeWinner;
            maybeWinner = tryGetTargetInARow(col + sideSize, sideSize);
            if (maybeWinner != Winner.Undecided) return maybeWinner;
        }

        {
            var mainDiagonals = tryGetTargetInARow(sideSize, sideSize + 1);
            if (mainDiagonals != Winner.Undecided) return mainDiagonals;
            mainDiagonals = tryGetTargetInARow(0, sideSize + 1);
            if (mainDiagonals != Winner.Undecided) return mainDiagonals;
            mainDiagonals = tryGetTargetInARow(sideSize + 1, sideSize + 1);
            if (mainDiagonals != Winner.Undecided) return mainDiagonals;
            mainDiagonals = tryGetTargetInARow(1, sideSize + 1);
            if (mainDiagonals != Winner.Undecided) return mainDiagonals;
        }

        {
            var secondDiagonals = tryGetTargetInARow(sideSize - 2, sideSize - 1);
            if (secondDiagonals != Winner.Undecided) return secondDiagonals;
            secondDiagonals = tryGetTargetInARow(sideSize - 1, sideSize - 1);
            if (secondDiagonals != Winner.Undecided) return secondDiagonals;
            secondDiagonals = tryGetTargetInARow(2 * (sideSize - 1), sideSize - 1);
            if (secondDiagonals != Winner.Undecided) return secondDiagonals;
            secondDiagonals = tryGetTargetInARow(2 * sideSize - 1, sideSize - 1);
            if (secondDiagonals != Winner.Undecided) return secondDiagonals;
        }

        if (emptyCount == 0) {
            return Winner.Draw;
        }
        return Winner.Undecided;
    }

    Winner tryGetTargetInARow(int start, int step) {
        var e = board.get(start);
        for (int i = 1; i < winTarget; i++) {
            var next = board.get(start + i * step);
            if (next != e) return Winner.Undecided;
        }
        return e.asWinner();
    }

    public List<Move> getMoves() {
        return Collections.unmodifiableList(board);
    }
}
