import java.util.*;

class Participant{
    String city, code, name;
    int age;
    public Participant(String city, String code, String name, int age){
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }
    public String getCity() {
        return city;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code,name,age);
    }
}

class Audition{
    Map<String, Set<Participant>> participants;
    Map<String, Set<String>> codes;
    public Audition(){
        participants = new HashMap<>();
        codes = new HashMap<>();
    }
    void addParticpant(String city, String code, String name, int age){ //goddamn the spelling my dudes what happened here
        Comparator<Participant> com = Comparator.comparing(Participant::getName)
                .thenComparing(Participant::getAge).thenComparing(Participant::getCode);
        participants.computeIfAbsent(city, key -> new TreeSet<>(com));
        codes.computeIfAbsent(city, key-> new HashSet<>());
        if(!codes.get(city).contains(code))
        {
            codes.computeIfPresent(city, (k,v)->{
                v.add(code);
                return v;
            });
            participants.computeIfPresent(city, (k,v) ->{
                v.add(new Participant(city,code,name,age));
                return v;
            });

        }
    }
    void listByCity(String city){
        participants.get(city).forEach(System.out::println);
    }

}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}