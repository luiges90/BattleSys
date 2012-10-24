package battlesys;

import battlesys.exception.*;
import java.io.*;
import java.util.*;

/**
 * Generic move class - The root of the Move Hirerachy.
 * @author Peter
 */
public abstract class Move {

    /**
     * indicating random bad status infliction
     */
    public static final int RANDOM_BAD = -2;
    /**
     * List of status that RANDOM_BAD will inflict
     */
    public static final int[] BAD_STATUS_LIST = {0, 1, 2, 3, 4, 5, 7, 11, 12};
    /**
     * Affect no status
     */
    public static final int AFFECT_NONE = -1;
    /**
     * Indicate that the move affects HP, for incType etc...
     */
    public static final int AFFECT_HP = 0;
    /**
     * Indicate that the move affects attack, for incType etc...
     */
    public static final int AFFECT_ATK = 1;
    /**
     * Indicate that the move affects defense, for incType etc...
     */
    public static final int AFFECT_DEF = 2;
    /**
     * Indicate that the move affects speed, for incType etc...
     */
    public static final int AFFECT_SPD = 3;
    /**
     * Indicate that the move affects morale, for incType etc...
     */
    public static final int AFFECT_MOR = 4;
    /**
     * Indicate that the move affects all abilities, for incType etc...
     */
    public static final int AFFECT_ALL = 5;
    /**
     * Indicate that the move affects any one of the abilities, for incType etc...
     */
    public static final int AFFECT_ANY = 6;
    /**
     * Indicate that the move affects MP, for incType etc...
     */
    public static final int AFFECT_MP = 7;
    /**
     * String array for showing the ability infliction
     */
    public static final String[] AFFECT_STRING = {"體力", "攻擊", "防禦", "速度", "鬥志", "所有能力", "隨意", "招式使用次數"};
    /**
     * Number of affects available
     */
    public static final int AFFECT_COUNT = AFFECT_STRING.length;
    /**
     * Name of the move, shown when it is used
     */
    String name;
    /**
     * Name to show when this move is critical
     */
    String critName;
    /**
     * Description of the move
     */
    String description;
    /**
     * Cost of this move
     */
    int cost;
    /**
     * How many times this move can be used, initially
     */
    int initialMoveTime;
    /**
     * How many times this move has been used
     */
    int usedTime;
    /**
     * The remaining time that the move can be used
     */
    int moveTime;
    /**
     * Reference to the owner of this move
     */
    protected Player owner;
    /**
     * Whether the move is a basic move
     */
    boolean basic;
    /**
     * Whether the move is buyable
     */
    boolean buyable;
    /**
     * Whether stat should be keep. Used by ChargeAtk (status infliction), which, when used, do no keep stat, but the real attack
     */
    protected boolean noKeepStat;
    /**
     * Whether the player faints after using the move
     */
    protected boolean faintAfterMove;
    /**
     * Whether this move is to be used on friendly side or opposing side
     */
    protected boolean onOpposing;
    /**
     * whether the target's hp should be ignored. Normally this move cannot be applied to whose hp is 0 or below.
     */
    protected boolean ignoreHp;
    /**
     * Whether this move can be used by random moves
     */
    protected boolean usableByRandomMove;
    /**
     * Whether this move can be used by public
     */
    boolean movePublic;
    /**
     * A set of names of moves that is followed to take when the move is bought.
     */
    protected List<String> alsoGetMove;
    /**
     * Store the move loaded by loadMoves method. All these moves have no owner.
     */
    private static List<Move> loadedMoves = null;
    /**
     * Whether the basic settings have been loaded.
     */
    private static Map<String, BasicSetting> basicSetting;
    
    private static class BasicSetting{
        private String name, description;
        private int cost, initialMoveTime;
        private boolean buyable, basic, movePublic;
        public BasicSetting(String name, String desc, int cost, int movetime, boolean basic, boolean buyable, boolean movePublic){
            this.name = name;
            this.description = desc;
            this.cost = cost;
            this.initialMoveTime = movetime;
            this.movePublic = movePublic;
            this.buyable = buyable;
            this.basic = basic;
        }
    }

    /**
     * Create and set up a move owned by a certain owner
     * @param owner
     * @throws IOException  
     */
    protected Move(Player owner) throws IOException {
        this.owner = owner;
        usedTime = 0;
        noKeepStat = false;
        faintAfterMove = false;
        onOpposing = true;
        usableByRandomMove = true;
        ignoreHp = false;
        alsoGetMove = new ArrayList<String>();
        setupBasicSetting();
    }

    /**
     * Whether an object is equal to this move. Two moves are equal iff their class name are equal.
     * @param o
     * @return
     */
    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Move)) {
            return false;
        }
        return o.getClass().getSimpleName().equals(this.getClass().getSimpleName()) /*&& (this.owner == ((Move)o).owner)*/;
    }

    /**
     * Get the hash code of object
     * @return
     */
    @Override
    public final int hashCode() {
        int hash = 0;
        //hash = 1245 * hash + (this.owner != null ? this.owner.hashCode() : 0);
        hash = 1245 * hash + this.getClass().getSimpleName().hashCode();
        return hash;
    }

    /**
     * Get the string representation of the object, which contains the class name and the owner name and its class name
     * @return
     */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() + " owned by " + (owner == null ? "no one" : (owner.getName() + "(" + owner.getClassName() + ")"));
    }

    /**
     * Set the basic settings, includes name, description, cost, initial move time, whether the move is basic, buyable or public
     * from moveSettings.csv
     * @throws IOException 
     */
    private final void setupBasicSetting() throws IOException {
        if (basicSetting == null) {
            basicSetting = new HashMap<String, BasicSetting>();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader("moveSettings.csv"));
            //skip the first line
            br.readLine();
            //read in the whole file
            String line;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(",");
                basicSetting.put(temp[0], new BasicSetting(temp[1], temp[2],
                        Integer.parseInt(temp[3]), Integer.parseInt(temp[4]), 
                        temp[5].equalsIgnoreCase("true") ? true : false, temp[6].equalsIgnoreCase("true") ? true : false, temp[7].equalsIgnoreCase("true") ? true : false));
            }
            br.close();
        }
        BasicSetting bs = basicSetting.get(this.getClass().getSimpleName());
        if (bs == null){
            throw new BadMoveSettingFileException();
        }
        name = bs.name;
        description = bs.description;
        cost = bs.cost;
        initialMoveTime = bs.initialMoveTime;
        moveTime = initialMoveTime;
        basic = bs.basic;
        buyable = bs.buyable;
        movePublic = bs.movePublic;
    }

    /**
     *
     * @return
     */
    public final int getCost() {
        return cost;
    }

    /**
     *
     * @return
     */
    public final String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public final String getCritName() {
        if (critName == null) return name + "暴擊";
        return critName;
    }

    /**
     *
     * @return
     */
    public final String getDescription() {
        return description;
    }

    /**
     *
     * @return
     */
    public final boolean isBuyable() {
        return buyable;
    }

    /**
     *
     * @return
     */
    public final boolean isBasic() {
        return basic;
    }

    /**
     *
     * @param s
     */
    public final void setName(String s) {
        name = s;
    }

    /**
     *
     * @param s
     */
    public final void setCritName(String s) {
        critName = s;
    }

    /**
     *
     * @return
     */
    public final int getInitialMoveTime() {
        return initialMoveTime;
    }

    /**
     * Determine whether the owner can only use basic moves, as used by checkUsable methods
     * It checks whether the owner has enough move time, is it disabled, and whether the attack is actually bought by the owner.
     * Does not check whether the move is basic
     * @return
     */
    protected final boolean basicAtkOnly() {
        if (moveTime <= 0 || owner.checkDisable(this) || (!owner.isAtkBought(this) && buyable)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Processing done before every move. All moves are required to call this method before their processing starts.
     * This method currently returns the usage of this move.
     */
    protected final String preMove() {
        if (!owner.moved) {
            return owner.getName() + "使出" + name + "，";
        }
        return "";
    }

    /**
     * Processing done after every move. All moves are required to call this method after their processing ends.
     * This method currently shows sets that the owner has moved, and keep stat
     * @param result The List of MoveResult returned by the useMove method, used for keep stat
     */
    protected final void postMove(List<SingleMoveResult> result) {
        /*if (result.size() > 0){
        if (result.get(0).isSuccessful()){
        System.out.println();
        }
        }*/
        owner.moved = true;
        if (!basic) {
            moveTime--;
        }
        usedTime++;
        if (!noKeepStat) {
            Player.keepStat(result);
        }
    }

    /**
     * @return the moveTime
     */
    public int getMoveTime() {
        return moveTime;
    }

    /**
     * @param moveTime the moveTime to set
     */
    public void setMoveTime(int moveTime) {
        this.moveTime = moveTime;
    }

    /**
     * Do something when the owner has no hp. This method will be called when the owner has no hp automatically.
     */
    protected String noHpTriggers(PlayerList thisSide, PlayerList opposingSide) {
        return "";
    }

    /**
     * Check whether this move is usable
     * @param user
     * @param target
     * @param attackers The PlayerList for the attacking side
     * @param defenders The PlayerList for the defending side
     * @return
     */
    protected boolean checkUsable(Player user, Player target, PlayerList attackers, PlayerList defenders) {
        if (owner.moved || owner.checkParalysis() || owner.checkFreeze() || (owner.checkProtect(target) && defenders.contains(target))) {
            return false;
        } else if (target.getHp() <= 0 && !ignoreHp) {
            //System.out.print("對" + target.getName() + "進行攻擊，但失敗了。");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Return the string to show when the user is unable to use a move or attacks itself.
     * If the user is not in the above case, empty string will be returned
     * @param user
     * @param target
     * @param attackers
     * @param defenders
     * @return
     */
    protected String unusableReasonString(Player user, Player target, PlayerList attackers, PlayerList defenders) {
        if (owner.moved) {
            return "";
        }
        if (owner.checkParalysis()) {
            return user.getName() + "受麻痺影響，不能行動！\n";
        }
        if (owner.checkFreeze()) {
            return user.getName() + "受"
                    + (user.status[Player.statusId.FREEZE.getId()] ? Player.STATUS_STRING[Player.statusId.FREEZE.getId()] : Player.STATUS_STRING[Player.statusId.FREEZE_3.getId()])
                    + "影響，不能行動！\n";
        }
        if (owner.checkDizzy()) {
            return user.getName() + "受暈眩影響，自我攻擊，";
        }
        if (owner.checkReflect(target)) {
            return target.getName() + "的" + Player.STATUS_STRING[Player.statusId.REFLECT.getId()] + "發生作用，使" + getName() + "向自己進攻，";
        }
        if (owner.checkProtect((target))) {
            return target.getName() + "的" + Player.STATUS_STRING[Player.statusId.PROTECT.getId()] + "發生作用， 令敵方無法攻擊！\n";
        }
        if (target.getHp() <= 0 && !ignoreHp) {
            return "對" + target.getName() + "進行攻擊，但失敗了。\n";
        }
        return "";
    }

    /**
     * Make the user use the move against the target
     * This method is meant to be used on Move** level
     * @param user
     * @param target
     * @param attackers The PlayerList for the attacking side
     * @param defenders The PlayerList for the defending side
     * @return List of results for every single attack of the move.
     * @throws BattleSysException
     */
    protected abstract List<SingleMoveResult> useMove(Player user, Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException;

    /**
     * Make the owner use the move against the target
     * This method is meant to be used by others
     * @param target
     * @param attackers The PlayerList for the attacking side
     * @param defenders The PlayerList for the defending side
     * @return List of results for every single attack of the move.
     * @throws BattleSysException
     */
    public abstract CompleteMoveResult useMove(Player target, PlayerList attackers, PlayerList defenders) throws BattleSysException;

    /**
     * Supporting function for loadMoves - recursive function for traversing the directory tree
     * @param path Path of the current process as in directory tree
     */
    private static void loadMoves_r(String path) {
        //Get information of current processing file
        File thisFile = new File(path);

        if (thisFile.isDirectory()) {
            //If this file is a directory, recursively load the contents of the folder
            String[] classFilePaths = thisFile.list();
            for (String s : classFilePaths) {
                loadMoves_r(path + "/" + s);
            }
        } else {
            //this file is not a directory - attempt to load the move class

            //Ignore files that are not java files
            if (!path.substring(path.length() - 5).equalsIgnoreCase(".java")) {
                return;
            }

            //Name of class is the file path excluding "src/" prefix and ".java" suffix, and all "/" to "."
            String className = path.substring(4, path.length() - 5).replace("/", ".");
            //import the class
            try {
                Class[] constructorParam = new Class[1];
                constructorParam[0] = Player.class;

                //we are passing the "null" value into the constructor, instead of using "null" as the param for newInstance method
                Object nullObject = null;

                //load the move
                Move tp = (Move) Class.forName(className).getConstructor(constructorParam).newInstance(nullObject);

                //add the move in the global movelist
                loadedMoves.add(tp);

            } catch (Exception ex) {
                throw new BadMoveFileException(className, ex);
            }
        }

    }

    /**
     * Load the moves in the move folder. They can be accessed by getMove method.
     * If moves are already loaded this method does nothing.
     */
    public static final void loadMoves() {
        //prevent multiple loadings of the moves
        if (loadedMoves != null) {
            return;
        }

        String moveRootPath = "src/battlesys/move";

        loadedMoves = new ArrayList<Move>();

        //Get player class files
        File moveClassFile = new File(moveRootPath);
        String[] classFilePaths = moveClassFile.list();

        for (String s : classFilePaths) {
            loadMoves_r(moveRootPath + "/" + s);
        }

        //all moves has been loaded - make it unmodifable
        loadedMoves = Collections.unmodifiableList(loadedMoves);

    }

    /**
     * Convert a given name of the move to an actual move object (Not the one in the bought move object)
     * Used to obtain information about the move, instead of using move object as owned by a player.
     * If moves are not loaded this will do loading.
     * @param name The name of the attack, e.g. BasicAtk. Case insensitive.
     * @return
     * @throws InvalidMoveNameException Thrown when the move specified by name parameter does not exist
     */
    public static final Move getMove(String name) throws InvalidMoveNameException {
        if (loadedMoves == null) {
            loadMoves();
        }
        for (Move m : loadedMoves) {
            if (m.getClass().getSimpleName().equalsIgnoreCase(name)) {
                try {
                    Class[] paramClassArray = new Class[]{Player.class};
                    Object nullObject = null;
                    Object[] paramArray = new Object[]{nullObject};
                    return (Move) Class.forName(m.getClass().getName()).getConstructor(paramClassArray).newInstance(paramArray);
                } catch (ClassNotFoundException ex) {
                    throw new InvalidMoveNameException(name);
                } catch (Exception ex) {
                    throw new BadMoveFileException(name, ex);
                }
            }
        }
        throw new InvalidMoveNameException(name);
    }

    /**
     * Get all the moves available.
     * If moves are not loaded this will do loading.
     * @return
     */
    public static final List<Move> getAllMoves() {
        if (loadedMoves == null) {
            loadMoves();
        }
        return new ArrayList<Move>(loadedMoves);
    }

    /**
     * Get all the moves that can be bought and is usable by "public"
     * If moves are not loaded this will do loading.
     * @return
     */
    public static final List<Move> getAllPublicMoves() {
        if (loadedMoves == null) {
            loadMoves();
        }
        List<Move> r = new ArrayList<Move>();
        for (Move m : loadedMoves) {
            if (m.buyable && !m.basic && m.movePublic) {
                r.add(m);
            }
        }
        return r;
    }

    /**
     * Get all the moves that can be bought
     * If moves are not loaded this will do loading.
     * @return
     */
    public static final List<Move> getAllBuyableMoves() {
        if (loadedMoves == null) {
            loadMoves();
        }
        List<Move> r = new ArrayList<Move>();
        for (Move m : loadedMoves) {
            if (m.buyable && !m.basic) {
                r.add(m);
            }
        }
        return r;
    }

    /**
     * Get the moves that are for MoveRandom moves. It will give moves except those not usable due to randomMoves or not buyable moves.
     * If moves are not loaded this will do loading.
     * @return
     */
    public static final List<Move> getAllRandomMoves() {
        if (loadedMoves == null) {
            loadMoves();
        }
        List<Move> result = new ArrayList<Move>();
        for (Move m : loadedMoves) {
            if (m.usableByRandomMove && m.buyable) {
                Move t = null;
                String name = m.getClass().getName();
                try {
                    Class[] paramClassArray = new Class[]{Player.class};
                    Object nullObject = null;
                    Object[] paramArray = new Object[]{nullObject};
                    t = (Move) Class.forName(name).getConstructor(paramClassArray).newInstance(paramArray);
                } catch (Exception ex) {
                    assert false;
                }
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Get number of moves loaded.
     * If moves are not loaded this will do loading.
     * @return
     */
    public static final int getMoveCount() {
        if (loadedMoves == null) {
            loadMoves();
        }
        return loadedMoves.size();
    }

    /**
     * Get all the Simple Class names of all the moves
     * If moves are not loaded this will do loading.
     * @param includeUnbuyableMoves Whether to include moves that are not buyable
     * @return
     */
    public static final List<String> getNamesOfAllMoves(boolean includeUnbuyableMoves) {
        if (loadedMoves == null) {
            loadMoves();
        }

        List<String> s = new ArrayList<String>(loadedMoves.size());
        for (Move m : loadedMoves) {
            if (m.buyable || includeUnbuyableMoves) {
                s.add(m.getClass().getSimpleName());
            }
        }
        return s;
    }

    /**
     * Get all the Simple Class names of all the moves
     * If moves are not loaded this will do loading.
     * @return
     */
    public static final List<String> getNamesOfAllMoves() {
        return getNamesOfAllMoves(false);
    }

    /**
     * @return the movePublic
     */
    public boolean isMovePublic() {
        return movePublic;
    }
}
