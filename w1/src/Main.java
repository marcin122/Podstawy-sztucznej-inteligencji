import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Perceptron perceptron = new Perceptron();
        perceptron.learnPerceptron("and");
        perceptron.showImportance();
        perceptron.learnPerceptron("or");
        perceptron.showImportance();
    }
}
