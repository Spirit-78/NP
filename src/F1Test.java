import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Racers implements Comparable<Racers>{
    private String name;
    private String bestLap;
    private ArrayList<String> time;
    public Racers(){
        time = new ArrayList<>();
    }
    Racers(String line){
        this.name = line.split(" ")[0];
        time = new ArrayList<>();
        time.add(line.split(" ")[1]);
        time.add(line.split(" ")[2]);
        time.add(line.split(" ")[3]);
        bestLap();
    }
    private String getName(){return name;}

    private void bestLap(){
        this.bestLap = time.stream().min(Comparator.naturalOrder()).get();
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", getName(), bestLap);
    }

    @Override
    public int compareTo(Racers o) {
        return this.bestLap.compareTo(o.bestLap);
    }
}

class F1Race {
    private ArrayList<Racers> racers;
    F1Race(){
    racers = new ArrayList<>();
    }

    void readResults(InputStream inputStream){
        Scanner in = new Scanner(inputStream);
        while(in.hasNextLine()){
            racers.add(new Racers(in.nextLine()));
        }
        in.close();
    }

    void printSorted(PrintStream outputStream){
        racers.sort(null);
        //System.out.println(racers.size());
        //IntStream.range(1,racers.size()+1).forEach(i -> outputStream.println(i + "." + racers.get(i-1)));
        outputStream.close();
    }

}