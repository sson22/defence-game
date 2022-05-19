import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * Player
 */
public class Player {

    private int numLives;
    private int cash;
    private static final int INITIAL_NUM_LIVES = 25;
    private static final int INITIAL_CASH = 500;
    private final Image buyPanel;
    private final Image statusPanel;
    private final Image tank;
    private final Image superTank;
    private final Image airplane;
    private static final String BUY_PANEL_IMAGE = "res/images/buypanel.png";
    private static final String STATUS_PANEL_IMAGE = "res/images/statuspanel.png";
    private static final String TANK_IMAGE = "res/images/tank.png";
    private static final String SUPER_TANK_IMAGE = "res/images/supertank.png";
    private static final String AIRPLANE_IMAGE = "res/images/airsupport.png";
    private static final String FONT = "res/fonts/DejaVuSans-Bold.ttf";
    private final Point buyPanelLocation;
    private final Point statusPanelLocation;
    private final Point tankLocation;
    private final Point superTankLocation;
    private final Point airplaneLocation;
    private final Point statusPanelFontLocation;
    private static final int TANK_PRICE= 250;
    private static final int SUPER_TANK_PRICE= 600;
    private static final int AIRPLANE_PRICE= 500;
    // Fonts needed to draw String on Panels
    private final Font itemPrice;
    private final Font totalPrice;
    private final Font statusPanelFont;
    private final Font keyBinds;
    // DrawOption need to set the color of String on Panels
    private final DrawOptions white;
    private final DrawOptions green;
    private final DrawOptions red;
    private final Rectangle tankRec;
    private final Rectangle superTankRec;
    private final Rectangle airplaneRec;
    private final Rectangle buyPanelRec;
    private final Rectangle statusPanelRec;
    private String selected;
    private Image imageSelected;

    /**
     * Create new Player
     */
    public Player(){
        this.numLives = INITIAL_NUM_LIVES;
        this.cash = INITIAL_CASH;
        this.buyPanel = new Image(BUY_PANEL_IMAGE);
        this.statusPanel = new Image(STATUS_PANEL_IMAGE);
        this.tank = new Image(TANK_IMAGE);
        this.superTank = new Image(SUPER_TANK_IMAGE);
        this.airplane = new Image(AIRPLANE_IMAGE);
        // Place the images according to the specification
        this.buyPanelLocation = new Point(bagel.Window.getWidth() / 2,buyPanel.getHeight() / 2);
        this.statusPanelLocation = new Point(bagel.Window.getWidth() / 2,bagel.Window.getHeight()-(statusPanel.getHeight() / 2));
        this.statusPanelFontLocation = new Point(5, statusPanelLocation.y + 6);
        this.tankLocation = new Point(64,buyPanelLocation.y - 10);
        this.superTankLocation = new Point(tankLocation.x + 120, tankLocation.y);
        this.airplaneLocation = new Point(superTankLocation.x + 120, superTankLocation.y);
        // Create Font needed with different size for different information
        this.itemPrice = new Font(FONT,20);
        this.totalPrice = new Font(FONT,50);
        this.statusPanelFont = new Font(FONT, 19);
        this.keyBinds = new Font(FONT,13);
        this.white = new DrawOptions();
        white.setBlendColour(Colour.WHITE);
        this.green= new DrawOptions();
        green.setBlendColour(Colour.GREEN);
        this.red = new DrawOptions();
        red.setBlendColour(Colour.RED);
        // Set Rectangle class with the location of the image
        this.tankRec = tank.getBoundingBoxAt(tankLocation);
        this.superTankRec = superTank.getBoundingBoxAt(superTankLocation);
        this.airplaneRec = airplane.getBoundingBoxAt(airplaneLocation);
        this.buyPanelRec = buyPanel.getBoundingBoxAt(buyPanelLocation);
        this.statusPanelRec = statusPanel.getBoundingBoxAt(statusPanelLocation);
        this.selected = null;
    }

    /**
     * Getter for NumLives
     * @return
     */

    public int getNumLives() {
        return numLives;
    }

    /**
     * Give penalty to player when slicer finish polyline
     * @param penalty
     */

    public void looseLives(int penalty){
        if(numLives > 0){
            this.numLives -= penalty;
        }
    }

    /**
     * Returns Color needed for tower price depending on current cash and it's availability to buy items
     * @param price
     * @return
     */
    public DrawOptions setPriceColour(int price){
        if(price <= cash){
            return green;
        }else{
            return red;
        }
    }

    /**
     * When Time scale is more than 1, change it's String color to green
     * @param timescale
     * @return
     */
    public DrawOptions setTimeScaleColour(int timescale){
        if(timescale > 1){
            return green;
        }else{
            return white;
        }
    }

    /**
     * Place Towers and it's price and current cash the player has
     */
    public void uploadBuyPanel(){
        buyPanel.draw(buyPanelLocation.x, buyPanelLocation.y);
        tank.draw(tankLocation.x, tankLocation.y);
        superTank.draw(superTankLocation.x, superTankLocation.y);
        airplane.draw(airplaneLocation.x, airplaneLocation.y);
        itemPrice.drawString("$"+Integer.toString(TANK_PRICE),tankLocation.x - 30,tankLocation.y + 50, setPriceColour(TANK_PRICE));
        itemPrice.drawString("$"+Integer.toString(SUPER_TANK_PRICE),superTankLocation.x - 30,superTankLocation.y + 50, setPriceColour(SUPER_TANK_PRICE));
        itemPrice.drawString("$"+Integer.toString(AIRPLANE_PRICE),airplaneLocation.x - 30,airplaneLocation.y + 50, setPriceColour(AIRPLANE_PRICE));

        keyBinds.drawString("Key binds:\n\nS - Start Wave\nL - Increase Timescale\nK - Decrease Timescale", airplaneLocation.x + 200, 30, white);

        totalPrice.drawString("$"+Integer.toString(cash),bagel.Window.getWidth() - 200, 65, white);
    }

    /**
     * Upload current wave number, time scale, status and the lives of the player
     */
    public void uploadStatusPanel(){
        statusPanel.draw(statusPanelLocation.x, statusPanelLocation.y);
        statusPanelFont.drawString("Wave: " + Integer.toString(ShadowDefend.getCurrentWave()),
                statusPanelFontLocation.x, statusPanelFontLocation.y, white);
        statusPanelFont.drawString("Time Scale: " + Double.toString((double)ShadowDefend.getTimescale()),
                statusPanelFontLocation.x + 200, statusPanelFontLocation.y, setTimeScaleColour(ShadowDefend.getTimescale()));
        statusPanelFont.drawString("Status: " + ShadowDefend.statusInformation(), Window.getWidth()-500, statusPanelFontLocation.y, white);
        statusPanelFont.drawString("Lives: " + Integer.toString(numLives), Window.getWidth() - 100, statusPanelFontLocation.y, white);
    }

    /**
     * Buy Towers if you have sufficient cash
     * @param input
     */
    public void buyTowers(Input input) {
        // Take input(mouse) information
        Point mousePosition = input.getMousePosition();
        // When the Tower is clicked, image copy created and checks for valid location to create towers
        // When it's on the un valid spot then image disappears
        if(selected != null && !ShadowDefend.intersectTower(mousePosition)
                && !intersectPanel(input) && ShadowDefend.validPoint(mousePosition)) {
            imageSelected.draw(mousePosition.x, mousePosition.y);
            if(ShadowDefend.waveStatus != ShadowDefend.WaveStatus.WINNER) {
                ShadowDefend.waveStatus = ShadowDefend.WaveStatus.PLACING;
            }
        }
        if (input.wasPressed(MouseButtons.LEFT)) {
            //Check if a Tower is being selected or a position on map is selected and has enough cash to purchase item
            if (tankRec.intersects(mousePosition) && cash >= TANK_PRICE) {
                selected = "tank";
                imageSelected = tank;
            } else if (superTankRec.intersects(mousePosition) && cash >= SUPER_TANK_PRICE) {
                selected = "supertank";
                imageSelected = superTank;
            } else if (airplaneRec.intersects(mousePosition) && cash >= AIRPLANE_PRICE) {
                selected = "airplane";
                imageSelected = airplane;
            } else {
                if (!ShadowDefend.intersectTower(mousePosition) && !intersectPanel(input) && selected != null
                        && ShadowDefend.validPoint(mousePosition)) {
                    // Create Tower instance and add the tower to main class
                    if (selected.equals("tank")) {
                        ShadowDefend.addTower(new Tank(mousePosition, TANK_IMAGE));
                        cash -= TANK_PRICE;
                    } else if (selected.equals("supertank")) {
                        ShadowDefend.addTower(new SuperTank(mousePosition, SUPER_TANK_IMAGE));
                        cash -= SUPER_TANK_PRICE;
                    } else if (selected.equals("airplane")) {
                        Point newPoint = null;
                        // Airplane instance created at left edge of the same y coordinate if airplane moves horizontally
                        if(Airplane.isHorizontal) {
                            newPoint = new Point(0,mousePosition.y);
                        // Airplane instance created at top edge of the same x coordinate if airplane moves vertically
                        } else {
                            newPoint = new Point(mousePosition.x,0);
                        }
                        ShadowDefend.addTower(new Airplane(newPoint, AIRPLANE_IMAGE));
                        cash -= AIRPLANE_PRICE;
                    }
                    selected = null;
                    ShadowDefend.waveStatus = ShadowDefend.WaveStatus.AWAITING;
                }
            }
        }
        // When left clicked, deselect the item
        if (input.wasPressed(MouseButtons.RIGHT)) {
            selected = null;
            if(ShadowDefend.waveStatus != ShadowDefend.WaveStatus.WAVEPROGRESS) {
                ShadowDefend.waveStatus = ShadowDefend.WaveStatus.AWAITING;
            }
        }
    }

    /**
     * Check if the point intersect the Panel images
     * @param input
     * @return
     */
    public boolean intersectPanel(Input input){
        Point mousePosition = input.getMousePosition();
        if(buyPanelRec.intersects(mousePosition) || statusPanelRec.intersects(mousePosition)){
            return true;
        }
        return false;
    }

    /**
     * Take the Reward and update the current cash
     * @param reward
     */
    public void updateCash(int reward){
        this.cash += reward;
    }
}

