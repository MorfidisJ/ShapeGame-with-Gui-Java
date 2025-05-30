public class Square extends Shape {

    public Square(int bounding_area){
        super(bounding_area);
        setType("Square");
    }

    public double computeArea(){
        
        return super.getBounding_area();

    }
}
