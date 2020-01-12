import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SuperString{
    List<String> lista;
    List<String> memory;
    public SuperString(){
        lista = new LinkedList<>();
        memory = new LinkedList<>();
    }
    public void addToMemory(String s){
        memory.add(0,s);


    }
    public void append(String s){
        lista.add(s);
        addToMemory(s);
    }
    public void insert(String s){
        lista.add(0,s);
        addToMemory(s);
    }
    public boolean contains(String s){
        String temp = String.join("", lista);
        return temp.contains(s);
    }
    public void reverse(){
        lista = lista.stream().map(string -> {
            StringBuilder sb = new StringBuilder(string);
            return sb.reverse().toString();
        }).collect(Collectors.toList());
        Collections.reverse(lista);
    }

    @Override
    public String toString() {
        return String.join("",lista);
    }
    public void removeLast(int k){
        memory.stream().limit(k).forEach(string ->{
            lista.remove(string);
            StringBuilder sb = new StringBuilder(string);
            lista.remove(sb.reverse().toString());
        });
        int i = 0;
        while(!memory.isEmpty() && i<k) {
            memory.remove(0);
            i++;
        }
    }
}


public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
