package lec3;

public class SLList {

    private static class IntNode {
        public int item;
        public IntNode next;

        public IntNode(int item, IntNode next) {
            this.item = item;
            this.next = next;
        }
    }


    private IntNode first;
    private IntNode last;
    private int length;

    public SLList(int x) {
        first = new IntNode(x, null);
        last = first;
        length = 1;
    }

    public void addFirst(int x) {
        first = new IntNode(x, first);
        length++;
    }

    /** Add IntNode to the end of the list */
    public void addLast(int x) {
        // IntNode p = this.first;
        // while(p.next != null) p = p.next;
        // p.next = new IntNode(x, null);
        this.last.next = new IntNode(x, null);
        this.last = this.last.next;
        length++;
    }

    /** Add IntNode to the end of the list recursion style */
    public void addLastRecur(int x) {
        IntNode p = iterateTillEnd(this.first);
        p.next = new IntNode(x, null);
    }

    private static IntNode iterateTillEnd(IntNode L) {
        if (L.next == null) return L;
        return iterateTillEnd(L.next);
    }

    /** Return the length/size of the list */
    public int size() {
        // IntNode p = this.first;
        // int size = 0;
        // while(p != null) {
        //    size++;
        //    p = p.next;
        // }
        // return size;
        return this.length;
    }

    /** Return the size of the list recursion style */
    public int sizeRecur() {
        return size(this.first);
    }

    private static int size(IntNode L) {
        if (L == null) return 1;
        return 1 + size(L.next);
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
