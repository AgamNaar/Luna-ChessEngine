package Pieces;

// Class that represent a pawn
public class Pawn extends Piece {
    public Pawn(byte square, boolean color) {
        super(square, color);
    }

    @Override
    public long getMovesAsBitBoard(long allPiecesBitBoard, long allSameColorPiecesBitBoard) {
        long enemyPiecesBitBoards = allPiecesBitBoard & ~allSameColorPiecesBitBoard;
        return pieceMovement.getPawnMovement(getSquare(), getColor(), allPiecesBitBoard, enemyPiecesBitBoards);
    }
}
