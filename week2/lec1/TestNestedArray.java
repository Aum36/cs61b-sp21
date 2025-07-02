public class TestNestedArray {
    public static void main(String[] args) {
        Object testInt = new Object[][]{
                {1, 2, 3},
                {4, 5, 6}
        };
        Object[] testNewInt = (Object[]) testInt;
        Object subInt = testNewInt[0];
        Object[] getInt = (Object[]) subInt;
        System.out.println(getInt[0]);

        Object[][] arr1 = new Object[][]{
                {1, 2, 3},
                {4, 5, 6}
        };

        Object [][] arr2 = new Object[][]{
                {1, 2, 3},
                {4, 5, 7}
        };
        System.out.println(assertNestedArrayEquals(arr1, arr2));
    }

    public static boolean assertNestedArrayEquals(Object arr1, Object arr2) {
        return nestedArrayEquals(arr1, arr2);
    }

    public static boolean nestedArrayEquals(Object obj1, Object obj2) {
        if(!obj1.getClass().isArray() && !obj2.getClass().isArray()) {
            return obj1.equals(obj2);
        } else if(checkDimensionMismatch(obj1, obj2)) {
            System.out.println("Dimension Mismatch");
            return false;
        }
        Object[] arr1 = (Object[]) obj1;
        Object[] arr2 = (Object[]) obj2;
        if(arr1.length != arr2.length) return false;
        for(int i = 0; i < arr1.length; i++) {
            boolean isEqual = nestedArrayEquals(arr1[i], arr2[i]);
            if (!isEqual) return false;
        }
        return true;
    }

    /* public static int getDimensionsNo(Object arr1, Object arr2, int k) {
        if(!arr1.getClass().isArray() && !arr2.getClass().isArray()) {
            return k;
        } else if(checkDimensionMismatch(arr1, arr2)) {
            throw new RuntimeException("Dimension Mismatch");
        }
        Object[] arr1new = (Object[]) arr1;
        Object[] arr2new = (Object[]) arr2;
        return getDimensionsNo(arr1new[0], arr2new[0], k + 1);
    } */

    public static boolean checkDimensionMismatch(Object arr1, Object arr2) {
        return ((!arr1.getClass().isArray() && arr2.getClass().isArray()) ||
                (arr1.getClass().isArray() && !arr2.getClass().isArray()));
    }
}
