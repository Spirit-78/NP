import javafx.scene.shape.Arc;

import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;

class NonExistingItemException extends Exception {

    public NonExistingItemException(String message) {
        super(message);
    }
}

abstract class Archive{
    private int id;
    private Date dateArchived;
    public Archive(int id){
        this.id = id;
        this.dateArchived = null;
    }

    void setDateArchived(Date dateArchived){this.dateArchived = (Date) dateArchived.clone();}
    int getID(){return this.id;}
    abstract String open(Date date);
}

class LockedArchive extends Archive{
    private Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen){
        super(id); this.dateToOpen = (Date) dateToOpen.clone();
    }

    @Override
    String open(Date date) {
        if(date.before(dateToOpen))
            return "Item " + this.getID() + " cannot be opened before " + dateToOpen.toString() + "\n";
        else
            return "Item " + this.getID() + " opened at " + date + "\n";
    }
}

class SpecialArchive extends Archive{
    private int maxOpen;
    private int opened;
    public SpecialArchive(int id, int maxOpen){
        super(id); this.maxOpen = maxOpen;
        this.opened = 0;
    }

    @Override
    String open(Date date) {
        if(opened == maxOpen)
            return String.format("Item %d cannot be opened more than %d times\n", this.getID(),maxOpen);
        else {
            opened++;
            return String.format("Item %d opened at %s\n", this.getID(),date);
        }
    }
}

class ArchiveStore{
    private ArrayList<Archive> store;
    private StringBuilder message;

    public ArchiveStore(){
        store = new ArrayList<>();
        message = new StringBuilder();
    }

    void archiveItem(Archive item, Date date){
        item.setDateArchived(date);
        store.add(item);
        message.append(String.format("Item %d archived at %s\n", item.getID(),date.toString()));
    }

    void openItem(int id, Date date) throws NonExistingItemException {
        Optional<Archive> t = store.stream().filter(item -> item.getID() == id).findFirst();
        if(!t.isPresent())
            throw new NonExistingItemException(String.format("Item with id %d doesn't exist", id));
        message.append(t.get().open(date));
    }

    String getLog(){
        return message.toString().replaceAll("GMT","UTC");
    }
}


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}