import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Song{
    String title;
    String artist;
    public Song(String title, String artist){
        this.title = title; this.artist = artist;
    }
    @Override
    public String toString() {
        return String.format("Song{title=%s, artist=%s}", title,artist);
    }
}
class MP3Player{
    List<Song> songs;
    int i, pom;
    boolean stop, play;
    public MP3Player(List<Song> songs){
        this.songs = songs;
        i = 0;
        pom = 10;
        stop = true; play = false;
    }
    void pressPlay(){
        if(play)
            System.out.println("Song is already playing");
        else {
            System.out.println("Song " + i + " is playing");
            play = true; stop =false;
            pom = 10;
        }
    }
    void pressStop(){
        if(stop && i == 0 && pom == -1)
           { System.out.println("Songs are already stopped");
           return;}
        if(stop){
            System.out.println("Songs are stopped");
            i = 0;
            pom = -1;
        }
        else {
            stop = true; play = false;
            System.out.println("Song " + i + " is paused");
        }
    }
    void pressREW(){
        System.out.println("Reward...");
        if(i == 0)
            i = songs.size() - 1;
        else
            i--;
        play = false;
        stop = true;
        pom = 10;
    }
    void pressFWD(){
        System.out.println("Forward...");
        if(i == songs.size()-1)
            i = 0;
        else
            i++;
        play = false;
        stop = true;
        pom = 10;
    }
    void printCurrentSong(){
        System.out.println(songs.get(i).toString());
    }
    String songsToString(){
        return songs.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "MP3Player{currentSong = " + i + ", songList = [" + songsToString() + "]}";
    }
}


public class PatternTest {
    public static void main(String[] args) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}
