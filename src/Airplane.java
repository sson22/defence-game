import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Airplane
 */
public class Airplane extends Tower {

    private static final String AIRPLANE_IMAGE_FILE = "res/images/airsupport.png";
    private static final int AIRPLANE_SPEED = 3;
    private static final int EXPLOSIVE_DAMAGE = 500;
    // isHorizontal is made public so that the player could read the information to set Airplane when it's purchased
    public static boolean isHorizontal = true;
    private boolean direction;
    private int airplaneSpeed;
    private int dropPeriod;
    private boolean isOnDropPeriod;
    private int damage;
    private boolean finished;
    private int time;
    private List<Explosive> explosives;

    /**
     * Create new Airplane
     * @param point
     * @param imageSrc
     */
    public Airplane(Point point, String imageSrc){
        super(point, AIRPLANE_IMAGE_FILE);
        airplaneSpeed = AIRPLANE_SPEED;
        // First airplane moves Horizontally
        direction = isHorizontal;
        // Direcdtion(Horizontal, Vertical) changes alternately
        isHorizontal = !isHorizontal;
        damage = EXPLOSIVE_DAMAGE;
        explosives = new ArrayList<>();
        // Explosive drops at random time between 1 and 2 secs inclusive)
        dropPeriod = (int)((Math.random() * 2) + 1) * ShadowDefend.FPS;
        // Rotate airplane 90 degrees and 180 degrees
        if(direction) {
            super.setAngle(Math.PI / 2);
        } else {
            super.setAngle(Math.PI);
        }
    }

    /**
     * Return finished state
     * @return
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Over ride the attackEnemy with Airplane specification
     * @param slicers
     */
    @Override
    public void attackEnemy(List<Slicer> slicers) {
        // Check if change of state needed
        if(isOnDropPeriod){
            if(time >= dropPeriod){
                isOnDropPeriod = false;
                time = 0;
                //Drop the explosive at random number between 1 and 2 inclusive.
                dropPeriod = (int)((Math.random() * 2) + 1) * ShadowDefend.FPS;
            } else {
                time += ShadowDefend.getTimescale();
            }
        } else {
            isOnDropPeriod = true;
            explosives.add(new Explosive(new Point(getCenter().x,getCenter().y),slicers,damage));
        }
    }

    @Override
    public void update(Input input) {
        if(finished) {
            return;
        }
        // Update explosives (if any)
        for(int i = explosives.size() - 1; i >= 0; i--) {
            Explosive explosive = explosives.get(i);
            explosive.update(input);
            // Now attack, and if so, remove it
            if(explosive.detonate()) {
                explosives.remove(i);
            }
        }
        // Now, update airplane
        double newX = 0;
        double newY = 0;
        if(direction) {
            newX += airplaneSpeed * ShadowDefend.getTimescale();
        } else {
            newY += airplaneSpeed * ShadowDefend.getTimescale();
        }
        Point newPoint = new Point(newX, newY);
        super.move(newPoint.asVector());
        // Check if finished
        if(getCenter().x > Window.getWidth() || getCenter().y > Window.getHeight()){
            finished = true;
        }
        super.update(input);
    }
}
