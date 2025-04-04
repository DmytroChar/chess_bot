package com.procore.Board;

import java.awt.*;

public class InitializeGameStart {

    private Board board;

    public InitializeGameStart() {
        board = new Board();
    }
    public void update(MouseListener input, float delta) {
        board.update(input, delta);
    }

    public void render(Graphics2D g) {
        board.render(g);
    }
}
