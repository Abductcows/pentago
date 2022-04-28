package io.github.abductcows.pentago.gui;

import io.github.abductcows.pentago.Board;
import io.github.abductcows.pentago.Move;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PentagoGUI {

    private Board board;
    private JFrame frame;
    private List<PentagoCell> cells;
    private Move nextMove;

    public void run() {
        init();

        var content = new JPanel(new GridLayout(6, 6));
        cells.forEach(content::add);
        frame.add(content);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        board = new Board();
        nextMove = Move.W;

        frame = new JFrame("Pentago");
        frame.setSize(1200, 1200);

        initCells();
    }

    private void initCells() {

        cells = IntStream.range(0, board.sideSize * board.sideSize)
                .mapToObj(PentagoCell::new)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    private void cleanUp() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }
}
