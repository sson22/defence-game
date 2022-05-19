import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.util.List;

/**
 * Tank
 */
public class Tank extends Tower {
    // Variables are made protected so that other subclasses could reference the variables
    protected int coolDownTime;
    protected boolean isOnCoolDownPeriod;
    protected int time;
    protected Rectangle zone;
    protected String projImage;
    protected int damage;
    protected Projectile currentProjectile;
    private static final String TANK_IMAGE_FILE = "res/images/tank.png";
    private static final String TANK_PROJECTILE_IMAGE_FILE = "res/images/tank_projectile.png";
    public static final int TANK_COOLDOWN_TIME = 1000;
    public static final int TANK_RADIUS = 100;
    public static final int TANK_PROJECTILE_DAMAGE = 1;

    /**
     * Create new Tank and set its Projectile image
     * @param point
     */

    public Tank(Point point) {
        super(point, TANK_IMAGE_FILE);
    }

    public Tank(Point point, String imageSrc) {
        super(point, imageSrc);
        // Change coolDownTime unit to second to unify the units with other classes
        this.coolDownTime = TANK_COOLDOWN_TIME / ShadowDefend.MILLISECONDS_IN_SEC * ShadowDefend.FPS;
        this.time = 0;
        // Set Tank zone with width and height as Tank's diameter
        this.zone = new Rectangle(point.x - TANK_RADIUS, point.y - TANK_RADIUS, 2 * TANK_RADIUS, 2 * TANK_RADIUS);
        this.damage = TANK_PROJECTILE_DAMAGE;
        this.projImage = TANK_PROJECTILE_IMAGE_FILE;
    }

    /**
     * Over ride the attackEnemy with Tank specification
     * @param slicers
     */
    @Override
    public void attackEnemy(List<Slicer> slicers) {
        // Check if change of state needed
        if (isOnCoolDownPeriod) {
            if (time >= coolDownTime) {
                isOnCoolDownPeriod = false;
                time = 0;
            } else {
                time += ShadowDefend.getTimescale();
            }
        } else if(currentProjectile == null) {
            // Choose and rotate Tank to new enemy if projectile is gone
            for (Slicer enemy : slicers) {
                if (enemy.getRect().intersects(zone)) {
                    isOnCoolDownPeriod = true;
                    Point targetPoint = enemy.getCenter();
                    Point currentPoint = getCenter();
                    setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.y) + Math.PI/2);
                    currentProjectile = new Projectile(currentPoint, enemy, damage, projImage);
                    return;
                }
            }
        }
    }

    @Override
    public void update(Input input) {
        // First, update projectile if not null
        if(currentProjectile != null) {
            currentProjectile.update(input);
            // Now attack, and if so, set it as null
            if(currentProjectile.attack()) {
                currentProjectile = null;
            }
        }

        super.update(input);
    }
}