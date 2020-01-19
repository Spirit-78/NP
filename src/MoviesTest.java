import java.util.*;
import java.util.stream.Collectors;

class Movie{
    String title;
    List<Integer> ratings;
    public Movie(String title, int [] ratings){
        this.title = title;
        this.ratings = Arrays.stream(ratings).boxed().collect(Collectors.toList());
    }
    double avgRating(){
        return ratings.stream().mapToDouble(i -> i).sum()/ratings.size();
    }
    int getSize(){return ratings.size();}
    String getTitle(){return title;}

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",title,avgRating(),ratings.size());
    }
}

class MoviesList{
    List<Movie> movies;
    Comparator<Movie> com = Comparator.comparing(Movie::avgRating).reversed().thenComparing(Movie::getTitle);
    public MoviesList(){
        movies = new ArrayList<>();
    }
    public void addMovie(String title, int [] ratings){
        movies.add(new Movie(title,ratings));
    }
    public List<Movie> top10ByAvgRating(){
        return movies.stream().sorted(com).limit(10).collect(Collectors.toList());
    }
    public List<Movie> top10ByRatingCoef(){
        int max = movies.stream().mapToInt(Movie::getSize).sum();
        Comparator<Movie> comparator = Comparator.comparing(movie ->-(movie.avgRating()*movie.getSize())/max);
        return movies.stream().sorted(comparator.thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }
}


public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde