import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Perceptron {
    ArrayList<wektorUczacy> data=new ArrayList<>();
    private double psi=0.5;
    private double w1;
    private double w2;
    private double k;

    public void initiationImportance(){
        Random random=new Random();
        w1 = random.nextDouble();
        w2 = random.nextDouble();
    }

    /*public boolean activationFunction(double s){
        if(s<psi) return false;
        else return true;
    }*/

    public void readData(String name) throws FileNotFoundException {
        name = "C:\\Users\\PC\\Desktop\\Nowy folder\\src\\"+name+".txt";
        File file = new File(name);
        //System.out.println(name);
        Scanner scanner = new Scanner(file);
        data.clear();
        while (scanner.hasNext()) {
            double x1 = scanner.nextDouble();
            double x2 = scanner.nextDouble();
            double d = scanner.nextDouble();
            data.add(new wektorUczacy(x1, x2, d));
        }
        scanner.close();
    }

    public void learnPerceptron(String name) throws FileNotFoundException {
        readData(name);
        initiationImportance();
        for(wektorUczacy w:data){
            double s=w.getA()*w1+w.getB()*w2-psi;
            if(s>0){
                k=1;
                if(k!=w.getD()){
                    w1+=psi*w.getA()*(w.getD()-k);
                    w2+=psi*w.getB()*(w.getD()-k);
                }
            }
            else{
                k=0;
                if(k!=w.getD()){
                    w1+=psi*w.getA()*(w.getD()-k);
                    w2+=psi*w.getB()*(w.getD()-k);
                }
            }
        }
    }

    public void showImportance(){
        System.out.println("w1 = "+w1);
        System.out.println("w2 = "+w1);
    }
}
