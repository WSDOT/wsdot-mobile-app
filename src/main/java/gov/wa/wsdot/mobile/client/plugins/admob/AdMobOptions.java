/*
 * Copyright (c) 2015 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.mobile.client.plugins.admob;

import com.google.gwt.core.client.JavaScriptObject;

public class AdMobOptions extends JavaScriptObject {
    protected AdMobOptions() {}
    
    /**
     * Defines the sizes for the banner Ad.
     */
    public enum AdSize {
        SMART_BANNER("SMART_BANNER"), // Default
        BANNER("BANNER"),
        MEDIUM_RECTANGLE("MEDIUM_RECTANGLE"),
        FULL_BANNER("FULL_BANNER"),
        LEADERBOARD("LEADERBOARD"),
        SKYSCRAPER("SKYSCRAPER"),
        CUSTOM("CUSTOM");
        
        private String size;
        
        private AdSize(String s) {
            size = s;
        }
        
        public String getSize() {
            return size;
        }
    }
    
    /**
     * Defines the position of the banner Ad.
     */
    public enum AdPosition {
        NO_CHANGE(0),
        TOP_LEFT(1),
        TOP_CENTER(2), // Default
        TOP_RIGHT(3),
        LEFT(4),
        CENTER(5),
        RIGHT(6),
        BOTTOM_LEFT(7),
        BOTTOM_CENTER(8),
        BOTTOM_RIGHT(9),
        POS_XY(10);
        
        private int position;
        
        private AdPosition(int p) {
            position = p;
        }
        
        public int getPosition() {
            return position;
        }
    }    
    
    public final native String getAdId() /*-{
        return this.adId;
    }-*/;
    
    /**
     * 
     * @param adId  the Ad unit Id for banner
     */
    public final native void setAdId(String adId) /*-{
        this.adId = adId;
    }-*/;
    
    public final native boolean isOffsetTopBar() /*-{
        return this.offsetTopBar;
    }-*/;
    
    /**
     * 
     * @param offset  offset position of banner and webview to avoid overlap
     *                by status bar (iOS7+)
     */
    public final native void setOffsetTopBar(boolean offset) /*-{
        this.offsetTopBar = offset;
    }-*/;
    
    public final native boolean isAutoShow() /*-{
        return this.autoShow;
    }-*/;
    
    /**
     * 
     * @param show  auto show interstitial ad when loaded, set to false if plan to
     *              control the show timing with prepareInterstitial/showInterstitial
     */
    public final native void setAutoShow(boolean show) /*-{
        this.autoShow = show;
    }-*/;
    
    public final native String getAdSize() /*-{
        return this.adSize;
    }-*/;
    
    /**
     * 
     * @param size  banner Ad size, Default: SMART_BANNER. Can be one of:
     * <p><ul>
     * <li>AdSize.SMART_BANNER
     * <li>AdSize.BANNER, 
     * <li>AdSize.MEDIUM_RECTANGLE 
     * <li>AdSize.FULL_BANNER 
     * <li>AdSize.LEADERBOARD 
     * <li>AdSize.SKYSCRAPER 
     * <li>AdSize.CUSTOM
     * </ul></p>
     * <p>Example:</p>
     * <pre>{@code
     * AdSize.SMART_BANNER.getSize()
     * }</pre> 
     */
    public final native void setAdSize(String size) /*-{
        this.adSize = size;
    }-*/;
    
    public final native int getWidth() /*-{
        return this.width;
    }-*/;

    /**
     * 
     * @param w  banner width, valid when set adSize: CUSTOM. Default: 0
     */
    public final native void setWidth(int w) /*-{
        this.width = w;
    }-*/;
 
    public final native int getHeight() /*-{
        return this.height;
    }-*/;

    /**
     * 
     * @param h  banner height, valid when set adSize: CUSTOM. Default: 0
     */
    public final native void setHeight(int h) /*-{
        this.height = h;
    }-*/;

    public final native int getPosition() /*-{
        return this.position;
    }-*/;

    /**
     * 
     * @param pos  position of banner Ad, Default: TOP_CENTER. Can be one of:
     * <p><ul>
     * <li>AdPosition.NO_CHANGE
     * <li>AdPosition.AD_POSITION.TOP_LEFT
     * <li>AdPosition.AD_POSITION.TOP_CENTER
     * <li>AdPosition.AD_POSITION.TOP_RIGHT
     * <li>AdPosition.AD_POSITION.LEFT
     * <li>AdPosition.AD_POSITION.CENTER
     * <li>AdPosition.AD_POSITION.RIGHT
     * <li>AdPosition.AD_POSITION.BOTTOM_LEFT
     * <li>AdPosition.AD_POSITION.BOTTOM_CENTER
     * <li>AdPosition.AD_POSITION.BOTTOM_RIGHT
     * <li>AdPosition.AD_POSITION.POS_XY
     * </ul></p>
     * <p>Example:</p>
     * <pre>{@code
     * AdPosition.TOP_CENTER.getPosition()
     * }</pre>
     */
    public final native void setPosition(int pos) /*-{
        this.position = pos;
    }-*/;

    public final native int getX() /*-{
        return this.x;
    }-*/;

    /**
     * 
     * @param x  in pixels. Offset from screen left
     */
    public final native void setX(int x) /*-{
        this.x = x;
    }-*/;

    public final native int getY() /*-{
        return this.y;
    }-*/;

    /**
     * 
     * @param y  in pixels. Offset from screen top
     */
    public final native void setY(int y) /*-{
        this.y = y;
    }-*/;

    public final native boolean isTesting() /*-{
        return this.isTesting;
    }-*/;
    
    /**
     * 
     * @param testing  set to true, to receiving test ad for testing purpose
     */
    public final native void setIsTesting(boolean testing) /*-{
        this.isTesting = testing;
    }-*/;

    public final native String getBgColor() /*-{
        return this.bgColor;
    }-*/;

    /**
     * 
     * @param color  background color of parent view, value may be color name
     *               like 'black', 'white', etc, or '#RRGGBB'
     */
    public final native void setBgColor(String color) /*-{
        this.bgColor = color;
    }-*/;

}
