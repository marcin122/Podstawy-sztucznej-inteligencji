import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by student on 2016-03-14.
 */
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

        nowaGraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                NewGameDialog newGame = new NewGameDialog();
                newGame.setVisible(true);

                if(newGame.isOk){
                    game.setPlayers(newGame.name1, newGame.name2);
                    startNewGame(newGame.name1, newGame.name2);
                }

            }
        });

        board = new ArrayList<>();
        board.addAll(Arrays.asList(btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22));

        connectButtonsToLogic();
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

                        if (game.isDrawn()) {
                            JOptionPane.showMessageDialog(null, "REMIS!", "KONIEC!", JOptionPane.WARNING_MESSAGE);
                            askForNextGame();
                        } else if (game.isWon()) {
                            JOptionPane.showMessageDialog(null, "WYGRAL " + game.getCurrentPlayerName(), "KONIEC!", JOptionPane.INFORMATION_MESSAGE);
                            game.addPoints();
                            updateGuiScores();
                            askForNextGame();
                        } else {
                            game.nextRound();
                        }
                    }

                }
            });
        }
    }

    private void askForNextGame() {
        int answ = JOptionPane.showConfirmDialog (null, "Jeszcze raz?", "Pytanie", JOptionPane.YES_NO_OPTION);
        if(answ == JOptionPane.YES_OPTION) {
            game.newGame();
            resetGame();
        } else {
            stopGame();
        }
    }

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

    private void startNewGame(String name1, String name2){
        player1.setText(name1);
        player2.setText(name2);

        for(JButton btn : board){
            btn.setText("");
            btn.setEnabled(true);
        }

        labelScore1.setText("0");
        labelScore2.setText("0");
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
