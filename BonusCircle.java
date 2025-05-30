public class BonusCircle extends Circle {
    
    double bonus_points;

    public BonusCircle(int area, double bonus){
        super(area);
        this.bonus_points = bonus;
    }

    @Override
    public String toString(){
        return getType() + ":" + String.valueOf(computeArea()) + String.format(" (Bonus: %s)", String.valueOf(bonus_points));
    }
}
