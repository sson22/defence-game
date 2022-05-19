import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * SuperTank
 */
public class SuperTank extends Tank{

    private static final String SUPER_TANK_IMAGE_FILE = "res/images/supertank.png";
    private static final String SUPER_TANK_PROJECTILE_IMAGE_FILE = "res/images/supertank_projectile.png";
    private static final int SUPER_TANK_COOLDOWN_TIME = 500;
    private static final int SUPER_TANK_RADIUS = 150;

    /**
     * Create new SuperTank that inherits from Tank
     * @param point
     * @param imageSrc
     */
    public SuperTank(Point point, String imageSrc) {
        super(point, SUPER_TANK_IMAGE_FILE);
        this.coolDownTime = SUPER_TANK_COOLDOWN_TIME / ShadowDefend.MILLISECONDS_IN_SEC * ShadowDefend.FPS;
        this.time = 0;
        this.zone = new Rectangle(point.x-SUPER_TANK_RADIUS, point.y-SUPER_TANK_RADIUS,
                2 * SUPER_TANK_RADIUS, 2 * SUPER_TANK_RADIUS);
        this.damage = 3 * Tank.TANK_PROJECTILE_DAMAGE;
        this.projImage = SUPER_TANK_PROJECTILE_IMAGE_FILE;

    }
}
