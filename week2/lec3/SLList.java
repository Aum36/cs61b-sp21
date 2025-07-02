package lec3;

public class SLList {
    public IntNode first;

    public SLList(int x) {
        first = new IntNode(x, null);
    }

    public void addFirst(int x) {
        first = new IntNode(x, first);
    }

    /** Add IntNode to the end of the list */
    public void addLast(int x) {
        IntNode p = this.first;
        while(p.next != null) p = p.next;
        p.next = new IntNode(x, null);
    }

    /** Return the length/size of the list */
    public int size() {
        IntNode p = this.first;
        int size = 0;
        while(p != null) {
            size++;
            p = p.next;
        }
        return size;
    }

    /** Return the size of the list recursion style */
    public int sizeRecur() {
        return size(this.first);
    }

    public static int size(IntNode L) {
        if (L == null) return 1;
        return 1 + size(L.next);
    }

    /** Add IntNode to the end of the list recursion style */
    public void addLastRecur(int x) {
        IntNode p = iterateTillEnd(this.first);
        p.next = new IntNode(x, null);
    }

    public static IntNode iterateTillEnd(IntNode L) {
        if (L.next == null) return L;
        return iterateTillEnd(L.next);
    }

    /** Returns the first item in the list */
    public int getFirst() {
        return first.item;
    }

    public static void main(String[] args) {
        SLList l = new SLList(15);
        l.addFirst(10);
        l.addFirst(5);
    }
}
