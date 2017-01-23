import java.util.Random;
import java.lang.Math;

public class Neuron {
    private int numInput;
    private double[] wages;
    private double output;
    private double[] input;
    private double sum=0;
    private double beta;

    public Neuron(int numInput) {
        this.numInput = numInput;
        wages=new double[numInput+1];
        input=new double[numInput+1];
        initWages();
    }

    public void initWages(){

        Random random=new Random();

        for(int i=0;i<numInput+1;i++){
            wages[i]=random.nextDouble();
        }
    }

    public void sumWagesInput(){
        sum=0;
        for(int i=0;i<=numInput;i++){
            sum+=wages[i]*input[i];
        }
    }

    public double activationFunction(){

        //output=1/(1+Math.pow(Math.E,-beta*sum));

        //funckja sigmoidalna bipolarna
        output=(2/(1+Math.pow(Math.E,-beta*sum)))-1;

        return output;
    }

    public double pochodnaActivationFunction(){
        double i=(2*beta*Math.pow(Math.E,-beta*sum))/(Math.pow((1+Math.pow(Math.E,-beta*sum)),2));
        return i;
    }

    public int getNumInput() {
        return numInput;
    }

    public double[] getWages() {
        return wages;
    }

    public double getOutput() {
        return output;
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;
    }

    public void setWages(double[] wages) {
        this.wages = wages;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getSum() {
        return sum;
    }
}
