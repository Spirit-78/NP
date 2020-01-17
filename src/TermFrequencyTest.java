import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

class TermFrequency{
    Map<String,Integer> terms;
    public TermFrequency(InputStream in, String [] stop){
        terms = new TreeMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.lines().forEach(line -> Arrays.stream(line.split("\\s+"))
                .forEach(word -> {
                    if(!Arrays.asList(stop).contains(fixedWord(word)) && !fixedWord(word).isEmpty())
                    {
                        terms.computeIfAbsent(fixedWord(word), key -> 0);
                        terms.computeIfPresent(fixedWord(word), (k,v) -> terms.get(k)+1);
                    }
                }));
    }
    int countTotal(){return terms.values().stream().mapToInt(i -> i).sum();}
    int countDistinct(){
        return (int) terms.keySet().stream().distinct().count();
    }
    List<String> mostOften(int k){
        return terms.keySet().stream()
                .sorted(Comparator.comparing(key -> terms.get(key))
                        .reversed())
                .limit(k)
                .collect(Collectors.toList());
    }
    String fixedWord(String word){
        return word.toLowerCase().replace('.','\0').replace(',','\0').trim();
    }
}

public class TermFrequencyTest {
    public static void main(String[] args) throws FileNotFoundException {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in,
                stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
