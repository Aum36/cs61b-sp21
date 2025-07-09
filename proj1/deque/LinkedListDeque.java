package deque;

/* Implementation requirements:
1. add and remove operations must not involve any looping or recursion.
   A single such operation must take “constant time”,
   i.e. execution time should not depend on the size of the deque.
   This means that you cannot use loops that go over all/most elements of the deque.

2. get must use iteration, not recursion

3. size must take constant time

4. Iterating over the LinkedListDeque using a for-each loop
   should take time proportional to the number of items.

5. Do not maintain references to items that are no longer in the deque.
   The amount of memory that your program uses at any given time must be proportional to
   the number of items.
   For example, if you add 10,000 items to the deque, and then remove 9,999 items,
   the resulting memory usage should amount to a deque with 1 item, and not 10,000.
   Remember that the Java garbage collector will “delete” things for us
   if and only if there are no pointers to that object.
*/


public class LinkedListDeque<Item> implements Deque<Item> {
    private static class ItemNode<Item> {
        Item item;
        ItemNode<Item> prev;
        ItemNode<Item> next;

        ItemNode(Item item, ItemNode<Item> prev, ItemNode<Item> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return this.item.toString();
        }
    }

    private ItemNode<Item> sentinel;
    private int size;


    public LinkedListDeque() {
        this.sentinel = new ItemNode<>(null, null, null);
        this.sentinel.next = this.sentinel;
        this.sentinel.prev = this.sentinel;
        this.size = 0;
    }

    private LinkedListDeque(Item item) {
        this.sentinel = new ItemNode<>(null, null, null);
        this.sentinel.next = new ItemNode<>(item, this.sentinel, this.sentinel);
        this.sentinel.prev = this.sentinel.next;
        size = 1;
    }

    // Methods
    @Override
    public void addFirst(Item item) {
        ItemNode<Item> oldFirst = this.sentinel.next;
        this.sentinel.next = new ItemNode<>(item, this.sentinel, oldFirst);
        // Is the below statement needed? As addFirst wouldn't only need changes to this.prev
            // temp.next = this.sentinel;
        /* the below example proves this.next doesn't need changing.
                sent <-> oldFirst  to  sent <-> newFirst <-> oldFirst
                  \_________/            \______________________/
           the link between going below b/w sent and 2 remains unchanged.
        */
        oldFirst.prev = this.sentinel.next;
        this.size = this.size + 1;
    }

    @Override
    public void addLast(Item item) {
        if (this.sentinel.prev == null) {
            this.sentinel.prev = new ItemNode<>(item, this.sentinel, this.sentinel);
            this.sentinel.next = this.sentinel.prev;
        }
        else {
            ItemNode<Item> oldLast = this.sentinel.prev;
            /*
                sent <-> oldLast  to  sent <-> oldLast <-> newLast
                  \_________/           \_____________________/
            */
            this.sentinel.prev = new ItemNode<>(item, oldLast, this.sentinel);
            oldLast.next = this.sentinel.prev;
        }
        this.size = this.size + 1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void printDeque() {
        ItemNode<Item> first = this.sentinel.next;
        while(first.next != this.sentinel) {
            System.out.print(first.item + " -> ");
            first = first.next;
        }
        System.out.println(first.item);
    }

    @Override
    public Item removeLast() {
        if (this.size == 0) return null;
        // Assumes at least one node exists
        ItemNode<Item> oldLast = this.sentinel.prev;
        /*
            sent <-> oldFirst <-> newFirst <-> node  to  sent <-> newFirst <-> node
              \_________________________________/           \___________________/
        */
        this.sentinel.prev = oldLast.prev;
        oldLast.prev.next = this.sentinel;
        this.size = this.size - 1;
        return oldLast.item;
    }

    @Override
    public Item removeFirst() {
        if (this.size == 0) return null;
        ItemNode<Item> oldFirst = this.sentinel.next;
        this.sentinel.next = oldFirst.next;
        oldFirst.next.prev = this.sentinel;
        this.size = this.size - 1;
        return oldFirst.item;
    }

    @Override
    public Item get(int i) {
        int k = 0;
        ItemNode<Item> it = this.sentinel.next;
        while (k != i) {
            if (it == this.sentinel) return null;
            it = it.next;
            k++;
        }
        return it.item;
    }

    public Item getRecursive(int i) {
        return getRecursiveHelper(i, 0, this.sentinel.next);
    }

    private Item getRecursiveHelper(int targetIndex, int curIndex, ItemNode<Item> node) {
        if (node == this.sentinel) {
            return null;
        } else if (curIndex == targetIndex) {
            return node.item;
        }
        return getRecursiveHelper(targetIndex, curIndex + 1, node.next);
    }
}
