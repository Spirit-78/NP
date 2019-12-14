import java.nio.file.Watchable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Scanner;
import java.util.*;
import java.lang.*;
import java.util.stream.Collectors;

class WeatherMeasurement implements Comparable{
    private float temperature, wind, humidity, visibility;
    private Date date;

    public WeatherMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = new Date(date.getTime());
    }
    public WeatherMeasurement(){
        this.temperature = 0; this.humidity = 0;
        this.visibility = 0; this.wind = 0;
        this.date = null;
    }

    public Date getDate(){return date;}

    @Override
    public int compareTo(Object o) {
        WeatherMeasurement other = (WeatherMeasurement) o;
        if(this.date.after(other.getDate())) return 1;
        else if(this.date.before(other.getDate())) return -1;
        else  return 0;
    }

    @Override
    public String toString() {
        //return String.format("%f %f km/h %f  %f km %t", temperature, wind, humidity, visibility, date);
        return temperature + " " + wind + " km/h " + humidity + " % " + visibility + " km " + date.toString();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new WeatherMeasurement(this.temperature, this.wind, this.humidity,
                this.visibility, this.date);
    }

}

class WeatherStation{
    private int days;
    int num;
    ArrayList<WeatherMeasurement> wm = null;

    WeatherStation(int days) {
        this.days = days;
        this.num = 0;
        this.wm = new ArrayList<>();
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date){
        ArrayList<WeatherMeasurement> tmp = new ArrayList<>();
        wm.forEach(m->{
            long difference = Math.abs(date.getTime() - m.getDate().getTime());
            if((difference/1000) < 150)
                return;
        });
        wm.forEach(m -> {
            m.getDate().toInstant();
            LocalDateTime mData = LocalDateTime.ofInstant(m.getDate().toInstant(),ZoneId.of("GMT"));
            LocalDateTime newmData = LocalDateTime.ofInstant(date.toInstant(),ZoneId.of("GMT"));
            if(mData.plusDays(days).isAfter(newmData))
                tmp.add(m);
        });

        this.wm = tmp;
        this.wm.add(new WeatherMeasurement(temperature,wind,humidity,visibility,date));
        this.num = wm.size();
    }

    public int total(){ return this.wm.size();}

    public void status(Date from, Date to) throws CloneNotSupportedException {
        ArrayList<WeatherMeasurement> sorted = new ArrayList<>();
        for(WeatherMeasurement w : wm)
            sorted.add((WeatherMeasurement)w.clone());
        Collections.sort(sorted);
        for (WeatherMeasurement w : sorted)
            if(w.getDate().after(from) && w.getDate().before(to))
                System.out.println(w.toString());
    }
}



public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException | CloneNotSupportedException e) {
            System.out.println(e);
        }
    }
}