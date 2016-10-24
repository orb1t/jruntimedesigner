package net.sourceforge.jruntimedesigner.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * @author wolcen
 * 
 */
public class PropertyConverter {

	public static boolean isSupport(Class<?> clazz) {
		if (clazz.equals(String.class)) {
			return true;
		} else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
			return true;
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return true;
		} else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return true;
		} else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return true;
		} else if (clazz.equals(Color.class)) {
			return true;
		} else if (clazz.equals(Font.class)) {
			return true;
		} else if (clazz.equals(Rectangle.class)) {
			return true;
		} else if (clazz.equals(Dimension.class)) {
			return true;
		} else if (clazz.equals(Point.class)) {
			return true;
		}
		return false;
	}

	public static Object toObject(String value, Class<?> clazz) {
		if (clazz.equals(String.class)) {
			return value;
		} else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
			return Character.valueOf(value.charAt(0));
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return toDouble(value);
		} else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return toInteger(value);
		} else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return toBoolean(value);
		} else if (clazz.equals(Color.class)) {
			return toColor(value);
		} else if (clazz.equals(Font.class)) {
			return toFont(value);
		} else if (clazz.equals(Rectangle.class)) {
			return toRectangle(value);
		} else if (clazz.equals(Dimension.class)) {
			return toDimension(value);
		} else if (clazz.equals(Point.class)) {
			return toPoint(value);
		}
		return null;
	}

	public static String fromObject(Object value) {
		if (value == null)
			return String.valueOf(value);
		Class<?> clazz = value.getClass();
		if (clazz.equals(String.class)) {
			return String.valueOf(value);
		} else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
			return String.valueOf(value);
		} else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return String.valueOf(value);
		} else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return String.valueOf(value);
		} else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return String.valueOf(value);
		} else if (clazz.equals(Color.class)) {
			return fromColor((Color) value);
		} else if (clazz.equals(Font.class)) {
			return fromFont((Font) value);
		} else if (clazz.equals(Rectangle.class)) {
			return fromRectangle((Rectangle) value);
		} else if (clazz.equals(Dimension.class)) {
			return fromDimension((Dimension) value);
		} else if (clazz.equals(Point.class)) {
			return fromPoint((Point) value);
		}
		return null;
	}

	public static double toDouble(String value) {
		if (value == null || value.isEmpty()) {
			return 0;
		}
		double rslt = 0;
		try {
			rslt = Double.valueOf(value).doubleValue();
		} catch (NumberFormatException e) {
		}
		return rslt;
	}

	public static int toInteger(String value) {
		if (value == null || value.isEmpty()) {
			return 0;
		}
		int rslt = 0;
		try {
			rslt = Integer.valueOf(value).intValue();
		} catch (NumberFormatException e) {
		}
		return rslt;
	}

	public static boolean toBoolean(String value) {
		if (value == null || value.isEmpty()) {
			return false;
		}
		boolean rslt = Boolean.valueOf(value).booleanValue();
		return rslt;
	}

	public static Color toColor(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		String color = ((String) value).trim();

		int[] components = new int[3];

		try {
			if (color.indexOf(",") > 0) {
				// parse the components
				String[] sa = color.split(",");
				for (int i = 0; i < components.length; i++) {
					if (i < sa.length)
						components[i] = Integer.parseInt(sa[i]);
					else
						components[i] = 0;
				}
				// parse the transparency
				if (sa.length >= 4) {
					int alpha = Integer.parseInt(sa[3]);
					return new Color(components[0], components[1], components[2], alpha);
				} else {
					return new Color(components[0], components[1], components[2]);
				}
			} else {
				// check the size of the string
				int minlength = components.length * 2;
				if (color.length() < minlength) {
					return null;
				}

				// remove the leading #
				if (color.startsWith("#")) {
					color = color.substring(1);
				}

				// parse the components
				for (int i = 0; i < components.length; i++) {
					components[i] = Integer.parseInt(color.substring(2 * i, 2 * i + 2), 16);
				}
				// parse the transparency
				if (color.length() >= minlength + 2) {
					int alpha = Integer.parseInt(color.substring(minlength, minlength + 2), 16);
					return new Color(components[0], components[1], components[2], alpha);
				} else {
					return new Color(components[0], components[1], components[2]);
				}
			}
		} catch (Exception e) {
			return null;
		}
	}

	public static Font toFont(String value) {
		if (value == null)
			return null;
		Font font = null;
		if (value.indexOf(",") == -1) {
			font = Font.decode(value);
		} else {
			String[] bs = value.split(",");
			if (bs.length != 3)
				return null;
			font = new Font(bs[0], toInteger(bs[1]), toInteger(bs[2]));
		}
		return font;
	}

	public static Rectangle toRectangle(String value) {
		if (value == null)
			return null;
		String[] bs = value.split(",");
		if (bs.length != 4)
			return null;
		Rectangle rect = new Rectangle(toInteger(bs[0]), toInteger(bs[1]), toInteger(bs[2]),
				toInteger(bs[3]));
		return rect;
	}

	public static Dimension toDimension(String value) {
		if (value == null)
			return null;
		String[] bs = value.split(",");
		if (bs.length != 2)
			return null;
		Dimension size = new Dimension(toInteger(bs[0]), toInteger(bs[1]));
		return size;
	}

	public static Point toPoint(String value) {
		if (value == null)
			return null;
		String[] bs = value.split(",");
		if (bs.length != 2)
			return null;
		Point point = new Point(toInteger(bs[0]), toInteger(bs[1]));
		return point;
	}

	public static String fromColor(Color value) {
		if (value == null)
			return null;
		String str = "" + value.getRed() + "," + value.getGreen() + "," + value.getBlue() + "," + value.getAlpha();
		return str;
	}

	public static String fromFont(Font value) {
		if (value == null)
			return null;
		String str = "";
		str += value.getFamily() + "-";
		str += value.isPlain() ? "PLAIN" : value.isBold() ? "BOLD" : value.isItalic() ? "ITALIC" : "" + "-"
				+ value.getSize();
		return str;
	}

	public static String fromRectangle(Rectangle value) {
		if (value == null)
			return null;
		String str = "" + value.x + "," + value.y + "," + value.width + "," + value.height;
		return str;
	}

	public static String fromDimension(Dimension value) {
		String str = "" + value.width + "," + value.height;
		return str;
	}

	public static String fromPoint(Point value) {
		if (value == null)
			return null;
		String str = "" + value.x + "," + value.y;
		return str;
	}

}
