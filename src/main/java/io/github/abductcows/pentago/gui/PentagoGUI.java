package io.github.abductcows.pentago.gui;

import io.github.abductcows.pentago.Board;
import io.github.abductcows.pentago.Move;
import io.github.abductcows.pentago.Winner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class PentagoGUI {

    private Board board;
    private int size;
    private JFrame frame;
    private List<PentagoCell> cells;
    private Move nextMove;

    private List<Move> previousBoard;

    void init() {
        board = new Board();
        size = board.sideSize;
        previousBoard = board.getMoves();
        nextMove = Move.W;

        frame = new JFrame("Pentago");
        frame.setSize(1200, 1200);

        initCells();
    }

    private void cleanUp() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }

    public void run() {
        init();

        var r = new Random(987);
        var quads = genQuadrants(r);

        var content = new JPanel(new GridLayout(2, 2, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        content.setBackground(Color.BLACK);


        distributeCells(cells, quads);
        quads.forEach(content::add);
        frame.add(content);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    void distributeCells(List<PentagoCell> cells, List<JPanel> quads) {

        for (int i = 0; i < cells.size(); i++) {
            if (i / size < size / 2) {
                if (i % size < size / 2) {
                    quads.get(0).add(cells.get(i));
                } else {
                    quads.get(1).add(cells.get(i));
                }
            } else {
                if (i % size < size / 2) {
                    quads.get(2).add(cells.get(i));
                } else {
                    quads.get(3).add(cells.get(i));
                }
            }
        }
    }

    private List<JPanel> genQuadrants(Random r) {
        var quadrants = Stream.generate(JPanel::new).limit(4).toList();
        var border = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        quadrants.forEach(q -> {
            q.setBackground(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            q.setLayout(new GridLayout(3, 3, 8, 8));
            q.setBorder(border);
        });
        return quadrants;
    }

    private void initCells() {

        cells = IntStream.range(0, size * size)
                .mapToObj(PentagoCell::new)
                .collect(Collectors.toCollection(ArrayList::new));

        cells.forEach(c -> c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (board.get(c.index) == Move.Empty) {
                        board.set(c.index, nextMove);
                        processNewMove(c.index);
                    }
                    System.out.printf("Click on cell (%d, %d)\n", c.index / size, c.index % size);
                }
            }
        }));
    }

    private void processNewMove(int changeIndex) {

        cells.get(changeIndex).setBackground(nextMove);

        var winner = board.checkWinner();
        if (winner != Winner.Undecided) {

            showWinnerMessage(winner);
            cleanUp();
            run();
        } else {
            nextMove = nextMove.getNextMove();
        }
    }

    private void showWinnerMessage(Winner winner) {
        JOptionPane.showConfirmDialog(
                null,
                winner.getWinMessage(),
                "Game Over",
                JOptionPane.DEFAULT_OPTION);
    }
}
