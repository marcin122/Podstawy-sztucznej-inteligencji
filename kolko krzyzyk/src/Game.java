
public class Game {

    private String player1;
    private String player2;

    private int score1;
    private int score2;

    public neuralNetwork neuralNetwork=new neuralNetwork();

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    private int currentPlayer = 1;

    private double[] boardNetwortk={0,0,0,0,0,0,0,0,0,1};

    public void convertBoard(){
        for(int j=0;j<3;j++){
            for(int i=0;i<3;i++){
                boardNetwortk[i+j*3]=board[j][i];
            }
        }
    }

    private int [][] board = {
            {0,0,0},
            {0,0,0},
            {0,0,0}
    };

    public void setPlayers(String p1, String p2){
        player1 = p1;
        score1 = 0;
        player2 = p2;
        score2 = 0;

    }

    public String getSign(){
        if(currentPlayer == 1) return "X";
        else return "O";
    }

    public void nextRound(){
        if(currentPlayer == 1) currentPlayer = -1;
        else currentPlayer = 1;
    }

    public boolean canSet(int x, int y) {
        return board[x][y] == 0;
    }

    public boolean isDrawn(){
        int zeroCount = 0;
        for(int i = 0; i<3; ++i){
            for(int j = 0; j<3; ++j){
                if(board[i][j]==0) zeroCount++;
            }
        }

        return zeroCount == 0;
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
        if(currentPlayer == 1) return player1;
        else return player2;
    }

    public void newGame() {
        currentPlayer = 1;
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                board[i][j] = 0;
            }
        }
    }

    public void addPoints(){
        if(currentPlayer == 1) score1++;
        else score2++;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public neuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public double[] getBoardNetwortk() {
        convertBoard();
        return boardNetwortk;
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean boardIsEmpty(){
        convertBoard();
        boolean empty=true;
        for(int i=0;i<9;i++) {
            if(boardNetwortk[i]!=0){
                empty=false;
                break;
            }
        }
        return empty;
    }

    public boolean boardIsFull(){
        convertBoard();
        boolean full=true;
        for(int i=0;i<9;i++) {
            if(boardNetwortk[i]==0){
                full=false;
                break;
            }
        }
        return full;
    }
}
