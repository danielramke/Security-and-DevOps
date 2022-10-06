package de.lyth;

import java.lang.reflect.Field;
import java.util.Random;

public class TestHelper {

    public static void manipulateObject(Object target, String field, Object inject) {
        boolean wasPrivateField = false;
        try {
            Field declaredField = target.getClass().getDeclaredField(field);
            if(!declaredField.canAccess(target)) {
                declaredField.setAccessible(true);
                wasPrivateField = true;
            }
            declaredField.set(target, inject);
            if(wasPrivateField) {
                declaredField.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public static String randomString() {
        Random random = new Random();
        return random.ints(97, 122 + 1)
                .limit(7)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
