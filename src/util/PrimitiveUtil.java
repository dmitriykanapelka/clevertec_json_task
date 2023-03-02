package util;

import java.lang.reflect.Field;

public class PrimitiveUtil {

    public static Object getPrimitiveValue(String primitiveName, String value) {
        switch (primitiveName) {
            case ("int"):
                return Integer.parseInt(value);
        }
        return null;
    }

    public static String getStringFromPrimitiveValue(String primitiveName, Object value) {
        switch (primitiveName) {
            case ("int"):
                return String.valueOf(value);
        }
        return null;
    }
}
