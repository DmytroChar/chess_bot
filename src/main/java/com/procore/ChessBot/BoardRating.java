package com.procore.ChessBot;

import com.procore.Board.Board;
import com.procore.Model.Position;
import com.procore.Model.Team;
import com.procore.Pieces.DefaultPiece;

import java.util.ArrayList;

public class BoardRating {

    public static Board getBestMove(Board board, Team team, int prediction) {
        ArrayList<Board> boards = getPossibleBoards(board, team);
        Board bestBoard = null;
        float rating = -Float.MAX_VALUE;
        for (int i = 0; i < boards.size(); i++) {
            Board result = boards.get(i);
            if (prediction > 0) {
                result = getBestMove(result, result.getTeamIsMoving(), prediction - 1);
            }
            float r = BoardRating.rateBoard(result, team);
            if (r > rating) {
                rating = r;
                bestBoard = boards.get(i);
            }
        }
        return bestBoard;
    }

    public static float rateBoard(Board board, Team team) {
        float score = 0.0f;
        for (int x = 0; x < board.pieces.length; x++) {
            for (int y = 0; y < board.pieces[0].length; y++) {
                DefaultPiece DefaultPiece = board.getPiece(x, y);
                if (DefaultPiece == null) continue;

                int value = DefaultPiece.getValue();
                if (value == -1) value = 8192;
                if (DefaultPiece.getTeam() == team) {
                    score += value * 8.0f;
                } else {
                    score -= value * 8.0f;
                }
                ArrayList<Position> moves = DefaultPiece.getPossibleMoves(true);
                for (int i = 0; i < moves.size(); i++) {
                    DefaultPiece target = board.getPiece(moves.get(i).x, moves.get(i).y);
                    if (target == null) continue;

                    if (target.getTeam() == team) {
                        if (DefaultPiece.getTeam() == team) {
                            score += target.getValue() / 8.0f;
                        } else {
                            score -= target.getValue() * 1.5f;
                        }
                    } else {
                        if (DefaultPiece.getTeam() == team) {
                            score += target.getValue();
                        } else {
                            score -= target.getValue() / 8.0f;
                        }
                    }
                }
            }
        }
        return score;
    }

    public static ArrayList<Board> getPossibleBoards(Board board, Team team) {
        ArrayList<Board> boards = new ArrayList<>();
        for (int x = 0; x < board.pieces.length; x++) {
            for (int y = 0; y < board.pieces[0].length; y++) {
                DefaultPiece DefaultPiece = board.getPiece(x, y);
                if (DefaultPiece == null) continue;
                if (DefaultPiece.getTeam() != team) continue;

                ArrayList<Position> moves = DefaultPiece.getPossibleMoves(false);
                for (int i = 0; i < moves.size(); i++) {
                    Board b = new Board(board);
                    b.getPiece(x, y).moveTo(moves.get(i).x, moves.get(i).y);
                    boards.add(b);
                }
            }
        }
        return boards;
    }

}

