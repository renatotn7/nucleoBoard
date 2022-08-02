package main;


import org.apache.commons.text.diff.CommandVisitor;
import org.apache.commons.text.diff.StringsComparator;
public class TesteCompare {

	
	 
	
	 
		public static void main(String[] args) {
			// Create a diff comparator with two inputs strings.
			StringsComparator comparator = new StringsComparator("Its All Binary.", "Its All fun.");
			// Initialize custom visitor and visit char by char.
			MyCommandsVisitor myCommandsVisitor = new MyCommandsVisitor();
			comparator.getScript().visit(myCommandsVisitor);
			// Print final diff.
			System.out.println("FINAL DIFF = " + myCommandsVisitor.left + " | " + myCommandsVisitor.right);
		}
	}
	 
	/*
	 * Custom visitor.
	 */
	class MyCommandsVisitor implements CommandVisitor<Character> {
	 
		String left = "";
		String right = "";
	 
		@Override
		public void visitKeepCommand(Character c) {
			// Character is present in both files.
			left = left + c;
			right = right + c;
		}
	 
		@Override
		public void visitInsertCommand(Character c) {
			/*
			 * Character is present in right file but not in left. Method name
			 * 'InsertCommand' means, c need to insert it into left to match right.
			 */
			right = right + "(" + c + ")";
		}
	 
		@Override
		public void visitDeleteCommand(Character c) {
			/*
			 * Character is present in left file but not right. Method name 'DeleteCommand'
			 * means, c need to be deleted from left to match right.
			 */
			left = left + "{" + c + "}";
		}
	}


