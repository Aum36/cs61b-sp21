package deque;

// Invariants:
// 1. The next element of nextFirst is always the first element of the dequeue.
// 2. The previous element of nextLast is always the last element of the dequeue.
// 3. size represents the number of elements in the dequeue.
// 4. The items array guarantees to contain null if there is no element
//    in the dequeue in that position of the array
public class ArrayDeque<Item> implements Deque<Item> {
    private Item[] items;
    private int size; // cannot be used to track index
    private int nextFirst; // points the empty slot before first
    private int nextLast; // points to the empty slot after last
    public ArrayDeque() {
        this.items = (Item[]) new Object[8];
        size = 0;
        this.nextFirst = 0;
        this.nextLast = 1;
    }
    /*             nexL nexF
                     |  |
                     v  v
        [4, 5, 6, 7, 1, 2, 3]
     */
    /** Adds to the index by the specified offset */
    private int increment(int index, int offset) {
        return (index + offset) % items.length;
    }

    /** If no offset is mentioned then add by 1 */
    private int increment(int index) {
        return (index + 1) % items.length;
    }

    /** Subtracts from the index by the specified offset */
    private int decrement(int index, int offset) {
        return (((index - offset) % items.length) + (((index - 1) < 0) ? items.length : 0));
    }

    /** If no offset is mentioned then subtract by 1 */
    private int decrement(int index) {
        return (((index - 1) % items.length) + (((index - 1) < 0) ? items.length : 0));
    }

    private boolean shouldUpSize() {
        return size == items.length;
    }

    private boolean shouldDownSize() {
        return (size > 8) &&
                (size * 4 < items.length);
    }

    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        int s;
        for(s = 0;
            s < this.size; s = s + 1) {
            a[s] = this.items[increment(nextFirst, s + 1)];
        }
        this.items = a;
        this.nextFirst = this.decrement(0);
        this.nextLast = s;
    }

    public void addFirst(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = decrement(nextFirst);
        size = size + 1;
    }

    public void addLast(Item item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = increment(nextLast);
        size = size + 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int i;
        for(i = this.increment(nextFirst);
            i != this.decrement(nextLast); i = this.increment(i)) {
            System.out.print(this.items[i] + " -> ");
        }
        System.out.println(this.items[i]);
    }

    public Item removeFirst() {
        if (size == 0) return null;
        int first = this.increment(nextFirst);
        Item item = this.items[first];
        this.items[first] = null;
        nextFirst = first;
        size = size - 1;
         if (shouldDownSize()) {
            resize(this.items.length / 2);
         }
        return item;
    }

    public Item removeLast() {
        if (size == 0) return null;
        int last = this.decrement(nextLast);
        Item item = this.items[last];
        this.items[last] = null;
        nextLast = last;
        size = size - 1;
         if (shouldDownSize()) {
            resize(this.items.length / 2);
         }
        return item;
    }

    public Item get(int i) {
        return this.items[(this.increment(nextFirst, i + 1))];
    }
}
