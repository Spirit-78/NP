import java.util.*;

class InvalidPositionException extends Exception{
    public InvalidPositionException(int pos){
        super("Invalid position " + pos + ", alredy taken!"); //for real fix the spelling
    }
}

class Component implements Comparable<Component>{
    String color;
    int weight;
    Set<Component> collection;
    Comparator<Component> c = Comparator.comparing(Component::getWeight)
            //.reversed()
            .thenComparing(Component::getColor);
    public Component(String color, int weight){
        this.color = color;
        this.weight = weight;
        collection = new TreeSet<>(c);
    }
    void addComponent(Component component){
        collection.add(component);
    }
    int getWeight(){return weight;}
    String getColor(){return color;}

    @Override
    public int compareTo(Component o) {
        return c.compare(this, o);
    }

    public void changeColor(int weight, String color) {
        if(this.weight < weight)
            this.color = color;
        collection.forEach(component -> component.changeColor(weight,color));
    }
    public String format(String dashes){
        StringBuilder s = new StringBuilder(String.format("%s%d:%s\n", dashes, weight, color));
        for (Component component : collection) {
            s.append(component.format(dashes + "---"));
        }
        return s.toString();
    }
    public String toString() {
        return format("");
    }
}
class Window{
    String name;
    Map<Integer, Component> componentList;
    public Window(String name){
        this.name = name;
        componentList = new TreeMap<>();
    }
    void addComponent(int position, Component component) throws InvalidPositionException {
        if(componentList.containsKey(position))
            throw new InvalidPositionException(position);
        componentList.computeIfAbsent(position, key -> component);
    }
    void changeColor(int weight, String color){
        componentList.values().forEach(item -> item.changeColor(weight,color));
    }
    void swichComponents(int pos1, int pos2){  //madzarov fix yo spelling man
        Component tmp = componentList.get(pos1);
        componentList.put(pos1, componentList.get(pos2));
        componentList.put(pos2, tmp);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW ").append(name).append("\n");
        componentList.forEach((key, value) -> sb.append(key).append(":").append(value));
        return sb.toString();
    }
}

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде