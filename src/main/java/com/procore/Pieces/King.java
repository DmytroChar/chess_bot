package com.procore.Pieces;

import com.procore.Board.Board;
import com.procore.Model.Position;
import com.procore.Model.Team;

import java.util.ArrayList;

public class King extends DefaultPiece{
    public King(Board board, Team team) {
        super(board, team);
        sprite = 0;
        value = -1;
    }

    public ArrayList<Position> getPossibleMoves(boolean protection) {
        ArrayList<Position> positions = new ArrayList<>();
        int[][] directions = {{1, 0}, {1, 1}, {0, 1}, {-1, 1},
                {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        };

        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];

            int newX = position.x + dx;
            int newY = position.y + dy;

            if (newX >= 0 && newX < board.getPieces()[0].length && newY >= 0 && newY < board.getPieces()[0].length) {
                DefaultPiece piece = board.getPiece(newX, newY);
                if (piece == null || (piece.getTeam() != getTeam() || protection)) {
                    positions.add(new Position(newX, newY));
                }
            }
        }
        return positions;
    }
    protected DefaultPiece create() {
        return new King(board, team);
    }
}
