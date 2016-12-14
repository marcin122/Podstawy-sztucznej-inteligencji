import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class neuralNetwork {
    private Neuron[][] neurons;
    private double[][] delta;
    private double eta=0.3;
    private double[] wages;private double[] tabInputHiddenSecondLayer = {1,1,1,1,1,1,1,1,1,1};
    private double[] tabInputHiddenThirdLayer = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    private double[] tabOutputNetwrok = new double[9];
    private double[][] dataLearningInput;
    private double[][] dataLearningOutput;

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
            neurons[0][i].setBeta(1.5);
        }

        neurons[1]=new Neuron[48];
        for(int i=0;i<neurons[1].length;i++){
            neurons[1][i]=new Neuron(9);
            neurons[1][i].initWages();
            neurons[1][i].setBeta(0.3);
        }

        neurons[2]=new Neuron[9];
        for(int i=0;i<neurons[2].length;i++){
            neurons[2][i]=new Neuron(48);
            neurons[2][i].initWages();
            neurons[2][i].setBeta(0.0006);
        }

    }

    public int readData() throws FileNotFoundException {

        int n=0; //liczba rekordow

        String pathToFile = "C:\\Users\\PC\\Desktop\\psi\\Podstawy-sztucznej-inteligencji\\neuralNetwork\\src\\data.txt";
        File file = new File(pathToFile);
        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){
            n=scanner.nextInt();
            dataLearningInput=new double[n][10];
            dataLearningOutput=new double[n][9];

            for(int i=0;i<n;i++){
                for(int j=0;j<10;j++) dataLearningInput[i][j]=scanner.nextInt();
                for(int j=0;j<9;j++) dataLearningOutput[i][j]=scanner.nextInt();
            }

        }
        return n;
    }

    public void networkCompute(int m){
        //obliczenia dla pierwszej powloki
        for (int i = 0; i < neurons[0].length; i++) {
            neurons[0][i].setInput(dataLearningInput[m]);
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
    }

    public void deltaPropagation(int m){
        //blad na wyjsciu
        for (int i = 0; i < tabOutputNetwrok.length; i++) {
            delta[2][i] = dataLearningOutput[m][i] - tabOutputNetwrok[i];
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
    }

    public void backPropagation() throws FileNotFoundException {

        int recordNum=readData();
        double lastDelta=0, presentDelta=0;

            //uczenie sieci
            for(int m=0;m<recordNum;m++) {

                lastDelta=presentDelta;

                networkCompute(m);

                deltaPropagation(m);

                //korekta wag 1 powloka
                for(int i=0;i<neurons[0].length;i++){
                    wages=new double[neurons[0][i].getNumInput()+1];
                    for(int j=0;j<neurons[0][0].getNumInput()+1;j++) {
                        wages[j]=neurons[0][i].getWages()[j] + eta * delta[0][i] * neurons[0][i].pochodnaActivationFunction() * dataLearningInput[m][j];
                    }
                    neurons[0][i].setWages(wages);
                }

                //korekta wag 2 powloka
                for(int i=0;i<neurons[1].length;i++){
                    wages=new double[neurons[1][i].getNumInput()+1];
                    for(int j=0;j<neurons[1][0].getNumInput()+1;j++) {
                        wages[j]=neurons[1][i].getWages()[j] + eta * delta[1][i] * neurons[1][i].pochodnaActivationFunction() * tabInputHiddenSecondLayer[j];
                    }
                    neurons[1][i].setWages(wages);
                }

                //korekta wag 3 powloka
                for(int i=0;i<neurons[2].length;i++){
                    wages=new double[neurons[2][i].getNumInput()+1];
                    for(int j=0;j<neurons[2][0].getNumInput()+1;j++) {
                        wages[j]=neurons[2][i].getWages()[j] + eta * delta[2][i] * neurons[2][i].pochodnaActivationFunction() * tabInputHiddenThirdLayer[j];
                    }
                    neurons[2][i].setWages(wages);
                }

                presentDelta=0;
                for(int i=0;i<delta[2].length;i++){
                    presentDelta+=delta[2][i];
                }

                for(int i=0;i<delta[0].length;i++) delta[0][i]=0;
                for(int i=0;i<delta[1].length;i++) delta[1][i]=0;
                for(int i=0;i<delta[2].length;i++) delta[2][i]=0;
            }

            while(lastDelta>presentDelta){

                Random random=new Random();
                int n=random.nextInt(recordNum);
                lastDelta=presentDelta;

                networkCompute(n);

                deltaPropagation(n);

                //korekta wag 1 powloka
                for(int i=0;i<neurons[0].length;i++){
                    wages=new double[neurons[0][i].getNumInput()+1];
                    for(int j=0;j<neurons[0][0].getNumInput()+1;j++) {
                        wages[j]=neurons[0][i].getWages()[j] + eta * delta[0][i] * neurons[0][i].pochodnaActivationFunction() * dataLearningInput[n][j];
                    }
                    neurons[0][i].setWages(wages);
                }

                //korekta wag 2 powloka
                for(int i=0;i<neurons[1].length;i++){
                    wages=new double[neurons[1][i].getNumInput()+1];
                    for(int j=0;j<neurons[1][0].getNumInput()+1;j++) {
                        wages[j]=neurons[1][i].getWages()[j] + eta * delta[1][i] * neurons[1][i].pochodnaActivationFunction() * tabInputHiddenSecondLayer[j];
                    }
                    neurons[1][i].setWages(wages);
                }

                //korekta wag 3 powloka
                for(int i=0;i<neurons[2].length;i++){
                    wages=new double[neurons[2][i].getNumInput()+1];
                    for(int j=0;j<neurons[2][0].getNumInput()+1;j++) {
                        wages[j]=neurons[2][i].getWages()[j] + eta * delta[2][i] * neurons[2][i].pochodnaActivationFunction() * tabInputHiddenThirdLayer[j];
                    }
                    neurons[2][i].setWages(wages);
                }

                presentDelta=0;
                for(int i=0;i<delta[2].length;i++){
                    presentDelta+=delta[2][i];
                }
            }
    }

    public void doMove(double tabIn[]){

        //obliczenia dla pierwszej powloki
        for (int i = 0; i < neurons[0].length; i++) {
            neurons[0][i].setInput(tabIn);
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
        System.out.println();
        for(int i=0;i<tabOutputNetwrok.length;i++) System.out.print((int)tabOutputNetwrok[i]+" ");
        System.out.println();
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
        //network.showWages();

        try {
            network.backPropagation();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        network.showWages();
        double[] tab={1, -1,  1, -1,  1, -1,  0,  0,  0,1};
        network.doMove(tab);
    }
}
