// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.

package edu.uwm.cs351;

import static org.junit.Assert.assertNotNull;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import edu.uwm.cs.junit.LockedTestCase;

/*
 * Andrew Le
 * Homework 9, CS 351
 */

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
		
		public Node(Appointment o) {
			data = o;
			left = right = null;
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
		manyItems = version = 0;
		root = null;
		assert wellFormed() : "invariant failed at end of constructor";
	}
	
	// other methods of the main class.
	// You should not need to suppress *any* warnings in any code
	// you write this week.
	
	private Node doAdd(Node r, Appointment element) {
		if (r == null) {
			return new Node(element);
		}
		if (element.compareTo(r.data) >= 0) {
			r.right = doAdd(r.right, element);
		}
		else {
			r.left = doAdd(r.left, element);
		}
		
		return r;
	}
	
	public boolean add(Appointment element){
		assert wellFormed() : "invariant failed at start of add";
		
		if (element == null) {
			throw new IllegalArgumentException();
		}
		root = doAdd(root, element);
		manyItems++;
		version++;
		
		assert wellFormed() : "invariant failed at end of add";
		return true;
	}
	
	private void doAddAll(Node r) {
		if (r == null) {
			return;
		}
		
		add(r.data);
		doAddAll(r.right);
		doAddAll(r.left);
	}
	
	public boolean addAll(NewApptBook addend){
		assert wellFormed() : "invariant failed at start of addAll";
		
		NewApptBook addendClone = addend;
		if (addend == this) {
			addendClone = addend.clone();
		}
		doAddAll(addendClone.root);
		

		assert wellFormed() : "invariant failed at end of addAll";
		return true;
	}
	
	public int size() {
		return manyItems;
	}
	
	public Node doClone(Node r, NewApptBook answer) {
		if (r == null) {
			return null;
		}
		Node copy = new Node(r.data);
		copy.left = doClone(r.left, answer);
		copy.right = doClone(r.right, answer);

		return copy;
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
		answer.root = doClone(root, answer);
	
		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant on answer failed at end of clone";
		return answer;
	}
	
	@Override // required
	public Iterator<Appointment> iterator() {
		MyIterator it = new MyIterator();
		return it; // TODO
	}
	
	
	public Iterator<Appointment> iterator(Appointment appt) {
		if (appt == null) {
			throw new NullPointerException();
		}
		MyIterator it = new MyIterator(appt);
		return it; // TODO
	}
	
	public void clear() {
			assert wellFormed() : "invariant failed at the start of clear";
			if (manyItems == 0) {
				return;
			}
			version++;
			manyItems = 0;
			root = null;
	}
	
	private Node doRemove(Node r, Node c) {
		// can change fields, but not node itself.
		r.data = null;
		return null;
	}
	
	public void remove() {
		MyIterator it = new MyIterator();
		it.remove();
	}
	
	private class MyIterator implements Iterator<Appointment> {
		// TODO data structure and wellFormed
		// NB: don't declare as public or private
		
		MyIterator(boolean ignored) {} // do not change this
		
		MyIterator() {
			// TODO
			cursor = nextCursor = null;
			colVersion = version;
			assert wellFormed() : "invariant failed in iterator constructor";
		}
		// TODO: Finish the iterator class
		
		Node cursor;
		Node nextCursor;
		int colVersion;
		
		private Node firstInTree(Node r) {
			// TODO: non-recursive is fine
			if (r == null) {
				return null;
			}
			
			Node t = r;
			
			if (r.left != null) {
				t = firstInTree(r.left);
			}
			
			return t;
		}
		
		private boolean foundCursor(Node r) {
			// TODO
			if (r == cursor) {
				return true;
			}

			if (r == null) {
				return false;
			}

			return foundCursor(r.left) || foundCursor(r.right) ;
		}
		
		private Node nextInTree(Node r, Appointment appt, boolean acceptEquivalent, Node alt) {
			// TODO: recursion not required, but is simpler
			if (r == null) {
				return alt;
			}
			int c = appt.compareTo(r.data);
			
			if (c == 0 && acceptEquivalent) {
				return r;
			}
			if (c >= 0) {
				return nextInTree(r.right, appt, acceptEquivalent, alt);
			}
			
			return nextInTree(r.left, appt, acceptEquivalent, r);
		}
		
		private boolean wellFormed() {
			
			if (!NewApptBook.this.wellFormed()) {
				return false;
			}
			
			if (version != colVersion) {
				return true; 
			}
			
			//Checks if cursor is in the list.
			if (!foundCursor(root)) {
				return report("cursor not in tree");
			}
			
			if (cursor == null) {
				if (nextCursor != null) {
					return report("cursor is null while nextCursor is not null");
				}
			}
			else {
				if (doNext(cursor) != nextCursor && nextCursor != cursor) {
					return report("nextCursor does not equal cursor or the next node");
				}
			}

			return true;
		}
		
		private void checkVersion() {
			if (colVersion != version) throw new ConcurrentModificationException("stale iterator");
		}
		
		@Override //required
		public boolean hasNext() {
			assert wellFormed(): "invariant failed at the start of hasNext.";
			
			checkVersion();
			
			if (root != null && cursor == null) {
				return true;
			}
			
			return (cursor != null && nextCursor != null);
		}
		
		private Node doNext(Node r) {
			if (r == null) {
				return firstInTree(root);
			}
			
			if (r.right != null) {
				r = r.right;
				while (r.left != null) {
					r = r.left;
				}
			}
			else {
				r = nextInTree(root, r.data, false, null);
			}
			
			return r;
		}

		@Override //required
		public Appointment next() {
			//Checks to see if there exists an element beyond
			assert wellFormed(): "invariant failed at the start of next";


			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			else {
				checkVersion();
			}
			
			cursor = doNext(cursor);
			nextCursor = doNext(cursor);
			
			if (cursor == null) {
				throw new NoSuchElementException();
			}
			
				
			assert wellFormed(): "invariant failed at the end of next";
			
			return cursor.data;
		}
		
		
		public void remove() {
			assert wellFormed(): "invariant failed at the start of remove";
			checkVersion();
			
			if (manyItems == 1) {
				root = null;
			}
			else {
				doRemove(root, cursor);
			}
			
			cursor = nextCursor;
			if (cursor != null) {
				nextCursor = doNext(cursor);
			}
			manyItems--;
			
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

