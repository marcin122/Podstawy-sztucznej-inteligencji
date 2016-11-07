import java.util.Random;
import java.lang.Math;

public class Neuron {
    private int numInput;
    private double[] wages;
    private double output;
    private double[] input;
    private double sum=0;
    private double beta=0.5;

    public Neuron(int numInput) {
        this.numInput = numInput;
        wages=new double[numInput];
        input=new double[numInput];
        initWages();
    }

    public void initWages(){

        Random random=new Random();

        for(int i=0;i<numInput;i++){
            wages[i]=random.nextDouble();
        }
    }

    public void sumWagesInput(){

        for(int i=0;i<numInput;i++){
            sum+=wages[i]*input[i];
        }
    }

    public double activationFunction(){

        output=1/(1+Math.pow(Math.E,-beta*sum));

        return output;
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
}
