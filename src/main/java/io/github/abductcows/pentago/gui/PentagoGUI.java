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
    private JLabel statusBar;
    private Move nextMove;

    void init() {
        board = new Board();
        size = board.sideSize;
        nextMove = Move.W;

        statusBar = new JLabel(" ", SwingConstants.CENTER);
        frame = new JFrame("Pentago");

        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        var windowSize = Math.min(screenSize.width, screenSize.height) * 0.9;
        frame.setSize((int) windowSize, (int) (windowSize * 1.05));

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

        var quadrantSpace = new JPanel(new GridLayout(2, 2, 10, 10));
        quadrantSpace.setBorder(BorderFactory.createEmptyBorder(20, 18, 20, 18));
        quadrantSpace.setBackground(Color.BLACK);

        var content = new JPanel(new GridBagLayout());
        distributeCells(cells, quads);
        quads.forEach(quadrantSpace::add);

        var constraints = new GridBagConstraints();
        constraints.weightx = constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        content.add(quadrantSpace, constraints);

        addStatusBar(content);

        frame.add(content);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void addStatusBar(JPanel content) {
        var constraints = new GridBagConstraints();
        statusBar.setFont(new Font("Sans Serif", Font.PLAIN, 45));
        content.add(statusBar, constraints);
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
            q.setLayout(new GridLayout(board.quadrantSize, board.quadrantSize, 8, 8));
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
                } else {

                    var quadrantNumber = getQuadrantNumberTemp(c.index);
                    board.rotate(quadrantNumber, true);
                    processRotation();
                }
            }
        }));
    }

    // todo remove this
    private int getQuadrantNumberTemp(int elementIndex) {
        var half = size / 2;
        if (elementIndex / size < half) {
            if (elementIndex % size < half) {
                return 0;
            }
            return 1;
        }
        if (elementIndex % size < half) {
            return 2;
        }
        return 3;
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

    private void processRotation() {
        redrawBoard();

        var winner = board.checkWinner();
        if (winner != Winner.Undecided) {

            showWinnerMessage(winner);
            cleanUp();
            run();
        }
    }

    private void redrawBoard() {

        for (int i = 0; i < size * size; i++) {
            cells.get(i).setBackground(board.get(i));
        }
    }

    private void showWinnerMessage(Winner winner) {
        JOptionPane.showConfirmDialog(
                null,
                getWinMessage(winner),
                "Game Over",
                JOptionPane.DEFAULT_OPTION);
    }

    private String getWinMessage(Winner winner) {
        return switch (winner) {
            case B -> "Black Won";
            case W -> "White won";
            case Draw -> "It's a Draw";
            case Undecided -> throw new IllegalStateException("Win message requested for Undecided state");
        };
    }
}
