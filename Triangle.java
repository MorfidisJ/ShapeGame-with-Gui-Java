public class Triangle extends Shape{

    public Triangle(int bounding_area){
        super(bounding_area);
        setType("Triangle");
    }

    public double computeArea(){
        
        return 0.5 * super.getBounding_area();
    }

}
