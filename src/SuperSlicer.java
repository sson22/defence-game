import bagel.util.Point;
import java.util.List;

/**
 * SuperSlicer
 */
public class SuperSlicer extends Slicer {

    private static final String SUPER_SLICER_IMAGE_FILE = "res/images/superslicer.png";
    // Variables are made public static so that other Slicer could reference the value
    public static final int SUPER_SLICER_REWARD = 15;
    public static final double SUPER_SLICER_SPEED = (3.0/4) * RegSlicer.REG_SLICER_SPEED;
    public static final int SUPER_SLICER_HEALTH = RegSlicer.REG_SLICER_HEALTH;
    public static final int SUPER_SLICER_PENALTY = 2 * RegSlicer.REG_SLICER_PENALTY;


    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public SuperSlicer(List<Point> polyline) {
        super(polyline.get(0), polyline, SUPER_SLICER_IMAGE_FILE);
        speed = SUPER_SLICER_SPEED;
        health = SUPER_SLICER_HEALTH;
        reward = SUPER_SLICER_REWARD;
        penalty = SUPER_SLICER_PENALTY;
    }
    // Child Slicer created when parent were eliminated
    public SuperSlicer(Slicer parent){
        super(parent, SUPER_SLICER_IMAGE_FILE);
        speed = SUPER_SLICER_SPEED;
        health = SUPER_SLICER_HEALTH;
        reward = SUPER_SLICER_REWARD;
        penalty = SUPER_SLICER_PENALTY;
    }
}
