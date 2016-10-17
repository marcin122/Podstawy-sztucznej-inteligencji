import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Perceptron perceptron = new Perceptron();
        perceptron.learnPerceptorn("and");
        perceptron.showImportance();
        perceptron.learnPerceptorn("or");
        perceptron.showImportance();
    }
}
