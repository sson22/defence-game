import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ShadowDefend, a tower defence game.
 */

public class ShadowDefend extends AbstractGame {

    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final String MAP_FILE = "res/levels/1.tmx";
    private static final String WAVE_FILE = "res/levels/waves.txt";
    // Change to suit system specifications. This could be
    // dynamically determined but that is out of scope.
    // FPS is made static so that Slicers and Towers can change to suit the system specification
    public static final int FPS = 60;
    //Timescale is made static because it is a universal property of the game and the specification
    //says everything in the game is affected by this
    private static final int INITIAL_TIMESCALE = 1;
    private static final int MAXIMUM_TIMESCALE = 5;
    private static int timescale = INITIAL_TIMESCALE;
    // Change to read wave file, change millisecond to second
    // MILLISECONDS_IN_SEC is made static so that Slicers and Towers can change to suit the system specification
    public static final int MILLISECONDS_IN_SEC = 1000;
    private static TiledMap map;
    private List<Point> polyline;
    private final List<Slicer> slicers;
    private static final List<Tower> towers = new ArrayList<Tower>();
    private List<String> waveEvents;
    private double frameCount;
    private int numSlicers;
    private boolean waveStarted;
    private boolean allWaves;
    private int numMap;
    private Player player;
    private static int currentWave;
    private static int currentEvent;
    private double spawnDelay;
    private double currentDelay;
    private String eventType;
    //WaveStatus indicates the current status of the game, it is made public so that Panel and other classes could use
    //to be updated.
    public enum WaveStatus{
        WINNER, PLACING, WAVEPROGRESS, AWAITING
    }
    public static WaveStatus waveStatus;

    /**
     * Creates a new instance of the ShadowDefend game
     */
    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        map = new TiledMap(MAP_FILE);
        this.polyline = map.getAllPolylines().get(0);
        this.slicers = new ArrayList<Slicer>();
        this.numSlicers = 0;
        this.spawnDelay = 0;
        this.currentDelay = 0;
        this.waveStarted = false;
        this.allWaves = false;
        this.frameCount = 0;
        // Temporary fix for the weird slicer map glitch (might have to do with caching textures)
        // This fix is entirely optional
        new RegSlicer(polyline);
        this.numMap = 1;
        this.player = new Player();
        currentWave = 0;
        readWaves(WAVE_FILE);
        waveStatus = WaveStatus.AWAITING;
    }

    /**
     * The entry-point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    public static int getTimescale() {
        return timescale;
    }

    public static int getCurrentWave() {
        if(currentWave == 0){
            return currentWave+1;
        }return currentWave;
    }

    public static void addTower(Tower tower) {
        towers.add(tower);
    }


    /**
     * Increases the timescale
     */
    private void increaseTimescale() {
        if(timescale < MAXIMUM_TIMESCALE) {
            timescale++;
        }
    }

    /**
     * Decreases the timescale but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        if (timescale > INITIAL_TIMESCALE) {
            timescale--;
        }
    }

    /**
     * Update to next level by placing new map and reset player and sprites.
     */
    public void updateNextLevel() {
        if(numMap > 0) {
            numMap++;
            String newPath = "res/levels/" + numMap + ".tmx";
            // Handle exception when there isn't the map file that matches the newPath(when there's no next level)
            try {
                // Reset the game evironment
                map = new TiledMap(newPath);
                this.polyline = map.getAllPolylines().get(0);
                player = new Player();
                slicers.clear();
                towers.clear();
                timescale = INITIAL_TIMESCALE;
                numSlicers = 0;
                currentWave = 0;
                currentEvent = 0;
                waveEvents.clear();
                allWaves = false;
                waveStarted = false;
                readWaves(WAVE_FILE);
            } catch (Exception e) {
                // When there's no level to move on, player wins and state changes to Winner
                waveStatus = WaveStatus.WINNER;
                // Slicers stop moving
                for(Slicer slicer : slicers){
                    slicer.speed = 0;
                }
            }
        }
    }

    /**
     * Checks if point on the map is within the window and not on the blocked area
     * The method was made static so that it could be used on other classes
     * @param point
     * @return
     */
    public static boolean validPoint(Point point) {
        boolean invalidX = point.x < 0.0D || point.x > (double)Window.getWidth();
        boolean invalidY = point.y < 0.0D || point.y > (double)Window.getHeight();
        boolean outOfBounds = invalidX || invalidY;
        if (outOfBounds) {
            return false;
        } else {
            return !map.getPropertyBoolean((int)point.x, (int)point.y, "blocked", false);
        }
    }

    /**
     * Checks if the point on the map intersects other Tower, will be used when purchasing Towers
     * The method was made static so that it could be used on other classes
     * @param point
     * @return
     */
    public static boolean intersectTower(Point point){
        for(Tower t: towers) {
            if(t.getRect().intersects(point)){
                return true;
            }
        }return false;
    }

    /**
     * Read file Strings for wave events
     *
     */

    private void readWaves(String filename)  {
        waveEvents = new ArrayList<String>();
        try{
            File file = new File(filename);
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                waveEvents.add(scan.nextLine());
            }
            scan.close();
        }catch(FileNotFoundException ex){
            ex.getMessage();
        }
    }


    /**
     * Read each line of wave events and create slicers specified on the wave file, set the delay time
     */
    private void readEvent() {
        // When there's still wave events left to come for the current wave
        if(!allWaves) {
            if(currentDelay <= 0) {
                if(numSlicers == 0) {
                    if(currentEvent < waveEvents.size()) {
                        // Read next event
                        String event = waveEvents.get(currentEvent);
                        currentEvent++;
                        String[] parts = event.split(",");

                        if(Integer.parseInt(parts[0]) != currentWave) {
                            currentEvent--;
                            waveStarted = false;
                            return;
                        }
                        // Set number of slicers and type needed to create and delay time when event type is "spawn"
                        if (parts[1].equals("spawn")) {
                            numSlicers = Integer.parseInt(parts[2]);
                            eventType = parts[3];
                            spawnDelay = (Double.parseDouble(parts[4])) / MILLISECONDS_IN_SEC * FPS;
                            currentDelay = spawnDelay;
                        // Set delay time when the event type is "delay"
                        } else if (parts[1].equals("delay")) {
                            eventType = parts[1];
                            spawnDelay = (Double.parseDouble(parts[2])) / MILLISECONDS_IN_SEC * FPS;
                            currentDelay = spawnDelay;
                        } else {
                            System.out.println("Incorrect Event Type");
                        }
                    } else {
                        // Change allWaves state when all the wave events read for the current event
                        allWaves = true;
                    }
                }
                // Spawn next needed slicer (if event is spawn)
                if(!eventType.equals("delay") && numSlicers > 0) {
                    if (eventType.equals("slicer")) {
                        slicers.add(new RegSlicer(polyline));
                    } else if (eventType.equals("superslicer")) {
                        slicers.add(new SuperSlicer(polyline));
                    } else if (eventType.equals("megaslicer")) {
                        slicers.add(new MegaSlicer(polyline));
                    } else if (eventType.equals("apexslicer")) {
                        slicers.add(new ApexSlicer(polyline));
                    } else {
                        System.out.println("Incorrect Slicer Type");
                    }
                    currentDelay = spawnDelay;
                    numSlicers--;
                }
            } else {
                currentDelay -= timescale;
            }
        }
    }

    /**
     * Check if the current wave is Finished
     * @return
     */
    public boolean waveFinished(){
        // When slicer array is empty and there's no more slicers needed to spawn,
        if(numSlicers == 0 && slicers.isEmpty()
                // and all the events are read, wave is considered finished
                && (currentEvent == waveEvents.size() ||
                Integer.parseInt(waveEvents.get(currentEvent).split(",")[0]) != currentWave)) {
            return true;
        }
        return false;
    }

    /**
     * Award player $150 + wave × $100 where wave is the wave number that just finished
     */
    public void giveReward() {
        player.updateCash(150 + currentWave * 100);
    }

    /**
     * Spawn Child Slicers when it is eliminated by Towers
     * @param slicer
     */
    public void spawnChild(Slicer slicer){
        // When a super slicer is eliminated, it spawns two regular slicers at the location it was eliminated.
        if(slicer instanceof SuperSlicer){
            slicers.add(new RegSlicer(slicer));
            slicers.add(new RegSlicer(slicer));
        // When a mega slicer is eliminated, it spawns two super slicers at the location it was eliminated.
        }else if(slicer instanceof MegaSlicer){
            slicers.add(new SuperSlicer(slicer));
            slicers.add(new SuperSlicer(slicer));
        // When an apex slicer is eliminated, it spawns four mega slicers at the location it was eliminated.
        }else if(slicer instanceof ApexSlicer){
            slicers.add(new MegaSlicer(slicer));
            slicers.add(new MegaSlicer(slicer));
            slicers.add(new MegaSlicer(slicer));
            slicers.add(new MegaSlicer(slicer));
        }
    }

    /**
     * Return status of game as String to upload the status on the status Panel
     * The method was made static so that it could be used on Player to upload status panel
     * @return
     */
    public static String statusInformation(){
        String message = null;
        if(waveStatus == WaveStatus.WINNER){
            message = "Winner!";
        }else if(waveStatus == WaveStatus.PLACING){
            message = "Placing";
        }else if(waveStatus == WaveStatus.WAVEPROGRESS){
            message = "Wave In Progress";
        }else{
            message = "Awaiting Start";
        }
        return message;
    }

    /**
     * Update the state of the game, potentially reading from input
     *
     * @param input The current mouse/keyboard state
     */

    @Override
    protected void update(Input input) {
        // Increase the frame counter by the current timescale
        frameCount += getTimescale();
        //Sets the state of the game when the change is done
        if(!waveFinished() && waveStatus != WaveStatus.PLACING) {
            waveStatus = WaveStatus.WAVEPROGRESS;
        }
        if(waveFinished() && waveStatus != WaveStatus.PLACING && waveStatus != WaveStatus.WINNER) {
            waveStatus = WaveStatus.AWAITING;
        }
        if(waveStatus == WaveStatus.WINNER){
            timescale = INITIAL_TIMESCALE;
            currentWave = 0;
        }
        // Draw map from the top left of the window
        map.draw(0, 0, 0, 0, WIDTH, HEIGHT);
        player.uploadBuyPanel();
        player.uploadStatusPanel();
        player.buyTowers(input);
        for(Tower tower: towers){
            tower.attackEnemy(slicers);
        }

        // Handle key presses
        if (input.wasPressed(Keys.S) && waveFinished()) {
            waveStarted = true;
            currentWave++;
        }

        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        }

        if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }

        // Update to next level if there's no slicers left on the map, and have no event left on current wave
        // and player live is more than 0
        if ((numSlicers == 0 && slicers.isEmpty() && allWaves) && player.getNumLives() > 0){
            updateNextLevel();
        }
        // Close window when number of lives reaches 0
        if (player.getNumLives() <= 0){
            Window.close();
        }
        // when S key is pressed read event
        if(waveStarted) {
            readEvent();
        }

        // Update all slicers, and remove them if needed
        for (int i = slicers.size() - 1; i >= 0; i--) {
            Slicer slicer = slicers.get(i);
            slicer.update(input);
            // Remove slicer if it has finished traversing the polyline, and player gets penalty
            if (slicer.isFinished()) {
                player.looseLives(slicer.getPenalty());
                slicers.remove(i);
            }
            // Remove slicer when it's killed by Towers and spawn Child slicers at the spot it died.
            if(slicer.healthReachedZero()){
                player.updateCash(slicer.getReward());
                slicers.remove(i);
                spawnChild(slicer);
            }
            if(slicers.isEmpty()) {
                //This will be called once per wave, as soon as all slicers are gone
                // and there's no event left for the wave
                if(waveFinished()) {
                    giveReward();
                }
            }
        }

        // Update towers
        for(int i = 0; i < towers.size(); i++) {
            Tower tower = towers.get(i);
            tower.update(input);
        }
    }
}
