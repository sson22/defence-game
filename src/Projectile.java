import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * Projectile
 */
public class Projectile extends Sprite{

    private int damage;
    private Slicer enemy;
    private static final double PROJECTILE_SPEED = 10;

    /**
     * Create Projectile depending on type of tanks
     * @param currentPoint
     * @param enemy
     * @param damage
     * @param projImage
     */
    public Projectile(Point currentPoint, Slicer enemy, int damage, String projImage){
        super(currentPoint, projImage);
        this.damage = damage;
        this.enemy = enemy;
    }

    /**
     * Attack the enemy and damage them
     * @return
     */
    public boolean attack() {
        if(enemy.getRect().intersects(getRect())){
            enemy.damage(damage);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Update the location and rotation of projectile
     * @param input
     */
    @Override
    public void update(Input input){
        Vector2 target = enemy.getCenter().asVector();
        Vector2 current = getCenter().asVector();
        Vector2 distance = target.sub(current);
        super.move(distance.normalised().mul(PROJECTILE_SPEED * ShadowDefend.getTimescale()));
        setAngle(Math.atan2(target.y - current.y, target.x - current.y));
        super.update(input);
    }
}
