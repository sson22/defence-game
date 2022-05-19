import bagel.util.Point;
import java.util.List;

/**
 * Tower
 */
public abstract class Tower extends Sprite {
    protected int speed;

    /**
     * take new Tower information pass it to super
     * @param point
     * @param imageSrc
     */
    public Tower(Point point, String imageSrc){
        super(point, imageSrc);
    }

    /**
     * Attack slicers, and the method done differently depending on active or passive type
     * @param slicers
     */
    public abstract void attackEnemy(List<Slicer> slicers);


}
