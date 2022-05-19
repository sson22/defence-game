import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.List;

/**
 * Explosive
 */
public class Explosive extends Sprite {
    //Rectangle zone is made protected so that Slicer classes can take the information of it's zone
    protected Rectangle zone;
    private static final String EXPLOSIVE_IMAGE_FILE = "res/images/explosive.png";
    private static final int EXPLOSIVE_RADIUS = 200;
    private static final int EXPLOSION_TIME = 120;
    private int counter;
    private int damage;
    private List<Slicer> slicers;

    /**
     * Create a new Explosive
     * @param point
     * @param slicers
     * @param damage
     */
    public Explosive(Point point, List<Slicer> slicers, int damage){
        super(point, EXPLOSIVE_IMAGE_FILE);
        this.zone = new Rectangle(point.x-EXPLOSIVE_RADIUS, point.y-EXPLOSIVE_RADIUS, 2*EXPLOSIVE_RADIUS, 2*EXPLOSIVE_RADIUS);
        this.counter = 0;
        this.slicers = slicers;
        this.damage = damage;
    }

    /**
     * Detonate bombs after Explosion time and damage slicers
     * @return
     */
    public boolean detonate() {
        if(counter >= EXPLOSION_TIME) {
            for(Slicer enemy: slicers){
                if(enemy.getRect().intersects(zone)){
                    enemy.damage(damage);
                }
            }
            return true;
        } else {
            counter += ShadowDefend.getTimescale();
            return false;
        }
    }
}
