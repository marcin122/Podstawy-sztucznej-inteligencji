import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class neuralNetwork {
    private int numLayers=2;
    private Neuron[][] neurons;
    private double delta;

    public neuralNetwork() {

        neurons=new Neuron[2][];

        neurons[0]=new Neuron[48];
        for(int i=0;i<neurons[0].length;i++){
            neurons[0][i]=new Neuron(9);
            neurons[0][i].initWages();
        }

        neurons[1]=new Neuron[9];
        for(int i=0;i<neurons[1].length;i++){
            neurons[1][i]=new Neuron(48);
            neurons[1][i].initWages();
        }
    }

    public Scanner readData() throws FileNotFoundException {
        String pathToFile = "C:\\Users\\PC\\Desktop\\psi\\Podstawy-sztucznej-inteligencji\\neuralNetwork\\src\\data.txt";
        File file = new File(pathToFile);
        Scanner scanner = new Scanner(file);
        return scanner;
    }

    public void backPropagation() throws FileNotFoundException {

        Scanner scanner = readData();
        double[] tabInput = new double[9];
        double[] tabOutput=new double[9];

        while(scanner.hasNext()){

            for(int i=0;i<9;i++){
                tabInput[i] = scanner.nextInt();
            }

            for(int i=0;i<9;i++){
                tabOutput[i] = scanner.nextInt();
            }


            for(int i=0;i<neurons[0].length;i++){
                neurons[0][i].setInput(tabInput);
                neurons[0][i].sumWagesInput();
                tabInput[i]=neurons[0][i].activationFunction();
            }
            for(int i=0;i<neurons[1].length;i++){
                neurons[1][i].setInput(tabInput);
                neurons[1][i].sumWagesInput();
                tabOutput[i]=neurons[1][i].activationFunction();
            }
        }


    }
}
