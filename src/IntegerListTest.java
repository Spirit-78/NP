import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
class ArrayIndexOutOfBoundsException extends Exception{
    public ArrayIndexOutOfBoundsException(String message){
        super(message);
    }
}


class IntegerList{
    List<Integer> lista;
    public IntegerList(){
        lista = new LinkedList<>();
    }
    public IntegerList(Integer [] a){
        lista = new LinkedList<>();
        lista.addAll(Arrays.asList(a));
    }
    boolean check(int idx) throws ArrayIndexOutOfBoundsException {
        if(idx < 0 || idx >= lista.size())
            throw new ArrayIndexOutOfBoundsException("Out of Bounds");
        return true;
    }
    public void add(int el, int idx){
        if(idx > lista.size()){
            IntStream.range(0, idx - lista.size()).forEach(i ->lista.add(0));
            lista.add(el);
        }
        else if(idx == lista.size())
            lista.add(el);
        else
            lista.add(idx,el);
    }
    public int remove(int idx) throws ArrayIndexOutOfBoundsException {
        check(idx);
        return lista.remove(idx);
    }
    public void set(int el, int idx) throws ArrayIndexOutOfBoundsException {
        check(idx);
        lista.set(idx,el);
    }
    public int get(int idx) throws ArrayIndexOutOfBoundsException {
        check(idx);
        return lista.get(idx);
    }
    int size(){
        return lista.size();
    }
    int count(int el){
        int counter = 0;
        for(Integer item : lista)
            if(item == el)
                counter++;
        return counter;
    }
    public void removeDuplicates(){
        Collections.reverse(lista);
        LinkedList<Integer> temp = new LinkedList<>();
        lista.forEach(item ->{
            if(!temp.contains(item))
                temp.add(item);
        });
        Collections.reverse(temp);
        lista = temp;
    }
    int sumFirst(int k){
        return lista.stream().limit(k).flatMapToInt(IntStream::of).sum();
    }
    int sumLast(int k){
        return lista.stream().skip(lista.size() - k).flatMapToInt(IntStream::of).sum();
    }
    public void shiftRight(int idx, int k) throws ArrayIndexOutOfBoundsException {
        check(idx);
        int temp = lista.get(idx);
        lista.remove(idx);
        lista.add((k + idx)%(lista.size()+1),temp);

    }
        public void shiftLeft(int idx, int k) throws ArrayIndexOutOfBoundsException {
            check(idx);
            int helper2 = 0;
            if((idx - k)%lista.size() < 0)
                helper2 = lista.size();
            int temp = lista.get(idx);
            lista.remove(idx);
            lista.add((idx - k)%lista.size() + helper2,temp);

        }
        public IntegerList addValue(int val){
            return  new IntegerList(lista.stream().map(number -> number + val).collect(Collectors.toList()));
        }
        IntegerList(List<Integer> list){
        lista = new LinkedList<>();
        lista.addAll(list);
        }
}


public class IntegerListTest {

    public static void main(String[] args) throws ArrayIndexOutOfBoundsException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) { //test standard methods
            int subtest = jin.nextInt();
            if ( subtest == 0 ) {
                IntegerList list = new IntegerList();
                while ( true ) {
                    int num = jin.nextInt();
                    if ( num == 0 ) {
                        list.add(jin.nextInt(), jin.nextInt());
                    }
                    if ( num == 1 ) {
                        list.remove(jin.nextInt());
                    }
                    if ( num == 2 ) {
                        print(list);
                    }
                    if ( num == 3 ) {
                        break;
                    }
                }
            }
            if ( subtest == 1 ) {
                int n = jin.nextInt();
                Integer a[] = new Integer[n];
                for ( int i = 0 ; i < n ; ++i ) {
                    a[i] = jin.nextInt();
                }
                IntegerList list = new IntegerList(a);
                print(list);
            }
        }
        if ( k == 1 ) { //test count,remove duplicates, addValue
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    System.out.println(list.count(jin.nextInt()));
                }
                if ( num == 1 ) {
                    list.removeDuplicates();
                }
                if ( num == 2 ) {
                    print(list.addValue(jin.nextInt()));
                }
                if ( num == 3 ) {
                    list.add(jin.nextInt(), jin.nextInt());
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
        if ( k == 2 ) { //test shiftRight, shiftLeft, sumFirst , sumLast
            int n = jin.nextInt();
            Integer a[] = new Integer[n];
            for ( int i = 0 ; i < n ; ++i ) {
                a[i] = jin.nextInt();
            }
            IntegerList list = new IntegerList(a);
            while ( true ) {
                int num = jin.nextInt();
                if ( num == 0 ) { //count
                    list.shiftLeft(jin.nextInt(), jin.nextInt());
                }
                if ( num == 1 ) {
                    list.shiftRight(jin.nextInt(), jin.nextInt());
                }
                if ( num == 2 ) {
                    System.out.println(list.sumFirst(jin.nextInt()));
                }
                if ( num == 3 ) {
                    System.out.println(list.sumLast(jin.nextInt()));
                }
                if ( num == 4 ) {
                    print(list);
                }
                if ( num == 5 ) {
                    break;
                }
            }
        }
    }

    public static void print(IntegerList il) throws ArrayIndexOutOfBoundsException {
        if ( il.size() == 0 ) System.out.print("EMPTY");
        for ( int i = 0 ; i < il.size() ; ++i ) {
            if ( i > 0 ) System.out.print(" ");
            System.out.print(il.get(i));
        }
        System.out.println();
    }

}