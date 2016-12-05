import java.util.Random;
import java.lang.Math;

public class Neuron {
    private int numInput;
    private double[] wages;
    private double output;
    private double[] input;
    private double in;
    private double sum=0;
    private double beta=0.6;

    public Neuron(int numInput) {
        this.numInput = numInput;
        if(numInput==1){
            wages=new double[numInput+1];
            initWages();
        }
        else{
            wages=new double[numInput+1];
            input=new double[numInput];
            initWages();
        }
    }

    public void initWages(){

        Random random=new Random();

        for(int i=0;i<numInput+1;i++){
            wages[i]=random.nextDouble();
        }
    }

    public void sumWagesInput(){
        if(numInput==1){
            sum+=wages[0]*in+wages[1];
        }
        else{
            for(int i=0;i<numInput;i++){
                sum+=wages[i]*input[i];
                if(i==numInput-1) sum+=wages[i+1];
            }
        }
    }

    public double activationFunction(){

        output=1/(1+Math.pow(Math.E,-beta*sum));

        return output;
    }

    public double pochodnaActivationFunction(){
        return (beta*Math.pow(Math.E,-beta*sum))/(Math.pow((1+Math.pow(Math.E,-beta*sum)),2));
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

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }
}
