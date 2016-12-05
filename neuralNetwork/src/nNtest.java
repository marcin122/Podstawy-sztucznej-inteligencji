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
    private double[] wages;

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
        String pathToFile = "C:\\Users\\PC\\Desktop\\psi\\Podstawy-sztucznej-inteligencji\\neuralNetwork\\src\\d.txt";
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

            for (int i = 0; i < neurons[0].length; i++) {
                neurons[0][i].setIn(tabInput[i]);
                neurons[0][i].sumWagesInput();
                tabInputHiddenSecondLayer[i] = neurons[0][i].activationFunction();
            }
            for (int i = 0; i < neurons[1].length; i++) {
                neurons[1][i].setInput(tabInputHiddenSecondLayer);
                neurons[1][i].sumWagesInput();
                tabInputHiddenThirdLayer[i] = neurons[1][i].activationFunction();
            }

            //obliczenia dla trzeciej powloki
            for(int i=0;i<neurons[2].length;i++) {
                neurons[2][i].setInput(tabInputHiddenThirdLayer);
                neurons[2][i].sumWagesInput();
                tabOutputNetwrok[i] = neurons[2][i].activationFunction();
            }

            for (int i = 0; i < tabOutput.length; i++) {
                delta[2][i] = tabOutput[i] - tabOutputNetwrok[i];
            }

            //propagacja wsteczna na 2 powloke
            for (int i = 0; i < neurons[1].length; i++) {
                for (int j = 0; j < neurons[2].length; j++) {
                    delta[1][i] = (delta[2][j]*neurons[2][j].getWages()[i]);
                }
            }
            //propagacja wsteczna na 1 powloke
            for (int i = 0; i < neurons[0].length; i++) {
                for (int j = 0; j < neurons[1].length; j++) {
                    delta[0][i] = (delta[1][j]*neurons[1][j].getWages()[i]);
                }
            }
            //korekta wag 1 powloka
            for(int i=0;i<neurons[0].length;i++){
                wages=new double[neurons[0][i].getNumInput()+1];
                for(int j=0;j<neurons[0][0].getNumInput();j++) {
                    wages[j]=neurons[0][i].getWages()[j] + eta * delta[0][i] * neurons[0][i].pochodnaActivationFunction() * tabInput[j];
                }
                neurons[0][i].setWages(wages);
            }
            for (int i = 0; i < neurons[0].length; i++) {
                neurons[0][i].setIn(tabInput[i]);
                neurons[0][i].sumWagesInput();
                tabInputHiddenSecondLayer[i] = neurons[0][i].activationFunction();
            }

            //korekta wag 2 powloka
            for(int i=0;i<neurons[1].length;i++){
                wages=new double[neurons[1][i].getNumInput()+1];
                for(int j=0;j<neurons[1][0].getNumInput();j++) {
                    wages[j]=neurons[1][i].getWages()[j] + eta * delta[1][i] * neurons[1][i].pochodnaActivationFunction() * tabInputHiddenSecondLayer[j];
                }
                neurons[1][i].setWages(wages);
            }
            for (int i = 0; i < neurons[0].length; i++) {
                neurons[1][i].setInput(tabInputHiddenSecondLayer);
                neurons[1][i].sumWagesInput();
                tabInputHiddenThirdLayer[i] = neurons[1][i].activationFunction();
            }

            //korekta wag 3 powloka
            for(int i=0;i<neurons[2].length;i++){
                wages=new double[neurons[2][i].getNumInput()+1];
                for(int j=0;j<neurons[2][0].getNumInput();j++) {
                    wages[j]=neurons[2][i].getWages()[j] + eta * delta[2][i] * neurons[2][i].pochodnaActivationFunction() * tabInputHiddenThirdLayer[j];
                }
                neurons[2][i].setWages(wages);
            }
            for(int i=0;i<neurons[2].length;i++) {
                neurons[2][i].setInput(tabInputHiddenThirdLayer);
                neurons[2][i].sumWagesInput();
                tabOutputNetwrok[i] = neurons[2][i].activationFunction();
            }
        }
    }

    public void doMove(int[] tabInput){

        double[] tabInputHiddenSecondLayer = new double[2];
        double[] tabInputHiddenThirdLayer = new double[3];
        double[] tabOutputNetwrok = new double[2];

        for (int i = 0; i < neurons[0].length; i++) {
            neurons[0][i].setIn(tabInput[i]);
            neurons[0][i].sumWagesInput();
            tabInputHiddenSecondLayer[i] = neurons[0][i].activationFunction();
        }
        for (int i = 0; i < neurons[1].length; i++) {
            neurons[1][i].setInput(tabInputHiddenSecondLayer);
            neurons[1][i].sumWagesInput();
            tabInputHiddenThirdLayer[i] = neurons[1][i].activationFunction();
        }

        //obliczenia dla trzeciej powloki
        for(int i=0;i<neurons[2].length;i++) {
            neurons[2][i].setInput(tabInputHiddenThirdLayer);
            neurons[2][i].sumWagesInput();
            tabOutputNetwrok[i] = neurons[2][i].activationFunction();
            System.out.print(tabOutputNetwrok[i]+" ");
        }
    }

    public void showWages(){
        for(int i=0;i<neurons[0].length;i++){
            System.out.println("Powloka nr 1.");
            for(int j=0;j<2;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[0][i].getWages()[j]);
            }
        }

        for(int i=0;i<neurons[1].length;i++){
            System.out.println("Powloka nr 2.");
            for(int j=0;j<3;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[1][i].getWages()[j]);
            }
        }

        for(int i=0;i<neurons[2].length;i++){
            System.out.println("Powloka nr 3.");
            for(int j=0;j<4;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[2][i].getWages()[j]);
            }
        }
    }

    public static void main(String[] args){
        nNtest n=new nNtest();
        n.showWages();
        System.out.println("--------------------------------------");
        try {
            n.backPropagation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        n.showWages();
        int[] t={0,1};
        n.doMove(t);
    }
}
