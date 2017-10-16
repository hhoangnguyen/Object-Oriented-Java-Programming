package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 *
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
	    // initialize 2 sentinel nodes
		this.head = new LLNode<>();
		this.tail = new LLNode<>();

		// link them
		this.head.next = this.tail;
		this.tail.prev = this.head;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element)
	{
		// null element
		if (element == null) {
			throw new NullPointerException("null element");
		}

		// create new LLNode
		LLNode<E> newNode = new LLNode<>(element);

		// copy existing links first
		newNode.next = this.tail;
		newNode.prev = this.tail.prev;

		// change links
		this.tail.prev.next = newNode;
		this.tail.prev = newNode;

		// update size
		this.size++;

		return true;
	}

	/** Get the element at position index
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index)
	{
		if (this.size == 0) {
			throw new IndexOutOfBoundsException("index is out of bound");
		}

		LLNode<E> curNode = this.getNodeAtIndex(index);
		if (curNode != null) return curNode.data;

		return null;
	}

	/**
	 * Helper method to find a LLNode at specified index
	 * @param index, the index of element to be removed
	 * @return LLNode|null
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 */
	private LLNode<E> getNodeAtIndex(int index)
	{
		// out of bound:
		// 1. negative index (try to remove sentinel head node)
		if (index < 0) {
			throw new IndexOutOfBoundsException("index is out of bound");
		}
		// 2. index is greater or equal to size
		if (this.size > 0 && index >= this.size) {
			throw new IndexOutOfBoundsException("index is out of bound");
		}

		LLNode<E> curNode = this.head;
		int curIndex = -1;
		while (curNode != null) {
			if (curIndex == index) {
				return curNode;
			}

			curNode = curNode.next;
			curIndex++;
		}
		return null;
	}

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element)
	{
		// null element
		if (element == null) {
			throw new NullPointerException("null element");
		}

		// create the new node for element
		LLNode<E> newNode = new LLNode<>(element);

		LLNode<E> curNode = this.getNodeAtIndex(index);

		if (curNode != null) {
			// save existing links
			newNode.next = curNode;
			newNode.prev = curNode.prev;

			// update existing links
			curNode.prev.next = newNode;
			curNode.prev = newNode;

			this.size++;
		}
	}


	/** Return the size of the list */
	public int size()
	{
		return this.size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 *
	 */
	public E remove(int index)
	{
		if (this.size == 0) {
			throw new IndexOutOfBoundsException("index is out of bound");
		}

		// try to get this item first to catch index out of bound
		LLNode<E> curNode = this.getNodeAtIndex(index);

		// great, we can start removing it
		if (curNode != null) {
			// save 'about to break' links first
			curNode.prev.next = curNode.next;
			curNode.next.prev = curNode.prev;

			// update size
			this.size--;

			return curNode.data;
		}

		return null;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element)
	{
		// null element
		if (element == null) {
			throw new NullPointerException("null element");
		}

		if (this.size == 0) {
			throw new IndexOutOfBoundsException("index is out of bound");
		}

		LLNode<E> curNode = this.getNodeAtIndex(index);

		if (curNode != null) {
			E oldData = curNode.data;
			curNode.data = element;
			return oldData;
		}

		return null;
	}
}

class LLNode<E>
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	public LLNode()
	{
		this.data = null;
		this.prev = null;
		this.next = null;
	}

	public LLNode(E e)
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}

}
