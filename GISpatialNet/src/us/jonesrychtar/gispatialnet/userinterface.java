/*
 * abstract class for all user interfaces
 *
 * For research by Eric Jones and Jan Rychtar.
 *
 * Requires: ujmp
 */

package us.jonesrychtar.gispatialnet;

/**
 *
 * @author cfbevan
 */
public abstract class userinterface {
    abstract int getMenu(String title, String info, String[] options);
}
