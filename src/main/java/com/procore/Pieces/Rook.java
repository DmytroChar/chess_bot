package com.procore.Pieces;

import com.procore.Board.Board;
import com.procore.Model.Position;
import com.procore.Model.Team;

import java.util.ArrayList;

public class Rook extends DefaultPiece{
    public Rook(Board board, Team team) {
        super(board, team);
        sprite = 4;
        value = 5;
    }

    public ArrayList<Position> getPossibleMoves(boolean protection) {
        ArrayList<Position> positions = new ArrayList<>();

        addPossibleMoves(positions, -1, 0, protection);
        addPossibleMoves(positions, 1, 0, protection);
        addPossibleMoves(positions, 0, -1, protection);
        addPossibleMoves(positions, 0, 1, protection);

        return positions;
    }


    private void addPossibleMoves(ArrayList<Position> positions, int dx, int dy, boolean protection) {
        int x = position.x;
        int y = position.y;

        while (true) {
            x += dx;
            y += dy;

            if (x < 0 || x >= board.getPieces()[0].length || y < 0 || y >= board.getPieces()[0].length) {
                break;
            }

            DefaultPiece piece = board.getPiece(x, y);

            if (piece == null) {
                positions.add(new Position(x, y));
            } else {

                if (piece.getTeam() != getTeam() || protection) {
                    positions.add(new Position(x, y));
                }
                break;
            }
        }
    }

    protected DefaultPiece create() {
        return new Rook(board, team);
    }
}
