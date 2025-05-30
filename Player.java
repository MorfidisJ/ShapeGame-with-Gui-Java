import java.util.Stack;

public class Player {
    private Stack<Shape> playerShapes;
    private double playerPoints;
    private int size;

    public Player(int size) {
        this.playerShapes = new Stack<>();
        this.playerPoints = 0;
        this.size = size;
    }

    public double getPoints() {
        return this.playerPoints;
    }

    public boolean acceptShape(Shape shape) {
        if (!isStackFull()) {
            playerShapes.add(shape);
            this.playerPoints += shape.computePoints();
            return true;
        }
        return false;
    }
    
    public boolean isStackFull() {
        return this.playerShapes.size() == this.size;
    }

    public Stack<Shape> getPlayerShapes() {
        return this.playerShapes;
    }
}
