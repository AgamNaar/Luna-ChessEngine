package gamelogic.pieces;

import gamelogic.BoardUtils;

// Abstract class that represent a piece, has 2 attributes
// Color - color of the piece (white/black - true/false)
// Square - square of the piece on the board
public abstract class Piece implements Cloneable{

    private byte square;
    private final boolean color;

    protected static final PieceThreateningLine threateningLine = new PieceThreateningLine();
    protected static final PieceMovement pieceMovement = new PieceMovement();
    protected static final BoardUtils utils = new BoardUtils();

    public Piece(byte square, boolean color) {
        this.square = square;
        this.color = color;
    }


    // Abstract method, given bitboard of all pieces on the board and bitboard of only same color piece
    // Return bitboard of all the move the piece can do (without checking if It's legal or not)
    public abstract long getMovesAsBitBoard(long allPiecesBitBoard, long sameColorPiecesBitBoard);

    /*
    Given the enemy king and the board as a bitboard, return the treat line of this piece on a king as bitboard
    treat line - if the piece threat the king, or if an enemy piece block the piece from threading the king
    the treat line will be all the square from the piece to the king as bitboards
    if more than 1 piece block a piece from treating, or the king is not even on the attack line, return 0
    for pawn and a rook, a threat line is if they check the king, and the only square of the threat line will be their square
    */
    public abstract long getThreatLines(byte enemyKingSquare, Long boardBitBoard);

    // Return the position of the piece as bitboard
    public long getSquareAsBitBoard() {
        return utils.getSquarePositionAsBitboardPosition(square);
    }

    // getter and setter methods
    public byte getSquare() {
        return square;
    }

    public void setSquare(byte square) {
        this.square = square;
    }

    public boolean getColor() {
        return color;
    }

    public PieceMovement getPieceMovement() {
        return pieceMovement;
    }

    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}