package gamelogic.pieces;

// Class that represent a rook
public class Rook extends Piece {
    public Rook(byte square, boolean color) {
        super(square, color);
    }

    @Override
    public long getThreatLines(byte enemyKingSquare, Long boardBitBoard) {
        return threateningLine.getRookThreateningLine(getSquare(), enemyKingSquare, boardBitBoard);
    }

    @Override
    public int getPieceValue(long allPieceBitBoard, int gameStage) {
        return pieceEvaluation.evaluateRook(this, allPieceBitBoard, gameStage);
    }

    @Override
    public long getMovesAsBitBoard(long allPiecesBitBoard, long sameColorPiecesBitBoard) {
        return pieceMovement.getRookMovement(getSquare(), allPiecesBitBoard, sameColorPiecesBitBoard);
    }
}
