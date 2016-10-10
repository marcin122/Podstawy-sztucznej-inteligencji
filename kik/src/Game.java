/**
 * Created by Krzysztof on 2016-03-14.
 */
public class Game {
    private String player1;
    private String player2;

    private int score1;
    private int score2;

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    private int currentPlayer = 0;

    private int [][] board = {
            {-1, -1, -1},
            {-1, -1, -1},
            {-1, -1, -1}
    };

    public void setPlayers(String p1, String p2){
        player1 = p1;
        score1 = 0;
        player2 = p2;
        score2 = 0;

    }

    public String getSign(){
        if(currentPlayer == 0) return "X";
        else return "O";
    }

    public void nextRound(){
        if(currentPlayer == 0) currentPlayer = 1;
        else currentPlayer = 0;
    }

    public boolean canSet(int x, int y) {
        return board[x][y] == -1;
    }

    public boolean isDrawn(){
        int minusOneCount = 0;
        for(int i = 0; i<3; ++i){
            for(int j = 0; j<3; ++j){
                minusOneCount++;
            }
        }

        return minusOneCount == 0;
    }

    public boolean isWon() {
        if(board[0][0] == currentPlayer && board[0][1] == currentPlayer && board[0][2] == currentPlayer)
            return true;
        if(board[1][0] == currentPlayer && board[1][1] == currentPlayer && board[1][2] == currentPlayer)
            return true;
        if(board[2][0] == currentPlayer && board[2][1] == currentPlayer && board[2][2] == currentPlayer)
            return true;
        if(board[0][0] == currentPlayer && board[1][0] == currentPlayer && board[2][0] == currentPlayer)
            return true;
        if(board[0][1] == currentPlayer && board[1][1] == currentPlayer && board[2][1] == currentPlayer)
            return true;
        if(board[0][2] == currentPlayer && board[1][2] == currentPlayer && board[2][2] == currentPlayer)
            return true;
        if(board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer)
            return true;
        if(board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)
            return true;
        return false;
    }

    public String set(int x, int y) {
        board[x][y] = currentPlayer;
        return getSign();
    }

    public String getCurrentPlayerName(){
        if(currentPlayer == 0) return player1;
        else return player2;
    }

    public void newGame() {
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                board[i][j] = -1;
            }
        }
        currentPlayer = 0;
    }

    public void addPoints(){
        if(currentPlayer == 0) score1++;
        else score2++;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }
}
