package io.github.abductcows.pentago.gui;

import io.github.abductcows.pentago.Move;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Map;


public class PentagoCell extends JButton {

    public final int index;

    private static final Map<Move, Color> moveColors = Map.of(
            Move.B, Color.BLACK,
            Move.W, Color.WHITE,
            Move.Empty, new Color(0x7D7E8C)
    );

    public PentagoCell(int index) {
        super();
        this.index = index;
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width, size.height);
        setPreferredSize(size);
        setContentAreaFilled(false);

        setBackground(Move.Empty);
    }

    public void setBackground(Move move) {
        setBackground(moveColors.get(move));
    }

    // Overrides for round button

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillOval(0, 0, getSize().width - 1, getSize().height - 1);

        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width - 1, getSize().height - 1);
    }

    Shape shape;

    public boolean contains(int x, int y) {
        if (shape == null ||
                !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }
}
