import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ShapeGenerator{

    private ArrayList<Shape> shapes;
    private int[] bounding_area_values = {1,2,3,4,8,12,16};

    Random random = new Random();

    public ShapeGenerator(int number){
        
        initializeShapes(number);
        shuffleShapes();
        
    }
    
    public void initializeShapes(int number){

        shapes = new ArrayList<>();

        for (int i = 0; i < number; i++){
            
            int index = random.nextInt(bounding_area_values.length);
            
            Square square = new Square(bounding_area_values[index]);
            shapes.add(square);
        }

        for (int i = 0; i < number; i++){
            
            int index = random.nextInt(bounding_area_values.length);
            
            Triangle triangle = new Triangle(bounding_area_values[index]);
            shapes.add(triangle);
        }

        for (int i = 0; i < number; i++){
            
            int index = random.nextInt(bounding_area_values.length);
            int bonus = random.nextInt(11);

            BonusCircle circle = new BonusCircle(bounding_area_values[index], bonus);
            shapes.add(circle); 
        }

        for (int i = 0; i < number; i++){
            
            int index = random.nextInt(bounding_area_values.length);

            Circle circle = new Circle(bounding_area_values[index]);
            shapes.add(circle); 
        }

        for (int i = 0; i < number; i ++){

            int index = random.nextInt(bounding_area_values.length);
            System.out.println(bounding_area_values[index]);
            Pentagon pentagon = new Pentagon(bounding_area_values[index]);
            shapes.add(pentagon);
        }

    }

    public void shuffleShapes(){

        Collections.shuffle(shapes);

    }

    public Shape nextShape(){

        int index = random.nextInt(shapes.size());

        return shapes.remove(index);

    }

    public boolean hasShapes(){
        return !shapes.isEmpty();
    }

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder();

    for (Shape shape:shapes){
        str.append(shape.toString() + "\n");
    }        

        return str.toString();
    }

    public static void main(String[] args) {

        ShapeGenerator sp = new ShapeGenerator(2);

        while (sp.hasShapes()) {
            
            System.out.println(sp.toString());
            System.out.println("Next shape " + sp.nextShape().toString());
            System.out.println();
        }
    }
}
