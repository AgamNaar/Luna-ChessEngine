package gamelogic.pieces;

// Class that represent a bishop
public class Bishop extends Piece {
    public Bishop(byte square, boolean color) {
        super(square, color);
    }

    @Override
    public long getThreatLines(byte enemyKingSquare, Long boardBitBoard) {
        return threateningLine.getBishopThreateningLine(getSquare(), enemyKingSquare, boardBitBoard);
    }

    @Override
    public int getPieceValue(long allPieceBitBoard, int gameStage) {
        return pieceEvaluation.evaluateBishop(this, allPieceBitBoard);
    }

    @Override
    public long getMovesAsBitBoard(long allPiecesBitBoard, long sameColorPiecesBitBoard) {
        return pieceMovement.getBishopMovement(getSquare(), allPiecesBitBoard, sameColorPiecesBitBoard);
    }
}
