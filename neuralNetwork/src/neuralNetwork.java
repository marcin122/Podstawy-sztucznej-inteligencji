import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class neuralNetwork {
    private int numLayers=2;
    private Neuron[][] neurons;
    private double[][] delta;
    private double eta=0.7;

    public neuralNetwork() {

        delta=new double[2][];
        delta[0]=new double[48];

        delta[1]=new double[9];
        for(int i=0;i<delta[0].length;i++)delta[0][i]=0;

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
        double[] tabInputHidden=new double[48];
        double[] tabOutputNetwrok=new double[9];

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
                tabInputHidden[i]=neurons[0][i].activationFunction();
            }
            for(int i=0;i<neurons[1].length;i++){
                neurons[1][i].setInput(tabInputHidden);
                neurons[1][i].sumWagesInput();
                tabOutputNetwrok[i]=neurons[1][i].activationFunction();
            }

            for (int i=0;i<tabOutput.length;i++){
                delta[1][i]=tabOutput[i]-tabOutputNetwrok[i];
            }

            for(int i=0;i<neurons[0].length;i++){

                for(int j=0;j<neurons[1].length;j++){
                    delta[0][i]+=delta[1][j]*neurons[1][i].getWages()[i];
                }
            }
            for(int m=0;m<neurons.length;m++) {
                for (int i = 0; i < neurons[m].length; i++) {
                    for (int j = 0; j < neurons[m][i].getNumInput(); j++) {
                        neurons[m][i].getWages()[j] += eta * delta[m][i];
                    }
                }
            }

        }


    }
}
