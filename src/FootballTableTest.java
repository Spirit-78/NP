import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
class Team implements Comparable<Team>{
    String teamName;
    int wins, goals, even, lost, inGoals;
    public Team(String teamName){
        this.teamName = teamName;
        goals = wins = even = lost = inGoals = 0;
    }
    void setGoals(int goals){this.goals+=goals;}
    void setInGoals(int inGoals){this.inGoals+=inGoals;}
    void calc(int given, int taken){
        if(given > taken)
            wins++;
        else if(taken > given)
            lost++;
        else
            even++;
    }
    int points(){
        return wins*3 + even;
    }
    int played(){
        return wins + even + lost;
    }

    @Override
    public int compareTo(Team o) {
        if(this.points() > o.points())
            return 1;
        if(this.points() < o.points())
            return -1;
        else{
            if(this.goals - this.inGoals > o.goals - o.inGoals)
                return 1;
            if(this.goals - this.inGoals < o.goals - o.inGoals)
                return -1;
            else
                return this.teamName.compareTo(o.teamName);
        }
    }
    int ratio(){
        return goals - inGoals;
    }
    String getTeamName(){return teamName;}

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", this.teamName,this.played(),this.wins,this.even,this.lost,
                this.points());
    }
}

class FootballTable{
    Map<String,Team> matches;
    public FootballTable(){
        matches = new HashMap<>();
    }
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        matches.computeIfAbsent(homeTeam, key -> new Team(homeTeam));
        matches.computeIfAbsent(awayTeam, key -> new Team(awayTeam));
        matches.computeIfPresent(homeTeam,(k,v) ->{
         v.setGoals(homeGoals);
         v.setInGoals(awayGoals);
         v.calc(homeGoals,awayGoals);
         return v;
        });
        matches.computeIfPresent(awayTeam,(k,v) ->{
            v.setGoals(awayGoals);
            v.setInGoals(homeGoals);
            v.calc(awayGoals,homeGoals);
            return v;
        });
    }
    public void printTable(){
        List<Team> table = new ArrayList<>(this.matches.values());
        matches.values().stream()
                .sorted(Comparator.comparing(Team::points)
                        .thenComparing(Team::ratio).reversed().thenComparing(Team::getTeamName))
                .forEach(team -> System.out.printf("%2d. %s\n", counterFunc(), team));
    }
    public int i = 1;
    public int counterFunc(){
        return i++;
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

