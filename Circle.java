public class Circle extends Shape {
    
    public Circle(int bounding_area){
        super(bounding_area);
        setType("Circle");
    }

    public double computeArea(){
        
        return 3.14/4 * super.getBounding_area();

    }
}
