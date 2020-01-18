import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

class WrongDateException extends Exception{
    public WrongDateException(LocalDateTime time){
        super("Wrong date: " + Date.from(time.atZone(ZoneId.systemDefault()).toInstant()).toString().replace("GMT","UTC"));
    }
}

class Event implements Comparable<Event> {
    String name,location;
    LocalDateTime time;
    public static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMM, YYY HH:mm", Locale.US);
    public Event(String name, String location, LocalDateTime time){
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("%s at %s, %s", time.format(format), location, name);
    }

    @Override
    public int compareTo(Event o) {
        if(this.time.compareTo(o.time) == 0)
            return this.name.compareTo(o.name);
        return this.time.compareTo(o.time);
    }
}

class EventCalendar{
    int year;
    Map<LocalDate, TreeSet<Event>> dates;
    Map<Integer,Integer> months;
    public EventCalendar(int year){
        this.year = year;
        dates = new HashMap<>();
        months = new HashMap<>();
        for(int i=1; i<=12;i++)
            months.put(i,0);
    }
    int getYear(){return year;}
    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if(date.getYear() != this.year)
            throw new WrongDateException(date);
        LocalDate key = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        dates.computeIfAbsent(key, k-> new TreeSet<>());
        dates.computeIfPresent(key,(k,v) -> {
            v.add(new Event(name,location,date));
            return v;
        });
        months.putIfAbsent(date.getMonthValue(), 0);
        months.computeIfPresent(date.getMonthValue(),(k,v) -> v + 1);
    }
    void listEvents(LocalDateTime date){
        LocalDate key = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        if(dates.get(key) == null)
                System.out.println("No events on this day!");
        else
            dates.get(key).forEach(System.out::println);
    }
    void listByMonth(){
        months.keySet().forEach(key -> System.out.printf("%s : %s\n", key, months.get(key)));
    }
}


public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime date = LocalDateTime.parse(parts[2],df);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        LocalDateTime date = LocalDateTime.parse(scanner.nextLine(),df);
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde