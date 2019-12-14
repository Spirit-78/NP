import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidTimeException extends Exception{
    InvalidTimeException(String message){
        super(message);
    }
}

class UnsupportedFormatException extends Exception{
    UnsupportedFormatException(String message){
        super(message);
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class TimeTable{
        List<String> time;
    TimeTable(){
        time = new ArrayList<>();
    }

    public void writeTimes(PrintStream out, TimeFormat format24) {
        switch (format24){
            case FORMAT_24:
                time.forEach(out::println);
            case FORMAT_AMPM:{
                AMPM();
                time.forEach(out::println);
            }

        }

    }
    private void AMPM(){
        ArrayList<String> temp = new ArrayList<>();
        time.forEach(t ->{
            String [] times = (t.contains("."))? t.split(":") : t.split("\\.");
            if(Integer.parseInt(times[0]) == 0)
                temp.add("12:" + times[1] + " AM");
            if(Integer.parseInt(times[0]) >= 1 && Integer.parseInt(times[0]) <= 11)
                temp.add(times[0] + ":" + times[1] + " AM");
            if(Integer.parseInt(times[0]) == 12)
                temp.add("12:" + times[1] + " PM");
            if(Integer.parseInt(times[0]) >= 13 && Integer.parseInt(times[0]) <= 23)
                temp.add(Integer.parseInt(times[0])-12 + ":" + times[1] + " PM");

        });
        time = temp;
    }

    private void orderList(){
        ArrayList<String> temp = new ArrayList<>();
        this.time.forEach(t -> {
            temp.add(t.replace('.',':'));
        });
        time = temp;
        this.time.sort(Comparator.naturalOrder());
    }

    public void readTimes(InputStream in) throws UnsupportedFormatException, InvalidTimeException {
        Scanner br = new Scanner(in);
        while(br.hasNext()){
            String [] line = br.nextLine().split("\\s+");
            for(String s : line)
                {
                    if( !s.contains(":") && !s.contains("."))
                        throw new UnsupportedFormatException(s);
                     else if(s.contains(":") &&
                            (Integer.parseInt(s.split(":")[0]) > 23 ||
                            Integer.parseInt(s.split(":")[0]) < 0 ||
                            Integer.parseInt(s.split(":")[1]) < 0 ||
                            Integer.parseInt(s.split(":")[1]) > 59))
                                throw new InvalidTimeException(s);
                     else if(s.contains(".") &&
                            (Integer.parseInt(s.split("\\.")[1]) > 59 ||
                            Integer.parseInt(s.split("\\.")[1]) < 0 ||
                            Integer.parseInt(s.split("\\.")[0]) < 0 ||
                            Integer.parseInt(s.split("\\.")[0]) > 23))
                        throw new InvalidTimeException(s);
                     else
                        time.add(s);

                }
        }
        br.close();
        orderList();
    }
}