public class Pentagon extends Shape{
    
    public Pentagon(int bounding_area){
        super(bounding_area);
        setType("Pentagon");
    }

    public double computeArea(){
        
        return 0.75 * super.getBounding_area();

    }
}
