import bagel.util.Point;
import java.util.List;

/**
 * ApexSlicer
 */
public class ApexSlicer extends Slicer{

    private static final String APEX_SLICER_IMAGE_FILE = "res/images/apexslicer.png";
    // Variables are made public static so that other Slicer could reference the value
    public static final double APEX_SLICER_SPEED = (1.0/2)*MegaSlicer.MEGA_SLICER_SPEED;
    public static final int APEX_SLICER_HEALTH = 25*RegSlicer.REG_SLICER_HEALTH;
    public static final int APEX_SLICER_REWARD = 25;
    public static final int APEX_SLICER_PENALTY = 4 *MegaSlicer.MEGA_SLICER_PENALTY;

    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public ApexSlicer(List<Point> polyline) {
        super(polyline.get(0), polyline, APEX_SLICER_IMAGE_FILE);
        speed = APEX_SLICER_SPEED;
        health = APEX_SLICER_HEALTH;
        reward = APEX_SLICER_REWARD;
        penalty = APEX_SLICER_PENALTY;
    }
    // Child Slicer created when parent were eliminated
    public ApexSlicer(Slicer parent){
        super(parent, APEX_SLICER_IMAGE_FILE);
        speed =APEX_SLICER_SPEED;
        health = APEX_SLICER_HEALTH;
        reward = APEX_SLICER_REWARD;
        penalty = APEX_SLICER_PENALTY;
    }
}
