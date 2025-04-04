package com.procore.Pieces;

import com.procore.Board.Board;
import com.procore.ChessApp;
import com.procore.Model.Position;
import com.procore.Model.Team;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;

@Getter
@Setter
public class DefaultPiece {
    public Board board;
    public Position position;
    public final Team team;
    public int sprite;
    public boolean flipSprite;
    public int value;
    private int moves;

    private float xMovAnim;
    private float yMovAnim;

    public DefaultPiece(Board board, Team team) {
        this.board = board;
        position = new Position(-1, -1);
        this.team = team;
        moves = 0;
        flipSprite = team == Team.BLACK;
    }

    public void render(Graphics2D g) {
        int spriteX = sprite * board.piecesSpriteSheet.getWidth() / board.piecesSpritesX;
        int spriteY = (team == Team.BLACK ? 1 : 0) * board.piecesSpriteSheet.getHeight() / board.piecesSpritesY;

        int x = position.x * Board.BOARD_SIZE / 8 + ChessApp.instance.getWidth() / 2 - Board.BOARD_SIZE / 2;
        int y = position.y * Board.BOARD_SIZE / 8 + ChessApp.instance.getHeight() / 2 - Board.BOARD_SIZE / 2;
        int width = Board.BOARD_SIZE / 8;
        int height = Board.BOARD_SIZE / 8;

        if (getTeam() == board.getTeamIsMoving()) {
            g.setColor(new Color(0x1040ff40, true));
            g.fillRect(x, y, width + 1, height + 1);
        }

        x += xMovAnim * width;
        y += yMovAnim * height;

        if (flipSprite) {
            x += width;
            width = -width;
        }

        g.drawImage(board.piecesSpriteSheet, x, y, x + width, y + height, spriteX, spriteY, spriteX + board.piecesSpriteSheet.getWidth() / board.piecesSpritesX, spriteY + board.piecesSpriteSheet.getHeight() / board.piecesSpritesY, null);

    }

    public void update(float delta) {
        xMovAnim *= Math.pow(0.0005f, delta);
        yMovAnim *= Math.pow(0.0005f, delta);
    }

    public void moveTo(int x, int y) {
        xMovAnim = position.x - x;
        yMovAnim = position.y - y;
        board.moveTo(this, x, y);
        moves++;
    }

    public ArrayList<Position> getPossibleMoves(boolean protection) {
        ArrayList<Position> positions = new ArrayList<>();
        return positions;
    }

    protected DefaultPiece create() {
        throw new RuntimeException("Can't copy default piece");
    }

    public DefaultPiece copy() {
        DefaultPiece piece = create();
        piece.position.x = position.x;
        piece.position.y = position.y;
        piece.xMovAnim = xMovAnim;
        piece.yMovAnim = yMovAnim;
        piece.moves = moves;
        return piece;
    }
}
