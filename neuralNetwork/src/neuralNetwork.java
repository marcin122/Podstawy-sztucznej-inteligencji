import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class neuralNetwork {
    private int numLayers=3;
    private Neuron[][] neurons;
    private double[][] delta;
    private double eta=0.6;
    private double del1=0;
    private double del=20;
    private boolean endBP=false;

    public neuralNetwork() {

        //tablice bledow
        delta=new double[3][];
        delta[0]=new double[9];
        delta[1]=new double[48];
        delta[2]=new double[9];

        for(int i=0;i<delta[0].length;i++) delta[0][i]=0;
        for(int i=0;i<delta[1].length;i++) delta[1][i]=0;
        for(int i=0;i<delta[2].length;i++) delta[2][i]=0;

        neurons=new Neuron[3][];

        neurons[0]=new Neuron[9];
        for(int i=0;i<neurons[0].length;i++){
            neurons[0][i]=new Neuron(1);
            neurons[0][i].initWages();
        }

        neurons[1]=new Neuron[48];
        for(int i=0;i<neurons[1].length;i++){
            neurons[1][i]=new Neuron(9);
            neurons[1][i].initWages();
        }

        neurons[2]=new Neuron[9];
        for(int i=0;i<neurons[2].length;i++){
            neurons[2][i]=new Neuron(48);
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
        double[] tabInput = new double[9];
        double[] tabOutput = new double[9];
        double[] tabInputHiddenSecondLayer = new double[9];
        double[] tabInputHiddenThirdLayer = new double[48];
        double[] tabOutputNetwrok = new double[9];

        while (scanner.hasNext()) {

            //wczytanie danych do nauki
            for (int i = 0; i < tabInput.length; i++) {
                tabInput[i]=scanner.nextInt();//wejscie
            }

            for (int i = 0; i < tabOutput.length; i++) {
                tabOutput[i] = scanner.nextInt(); //wyjscie
            }

            //uczenie sieci
            while (true) {
                //obliczenia dla pierwszej powloki
                for (int i = 0; i < neurons[0].length; i++) {
                    neurons[0][i].setIn(tabInput[i]);
                    neurons[0][i].sumWagesInput();
                    tabInputHiddenSecondLayer[i] = neurons[0][i].activationFunction();
                }

                //obliczenia dla drugiej powloki
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


                //propagacja wsteczna na 2 powloke
                for (int i = 0; i < neurons[1].length; i++) {
                    for (int j = 0; j < neurons[2].length; j++) {
                        delta[1][i] += (delta[2][j]*neurons[2][j].getWages()[i]);
                    }
                }
                //propagacja wsteczna na 1 powloke
                for (int i = 0; i < neurons[0].length; i++) {
                    for (int j = 0; j < neurons[1].length; j++) {
                        delta[0][i] += (delta[1][j]*neurons[1][j].getWages()[i]);
                    }
                }

                //korekta wag 1 powloka
                for(int i=0;i<neurons[0].length;i++){
                    for(int j=0;j<neurons[0][0].getNumInput();j++) {
                        neurons[0][i].getWages()[j] += eta * delta[0][i] * neurons[0][i].pochodnaActivationFunction() * tabInput[j];
                    }
                }
                for (int i = 0; i < neurons[0].length; i++) {
                    neurons[0][i].setIn(tabInput[i]);
                    neurons[0][i].sumWagesInput();
                    tabInputHiddenSecondLayer[i] = neurons[0][i].activationFunction();
                }

                //korekta wag 2 powloka
                for(int i=0;i<neurons[1].length;i++){
                    for(int j=0;j<neurons[1][0].getNumInput();j++) {
                        neurons[1][i].getWages()[j] += eta * delta[1][i] * neurons[1][i].pochodnaActivationFunction() * tabInputHiddenSecondLayer[j];
                    }
                }
                for (int i = 0; i < neurons[0].length; i++) {
                    neurons[1][i].setInput(tabInputHiddenSecondLayer);
                    neurons[1][i].sumWagesInput();
                    tabInputHiddenThirdLayer[i] = neurons[1][i].activationFunction();
                }

                //korekta wag 3 powloka
                for(int i=0;i<neurons[2].length;i++){
                    for(int j=0;j<neurons[2][0].getNumInput();j++) {
                        neurons[2][i].getWages()[j] += eta * delta[2][i] * neurons[2][i].pochodnaActivationFunction() * tabInputHiddenThirdLayer[j];
                    }
                }
                for(int i=0;i<neurons[2].length;i++) {
                    neurons[2][i].setInput(tabInputHiddenThirdLayer);
                    neurons[2][i].sumWagesInput();
                    tabOutputNetwrok[i] = neurons[2][i].activationFunction();
                }

                //obliczanie bledu wyjscia sieci (3 powloka)
                for (int i = 0; i < tabOutput.length; i++) {

                    delta[2][i] = tabOutput[i] - tabOutputNetwrok[i];
                }

                for(int i=0;i<tabOutputNetwrok.length;i++){
                    del1+=delta[2][i];
                }
                del1/=tabOutputNetwrok.length;
                if(del1==del) {
                    del = 100;
                    break;
                }
                else del=del1;


                //for (int m = 0; m < neurons.length; m++) {
                //    for (int i = 0; i < neurons[m].length; i++) {
                //        for (int j = 0; j < neurons[m][i].getNumInput()+1; j++) {
                //            neurons[m][i].getWages()[j] += eta * delta[m][i] * neurons[m][i].pochodnaActivationFunction();
                //        }
                //    }
                //}
            }

        }
    }

    public void doMove(double tabIn[]){

        double[] tabInputHidden = new double[48];
        double[] tabOutputNetwrok = new double[9];
        double[] tabInputHiddenSecondLayer = new double[9];
        double[] tabInputHiddenThirdLayer = new double[48];

        //obliczenia dla pierwszej powloki
        for (int i = 0; i < neurons[0].length; i++) {
            neurons[0][i].setIn(tabIn[i]);
            neurons[0][i].sumWagesInput();
            tabInputHiddenSecondLayer[i] = neurons[0][i].activationFunction();
        }

        //obliczenia dla drugiej powloki
        for (int i = 0; i < neurons[1].length; i++) {
            neurons[1][i].setInput(tabInputHiddenSecondLayer);
            neurons[1][i].sumWagesInput();
            tabInputHiddenThirdLayer[i] = neurons[1][i].activationFunction();
        }

        //obliczenia dla trzeciej powloki
        for(int i=0;i<neurons[2].length;i++){
            neurons[2][i].setInput(tabInputHiddenThirdLayer);
            neurons[2][i].sumWagesInput();
            tabOutputNetwrok[i] = neurons[2][i].activationFunction();
        }

        for(int i=0;i<tabOutputNetwrok.length;i++) System.out.print(tabOutputNetwrok[i]+" ");
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
            for(int j=0;j<9;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[1][i].getWages()[j]);
            }
        }

        for(int i=0;i<neurons[2].length;i++){
            System.out.println("Powloka nr 3.");
            for(int j=0;j<48;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[2][i].getWages()[j]);
            }
        }
    }


    public static void main(String[] args){
        neuralNetwork network=new neuralNetwork();
        network.showWages();
        try {
            network.readData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            network.backPropagation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        network.showWages();
        double[] tab={-1,0,  0, 1,  0, 1, -1 , 0 , -1};
        network.doMove(tab);
    }


}
