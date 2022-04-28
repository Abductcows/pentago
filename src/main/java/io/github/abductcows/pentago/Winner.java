package io.github.abductcows.pentago;

public enum Winner {
    B, W, Draw, Undecided;

    public String getWinMessage() {
        return switch (this) {
            case B -> "Black Won";
            case W -> "White won";
            case Draw -> "It's a Draw";
            case Undecided -> throw new IllegalStateException("Win message requested for Undecided state");
        };
    }
}