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

	// TODO: Helper methods.  You are free to copy from Homework #8
	// (your code or the solution)
	
	private boolean wellFormed() {
		// Check the invariant.
		// Invariant: (simpler than in Homework #8
		// 1. The tree must have height bounded by the number of items
		// 2. The number of nodes must match manyItems
		// 3. Every node's data must not be null and be in range.
		
		// TODO: Use helper methods to do all the work.
		
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
			assert wellFormed() : "invariant failed in iterator constructor";
		}
		// TODO: Finish the iterator class
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

