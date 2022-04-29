package io.github.abductcows.pentago.gui;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class PentagoGUITest {

    private static PentagoGUI obj;

    @BeforeEach
    void setUp() {
        obj = new PentagoGUI();
        obj.init();
    }

    @Test
    void distributeCells() {

        var cells = IntStream.range(0, 36)
                .mapToObj(PentagoCell::new)
                .toList();
        var quads = Stream.generate(JPanel::new).limit(4).toList();

        obj.distributeCells(cells, quads);
        Assertions.assertThat(quads
                .stream()
                .mapToInt(q -> q.getComponents().length)
                .distinct()
        ).hasSize(1);
    }
}