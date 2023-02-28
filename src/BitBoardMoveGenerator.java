import java.util.HashMap;

// This class is responsible, to generate for each piece, for each square on the board (0-63)
// All the possible moves it can do - regardless of the position of the board
// For each piece it returns an array of 64, each position has a bitboard of the move co-responding for that position
public class BitBoardMoveGenerator {

    private static final byte[] KING_OFFSETS = {-9, -8, -7, -1, 1, 7, 8, 9};
    private static final byte[] ROOK_OFFSETS = {1, -1, 8, -8};
    private static final byte[] BISHOP_OFFSETS = {7, -7, 9, -9};
    private static final byte[] KNIGHT_OFFSETS = {-17, -15, -10, -6, 6, 10, 15, 17};
    private static final byte[] WHITE_PAWN_OFFSETS_ATK = {7, 9};
    private static final byte[] BLACK_PAWN_OFFSETS_ATK = {-7, -9};
    private static final byte ALL_BITES_ONE = -1;

    public static final boolean WHITE = true;

    // Change moves array that in each position of the array, it has a bitboard that represent the moves a king can do in the position
    public void generateKingMoves(long[] movesArray) {
        getMoves(movesArray, KING_OFFSETS);
    }

    // Change rook/bishop moves that, in each cell of the array, there is a hashmap that has the bitBoard movement for each row+column or diagonal
    // The value of row+column or diagonal is the key to that value of bitBoard movement
    public void generateLinePieceMoves(HashMap<Long, Long>[] rookMoves, HashMap<Long, Long>[] bishopMoves) {
        getMovementLinePiece(rookMoves, bishopMoves);
    }

    // Change moves array that in each position of the array, it has a bitboard that represent the moves a knight can do in the position
    public void generateKnightMoves(long[] movesArray) {
        getMoves(movesArray, KNIGHT_OFFSETS);
    }

    // Change moves array and atk array, that in each position of the array, it has a bitboard that represent the moves/capture a pawn can do in the position
    public void generatePawnMoves(long[] movesArray, long[] atkArray, boolean color) {
        if (color == WHITE) {
            getPawnMoves(movesArray, color);
            getMoves(atkArray, WHITE_PAWN_OFFSETS_ATK);
        } else {
            getPawnMoves(movesArray, color);
            getMoves(atkArray, BLACK_PAWN_OFFSETS_ATK);
        }
    }

    // Generate a mask for squares for rook row and columns
    public void generateMaskRook(long[] maskArray) {
        for (byte square = 0; square < 64; square++)
            maskArray[square] = tobitMapRook(square, ALL_BITES_ONE, ALL_BITES_ONE);
    }

    // Generate a mask for squares for bishop row and columns
    public void generateMaskBishop(long[] maskArray) {
        for (byte square = 0; square < 64; square++) {
            maskArray[square] = tobitMapRook(square, ALL_BITES_ONE, ALL_BITES_ONE);
        }

    }

    // Generate only the moves a pawn can do (not including capture)
    private void getPawnMoves(long[] moveArray, boolean color) {
        byte offset = (color) ? (byte) 8 : -8;
        for (byte square = 7; square < 56; square++) {
            long currSquareMoves = 0L;
            currSquareMoves |= squareToBitboard(offset + square);

            // if on 2nd raw as white, it can move twice
            if (square <= 15 && offset == 8)
                currSquareMoves |= squareToBitboard((offset * 2) + square);

            // if on the 6th row as black, it can move twice
            if (square >= 47 && offset == -8)
                currSquareMoves |= squareToBitboard((offset * -2) + square);

            moveArray[square] = currSquareMoves;
        }
    }

    // Generate moves for each square of the board for none line pieces (King, Knight, Pawn)
    private void getMoves(long[] moveArray, byte[] offsetArray) {
        for (byte square = 0; square < 64; square++) {
            long currSquareMoves = 0L;
            for (byte offset : offsetArray) {
                // check if legal offset, if yes added the possible moves
                if (isLegalOffset(square, offset))
                    currSquareMoves |= squareToBitboard(offset + square);
            }
            moveArray[square] = currSquareMoves;
        }
    }

    // Generate moves for each square of the board for line pieces (Rook, Bishop)
    private void getMovementLinePiece(HashMap<Long, Long>[] moveArrayRook, HashMap<Long, Long>[] moveArrayBishop) {
        // For each square, for each possible values of rows and columns, get the bitboard value
        for (byte square = 0; square < 64; square++) {
            for (byte rowValue = -128; rowValue < 128; rowValue++) {
                for (byte columnValue = -128; columnValue < 128; columnValue++) {
                    long rookMap = tobitMapRook(square, rowValue, columnValue);
                    long bishopMap = tobitMapBishop(square, rowValue, columnValue);
                    long movesRook = getMoves(square, ROOK_OFFSETS, rookMap);
                    long movesBishop = getMoves(square, BISHOP_OFFSETS, bishopMap);
                    moveArrayRook[square].put(rookMap, movesRook);
                    moveArrayBishop[square].put(bishopMap, movesBishop);
                }
            }
        }

    }

    // Given a square, rowValue. columnValue: set the value of row/column of the square to be rowValue and columnValue
    private long tobitMapRook(byte square, byte rowValue, byte columnValue) {
        int row = square % 8;
        long bitboard = 0L;
        for (int i = 0; i < 8; i++) {
            long rowMask = ((long) rowValue & 0xFF) << (i * 8);
            long colMask = ((long) columnValue & 0xFF) << i;
            if (i == row) {
                bitboard |= rowMask;
            }
            bitboard |= colMask;
        }
        return bitboard;

    }

    // Given a square, nwDiagonalValue. neDiagonalValue: set the value of nwDiagonalValue/neDiagonalValue of the square to be nwDiagonalValue/neDiagonalValue
    private long tobitMapBishop(byte square, byte nwDiagonalValue, byte neDiagonalValue) {
        int row = square % 8, column = square / 8;
        long bitboard = 0L;
        for (int i = 0; i < 8; i++) {
            long nwDiagonalMask = ((long) nwDiagonalValue & 0xFF) << ((i - column + 7) % 8) * 9;
            long neDiagonalMask = ((long) neDiagonalValue & 0xFF) << (i + column) % 8 * 9;
            bitboard |= (i == row) ? nwDiagonalMask | neDiagonalMask : 0L;
        }
        return bitboard;
    }

    // Given a square, an array of offsets and a long that represent the current board
    // Return a long with all the possible moves a piece with does offset can do on that board
    private long getMoves(byte square, byte[] offsetArray, long bitBoard) {
        long positionBit = squareToBitboard(square), result = 0;

        for (byte offset : offsetArray) {
            int i = 0;
            // Run until the edge of the board or found a piece
            while (isLegalOffset((byte) (square + (offset * i)), offset)) {
                long temp;
                // Check if offset is negative or positive to offset right or left
                if (offset > 0)
                    temp = positionBit << i * offset;
                else
                    temp = positionBit >> i * -offset;

                // if not 0, piece on that position, 0 mean position empty
                if ((temp & bitBoard) != 0) {
                    result |= temp;
                    break;
                } else
                    result |= temp;

                i++;
            }
        }
        return result;
    }

    // Given an offset and a square, check if that offset from that square is legal
    // If on the last/first rank, is not outside of board
    // If on the right/left most file, is not outside of board
    private boolean isLegalOffset(byte position, byte offset) {
        // Calculate the row and column of the given position
        int row = position / 8;
        int col = position % 8;

        // Calculate the row and column of the target square
        int targetRow = row + offset / 8;
        int targetCol = col + offset % 8;

        // Check if the target square is within the board limits
        return targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8;
    }

    // Convert position into bit position
    private long squareToBitboard(long square) {
        return 1L << square;
    }
}