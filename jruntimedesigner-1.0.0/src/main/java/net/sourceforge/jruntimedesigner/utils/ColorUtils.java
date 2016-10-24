/*******************************************************************************
 * Copyright (c) 2008 Igor Kunin.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Igor Kunin (ikunin) - initial API and implementation
 ******************************************************************************/
package net.sourceforge.jruntimedesigner.utils;

import java.awt.Color;

/**
 * Color utils
 * @author ikunin
 * @since 1.0
 */
public class ColorUtils {

    public ColorUtils() {
        super();
    }

    /**
     * @see getColor(String value, Color defaultColor)
     * @param value
     * @return
     */
    public static Color getColor(String value) {
        return getColor(value, null);
    }

    /**
     * Die Farbe wird als String in der RGB Form #FFFFFF --> 7 chars angegeben
     * 
     * @param value
     * 
     * @param defaultColor
     * @return geparster Color oder defailtColor
     */
    public static Color getColor(String value, Color defaultColor) {
        //
        if (value != null && value.length() == 7) {
            return ColorUtils.parseHexColor(value);
        }
        return defaultColor;
    }

    /**
     * Die Farbe wird als String in der RGB Form #FFFFFF --> 7 chars angegeben
     * 
     * @param colorDescription
     * @return
     */
    public static Color parseHexColor(String colorDescription) {
        Color color = null;
        if (colorDescription == null || colorDescription.length() == 0) {
            return null;
        }
        try {
            color = new Color(Integer.parseInt(colorDescription.substring(1, 3), 16),
                    Integer.parseInt(colorDescription.substring(3, 5), 16), Integer
                            .parseInt(colorDescription.substring(5, 7), 16));
        }
        catch (IllegalArgumentException iaex) {
            return null;
        }
        return color;
    }

    /**
     * Converts the color to HEX string form like #FFFFFF
     * 
     * @param color
     * @return
     */
    public static String toHexString(Color color) {
        if (color == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("#");
        String value = Integer.toHexString(color.getRed());
        if (value.length() < 2) {
            sb.append("0");
        }
        sb.append(value);
        value = Integer.toHexString(color.getGreen());
        if (value.length() < 2) {
            sb.append("0");
        }
        sb.append(value);
        value = Integer.toHexString(color.getBlue());
        if (value.length() < 2) {
            sb.append("0");
        }
        sb.append(value);
        return sb.toString();
    }

    /**
     * Returns opposite / complementary color to the given one. It works on the
     * base of simple rule that opposite color for <code>#0000FF</code> is
     * <code>#FFFF00</code>. It doesn't work good for gray colors because
     * their opposite representation is very similar to the base.
     * 
     * @param baseColor
     * @return
     */
    public static Color findOppositeColor(Color baseColor) {
        float[] hsbValues = Color.RGBtoHSB(
                baseColor.getRed(), 
                baseColor.getGreen(), 
                baseColor.getBlue(), null);

        float hue = hsbValues[0];
        float sat = hsbValues[1];
        float brg = hsbValues[2];

        hue = hue + (float) 0.5;
        if (hue >= (float) 1.0)
            hue -= (float) 1.0;

        double brgDiff = Math.abs(0.5 - (float) brg) * (1 - (float) sat);
        if (brg < 0.5)
            brg += brgDiff;
        else
            brg -= brgDiff;

        return new Color(Color.HSBtoRGB(hue, sat, brg));
    }

}
