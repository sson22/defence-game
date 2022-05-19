import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/**
 * A Slicer.
 */
public class Slicer extends Sprite {

    // Variables are made protected so that other subclasses could reference the variables
    protected double speed;
    protected int health;
    protected int reward;
    protected int penalty;
    protected final List<Point> polyline;
    protected int targetPointIndex;
    protected boolean finished;


    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */

    // Create new Slicer at the beginning of the polyline
    public Slicer(Point point, List<Point> polyline, String imageSrc) {
        super(point, imageSrc);
        this.polyline = polyline;
        this.finished = false;
        this.targetPointIndex = 1;
    }

    //Create new Slicer at location of parent slicer and given image
    public Slicer(Slicer parent, String imageSrc) {
        super(parent.getCenter(), imageSrc);
        this.polyline = parent.polyline;
        this.finished = false;
        this.targetPointIndex = parent.targetPointIndex;
    }

    /**
     * Updates the current state of the slicer. The slicer moves towards its next target point in
     * the polyline at its specified movement rate.
     */
    @Override
    public void update(Input input) {
        if (finished) {
            return;
        }
        // Obtain where we currently are, and where we want to be
        Point currentPoint = getCenter();
        Point targetPoint = polyline.get(targetPointIndex);
        // Convert them to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Distance we are (in pixels) away from our target point
        double magnitude = distance.length();
        // Check if we are close to the target point
        if (magnitude < speed * ShadowDefend.getTimescale()) {
            // Check if we have reached the end
            if (targetPointIndex == polyline.size() - 1) {
                finished = true;
                return;
            } else {
                // Make our focus the next point in the polyline
                targetPointIndex += 1;
            }
        }
        // Move towards the target point
        // We do this by getting a unit vector in the direction of our target, and multiplying it
        // by the speed of the slicer (accounting for the timescale)
        super.move(distance.normalised().mul(speed * ShadowDefend.getTimescale()));
        // Update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
        super.update(input);
    }

    public boolean isFinished() {
        return finished;
    }
    public int getPenalty(){
        return penalty;
    }
    public int getReward(){
        return reward;
    }
    public boolean healthReachedZero(){
        if(this.health <= 0){
            return true;
        }
        return false;
    }

    public void damage(int enemyDamage){
        this.health -= enemyDamage;
    }
}
