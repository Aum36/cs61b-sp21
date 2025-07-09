package deque;

public interface Deque<Item> {
    /**
     * Adds an element to the start of the queue.
     * @param item The element to be added to the start of the queue.
     */
    void addFirst(Item item);

    /**
     * Adds an element to the end of the queue.
     * @param item The element to be added to the end of the queue.
     */
    void addLast(Item item);

    /**
     * Returns true if deque is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the number of elements in the deque.
     */
    int size();

    /**
     * Prints the items in the deque from first to last.
     */
    void printDeque();

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     * @return Item at the front of the deque.
     */
    Item removeFirst();

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return Item at the back of the deque.
     */
    Item removeLast();

    /**
     * Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null.
     * @param i index of the Item to be returned
     * @return element at index i;
     */
    Item get(int i);
}
