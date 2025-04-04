package com.procore.Pieces;

import com.procore.Board.Board;
import com.procore.Model.Position;
import com.procore.Model.Team;

import java.util.ArrayList;

public class Knight extends DefaultPiece{

    public Knight(Board board, Team team) {
        super(board, team);
        sprite = 3;
        value = 3;
    }

    public ArrayList<Position> getPossibleMoves(boolean protection) {
        ArrayList<Position> positions = new ArrayList<>();

        int[] dxs = {2, 2, 1, -1, -2, -2, 1, -1};
        int[] dys = {1, -1, 2, 2, 1, -1, -2, -2};


        for (int i = 0; i < dxs.length; i++) {
            int dx = dxs[i];
            int dy = dys[i];


            if (position.x + dx >= 0 && position.x + dx < board.getPieces()[0].length &&
                    position.y + dy >= 0 && position.y + dy < board.getPieces()[0].length) {

                DefaultPiece piece = board.getPiece(position.x + dx, position.y + dy);

                if (piece == null || piece.getTeam() != getTeam() || protection) {
                    positions.add(new Position(position.x + dx, position.y + dy));
                }
            }
        }

        return positions;
    }

    protected DefaultPiece create() {
        return new Knight(board, team);
    }
}
