package util;

public class PrimitiveUtil {

    public static Object getPrimitiveValue(String primitiveName, String value) {
        switch (primitiveName) {
            case ("int"):
                return Integer.parseInt(value);
        }
        return null;
    }
}
