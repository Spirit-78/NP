import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }

    public static void findAll(InputStream inputStream) {
        Map<String, TreeSet<String>> map = new TreeMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.lines().forEach(word ->{
            char [] sort = word.toCharArray();
            Arrays.sort(sort);
            StringBuilder sb = new StringBuilder(String.valueOf(sort));
            map.computeIfAbsent(sb.toString(),key -> new TreeSet<>());
            map.get(sb.toString()).add(word);
        });
        map.values().stream().filter(set -> set.size() >= 5).sorted(Comparator.comparing(set -> set.first()))
                .forEach(set -> System.out.println(set.stream().collect(Collectors.joining(" "))));
    }
}
