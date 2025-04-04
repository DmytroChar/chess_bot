package com.procore.Board;

import com.procore.ChessApp;
import com.procore.ChessBot.BoardRating;
import com.procore.Model.Position;
import com.procore.Model.Team;
import com.procore.Pieces.Bishop;
import com.procore.Pieces.DefaultPiece;
import com.procore.Pieces.King;
import com.procore.Pieces.Knight;
import com.procore.Pieces.Pawn;
import com.procore.Pieces.Queen;
import com.procore.Pieces.Rook;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class Board {
    public static final int BOARD_SIZE = 512;

    public DefaultPiece[][] pieces;
    public BufferedImage piecesSpriteSheet;
    public int piecesSpritesX = 6;
    public int piecesSpritesY = 2;

    private int hoverX;
    private int hoverY;
    private int selectedX;
    private int selectedY;

    private ArrayList<Position> selectedMoves;
    private ArrayList<ArrayList<Position>> possibleMoves;

    private Team teamIsMoving;

    private float timer = 0.0f;

    public Board() {
        pieces = new DefaultPiece[8][8];
        initializePieces();
        try {
            piecesSpriteSheet = ImageIO.read(Objects.requireNonNull(Board.class.getResourceAsStream("/pieces.png")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        selectedX = -1;
        selectedY = -1;
        teamIsMoving = Team.WHITE;
        possibleMoves = new ArrayList<>();
        int NUM_TEAMS = 2;
        for (int i = 0; i < NUM_TEAMS; i++) {
            possibleMoves.add(null);
        }
    }
    public Board(Board board) {
        apply(board);
    }

    public void setPiece(DefaultPiece piece, int x, int y) {
        pieces[x][y] = piece;
        piece.setPosition(new Position(x, y));
        piece.setBoard(this);
    }

    public void initializePieces() {
        setPiece(new Rook(this, Team.BLACK), 7, 7);
        setPiece(new Rook(this, Team.BLACK), 0, 7);
        setPiece(new Rook(this, Team.WHITE), 7, 0);
        setPiece(new Rook(this, Team.WHITE), 0, 0);

        setPiece(new Knight(this, Team.BLACK), 1, 7);
        setPiece(new Knight(this, Team.BLACK), 6, 7);
        setPiece(new Knight(this, Team.WHITE), 1, 0);
        setPiece(new Knight(this, Team.WHITE), 6, 0);

        setPiece(new Bishop(this, Team.BLACK), 2, 7);
        setPiece(new Bishop(this, Team.BLACK), 5, 7);
        setPiece(new Bishop(this, Team.WHITE), 2, 0);
        setPiece(new Bishop(this, Team.WHITE), 5, 0);

        setPiece(new Queen(this, Team.BLACK), 4, 7);
        setPiece(new King(this, Team.BLACK), 3, 7);
        setPiece(new Queen(this, Team.WHITE), 4, 0);
        setPiece(new King(this, Team.WHITE), 3, 0);

        for (int x= 0; x < pieces[0].length; x++) {
            setPiece(new Pawn(this, Team.BLACK), x, 6);
            setPiece(new Pawn(this, Team.WHITE), x, 1);
        }
    }

    public DefaultPiece getPiece(int x, int y) {
        return pieces[x][y];
    }

    public void apply(Board board) {
        pieces = new DefaultPiece[board.pieces.length][board.pieces[0].length];
        for (int x = 0; x < pieces.length; x++) {
            for (int y = 0; y < pieces[0].length; y++) {
                if (board.getPiece(x, y) != null) {
                    setPiece(board.getPiece(x, y).copy(), x, y);
                }
            }
        }
        piecesSpriteSheet = board.piecesSpriteSheet;
        selectedX = board.selectedX;
        selectedY = board.selectedY;
        teamIsMoving = board.teamIsMoving;
    }

    public void moveTo(DefaultPiece piece, int x, int y) {
        pieces[piece.getPosition().x][piece.getPosition().y] = null;
        setPiece(piece, x, y);
        teamIsMoving = teamIsMoving.equals(Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    public void update(MouseListener mouseListener, float delta) {
        Coords coords = getCoords();
        hoverX = (int) Math.floor((float) (mouseListener.getCurrentX() - coords.x) / (float) BOARD_SIZE * pieces.length);
        hoverY = (int) Math.floor((float) (mouseListener.getCurrentY() - coords.y) / (float) BOARD_SIZE * pieces[0].length);

        if (hoverX >= 0 && hoverX < pieces.length && hoverY >= 0 && hoverY < pieces[0].length) {
            if (mouseListener.isButtonPressed(MouseListener.BUTTON_LEFT)) {
                if (selectedX == -1 && selectedY == -1) {
                    if (getPiece(hoverX, hoverY) != null && getPiece(hoverX, hoverY).getTeam() == teamIsMoving) {
                        selectedX = hoverX;
                        selectedY = hoverY;
                        selectedMoves = getPiece(selectedX, selectedY).getPossibleMoves(false);
                    }
                } else {
                    for (Position selectedMove : selectedMoves) {
                        if (hoverX == selectedMove.x && hoverY == selectedMove.y) {
                            getPiece(selectedX, selectedY).moveTo(hoverX, hoverY);
                            break;
                        }
                    }
                    selectedX = -1;
                    selectedY = -1;
                    selectedMoves = null;
                }
            }
        }
        for (int x = 0; x < pieces.length; x++) {
            for (int y = 0; y < pieces[0].length; y++) {
                if (getPiece(x, y) != null) {
                    getPiece(x, y).update(delta);
                }
            }
        }
        if (teamIsMoving == Team.BLACK) {
            timer += delta;
        }
        if (timer > 0.5f) {
            timer = 0.0f;
            long start = System.nanoTime();
            apply(BoardRating.getBestMove(this, teamIsMoving, 2));
            long end = System.nanoTime();
            long elapsedTime = end - start;
            System.out.println(TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
        }
    }
    record Coords(int x, int y) {}

    private Coords getCoords(){
        return new Coords(ChessApp.instance.getWidth() / 2 - BOARD_SIZE / 2, ChessApp.instance.getHeight() / 2 - BOARD_SIZE / 2);
    }

    public void render(Graphics2D g) {
        int boardX = ChessApp.instance.getWidth() / 2 - BOARD_SIZE / 2;
        int boardY = ChessApp.instance.getHeight() / 2 - BOARD_SIZE / 2;
        int cellWidth = BOARD_SIZE / pieces.length;
        int cellHeight = BOARD_SIZE / pieces[0].length;

        g.setColor(Color.BLACK);
        g.fillRect(boardX - 3, boardY - 3, BOARD_SIZE + 6, BOARD_SIZE + 6);

        for (int x = 0; x < pieces.length; x++) {
            for (int y = 0; y < pieces[0].length; y++) {
                g.setColor(((x + y) % 2 == 0) ? new Color(0x755449) : new Color(0xB3A396));
                g.fillRect(boardX + x * cellWidth, boardY + y * cellHeight, cellWidth, cellHeight);
            }
        }

        if (selectedMoves != null) {
            g.setColor(new Color(0x503050f0, true));
            for (Position move : selectedMoves) {
                g.fillRect(boardX + move.x * cellWidth, boardY + move.y * cellHeight, cellWidth, cellHeight);
            }
        }

        if (selectedX >= 0 && selectedX < pieces.length && selectedY >= 0 && selectedY < pieces[0].length) {
            g.setColor(Color.WHITE);
            g.fillRect(boardX + selectedX * cellWidth, boardY + selectedY * cellHeight, cellWidth, cellHeight);
        }

        for (DefaultPiece[] piece : pieces) {
            for (int y = 0; y < pieces[0].length; y++) {
                if (piece[y] != null) {
                    piece[y].render(g);
                }
            }
        }

        if (hoverX >= 0 && hoverX < pieces.length && hoverY >= 0 && hoverY < pieces[0].length) {
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(4.0f));
            g.drawRect(boardX + hoverX * cellWidth, boardY + hoverY * cellHeight, cellWidth - 1, cellHeight - 1);
            g.setStroke(new BasicStroke(1.0f));
        }
    }
    public boolean isKingInCheck(Team team) {
        Position kingPosition = findKing(team);
        if (kingPosition == null) {
            return false;
        }
        for (int x = 0; x < pieces.length; x++) {
            for (int y = 0; y < pieces[0].length; y++) {
                DefaultPiece piece = getPiece(x, y);
                if (piece != null && piece.getTeam() != team) {
                    ArrayList<Position> moves = piece.getPossibleMoves(false);
                    for (Position move : moves) {
                        if (move.equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(Team team) {
        if (!isKingInCheck(team)) {
            return false;
        }
        for (int x = 0; x < pieces.length; x++) {
            for (int y = 0; y < pieces[0].length; y++) {
                DefaultPiece piece = getPiece(x, y);
                if (piece != null && piece.getTeam() == team) {
                    ArrayList<Position> moves = piece.getPossibleMoves(false);
                    for (Position move : moves) {
                        Board testBoard = new Board(this);
                        testBoard.moveTo(testBoard.getPiece(x, y), move.x, move.y);
                        if (!testBoard.isKingInCheck(team)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private Position findKing(Team team) {
        for (int x = 0; x < pieces.length; x++) {
            for (int y = 0; y < pieces[0].length; y++) {
                DefaultPiece piece = getPiece(x, y);
                if (piece instanceof King && piece.getTeam() == team) {
                    return new Position(x, y);
                }
            }
        }
        return null;
    }

}
