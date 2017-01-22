import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

public class MainForm {
    private JPanel panel1;
    private JButton nowaGraButton;
    private JButton btn01;
    private JButton btn02;
    private JButton btn10;
    private JButton btn20;
    private JButton btn11;
    private JButton btn21;
    private JButton btn12;
    private JButton btn22;
    private JButton btn00;
    private JLabel player1;
    private JLabel player2;
    private JLabel labelScore1;
    private JLabel labelScore2;

    private List<JButton> board;

    Game game = new Game();

    public MainForm() {

        labelScore1.setText("0");
        labelScore2.setText("0");

        game.neuralNetwork.readWages();
        game.neuralNetwork.showWages();

        game.setPlayers("komputer", "gracz");
        player1.setText("komputer");
        player2.setText("gracz");

        nowaGraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                    startNewGame();

                    int index = networkMove();
                    int x = index / 3;
                    int y = index % 3;

                    if (game.canSet(x, y)) {

                        String sign = game.set(x, y);
                        board.get(index).setText(sign);
                        game.convertBoard();
                        game.nextRound();
                    }


            }
        });

        board = new ArrayList<>();
        board.addAll(Arrays.asList(btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22));

        connectButtonsToLogic();
    }

    private int networkMove(){
        double[] tabOutputNetwork;
        double out;
        int index=0;
        boolean end=true;

        tabOutputNetwork=game.neuralNetwork.doMove(game.getBoardNetwortk());
        out=tabOutputNetwork[0];

        while(end) {
            for (int i = 0; i < tabOutputNetwork.length; i++) {
                if (out < tabOutputNetwork[i]) {
                    out = tabOutputNetwork[i];
                    index = i;
                }
            }
            if(game.getBoardNetwortk()[index]==0) {
                end=false;
                tabOutputNetwork[index]=1;
            }
            else {
                tabOutputNetwork[index]=-2;
                out=-2;
            }
        }

        return index;
    }

    private void checkGame(){
        if (game.isDrawn()) {
            JOptionPane.showMessageDialog(null, "REMIS!", "KONIEC!", JOptionPane.WARNING_MESSAGE);
            //askForNextGame();
            stopGame();
        } else if (game.isWon()) {
            JOptionPane.showMessageDialog(null, "WYGRAL " + game.getCurrentPlayerName(), "KONIEC!", JOptionPane.INFORMATION_MESSAGE);
            game.addPoints();
            updateGuiScores();
            //askForNextGame();
            stopGame();
        } else {
            game.nextRound();
        }
    }

    private void connectButtonsToLogic() {
        for(JButton btn : board){
            btn.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton btn = (JButton) e.getSource();
                    int index = board.indexOf(btn);
                    int x = index/3;
                    int y = index%3;

                    if(game.canSet(x,y)) {

                        String sign = game.set(x, y);
                        btn.setText(sign);
                        game.convertBoard();

                        checkGame();
                    }

                    boolean end=false;
                    if(game.isDrawn()||game.isWon()) end=true;

                    if(!end) {
                        index = networkMove();
                        x = index / 3;
                        y = index % 3;

                        if (game.canSet(x, y)) {

                            String sign = game.set(x, y);
                            board.get(index).setText(sign);
                            game.convertBoard();

                            checkGame();
                        }
                    }
                }
            });
        }
    }

    /*private void askForNextGame() {
        int answ = JOptionPane.showConfirmDialog (null, "Jeszcze raz?", "Pytanie", JOptionPane.YES_NO_OPTION);
        if(answ == JOptionPane.YES_OPTION) {
            game.newGame();
            resetGame();
        } else {
            stopGame();
        }
    }*/

    private void updateGuiScores() {
        labelScore1.setText(String.valueOf(game.getScore1()));
        labelScore2.setText(String.valueOf(game.getScore2()));
    }

    private void resetGame() {
        for(JButton btn : board){
            btn.setEnabled(true);
            btn.setText("");
        }
    }

    private void stopGame() {
        for(JButton btn : board){
            btn.setEnabled(false);
        }
    }

    private void startNewGame(){
        for(JButton btn : board){
            btn.setText("");
            btn.setEnabled(true);
        }
        game.newGame();

    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
