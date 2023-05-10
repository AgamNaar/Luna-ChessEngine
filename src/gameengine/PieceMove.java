package gameengine;

// Class that represent a move of a piece
public class PieceMove {
    private static final byte FIRST_SQUARE_ON_SECOND_ROW = 8;
    private static final int LAST_SQUARE_ON_7TH_ROW = 55;
    private final byte piecePosition;
    private final byte targetSquare;
    private final char typeOfPieceToPromoteTo;
    private final boolean isAPawnMove;

    public PieceMove(byte piecePosition, byte targetSquare, char typeOfPieceToPromoteTo, boolean isAPawnMove) {
        this.piecePosition = piecePosition;
        this.targetSquare = targetSquare;
        this.typeOfPieceToPromoteTo = typeOfPieceToPromoteTo;
        this.isAPawnMove = isAPawnMove;
    }

    public byte getPiecePosition() {
        return piecePosition;
    }

    public byte getTargetSquare() {
        return targetSquare;
    }

    @Override
    public String toString() {
        // If it's a promotion move, add the type of piece to promote
        if (isItPromotionMove() && isAPawnMove)
            return positionToNotation(piecePosition) + positionToNotation(targetSquare) + typeOfPieceToPromoteTo;
        return positionToNotation(piecePosition) + positionToNotation(targetSquare);
    }

    // Return the notation of the byte square
    private String positionToNotation(byte position) {
        String row = String.valueOf((position / 8) + 1);
        char colum = (char) ((7 - (position % 8)) + 'a');
        return colum + row;
    }

    // Check if the target square is a promotion square
    public boolean isItPromotionMove() {
        return targetSquare < FIRST_SQUARE_ON_SECOND_ROW || targetSquare > LAST_SQUARE_ON_7TH_ROW;
    }

    // Return the type of piece to promote
    public char getTypeOfPieceToPromoteTo() {
        return typeOfPieceToPromoteTo;
    }
}
