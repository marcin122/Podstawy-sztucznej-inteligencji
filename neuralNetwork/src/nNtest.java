import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by PC on 2016-12-01.
 */
public class nNtest {

    private int numLayers=3;
    private Neuron[][] neurons;
    private double[][] delta;
    private double eta=0.6;
    private double del=0.1;
    private boolean endBP=false;

    public nNtest() {

        delta=new double[3][];
        delta[0]=new double[2];
        delta[1]=new double[3];
        delta[2]=new double[2];
        for(int i=0;i<delta[0].length;i++) delta[0][i]=0;
        for(int i=0;i<delta[1].length;i++) delta[1][i]=0;
        for(int i=0;i<delta[2].length;i++) delta[2][i]=0;

        neurons=new Neuron[3][];

        neurons[0]=new Neuron[2];
        for(int i=0;i<neurons[0].length;i++){
            neurons[0][i]=new Neuron(1);
            neurons[0][i].initWages();
        }

        neurons[1]=new Neuron[3];
        for(int i=0;i<neurons[1].length;i++){
            neurons[1][i]=new Neuron(2);
            neurons[1][i].initWages();
        }

        neurons[2]=new Neuron[2];
        for(int i=0;i<neurons[2].length;i++){
            neurons[2][i]=new Neuron(3);
            neurons[2][i].initWages();
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
        double[] tabInput = new double[2];
        double[] tabOutput = new double[2];
        double[] tabInputHiddenSecondLayer = new double[2];
        double[] tabInputHiddenThirdLayer = new double[3];
        double[] tabOutputNetwrok = new double[2];

        while(scanner.hasNext()){
            for (int i = 0; i < tabInput.length; i++) {
                tabInput[i]=scanner.nextInt();//wejscie
            }

            for (int i = 0; i < tabOutput.length; i++) {
                tabOutput[i] = scanner.nextInt(); //wyjscie
            }
        }
    }

    public static void main(String[] args){
        nNtest n=new nNtest();

    }
}
