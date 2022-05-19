import bagel.util.Point;
import java.util.List;

/**
 * RegSlicer
 */
public class RegSlicer extends Slicer{

    private static final String SLICER_IMAGE_FILE = "res/images/slicer.png";
    // Variables are made public static so that other Slicer could reference the value
    public static final double REG_SLICER_SPEED = 2;
    public static final int REG_SLICER_HEALTH = 1;
    public static final int REG_SLICER_REWARD = 2;
    public static final int REG_SLICER_PENALTY = 1;



    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     *
     */

    public RegSlicer(List<Point> polyline) {
        super(polyline.get(0), polyline, SLICER_IMAGE_FILE);
        speed = REG_SLICER_SPEED;
        health = REG_SLICER_HEALTH;
        reward = REG_SLICER_REWARD;
        penalty = REG_SLICER_PENALTY;
    }
    // Child Slicer created when parent were eliminated
    public RegSlicer(Slicer parent){
        super(parent, SLICER_IMAGE_FILE);
        speed = REG_SLICER_SPEED;
        health = REG_SLICER_HEALTH;
        reward = REG_SLICER_REWARD;
        penalty = REG_SLICER_PENALTY;
    }

}
