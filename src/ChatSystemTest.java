import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;
class NoSuchRoomException extends Exception{
    public NoSuchRoomException(String roomName){
        super(roomName);
    }
}
class NoSuchUserException extends Exception{
    public NoSuchUserException(String username){
        super(username);
    }
}

class ChatRoom{
    String roomName;
    Set<String> names;
    public ChatRoom(String roomName){
        this.roomName = roomName;
        names = new TreeSet<>();
    }
    void addUser(String username){
        names.add(username);
    }
    void removeUser(String username){
        names.remove(username);
    }

    @Override
    public String toString() {
       String emptyFlag = "";
       if(names.isEmpty())
           emptyFlag = "EMPTY";
        return roomName + "\n" + String.join("\n", names) + emptyFlag + "\n";
    }
    boolean hasUser(String username){
        return names.contains(username);
    }
    int numUsers(){ return names.size();}
}

class ChatSystem{
    Map<String, ChatRoom> rooms;
    Set<String> users;
    public ChatSystem(){
        rooms = new TreeMap<>();
        users = new TreeSet<>();
    }
    public void addRoom(String roomName){
        rooms.put(roomName, new ChatRoom(roomName));
    }
    public void removeRoom(String roomName){
        rooms.remove(roomName);
    }
    ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        return rooms.get(roomName);
    }
    void addUserToRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        checkDOMINANCE(username,roomName);
        users.add(username);
        rooms.get(roomName).addUser(username);
    }
    public void register(String username) throws NoSuchRoomException, NoSuchUserException {
       if(!rooms.isEmpty()) {
           String minRoom = rooms.values().stream()
                   .min(Comparator.comparing(ChatRoom::numUsers).thenComparing(room -> room.roomName))
                   .get().roomName;
           registerAndJoin(username, minRoom);
       }
        users.add(username);
    }
   public  void registerAndJoin(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        users.add(username);
        joinRoom(username,roomName);
    }
    void checkDOMINANCE(String username,String roomName) throws NoSuchRoomException, NoSuchUserException {
        if(!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if(!users.contains(username))
            throw new NoSuchUserException(username);
    }
   public  void joinRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        checkDOMINANCE(username,roomName);
        rooms.get(roomName).addUser(username);
    }
    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        checkDOMINANCE(username,roomName);
        rooms.get(roomName).removeUser(username);
    }
    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if(!users.contains(friend_username))
            throw new NoSuchUserException(friend_username);
        rooms.values().forEach(room ->{
            if(room.names.contains(friend_username)) {
                    room.addUser(username);
                }
        });
        //rooms.values().stream().filter(room -> room.hasUser(friend_username)).forEach(room -> room.addUser(username));
    }

}



public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}
