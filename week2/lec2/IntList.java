public class IntList {
    public int first;
    public IntList rest;

    public IntList(int f, IntList r) {
        this.first = f;
        this.rest = r;
    }

    /** Return the size of the list using recursion */
    public int size() {
        if(this.rest == null) {
            return 1;
        }
        return 1 + this.rest.size();
    }

    public int iterativeSize() {
        IntList L = this;
        int size = 0;
        while(L != null) {
            size = size + 1;
            L = L.rest;
        }
        return size;
    }

    public int get(int i) {
        return get(i, 0);
    }

    public int get(int i, int k) {
        if(k == i) {
            return this.first;
        }
        return this.rest.get(i, k + 1);
    }

    public int iterativeGet(int i) {
        IntList L = this;
        int k = 0;
        while(i != k) {
            L = L.rest;
            k++;
        }
        return L.first;
    }

    public static IntList addFirst(IntList L, int x) {
        return new IntList(x, L);
    }

    public IntList addFirst(int x) {
        return new IntList(x, this);
    }
    /** Returns an IntList identical to L, but with
     * each element incremented by x. L is not allowed
     * to change. */
    public static IntList incrList(IntList L, int x) {
        IntList returnList = new IntList(L.first + x, null);
        IntList q = returnList;
        IntList p = L.rest;
        while(p != null) {
            q.rest = new IntList(p.first + x, null);
            p = p.rest;
            q = q.rest;
        }
        return returnList;
    }

    public static IntList recurIncrList(IntList L, int x) {
        if(L == null) return null;
        return new IntList(L.first + x, recurIncrList(L.rest, x));
    }

    /** Returns an IntList identical to L, but with
     * each element incremented by x. Not allowed to use
     * the 'new' keyword. */
    public static IntList dincrList(IntList L, int x) {
        IntList p = L;
        while(p != null) {
            p.first = p.first - x;
            p = p.rest;
        }
        return L;
    }

    public static IntList recurDincrList(IntList L, int x) {
        act_recurDincrList(L, x);
        return L;
    }

    public static void act_recurDincrList(IntList L, int x) {
        if(L == null) {
            return;
        }
        L.first = L.first - x;
        act_recurDincrList(L.rest, x);
    }

    public String toString() {
        String s = "[" + this.first;
        IntList L = this.rest;
        while(L != null) {
            s = s + ", " + L.first;
            L = L.rest;
        }
        s = s + "]";
        return s;
    }

    public static void main(String[] args) {
        IntList L = new IntList(15, null);
        L = L.addFirst(10);
        L = L.addFirst(5);
        // L = addFirst(L, 10);
        // L = addFirst(L, 5);
        // L = new IntList(10, L);
        // L = new IntList(5, L);
        IntList M = recurIncrList(L, 3);
        System.out.println(recurDincrList(M, 3));
    }
}
