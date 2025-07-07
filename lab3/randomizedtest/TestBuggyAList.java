package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> broken = new BuggyAList<>();
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        for(int i = 0; i < 3; i++) {
            broken.addLast(i);
            correct.addLast(i);
        }
        assertEquals(broken.size(), correct.size());
        for(int i = 2; i >= 0; i--) {
            int itemList = broken.removeLast();
            int itemNoResizeList = correct.removeLast();
            assertEquals(itemList, itemNoResizeList);
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> LBuggy = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            switch (operationNumber) {
                case 0:
                    int randVal = StdRandom.uniform(0, 100);
                    L.addLast(randVal);
                    LBuggy.addLast(randVal);
                    // System.out.println("addLast(" + randVal + ")");
                    assertEquals(L.size(), LBuggy.size());
                    break;
                case 1:
                    int size = L.size();
                    int sizeBuggy = LBuggy.size();
                    // System.out.println("size: " + size);
                    assertEquals(size, sizeBuggy);
                    break;
                case 2:
                    int returned = 0, returnedB = 0;
                    if (L.size() > 0) {
                        returned = L.getLast();
                        returnedB = LBuggy.getLast();
                    }
                    // System.out.println("getLast(): " + ((L.size() > 0) ? returned : "empty"));
                    assertEquals(L.size(), LBuggy.size());
                    assertEquals(returned, returnedB);
                    break;
                case 3:
                    int lastVal = 0;
                    int lastValB = 0;
                    if (L.size() > 0) {
                        lastVal= L.removeLast();
                        lastValB = LBuggy.removeLast();
                    }
                    // System.out.println("removeLast(): " + ((L.size() > 0) ? lastVal : "empty"));
                    assertEquals(L.size(), LBuggy.size());
                    assertEquals(lastVal, lastValB);
                    break;
                default:
                    break;
            }
        }
    }

}
