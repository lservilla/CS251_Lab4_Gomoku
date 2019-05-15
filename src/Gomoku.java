
/**
 * @version date (CS_251_004, 2019-03-06)
 * @author Lasair Servilla
 */


import cs251.lab4.GomokuGUI;
import cs251.lab4.GomokuModel;

import java.util.Random;
import java.util.Scanner;


public class Gomoku implements GomokuModel {


    private char[][] gomokuBoard =
            new char[getNumberOfRows()][getNumberOfCols()];
    private Random rand = new Random();
    private int setPlayer = 0;
    private char currentPlayer;
    private static int answer;


    /**
     * Set the number of columns, currently default number.
     *
     * @return Number of columns
     */
    @Override
    public int getNumberOfCols() {
        return GomokuModel.DEFAULT_NUM_COLS;

    }

    /**
     * Set the number of rows, currently default number.
     *
     * @return Number of rows.
     */
    @Override
    public int getNumberOfRows() {
        return GomokuModel.DEFAULT_NUM_ROWS;
    }

    /**
     * Get number in a row needed to win, currently default number.
     *
     * @return Number in a row needed to win.
     */
    @Override
    public int getNumberInLineForWin() {

        return GomokuModel.SQUARES_IN_LINE_FOR_WIN;
    }

    /**
     * Method that brings up correct game based on player's choice
     * and then handles click event.
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public Outcome handleClickAtLocation(int i, int i1) {
        switch (answer) {
            case 1: // Human player
                getCurrentPlayer(i, i1);
                return winDetect(i, i1, currentPlayer,
                        getNumberInLineForWin());

            case 2: // Copy cat
                while (gomokuBoard[i][i1] != Square.EMPTY.toChar()) {
                    return Outcome.GAME_NOT_OVER;
                }
                Outcome outcomeResults = humanPlayer(i, i1);
                if (outcomeResults.equals(Outcome.RING_WINS)) {
                    return outcomeResults;
                }
                outcomeResults = copyCatComputer(i, i1);
                if (outcomeResults.equals(Outcome.CROSS_WINS)) {
                    return outcomeResults;
                }
                return outcomeResults;

            case 3: // In to win
                while (gomokuBoard[i][i1] != Square.EMPTY.toChar()) {
                    return Outcome.GAME_NOT_OVER;
                }
                outcomeResults = humanPlayer(i, i1);
                if (outcomeResults.equals(Outcome.RING_WINS)) {
                    return outcomeResults;
                }
                outcomeResults = inToWinComputer();
                if (outcomeResults.equals(Outcome.CROSS_WINS)) {
                    return outcomeResults;
                }
                return outcomeResults;

        }

        return null;
    }

    /**
     * @param row
     * @param col
     * @return
     */
    private Outcome humanPlayer(int row, int col) {
        currentPlayer = Square.RING.toChar();
        gomokuBoard[row][col] = currentPlayer;
        Outcome outcomeResult = winDetect(row, col, currentPlayer,
                getNumberInLineForWin());
        if (outcomeResult.equals(Outcome.RING_WINS)) {
            return outcomeResult;
        } else if (outcomeResult.equals(Outcome.DRAW)) {
            return outcomeResult;
        }
        return Outcome.GAME_NOT_OVER;
    }


    /**
     * Win detection that checks for wins in all possible directions
     * based on last move made.
     *
     * @param row       Y coordinate of last play.
     * @param col       X coordinate of last play.
     * @param curPlayer Current player of click event.
     * @param numToWin  The number in a row needed to win.
     * @return Returns if there is a win of associated player,
     * otherwise game not over.
     */
    private Outcome winDetect(int row, int col, char curPlayer, int numToWin) {
        if (horizontalWin(row, col, curPlayer) != Outcome.GAME_NOT_OVER) {
            return horizontalWin(row, col, curPlayer);
        } else if (verticalWin(row, col, curPlayer) != Outcome.GAME_NOT_OVER) {
            return verticalWin(row, col, curPlayer);
        } else if (positiveWin(row, col, curPlayer) != Outcome.GAME_NOT_OVER) {
            return positiveWin(row, col, curPlayer);
        } else if (negativeWin(row, col, curPlayer) != Outcome.GAME_NOT_OVER) {
            return negativeWin(row, col, curPlayer);
        }
        return Outcome.GAME_NOT_OVER;

    }

    /**
     * Method to determine if there is a win in the horizontal direction.
     *
     * @param row       Y coordinate of last play.
     * @param col       x location for player
     * @param curPlayer Current player of click event.
     * @return Returns if there is a win of associated player,
     * otherwise game not over.
     */
    private Outcome horizontalWin(int row, int col, char curPlayer) {
        int x = col;
        int y = row;
        int horizontalCounter = 0;
        while (x > 0 && x > (col -
                (getNumberInLineForWin() - 1)) &&
                gomokuBoard[y][x] == curPlayer) {
            x--;
        }

        while (x < getNumberOfCols() && x < (col + getNumberInLineForWin())
                && gomokuBoard[y][x] == curPlayer) {
            horizontalCounter++;
            x++;
            if (horizontalCounter == getNumberInLineForWin()) {
                if (curPlayer == Square.CROSS.toChar()) {
                    return Outcome.CROSS_WINS;
                } else return Outcome.RING_WINS;
            }
        }
        return Outcome.GAME_NOT_OVER;
    }

    /**
     * Method to determine if there is a win in the vertical direction.
     *
     * @param row       Y coordinate of last play.
     * @param col       x location for player
     * @param curPlayer Current player of click event.
     * @return Returns if there is a win of associated player,
     * otherwise game not over.
     */
    private Outcome verticalWin(int row, int col, char curPlayer) {
        int x = col;
        int y = row;
        int verticalCounter = 0;
        while (y > 0 && y > (row - (getNumberInLineForWin() - 1)) &&
                gomokuBoard[y][x] == curPlayer) {
            y--;
        }

        while (y < getNumberOfRows() && y < (row + getNumberInLineForWin()) &&
                gomokuBoard[y][x] == curPlayer) {
            verticalCounter++;
            y++;
            if (verticalCounter == getNumberInLineForWin()) {
                if (curPlayer == Square.CROSS.toChar()) {
                    return Outcome.CROSS_WINS;
                } else return Outcome.RING_WINS;
            }
        }
        return Outcome.GAME_NOT_OVER;
    }

    /**
     * Method to determine if there is a win in the positive direction.
     *
     * @param row       Y coordinate of last play.
     * @param col       x location for player
     * @param curPlayer Current player of click event.
     * @return Returns if there is a win of associated player,
     * otherwise game not over.
     */
    private Outcome positiveWin(int row, int col, char curPlayer) {
        int x = col;
        int y = row;
        int positiveCounter = 0;
        while (y < (row + (getNumberInLineForWin() - 1)) &&
                (y < (getNumberOfRows() - 1)) &&
                x < (col + (getNumberInLineForWin() - 1)) &&
                (x < (getNumberOfCols() - 1))) {
            y++;
            x++;
        }
        while (y >= (row - (getNumberInLineForWin() - 1)) &&
                x >= (col - (getNumberInLineForWin() - 1)) &&
                x >= 0 && y >= 0) {
            if (gomokuBoard[y][x] == curPlayer) {
                positiveCounter++;
            }
            x--;
            y--;
            if (positiveCounter == getNumberInLineForWin()) {
                if (curPlayer == Square.CROSS.toChar()) {
                    return Outcome.CROSS_WINS;
                } else return Outcome.RING_WINS;
            }
        }
        return Outcome.GAME_NOT_OVER;
    }

    /**
     * Method determines if there is a win in the negative direction.
     *
     * @param row       Y coordinate of last play.
     * @param col       x location for player
     * @param curPlayer Current player of click event.
     * @return Returns if there is a win of associated player,
     * otherwise game not over.
     */
    private Outcome negativeWin(int row, int col, char curPlayer) {
        int x = col;
        int y = row;
        int negativeCounter = 0;
        while ((y < (row + (getNumberInLineForWin() - 1))) &&
                (y < (getNumberOfRows() - 1)) &&
                x > (col - (getNumberInLineForWin() - 1)) &&
                (x > 0)) {
            y++;
            x--;
        }

        int rowToStop = row - (getNumberInLineForWin() - 1);
        int colToStop = col + (getNumberInLineForWin() + 1);

        while (y >= rowToStop && x <= colToStop && x >= 0 &&
                y >= 0 && x < getNumberOfCols()) {
            if (gomokuBoard[y][x] == curPlayer) {
                negativeCounter++;
            }

            x++;
            y--;

            if (negativeCounter == getNumberInLineForWin()) {
                if (curPlayer == Square.CROSS.toChar()) {
                    return Outcome.CROSS_WINS;
                } else return Outcome.RING_WINS;
            }
        }
        return Outcome.GAME_NOT_OVER;
    }

    /**
     * Method that determines current player, when two humans are playing.
     * Switches between ring and cross.
     *
     * @param row Y location for player
     * @param col x location for player
     * @return Returns location that is given by either a ring or cross.
     */
    private char getCurrentPlayer(int row, int col) {
        while (gomokuBoard[row][col] == Square.EMPTY.toChar()) {
            if (setPlayer % 2 == 0) {
                gomokuBoard[row][col] = Square.RING.toChar();
            } else {
                gomokuBoard[row][col] = Square.CROSS.toChar();
            }
            currentPlayer = gomokuBoard[row][col];
            setPlayer++;
        }
        return gomokuBoard[row][col];
    }

    /**
     * Method to fill board with empty squares, and sets first player as winner
     * of last game.
     */
    @Override
    public void startNewGame() {
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int i1 = 0; i1 < getNumberOfCols(); i1++) {
                gomokuBoard[i][i1] = Square.EMPTY.toChar();
            }
        }

        //if ()

    }

    /**
     * Method to create string representation of game board.
     *
     * @return
     */
    @Override
    public String getBoardAsString() {
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < getNumberOfRows(); row++) {
            for (int col = 0; col < getNumberOfCols(); col++) {
                boardString.append(gomokuBoard[row][col]);
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }

    /**
     * @param s
     */
    @Override
    public void setComputerPlayer(String s) {
    }

    /**
     * @param row
     * @param col
     * @return
     */
    private Outcome copyCatComputer(int row, int col) {
        if (!getBoardAsString().contains("-")) { //Check for draw.
            return Outcome.DRAW;
        }
        int y = row;
        int y1 = row;
        int y2 = row;
        int x = col;
        int x1 = col;
        int x2 = col;
        if (x != getNumberOfCols() - 1 && gomokuBoard[y][++x1] ==
                Square.EMPTY.toChar()) {
            col++;
        } else if (x != 0 && gomokuBoard[y][--x2] == Square.EMPTY.toChar()) {
            col--;
        } else if (y != getNumberOfRows() - 1 && gomokuBoard[++y1][x] ==
                Square.EMPTY.toChar()) {
            row++;
        } else if (y != 0 && gomokuBoard[--y2][x] == Square.EMPTY.toChar()) {
            row--;
        } else {
            do {
                row = rand.nextInt(getNumberOfRows());
                col = rand.nextInt(getNumberOfCols());
            } while (gomokuBoard[row][col] != Square.EMPTY.toChar());
        }
        gomokuBoard[row][col] = Square.CROSS.toChar();
        currentPlayer = Square.CROSS.toChar();
        return winDetect(row, col, currentPlayer, getNumberInLineForWin());
    }

    /**
     * Method for computer player that chooses move right next to each other,
     * in order to win.
     *
     * @return
     */
    private Outcome inToWinComputer() {
        if (!getBoardAsString().contains("-")) {
            return Outcome.DRAW;
        }

        int row = 0;
        int col = 0;

        if(gomokuBoard[row][col] != Square.EMPTY.toChar()){
            col++;
        }


        gomokuBoard[row][col] = Square.CROSS.toChar();
        currentPlayer = Square.CROSS.toChar();

        return winDetect(row, col, currentPlayer, getNumberInLineForWin());
    }


    /**
     * Main method to start game of gomoku.
     * @param args Not used.
     */
    public static void main(String[] args){
        Gomoku game1 = new Gomoku();

        //Choose how you want to play
        Scanner question = new Scanner(System.in);

        System.out.println("Pick how to play, choose a number 1,or 2, or 3.");
        System.out.println("(1) Play against another human.");
        System.out.println("(2) Play against, copy cat, computer.");
        System.out.println("(3) Play against, in to win, computer.");

        String stNum = question.next();

        //Input error detection
        while (!GivenChoice(stNum)){
            System.out.println("Pick how to play, choose a number 1, 2, or 3.");

            stNum = question.next();
        }
        GomokuGUI.showGUI(game1);
    }

    /**
     * Method to take a string input and parse into an integer.
     * Throws exception if unable to parse integer.
     * Returns false if value isn't a valid choice.
     * @param number
     * @return Returns true if value can be parsed and is a valid answer
     * otherwise returns false.
     */
    public static boolean GivenChoice(String number){
        try {
            answer = Integer.parseInt(number);
            while (answer !=1 && answer!=2 && answer!=3){
                System.out.println("Not a valid number.");
                return false;
            }
            return true;
        } catch (NumberFormatException e){
            System.out.println("Can not be parsed as a number. ");
        }
        return false;
    }
}