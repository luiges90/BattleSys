package battlesys;

import ec.util.MersenneTwisterFast;
import java.util.*;

/**
 * Storing some static useful functions
 * @author Peter
 */
public final class Utility {

    //RNG seed for battle generation - do NOT change unless starting a new round
    private static final int RNG_SEED = 31922;
    private static MersenneTwisterFast rng = new MersenneTwisterFast();

    /*
     * Prevents instantiation of the class.
     */
    private Utility() {
    }

    /**
     * Avoid a number smaller than a certain number
     * @param n The number to be checked
     * @param margin The margin of the number. n cannot be smaller than this
     * @return If n is smaller than margin, return margin, otherwise return n
     */
    public static int noSmallerThan(int n, int margin) {
        return n < margin ? margin : n;
    }

    /**
     * Avoid a number getting out of a certain range
     * @param n The number to be checked
     * @param lo The Lower margin of the number. n cannot be smaller than this
     * @param hi The Upper margin of the number. n cannot be bigger than this
     * @return If n is out of range, set to defined extreme values, otherwise return n
     */
    public static int inRange(int n, int lo, int hi) {
        return n < lo ? lo : (n > hi ? hi : n);
    }

    /**
     * Avoid a number getting out of a certain range
     * @param n The number to be checked
     * @param lo The Lower margin of the number. n cannot be smaller than this
     * @param hi The Upper margin of the number. n cannot be bigger than this
     * @return If n is out of range, set to defined extreme values, otherwise return n
     */
    public static double inRange(double n, double lo, double hi) {
        return n < lo ? lo : (n > hi ? hi : n);
    }

    /**
     * Get a random number between lo and hi
     * @param lo Lower margin
     * @param hi Higher (Upper) margin
     * @return Random number between lo and hi
     */
    public static double randBetween(double lo, double hi) {
        if (hi < lo) {
            double t;
            t = hi;
            hi = lo;
            lo = t;
        }
        return rng.nextDouble() * (hi - lo) + lo;
    }

    /**
     * Get a random number between lo and hi
     * @param lo Lower margin
     * @param hi Higher (Upper) margin
     * @return Random number between lo and hi
     */
    public static int randBetween(int lo, int hi) {
        if (hi < lo) {
            int t;
            t = hi;
            hi = lo;
            lo = t;
        }
        return rng.nextInt(hi - lo + 1) + lo;
    }

    /**
     * Get a random number between lo and hi
     * @param lo Lower margin
     * @param hi Higher (Upper) margin
     * @return Random number between lo and hi
     */
    public static long randBetween(long lo, long hi) {
        if (hi < lo) {
            long t;
            t = hi;
            hi = lo;
            lo = t;
        }
        return rng.nextLong(hi - lo + 1) + lo;
    }

    /**
     * Get a random value with mean and variance
     * @param mean Mean
     * @param var Variance
     * @return Random value obtained
     */
    public static int randValue(int mean, int var){
        return mean + Utility.randBetween(-var, var);
    }

    /**
     * Get a random value with mean and variance
     * @param mean Mean
     * @param var Variance
     * @return Random value obtained
     */
    public static double randValue(double mean, double var){
        return mean + Utility.randBetween(-var, var);
    }

    /**
     * Return a value to be shown in percentage form
     * @param n The value to be shown
     * @return The percentage form of the value
     */
    public static double percentageForm(double n) {
        return Math.round(Math.abs(n * 100));
    }

    /**
     * Perform a random number test
     * @param prob The probability of success, 0 to 100
     * @return Whether the test is a success
     */
    public static boolean probTest(double prob) {
        //return (prob >= randBetween(0.0,100.0));
        return rng.nextBoolean(inRange(prob, 0, 100) / 100);
    }

    /**
     * Test whether the HP of a player is below a certain percentage of its initial
     * @param ratio The percentage, a real number from 0 to 100
     * (or above, actually... in some cases it is meaningful)
     * @param p The player object
     * @return Whether the player has its HP below a percentage of its initial
     */
    public static boolean hpRatioTest(double ratio, Player p) {
        return p.getHp() < p.getInitHp() * (ratio / 100);
    }

    /**
     * Select a subset of k numbers from 0 to n-1. It is guaranteed that it is equally likely to generate all subsets,
     * and the returned list is sorted
     * @param n Number of elements to choose
     * @param k Number of elements to be chosen into the subset
     * @return Integer array containing the subset.
     * @throws IllegalArgumentException If n &lt; k or n &lt; 0 or k &lt; 0
     */
    public static int[] intSubset(int n, int k) {
        if (n < k || n < 0 || k < 0) {
            throw new IllegalArgumentException();
        }

        //Number of elements chosen
        int chosen = 0;
        int[] result = new int[k];

        //Choose k 1's from n, forming binary containing k 1's
        if (Utility.probTest((double) k / n * 100)) {
            result[chosen] = 0;
            chosen++;
        }

        for (int j = 1; j < n; ++j) {
            if (Utility.probTest(((double) k - chosen) / (n - j) * 100)) {
                result[chosen] = j;
                chosen++;
                if (chosen == k) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Get a subset of elements in the given list
     * @param <T>
     * @param o List to select sub elements from
     * @param k Number of elements
     * @return Subset of elements in the given list
     */
    public static <T> ArrayList<T> subset(List<T> o, int k){
        int[] choose = intSubset(o.size(), k);

        ArrayList<T> t = new ArrayList<T>(k);
        for (int i = 0; i < k; ++i){
            t.add(o.get(choose[i]));
        }

        return t;
    }

    /**
     * Remove any surplus elements in the end of the array
     * @param <T>
     * @param p Array of player to be shrunk
     * @param l Only this number of the elements will be kept in the front of the array
     * @return shrunk array
     */
    public static <T> T[] shrinkArray(T[] p, int l){
        return Arrays.copyOf(p, l);
    }

    /**
     * Remove any surplus elements in the end of the array of ints
     * @param p Array of ints to be shrunk
     * @param l Only this number of the elements will be kept in the front of the array
     * @return shrunk array
     */
    public static int[] shrinkArray(int[] p, int l){
        return Arrays.copyOf(p, l);
    }

    /**
     * Combine a list of object lists into one list
     * @param <T>
     * @param p List of object lists concerned
     * @return Combined list
     */
    public static <T> List<T> combineArray(List<T> ... p){
       List<T> c = new ArrayList<T>();
       for (int i = 0; i < p.length; ++i){
           for (T pl : p[i]){
               c.add(pl);
           }
       }
       return c;
    }

    /**
     * Return a randomly picked object from a list
     * @param <T>
     * @param o The list to be taken an element from
     * @return
     */
    public static <T> T randomPick(List<T> o){
        return o.get(randBetween(0, o.size()-1));
    }

    /**
     * Return a randomly picked object from a set
     * @param <T>
     * @param o The set to be taken an element from
     * @return
     */
    public static <T> T randomPick(Set<T> o){
        int elementToGet = randBetween(0, o.size()-1);
        Iterator t = o.iterator();
        for (int i = 0; t.hasNext(); ++i){
            if (elementToGet == i){
                return (T) t.next();
            }
            t.next();
        }
        throw new AssertionError();
    }

    /**
     * Return a randomly picked object from the array
     * @param <T>
     * @param o The array to be takenn
     * @return
     */
    public static <T> T randomPick(T[] o){
        return o[randBetween(0, o.length-1)];
    }

    /**
     * Return a random int from an array of ints
     * @param s List of ints
     * @return Randomly picked object from the given list of ints
     */
    public static int randomPick(int[] s){
        return s[randBetween(0,s.length - 1)];
    }

    /**
     * Combine all elements in a string array to one string
     * @param str String array of strings to be combined
     * @param delimiter The delimiter to separate the strings
     * @return The combined string
     */
    public static String listString(String[] str, String delimiter){
        StringBuilder res = new StringBuilder(128);
        for (int i = 0; i < str.length; ++i) {
            res.append(i == 0 ? "" : delimiter);
            res.append(str[i]);
        }
        return res.toString();
    }

    /**
     * Convert (cast) an object array to a string array
     * @param obj Object Array to be converted
     * @return String array same length of object array containing the toString outputs of each objects
     */
    public static String[] objArr2StrArr(Object[] obj){
        String[] res = new String[obj.length];
        for (int i = 0; i < obj.length; ++i){
            res[i] = obj[i].toString();
        }
        return res;
    }

    /**
     * Find the smallest value from an array of integer
     * @param v Array concerned
     * @return Smallest value
     */
    public static int findMin(int[] v){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < v.length; ++i){
            if (v[i] < min){
                min = v[i];
            }
        }
        return min;
    }

    /**
     * Find the greatest value from an array of integer
     * @param v Array concerned
     * @return Greatest value
     */
    public static int findMax(int[] v){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < v.length; ++i){
            if (v[i] > max){
                max = v[i];
            }
        }
        return max;
    }

    /**
     * Tell whether an object is in an array
     * @param <T>
     * @param needle Object to look for
     * @param hayshack The array to look from
     * @return True if found, false otherwise.
     */
    public static <T> boolean inArray(T needle, T[] hayshack){
        for (int i = 0; i < hayshack.length; ++i){
            if (needle.equals(hayshack[i])){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Tell whether an int is in an array of ints
     * @param needle int to find
     * @param hayshack int list to find from
     * @return true if the int is in the list, false otherwise
     */
    public static boolean inArray(int needle, int[] hayshack){
        for (int i = 0; i < hayshack.length; ++i){
            if (needle == hayshack[i]){
                return true;
            }
        }
        return false;
    }

    /**
     * Shuffle an integer array. Every combinations are guaranteed to be equally likely to appear
     * @param a An array to be shuffled
     * @return The shuffled array
     */
    public static int[] shuffle(int[] a){
        int j, tp;
        for (int i = a.length - 1; i >= 0; i--){
            j = Utility.randBetween(0, i);
            tp = a[j];
            a[j] = a[i];
            a[i] = tp;
        }
        return a;
    }

    /**
     * Shuffle an array. Every combinations are guaranteed to be equally likely to appear
     * @param <T> 
     * @param p The array to be shuffled
     * @return The shuffled array
     */
    public static <T> T[] shuffle(T[] p){
        for (int i = p.length - 1; i > 0; --i) {
            T tp;
            int toSwap = Utility.randBetween(0, i);
            tp = p[i];
            p[i] = p[toSwap];
            p[toSwap] = tp;
        }
        return p;
    }

    /**
     * Shuffle a list. Every combinations are guaranteed to be equally likely to appear
     * @param <T>
     * @param p The list to be shuffled
     * @return The shuffled array
     */
    public static <T> List<T> shuffle(List<T> p){
        for (int i = p.size() - 1; i > 0; --i) {
            T tp;
            int toSwap = Utility.randBetween(0, i);
            tp = p.get(i);
            p.set(i, p.get(toSwap));
            p.set(toSwap, tp);
        }
        return p;
    }

    /**
     * Invert an array. That is, {1, 3, 5} is changed to {0, 2, 4, 6, 7} if top = 8.
     * @param a Array to invert
     * @param top The top value - that is, the maximum value of the array, exclusive. The minimum is always 0.
     * @return Inverted array
     */
    public static int[] invertArray(int[] a, int top){
        int[] r = new int[top];
        int ri = 0;
        for (int i = 0; i < top; ++i){
            if (!inArray(i, a)){
                r[ri++] = i;
            }
        }
        return shrinkArray(r, ri);
    }
    
    /**
     * Convert a color with its RGB value to HTML ready code
     * @param r Red component of color, in range 0 to 255
     * @param g Green component of color, in range 0 to 255
     * @param b Blue component of color, in range 0 to 255
     * @return String of color which is HTML ready
     */
    public static String colorCode(int r, int g, int b) {
        r = inRange(r, 0, 255);
        g = inRange(g, 0, 255);
        b = inRange(b, 0, 255);

        String rx = Integer.toHexString(r);
        String gx = Integer.toHexString(g);
        String bx = Integer.toHexString(b);
        if (rx.length() == 1) {
            rx = '0' + rx;
        }
        if (gx.length() == 1) {
            gx = '0' + gx;
        }
        if (bx.length() == 1) {
            bx = '0' + bx;
        }

        return '#' + rx + gx + bx;
    }

    /**
     * Color a text using BBCode such that the color either increase or decrease one value to
     * other value for each component of the color
     * A gradient would be produced from vlo to vhi, as v varies.
     * @param v The value to be shown. Between vlo and vhi. Extreme value taken if it is out of range vlo or vhi.
     * @param vlo The Lower bound of value
     * @param vhi The Upper bound of value
     * @param rlo The value of red component when v is at its lower bound, in range 0 to 255
     * @param rhi The value of red component when v is at its upper bound, in range 0 to 255
     * @param glo The value of green component when v is at its lower bound, in range 0 to 255
     * @param ghi The value of green component when v is at its upper bound, in range 0 to 255
     * @param blo The value of blue component when v is at its lower bound, in range 0 to 255
     * @param bhi The value of blue component when v is at its upper bound, in range 0 to 255
     * @return BBCoded string showing the text in generated color
     */
    public static String colorText(int v, int vlo, int vhi, int rlo, int glo, int blo, int rhi, int ghi, int bhi) {
        boolean rInc = true;
        boolean gInc = true;
        boolean bInc = true;
        int rSmall = rlo;
        int gSmall = glo;
        int bSmall = blo;

        if (rhi < rlo) {
            rInc = false;
            rSmall = rhi;
        }
        if (ghi < glo) {
            gInc = false;
            gSmall = ghi;
        }
        if (bhi < blo) {
            bInc = false;
            bSmall = bhi;
        }

        double vRatio = (v + 0.0) / Math.abs(vhi - vlo);
        int r = (int) (rInc ? vRatio * Math.abs(rhi - rlo) + rSmall : (1 - vRatio) * Math.abs(rhi - rlo) + rSmall);
        int g = (int) (gInc ? vRatio * Math.abs(ghi - glo) + gSmall : (1 - vRatio) * Math.abs(ghi - glo) + gSmall);
        int b = (int) (bInc ? vRatio * Math.abs(bhi - blo) + bSmall : (1 - vRatio) * Math.abs(bhi - blo) + bSmall);
        String code = colorCode(r, g, b);

        return "[color=" + code + "]" + v + "[/color]";
    }

}
