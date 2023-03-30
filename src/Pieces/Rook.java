package Pieces;

// Class that represent a rook
public class Rook extends LinePiece {
    public Rook(byte square, boolean color) {
        super(square, color);
    }

    @Override
    public long getTreatLines(byte enemyKingSquare, Long boardBitBoard) {
        return threateningLine.getRookThreateningLine(getSquare(), enemyKingSquare, boardBitBoard);
    }

    @Override
    public long getMovesAsBitBoard(long allPiecesBitBoard, long sameColorPiecesBitBoard) {
        return pieceMovement.getRookMovement(getSquare(), allPiecesBitBoard, sameColorPiecesBitBoard);
    }
}
