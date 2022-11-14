// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.NewApptBook.Node;



/******************************************************************************
 * This class is a homework assignment;
 * A NewApptBook ("book" for short) is a collection of Appointment objects in sorted order.
 ******************************************************************************/
public class NewApptBook extends AbstractCollection<Appointment> implements Cloneable {
	// TODO: Declare the data structure

	private static Consumer<String> reporter = (s) -> { System.err.println("Invariant error: " + s); };
	
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	private static class Node{
		Appointment data;
		Node left;
		Node right;
		Node parent;
		
		public Node(Appointment o) {
			data = o;
			left = right = parent = null;
		}
	}
	
	private int manyItems;
	private int version;
	private Node root;

	// TODO: Helper methods.  You are free to copy from Homework #8
	// (your code or the solution)
	
	//from solution
	private boolean checkHeight(Node r, int max) {
		if (max < 0) {
			return false;
		}
		if (r == null) {
			return true;
		}
		return checkHeight(r.left, max-1) && checkHeight(r.right, max -1);
	}
	
	private int countNodes(Node r) {
		// TODO
		if (r == null) {
			return 0;
		}
		else {
			return 1 + countNodes(r.left) + countNodes(r.right);
		}
		
	}
	
	private boolean allInRange(Node r, Appointment lo, Appointment hi) {
		// TODO
		
		if (r == null) {
			return true;
		}
		
		if (r.data == null) {
			return report("data was null");
		}
		
		//making sure the bounds are correct
		if (hi != null && hi.compareTo(r.data) <= 0 || lo != null && lo.compareTo(r.data) > 0) {
			return report("not in range");
		}
		
		return allInRange(r.left, lo, r.data) && allInRange(r.right, r.data, hi);
	}
	
	
	private boolean wellFormed() {
		// Check the invariant.
		// Invariant: (simpler than in Homework #8
		// 1. The tree must have height bounded by the number of items
		// 2. The number of nodes must match manyItems
		// 3. Every node's data must not be null and be in range.
		
		// TODO: Use helper methods to do all the work.
		
		//Invariant 1
		if (!checkHeight(root, manyItems)) {
			return report("the tree must be bound by number of items");
		}
		
		if (countNodes(root) != manyItems) {
			return report("number of nodes does not match manyItems");
		}
		
		if (!allInRange(root, null, null)) {
			return false;
		}
		
		// If no problems found, then return true:
		return true;
	}

	// This is only for testing the invariant.  Do not change!
	private NewApptBook(boolean testInvariant) { }

	/**
	 * Initialize an empty book. 
	 **/   
	public NewApptBook( )
	{
		// TODO: Implemented by student.
		assert wellFormed() : "invariant failed at end of constructor";
	}
	
	// other methods of the main class.
	// You should not need to suppress *any* warnings in any code
	// you write this week.
	
	private Node doAdd(Node r, Appointment element) {
		return r;
	}
	
	public boolean add(){
		assert wellFormed() : "invariant failed at start of add";
		
		assert wellFormed() : "invariant failed at end of add";
		return true;
	}
	
	private void doAddAll(Node r) {
		
		
	}
	
	public boolean addAll(){
		assert wellFormed() : "invariant failed at start of addAll";
		
		assert wellFormed() : "invariant failed at end of addAll";
		return true;
	}
	
	public int size() {
		return manyItems;
	}
	
	public Node doClone(Node r, NewApptBook answer) {
		
		return null;
	}
	
	public NewApptBook clone() {
		assert wellFormed() : "invariant failed at start of clone";
		NewApptBook answer;
	
		try
		{
			answer = (NewApptBook) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}
	
		// TODO: copy the structure (use helper method)
		
	
		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant on answer failed at end of clone";
		return answer;
	}
	
	@Override // required
	public Iterator<Appointment> iterator() {
		return null; // TODO
	}
	
	/**
	 * TODO
	 */
	public Iterator<Appointment> iterator(Appointment appt) {
		return null; // TODO
	}
	
	private class MyIterator implements Iterator<Appointment> {
		// TODO data structure and wellFormed
		// NB: don't declare as public or private
		
		MyIterator(boolean ignored) {} // do not change this
		
		MyIterator() {
			// TODO
			cursor = root;
			assert wellFormed() : "invariant failed in iterator constructor";
		}
		// TODO: Finish the iterator class
		
		Node cursor;
		Node nextCursor;
		int colVersion = version;
		boolean canRemove = false;
		
		private boolean foundCursor(Node r) {
			// TODO
			if (cursor == r) {
				return true;
			}
			
			if (r == null && cursor != null) {
				return false;
			}

			return foundCursor(r.left)|| foundCursor(r.right) ;
		}
		
		private boolean wellFormed() {
			
			if (!NewApptBook.this.wellFormed()) {
				return false;
			}
			
			if (version != colVersion) {
				return true; 
			}
			
			//Checks if cursor is in the list.
			if (cursor != null && foundCursor(cursor)) {
				return report("cursor not found in binary tree");
			}
			
			return true;
		}
		
		@Override //required
		public boolean hasNext() {
			assert wellFormed(): "invariant failed at the start of hasNext.";
			return false;
		}

		@Override //required
		public Appointment next() {
			//Checks to see if there exists an element beyond
			assert wellFormed(): "invariant failed at the start of next";
			
				
			assert wellFormed(): "invariant failed at the end of next";
			
			return cursor.data;
		}
		
		/**
		 * remove() methods that removes the current position where
		 * cursor is.
		 * @exception
		 * 		if version is not equal to the colVersion, then throw exception.
		 * 		if canRemove is false, then throw exception.
		 */
		public void remove() {
			assert wellFormed(): "invariant failed at the start of remove";
			
			assert wellFormed(): "invariant failed at the end of remove";
				
		}
		
		public MyIterator(Appointment o) {
			
		}
	}
	
	// don't change this nested class:
	public static class TestInvariantChecker extends LockedTestCase {
		protected NewApptBook self;
		protected NewApptBook.MyIterator iterator;

		protected Consumer<String> getReporter() {
			return reporter;
		}
		
		protected void setReporter(Consumer<String> c) {
			reporter = c;
		}
		
		private static Appointment a = new Appointment(new Period(new Time(), Duration.HOUR), "default");
		
		protected class Node extends NewApptBook.Node {
			public Node(Appointment d, Node n1, Node n2) {
				super(a);
				data = d;
				left = n1;
				right = n2;
			}
			public void setLeft(Node l) {
				left = l;
			}
			public void setRight(Node r) {
				right = r;
			}
		}
		
		protected class Iterator extends MyIterator {
			public Iterator(Node n1, Node n2, int v) {
				self.super(false);
				cursor = n1;
				nextCursor = n2;
				colVersion = v;
			}
			
			public Iterator() {
				this(null,null,self.version);
			}
			
			public boolean wellFormed() {
				return super.wellFormed();
			}
			
			public void setColVersion(int cv) {
				colVersion = cv;
			}
			
			public void setCursor(Node c) {
				cursor = c;
			}
			
			public void setNextCursor(Node c) {
				nextCursor = c;
			}
		}
		
		protected Node newNode(Appointment a, Node l, Node r) {
			return new Node(a, l, r);
		}
		
		protected void setRoot(Node n) {
			self.root = n;
		}
		
		protected void setManyItems(int mi) {
			self.manyItems = mi;
		}
		
		protected void setUp() {
			self = new NewApptBook(false);
			self.root = null;
			self.manyItems = 0;
		}

		protected boolean wellFormed() {
			return self.wellFormed();
		}
		
		/// Prevent this test suite from running by itself
		
		public void test() {
			assertFalse("Don't attempt to run this test", true);
		}
	}
}

