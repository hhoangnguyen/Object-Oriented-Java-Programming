/**
 * 
 */
package textgen;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

/**
 * @author UC San Diego MOOC team
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<Integer> list1;
	MyLinkedList<Integer> list2;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
	    shortList = new MyLinkedList<String>();
		shortList.add("A");
		shortList.add("B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();
		for (int i = 0; i < LONG_LIST_LENGTH; i++)
		{
			longerList.add(i);
		}
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);

		list2 = new MyLinkedList<Integer>();
		list2.add(10);
		list2.add(20);
		list2.add(30);

		Integer element = 30;
		list2.add(1, element);
		list2.add(1, element);
	}

	@Test
	public void testSentinels()
	{
		assertEquals(null, shortList.head.data);
		assertEquals(null, shortList.tail.data);
	}
	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet()
	{
		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
			
		}
		
		// test short list, first contents, then out of bounds
		assertEquals("Check first", "A", shortList.get(0));
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			shortList.get(2);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		
		}
		try {
			longerList.get(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {
		}
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove()
	{
		// remove index out of bound
		try {
			list1.remove(9);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {

		}

		// list1: 65 - 21 - 42
		int a = list1.remove(0); // 65
		assertEquals("Remove: check a is correct ", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());

		int b = list1.remove(1); // 42
		assertEquals("Remove: check b is correct ", b, 42);
		int c = list1.remove(0); // 21
		assertEquals("Remove: check c is correct ", c, 21);
		assertEquals("Remove: check size is correct ", 0, list1.size());

		// remove negative index
		try {
			list1.remove(-1);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {

		}

		// remove item from an empty list
		try {
			list1.remove(0);
			fail("Check empty list can't remove");
		}
		catch (IndexOutOfBoundsException e) {

		}
	}
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd()
	{
		// add null element
		try {
			list2.add(1, null);
			fail("null element");
		}
		catch (NullPointerException e) {

		}

        assertEquals("Empty list ", emptyList.head, emptyList.tail.prev);
		assertEquals("Short list ", "B", shortList.tail.prev.data);
		assertEquals("Long list ", (Integer)9, longerList.tail.prev.data);
	}

	/** Test the size of the list */
	@Test
	public void testSize()
	{
		assertEquals("Empty list size ", 0, emptyList.size());
		assertEquals("Short list size ", 2, shortList.size());
		assertEquals("Long list size ", 10, longerList.size());
	}
	
	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex()
	{
		// get negative index
		try {
			list2.add(-1, 19);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {

		}

		// get larger than size
		try {
			list2.add(7, 19);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {

		}

		// add null element
		try {
			list2.add(1, null);
			fail("null element");
		}
		catch (NullPointerException e) {

		}

		Integer element = 30;
		assertEquals("AddAtIndex: Check index 1 ", element, list2.get(1));
		assertEquals("AddAtIndex: Check index 1 ", element, list2.get(1));
		assertEquals("AddAtIndex: Check index 2 ", element, list2.get(2));
	}
	
	/** Test setting an element in the list */
	@Test
	public void testSet()
	{
		// get negative index
		try {
			list2.set(-1, 19);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {

		}

		// get larger than size
		try {
			list2.set(7, 19);
			fail("Check out of bounds");
		}
		catch (IndexOutOfBoundsException e) {

		}

		Integer lastIndex = list2.size() - 1;
		Integer element = 40;
		assertEquals("Set: Check last index ", (Integer)30, list2.set(lastIndex, element));
	}
}
