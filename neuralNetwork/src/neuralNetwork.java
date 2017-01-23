import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class neuralNetwork {
    private Neuron[][] neurons;
    private double[][] delta;
    private double eta=0.6;
    private double[] wages;
    private double[] tabInputThirdLayer = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    private double[] tabInputSecondLayer = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    private double[] tabOutputNetwrok = new double[9];
    private double[][] dataLearningInput;
    private double[][] dataLearningOutput;
    private boolean[] TicTacToeProtagonistBoardChecklist=new boolean[19683];

    public neuralNetwork() {

        //tablice bledow
        delta=new double[3][];
        delta[0]=new double[48];
        delta[1]=new double[21];
        delta[2]=new double[9];

        for(int i=0;i<delta[0].length;i++) delta[0][i]=0;
        for(int i=0;i<delta[1].length;i++) delta[1][i]=0;
        for(int i=0;i<delta[2].length;i++) delta[2][i]=0;


        neurons=new Neuron[3][];

        neurons[0]=new Neuron[48];
        for(int i=0;i<neurons[0].length;i++){
            neurons[0][i]=new Neuron(9);
            neurons[0][i].setBeta(0.5);
        }

        neurons[1]=new Neuron[21];
        for(int i=0;i<neurons[1].length;i++){
            neurons[1][i]=new Neuron(48);
            neurons[1][i].setBeta(0.5);
        }

        neurons[2]=new Neuron[9];
        for(int i=0;i<neurons[2].length;i++){
            neurons[2][i]=new Neuron(21);
            neurons[2][i].setBeta(0.5);
        }

    }

    public int readData() throws FileNotFoundException {

        int n=0; //liczba rekordow

        String pathToFile = "C:\\Users\\PC\\Desktop\\psi\\Podstawy-sztucznej-inteligencji\\neuralNetwork\\src\\ww.txt";
        File file = new File(pathToFile);
        Scanner scanner = new Scanner(file).useLocale(Locale.US);

        while(scanner.hasNext()){
            n=scanner.nextInt();
            dataLearningInput=new double[n][10];
            dataLearningOutput=new double[n][9];

            for(int i=0;i<n;i++){
                for(int j=0;j<9;j++) {
                    dataLearningInput[i][j]=scanner.nextDouble();
                }
                dataLearningInput[i][9]=1;
                for(int j=0;j<9;j++) dataLearningOutput[i][j]=scanner.nextDouble();
            }
        }
        return n;
    }

    public void networkCompute(int m){
        //obliczenia dla pierwszej powloki
        for (int i = 0; i < neurons[0].length; i++) {
            neurons[0][i].setInput(dataLearningInput[m]);
            neurons[0][i].sumWagesInput();
            tabInputSecondLayer[i] = neurons[0][i].activationFunction();
        }

        //obliczenia dla drugiej powloki
        for(int i=0;i<neurons[1].length;i++){
            neurons[1][i].setInput(tabInputSecondLayer);
            neurons[1][i].sumWagesInput();
            tabInputThirdLayer[i] = neurons[1][i].activationFunction();
        }

        //obliczenia dla trzeciej powloki
        for (int i = 0; i < neurons[2].length; i++) {
            neurons[2][i].setInput(tabInputThirdLayer);
            neurons[2][i].sumWagesInput();
            tabOutputNetwrok[i] = neurons[2][i].activationFunction();
        }
    }

    public void deltaPropagation(int m){
        //blad na wyjsciu 3 powloka
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

    public void sumDelta(){
        double sum=0;
        for(int i=0;i<9;i++){
            sum+=delta[2][i];
        }
        System.out.println(sum);
    }

    public void backPropagation() throws FileNotFoundException {

        int recordNum=readData();
        double lastDelta=0, presentDelta=0;

            //uczenie sieci
            for(int m=0;m<recordNum;m++) {

                for(int k=0;k<500;k++) {
                    lastDelta = presentDelta;

                    networkCompute(m);

                    deltaPropagation(m);

                    changeWages(m);

                    presentDelta = 0;
                    for (int i = 0; i < delta[2].length; i++) {
                        presentDelta += delta[2][i];
                    }

                    for (int i = 0; i < delta[0].length; i++) delta[0][i] = 0;
                    for (int i = 0; i < delta[1].length; i++) delta[1][i] = 0;
                    for (int i = 0; i < delta[2].length; i++) delta[2][i] = 0;


                    deltaPropagation(m);
                    sumDelta();
                    for (int i = 0; i < delta[0].length; i++) delta[0][i] = 0;
                    for (int i = 0; i < delta[1].length; i++) delta[1][i] = 0;
                    for (int i = 0; i < delta[2].length; i++) delta[2][i] = 0;
                }
            }

           /*while(lastDelta>presentDelta){

                Random random=new Random();
                int n=random.nextInt(recordNum);
                lastDelta=presentDelta;

                networkCompute(n);

                deltaPropagation(n);

               changeWages(n);

                presentDelta=0;
                for(int i=0;i<delta[1].length;i++){
                    presentDelta+=delta[1][i];
                }
            }*/
        saveWages();
    }

    void changeWages(int m){
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
                wages[j]=neurons[1][i].getWages()[j] + eta * delta[1][i] * neurons[1][i].pochodnaActivationFunction() * tabInputSecondLayer[j];
            }
            neurons[1][i].setWages(wages);
        }

        //korekta wag 3 powloka
        for(int i=0;i<neurons[2].length;i++){
            wages=new double[neurons[2][i].getNumInput()+1];
            for(int j=0;j<neurons[2][0].getNumInput()+1;j++) {
                wages[j]=neurons[2][i].getWages()[j] + eta * delta[2][i] * neurons[2][i].pochodnaActivationFunction() * tabInputThirdLayer[j];
            }
            neurons[2][i].setWages(wages);
        }
    }

    void saveWages(){
        String pathFile="C:\\Users\\PC\\Desktop\\marcin\\psi\\Podstawy-sztucznej-inteligencji\\neuralNetwork\\src\\w.txt";
        try {
            PrintWriter writer=new PrintWriter(pathFile,"UTF-8");

            for(int i=0;i<neurons[0].length;i++){
                for(int j=0;j<=neurons[0][i].getNumInput();j++)
                    writer.println(neurons[0][i].getWages()[j]);
            }

            for(int i=0;i<neurons[1].length;i++){
                for(int j=0;j<=neurons[1][i].getNumInput();j++)
                    writer.println(neurons[1][i].getWages()[j]);
            }

            for(int i=0;i<neurons[2].length;i++){
                for(int j=0;j<=neurons[2][i].getNumInput();j++)
                    writer.println(neurons[2][i].getWages()[j]);
            }

            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void readWages(){
        String pathFile="C:\\Users\\PC\\Desktop\\psi\\Podstawy-sztucznej-inteligencji\\neuralNetwork\\src\\w.txt";
        File file=new File(pathFile);
        double[] w1=new double[10];
        double[] w2=new double[49];
        double[] w3=new double[22];
        int i=0,j=0;

        try {
            Scanner scanner=new Scanner(file).useLocale(Locale.US);
                while (i < 48) {
                    while (j < 10) {
                        w1[j] = scanner.nextDouble();
                        System.out.println(w1[j]);
                        j++;
                    }
                    j = 0;
                    neurons[0][i].setWages(w1);
                    w1=new double[10];
                    i++;
                }
                i = 0;
                while (i < 21) {
                    while (j < 49) {
                        w2[j] = scanner.nextDouble();
                        j++;
                    }
                    j = 0;
                    neurons[1][i].setWages(w2);
                    w2=new double[49];
                    i++;
                }
                i = 0;
                while (i < 9) {
                    while (j < 22) {
                         w3[j] = scanner.nextDouble();
                         j++;
                    }
                    j = 0;
                    neurons[2][i].setWages(w3);
                    w3=new double[22];
                    i++;
                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doMove(double tabIn[]){

        //obliczenia dla pierwszej powloki
        for (int i = 0; i < neurons[0].length; i++) {
            neurons[0][i].setInput(tabIn);
            neurons[0][i].sumWagesInput();
            tabInputSecondLayer[i] = neurons[0][i].activationFunction();
        }

        //obliczenia dla drugiej powloki
        for(int i=0;i<neurons[1].length;i++){
            neurons[1][i].setInput(tabInputSecondLayer);
            neurons[1][i].sumWagesInput();
            tabInputThirdLayer[i] = neurons[1][i].activationFunction();
        }

        //obliczenia dla trzeciej powloki
        for (int i = 0; i < neurons[2].length; i++) {
            neurons[2][i].setInput(tabInputThirdLayer);
            neurons[2][i].sumWagesInput();
            tabOutputNetwrok[i] = neurons[2][i].activationFunction();
        }

        /*double out;
        int n=0;
        out=tabOutputNetwrok[0];
        for(int i=0;i<tabOutputNetwrok.length;i++) {
            if(out<tabOutputNetwrok[i]) {
                out=tabOutputNetwrok[i];
                n=i;
            }
        }
        tabOutputNetwrok[n]=1;
        for(int i=0;i<tabOutputNetwrok.length;i++) System.out.print((int)tabOutputNetwrok[i]+" ");
        System.out.println();
        */

        double[] tabOutput;
        double out;
        int index=0;
        boolean end=true;

        tabOutput=tabOutputNetwrok;
        out=tabOutput[0];

        while(end) {
            for (int i = 0; i < tabOutput.length; i++) {
                if (out < tabOutput[i]) {
                    out = tabOutput[i];
                    index = i;
                }
            }
            if(tabIn[index]==0) {
                tabOutput[index]=1;
                end=false;
            }
            else {
                tabOutput[index]=0;
                out=0;
            }
        }
        for(int i=0;i<tabOutput.length;i++) System.out.print(tabOutput[i]+" ");
        System.out.println();
        for(int i=0;i<tabOutput.length;i++) System.out.print((int)tabOutput[i]+" ");
        System.out.println();
    }

    public void showWages(){
        for(int i=0;i<neurons[0].length;i++){
            System.out.println("Powloka nr 1.");
            for(int j=0;j<10;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[0][i].getWages()[j]);
            }
        }

        for(int i=0;i<neurons[1].length;i++){
            System.out.println("Powloka nr 2.");
            for(int j=0;j<48;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[1][i].getWages()[j]);
            }
        }

        for(int i=0;i<neurons[2].length;i++){
            System.out.println("Powloka nr 3.");
            for(int j=0;j<21;j++){
                System.out.print("w"+i+j+" : ");
                System.out.println(neurons[2][i].getWages()[j]);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        neuralNetwork network=new neuralNetwork();

        network.backPropagation();
        network.saveWages();

        network.showWages();

        double[] tab=new double[]{0, 0,  0, 0,  0,  0, 0,  0, 0,1};
        network.doMove(tab);
    }
}
