package com.procore.Pieces;

import com.procore.Board.Board;
import com.procore.Model.Position;
import com.procore.Model.Team;

import java.util.ArrayList;

public class Pawn extends DefaultPiece {

    public Pawn(Board board, Team team) {
        super(board, team);
        sprite = 5;
        value = 1;
    }
    public ArrayList<Position> getPossibleMoves(boolean protection) {
        ArrayList<Position> positions = new ArrayList<>();
        int direction = (team == Team.WHITE) ? 1 : -1;

        // 1. Check for moving forward one cell
        if (isValidMove(position.x, position.y + direction) && board.getPiece(position.x, position.y + direction) == null) {
            positions.add(new Position(position.x, position.y + direction));
        }

        // 2. Check for the possibility of a double forward move if this is the first move
        if ((team == Team.WHITE && position.y == 1) || (team == Team.BLACK && position.y == 6)) {
            if (isValidMove(position.x, position.y + 2 * direction) && board.getPiece(position.x, position.y + 2 * direction) == null) {
                positions.add(new Position(position.x, position.y + 2 * direction));
            }
        }

        // 3. Attack diagonally to the left (if there is an enemy piece)
        if (position.x - 1 >= 0) {
            DefaultPiece piece = board.getPiece(position.x - 1, position.y + direction);
            if (piece != null && piece.getTeam() != getTeam()) {
                positions.add(new Position(position.x - 1, position.y + direction));
            }
        }

        // 4. Attack diagonally to the right (if there is an enemy piece)
        if (position.x + 1 < board.getPieces()[0].length) {
            DefaultPiece piece = board.getPiece(position.x + 1, position.y + direction);
            if (piece != null && piece.getTeam() != getTeam()) {
                positions.add(new Position(position.x + 1, position.y + direction));
            }
        }

        return positions;
    }

    private boolean isValidMove(int x, int y) {
        return x >= 0 && x < board.getPieces()[0].length && y >= 0 && y < board.getPieces()[0].length;
    }

    protected DefaultPiece create() {
        return new Pawn(board,  team);
    }

}