package com.procore.Pieces;

import com.procore.Board.Board;
import com.procore.Model.Position;
import com.procore.Model.Team;

import java.util.ArrayList;

public class Queen extends DefaultPiece{
    public Queen(Board board, Team team) {
        super(board, team);
        sprite = 1;
        value = 9;
    }
    public ArrayList<Position> getPossibleMoves(boolean protection) {
        ArrayList<Position> positions = new ArrayList<>();

        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1},
                {-1, -1},
                {1, -1},
                {-1, 1},
                {1, 1}
        };

        for (int[] direction : directions) {
            exploreDirection(positions, direction[0], direction[1], protection);
        }

        return positions;
    }

    private void exploreDirection(ArrayList<Position> positions, int dx, int dy, boolean protection) {
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
        return new Queen(board,  team);
    }
}
