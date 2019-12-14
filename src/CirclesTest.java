import com.sun.corba.se.spi.ior.ObjectKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception{
    public ObjectCanNotBeMovedException(int x, int y){super("Point (" + x + "," + y +") is out of bounds");}
}
class MovableObjectNotFittableException extends Exception{
    public MovableObjectNotFittableException(){super("This object can't be added");}
}


interface Movable{
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();

    boolean canFit(int x_max, int y_max);
    TYPE getType();
}

class MovingPoint implements Movable{
    int x,y;
    int xSpeed, ySpeed;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed){
        this.x = x; this.y = y;
        this.xSpeed = xSpeed; this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(y + ySpeed > MovablesCollection.y_MAX)
            throw new ObjectCanNotBeMovedException(x,y + ySpeed);
        else
            y += ySpeed;
    }
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(x - xSpeed < 0)
            throw new ObjectCanNotBeMovedException(x - xSpeed,y);
        else
            x -= xSpeed;
    }
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(x + xSpeed > MovablesCollection.x_MAX)
            throw new ObjectCanNotBeMovedException(x + xSpeed,y);
        else
            x += xSpeed;
    }
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(y - ySpeed < 0)
            throw new ObjectCanNotBeMovedException(x,y - ySpeed);
        else
            y -= ySpeed;
    }
    @Override
    public int getCurrentXPosition() { return x;}
    @Override
    public int getCurrentYPosition() { return y;}

    @Override
    public boolean canFit(int x_max, int y_max) {
        return x >= 0 && x <= x_max && y <= y_max && y >= 0;
    }

    @Override
    public String toString() {
        return "Movable point with coordinates (" + x + "," + y + ")\n";
    }

    public TYPE getType(){ return TYPE.POINT;}
}

class MovingCircle implements Movable{
    int radius;
    MovingPoint center;
    public MovingCircle(int radius, MovingPoint center)
    {
        this.radius = radius;
        this.center = center;
    }
    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(center.ySpeed + center.getCurrentYPosition() + radius > MovablesCollection.y_MAX)
            throw new ObjectCanNotBeMovedException(center.x, center.y + center.ySpeed + radius);
        else
            center.y += center.ySpeed;
    }
    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if(center.getCurrentXPosition() - center.xSpeed  - radius < 0)
            throw new ObjectCanNotBeMovedException(center.x = center.xSpeed - radius, center.y);
        else
            center.x -= center.xSpeed;
    }
    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if(center.getCurrentXPosition() + center.xSpeed + radius > MovablesCollection.x_MAX)
            throw new ObjectCanNotBeMovedException(center.x + center.xSpeed + radius, center.y);
        else
            center.x += center.xSpeed;
    }
    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if(center.y - center.ySpeed - radius < 0)
            throw new ObjectCanNotBeMovedException(center.x, center.y - center.ySpeed - radius);
        else
            center.y -= center.ySpeed;
    }
    @Override
    public int getCurrentXPosition() {
        return center.x;
    }
    @Override
    public int getCurrentYPosition() {
        return center.y;
    }

    @Override
    public boolean canFit(int x_max, int y_max) {
        if(!center.canFit(x_max,y_max))
            return false;
        if((center.x - radius < 0) || (center.x + radius > x_max) ||
           (center.y - radius < 0) || (center.y + radius > y_max))
            return false;
        return true;
    }

    public TYPE getType() { return TYPE.CIRCLE;}

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + center.x + "," + center.y + ") and radius "+ radius + "\n";
    }
}

class MovablesCollection{
    ArrayList<Movable> movable;
    static int x_MAX;
    static int y_MAX;

    public MovablesCollection(int x_MAX, int y_MAX){
        this.x_MAX = x_MAX; this.y_MAX = y_MAX;
        movable = new ArrayList<>();
    }

    public static void setxMax(int i) { MovablesCollection.x_MAX = i;
    }

    public static void setyMax(int i) { MovablesCollection.y_MAX = i;
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        if(!m.canFit(MovablesCollection.x_MAX,MovablesCollection.y_MAX))
            throw new MovableObjectNotFittableException();
        movable.add(m);
    }

    public void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) throws ObjectCanNotBeMovedException{
         List<Movable> tmp = movable.stream().filter(object -> object.getType().equals(type)).collect(Collectors.toList());
//        .forEach(object ->
//        {
//            switch (direction){
//                case UP: object.moveUp();
//                case DOWN: object.moveDown();
//                case LEFT: object.moveLeft();
//                case RIGHT: object.moveRight();
//            }
//        });
        for (Movable i : tmp) {
            if (direction == DIRECTION.UP) {
                i.moveUp();
            } else if (direction == DIRECTION.DOWN) {
                i.moveDown();
            } else if (direction == DIRECTION.RIGHT) {
                i.moveRight();
            } else if (direction == DIRECTION.LEFT) {
                i.moveLeft();
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size ");
        sb.append(movable.size()).append(":\n");
        movable.stream().forEach(object -> sb.append(object.toString()));
        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) throws MovableObjectNotFittableException, ObjectCanNotBeMovedException {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e){
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try{
                    collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e){
                    System.out.println("Movable circle with center (" + x + "," + y + ") " +
                            "and radius " + radius + " can not be fitted into the collection" );
                }
            }

        }
        System.out.println(collection.toString());
        System.out.println("MOVE POINTS TO THE LEFT");
        try {
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        } catch (ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());
        System.out.println("MOVE CIRCLES DOWN");
        try{
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        } catch (ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());
        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);
        System.out.println("MOVE POINTS TO THE RIGHT");
        try{
            collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);

        } catch (ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());
        System.out.println("MOVE CIRCLES UP");
        try{
            collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);

        } catch (ObjectCanNotBeMovedException e){
            System.out.println(e.getMessage());
        }
        System.out.println(collection.toString());



    }


}
