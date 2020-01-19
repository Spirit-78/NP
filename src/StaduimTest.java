import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class SeatTakenException extends Exception{
    public SeatTakenException(){
        super();
    }
}
class SeatNotAllowedException extends Exception{
    public SeatNotAllowedException(){
        super();
    }
}

class Sector{
    String code;
    int seats;
    Map<Integer, Integer> taken;
    public Sector(String code, int seats){
        this.code = code; this.seats = seats;
        taken = new HashMap<>();
    }

    public String getCode() {
        return code;
    }
    public int getSeats() {
        return seats;
    }
    public int free(){
        return seats-taken.size();
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",code,free(),seats,
                (double)taken.size()/(seats/100));
    }
}
class Stadium{
    String name;
    Map<String,Sector> sectorMap;
    Comparator<Sector> com = Comparator.comparing(Sector::free).reversed().thenComparing(Sector::getCode);
    public Stadium(String name){
        this.name = name;
        sectorMap = new HashMap<>();
    }
    public void createSectors(String [] sectorNames, int [] sizes){
        for(int i=0;i<sizes.length;i++)
            sectorMap.put(sectorNames[i], new Sector(sectorNames[i],sizes[i]));
    }
    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if(sectorMap.get(sectorName).taken.containsKey(seat))
            throw new SeatTakenException();
        if(type!=0)
            if(sectorMap.get(sectorName).taken.values().stream().anyMatch(i -> i > 0 && i !=type))
                throw new SeatNotAllowedException();
        sectorMap.get(sectorName).taken.put(seat,type);
    }
    public void showSectors(){
        sectorMap.values().stream().sorted(com).forEach(System.out::println);
    }

}


public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
