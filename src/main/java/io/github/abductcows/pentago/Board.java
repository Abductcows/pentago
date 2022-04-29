package io.github.abductcows.pentago;

import java.util.Collections;
import java.util.List;

public class Board {

    public final int sideSize = 6;
    public final int quadrantSize = 3;
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

    public void rotate(int quadrantNumber, boolean isLeftRotation) {

        var quadrant = getQuadrant(quadrantNumber);

        if (isLeftRotation) {
            rotateLeft90(quadrant);
        } else {
            rotateRight90(quadrant);
        }

        applyQuadrant(quadrantNumber, quadrant);
    }

    private Move[][] getQuadrant(int quadrant) {

        var result = new Move[quadrantSize][quadrantSize];

        var n = board.size();
        int inserted = 0;

        for (int i = 0; i < n; i++) {
            if (quadrantNumber(i) != quadrant) continue;
            result[inserted / quadrantSize][inserted % quadrantSize] = board.get(i);
            ++inserted;
        }

        return result;
    }

    private void applyQuadrant(int quadrantNumber, Move[][] quadrant) {

        for (int i = 0; i < quadrantSize; i++) {
            for (int j = 0; j < quadrantSize; j++) {
                board.set(transformIndex(i, j, quadrantNumber), quadrant[i][j]);
            }
        }
    }

    private int quadrantNumber(int elementIndex) {
        var half = sideSize / 2;
        if (elementIndex / sideSize < half) {
            if (elementIndex % sideSize < half) {
                return 0;
            }
            return 1;
        }
        if (elementIndex % sideSize < half) {
            return 2;
        }
        return 3;
    }


    private int transformIndex(int i, int j, int quadrantTarget) {

        if (quadrantTarget == 0) {
            return i * sideSize + j;
        }
        if (quadrantTarget == 1) {
            return i * sideSize + j + sideSize / 2;
        }
        if (quadrantTarget == 2) {
            return quadrantSize * sideSize + i * sideSize + j;
        }
        return quadrantSize * sideSize + i * sideSize + j + sideSize / 2;
    }

    private <T> void rotateLeft90(T[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                T temp = matrix[i][j];
                matrix[i][j] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = matrix[n - 1 - i][n - j - 1];
                matrix[n - 1 - i][n - j - 1] = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = temp;
            }
        }
    }

    private <T> void rotateRight90(T[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                T temp = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - j - 1];
                matrix[n - 1 - i][n - j - 1] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = matrix[i][j];
                matrix[i][j] = temp;
            }
        }
    }

}
