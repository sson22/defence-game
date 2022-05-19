import bagel.util.Point;
import java.util.List;

/**
 * MegaSlicer
 */
public class MegaSlicer extends Slicer{

    private static final String MEGA_SLICER_IMAGE_FILE = "res/images/megaslicer.png";
    // Variables are made public static so that other Slicer could reference the value
    public static final int MEGA_SLICER_REWARD = 10;
    public static final double MEGA_SLICER_SPEED = SuperSlicer.SUPER_SLICER_SPEED;
    public static final int MEGA_SLICER_HEALTH = 2 * SuperSlicer.SUPER_SLICER_HEALTH;
    public static final int MEGA_SLICER_PENALTY = 2 * SuperSlicer.SUPER_SLICER_PENALTY;

    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public MegaSlicer(List<Point> polyline) {
        super(polyline.get(0), polyline, MEGA_SLICER_IMAGE_FILE);
        speed = MEGA_SLICER_SPEED;
        health = MEGA_SLICER_HEALTH;
        reward = MEGA_SLICER_REWARD;
        penalty = MEGA_SLICER_PENALTY;
    }
    // Child Slicer created when parent were eliminated
    public MegaSlicer(Slicer parent){
        super(parent, MEGA_SLICER_IMAGE_FILE);
        speed = MEGA_SLICER_SPEED;
        health = MEGA_SLICER_HEALTH;
        reward = MEGA_SLICER_REWARD;
        penalty = MEGA_SLICER_PENALTY;
    }
}
