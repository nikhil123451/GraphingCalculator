//taken from d2L
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExpressionSimplifier {

	final static int ENCLOSED_IN_BRACKETS = -2;
	final static int OPERATOR_NOT_FOUND = -1;

	class Operation {

		//one of {"/","*", "-", "+"}
		protected String operator = "";
		//left-hand-side of operation
		protected Operation lhs = null;
		//righ-hand-side of operation
		protected Operation rhs = null;
		//value is only valid if operator is not provided
		protected double value = 0;

		//constructors
		public Operation(String operator, Operation lhs, Operation rhs) {
			this.operator = operator;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		public Operation(double value) {
			this.value = value;
		}

		public String toString() {
			if (this.operator.isEmpty() == false) {
				String lhs = (this.lhs == null ? "" : this.lhs.toString());
				String rhs = (this.rhs == null ? "" : this.rhs.toString());
				return String.format("(%s)%s(%s)", lhs,operator,rhs);			
			}
			else {
				return Double.toString(this.value);
			}
		}
	}

	//wrapper
	public double simplifyExpression(String expression) {
		
		expression = insertImplicitMultiplication(expression); //method to handle cases like ")(" or "4x"
		//two stop process:
		//1. go through the expression and parse it into a data structure representing the operations, the order of the operations, and values
		Operation node = parseExpression(expression);
		//2. go through the data structure and simplify to a single value
		double result = simplifyExpression(node);
		return result;
	}

	private Operation parseExpression(String expression) {

		int operatorIndex = findHighestOrderOperator(expression);

		if (operatorIndex == OPERATOR_NOT_FOUND) {
			return new Operation(Double.parseDouble(expression));	
		}
		else if (operatorIndex == ENCLOSED_IN_BRACKETS) {
			//simple recursive case... just strip the brackets and reparse
			String newExpression = expression.substring(1, expression.length() - 1); //removing the first and last parenthesis
			return parseExpression(newExpression);
		}
		else {
			//recursive case... split into left expression and right expression, and combine in an Operation object
			String operator = Character.toString(expression.charAt(operatorIndex));
			Operation lhs = parseExpression(expression.substring(0, operatorIndex));
			Operation rhs = parseExpression(expression.substring(operatorIndex + 1, expression.length()));
			return new Operation(operator, lhs, rhs);
		}		
	}

	private double simplifyExpression(Operation node) {

		if (node.operator.isEmpty()) {
			return node.value;
		}
		else {
			if (node.operator.equals("^")) {
				return Math.pow(simplifyExpression(node.lhs), simplifyExpression(node.rhs));
			} else if (node.operator.equals("/")) {
				return simplifyExpression(node.lhs) / simplifyExpression(node.rhs);
			} else if (node.operator.equals("*")) {
				return simplifyExpression(node.lhs) * simplifyExpression(node.rhs);
			} else if (node.operator.equals("-")) {
				return simplifyExpression(node.lhs) - simplifyExpression(node.rhs);
			} else if (node.operator.equals("+")) {
				return simplifyExpression(node.lhs) + simplifyExpression(node.rhs);
			} else {
				return 0; //should not reach here
			}
		}
		
	}

	//helper function... no need to modify
	//returns the index in the input String of the single-character operator that should be evaluated next
	private static int findHighestOrderOperator(String equation) {
		
		if (equation.length() == 0) return OPERATOR_NOT_FOUND;

		int location = OPERATOR_NOT_FOUND;
		int start = 0;
		int end = equation.length() - 1;
		int bracketCount = 0;
		boolean additionFound = false;
		boolean multiplicationFound = false;
		boolean exponentFound = false;

		//iterate through the String to find the operator
		for (int index = start; index <= end; index++) {
			char current = equation.charAt(index);
			if (current == '(') {
				bracketCount++;
			}
			else if (current == ')') {
				bracketCount--;
			}
			
			else if ((current == '^') && (bracketCount == 0)) {
			    if ((additionFound == false) && (multiplicationFound == false) && (exponentFound == false)) {
			        location = index;
			        exponentFound = true;
			    }
			}

			else if ((current == '*' || current == '/') && (bracketCount == 0)) {
			    if (additionFound == false) {
			        location = index;
			        multiplicationFound = true;
			    }
			}

			else if ((current == '+' || current == '-') && (bracketCount == 0)) {

			    boolean unary = false; //assume it's a real addition/subtraction

			    if (index == 0) { //nothing before the +/- sign
			        unary = true;
			    } else {

			        char previous = equation.charAt(index - 1);

			        if (previous == '(' || previous == '+' || previous == '-' || previous == '*' || previous == '/' || previous == '^') unary = true;
			    }

			    if (!unary) {
			        location = index;
			        additionFound = true;
			    }
			}
//			System.out.println(String.format("index %d bracket %d location %d char %c +-%b */%b", index, bracketCount, location, current, additionFound, multiplicationFound));
		}

		if ((location == OPERATOR_NOT_FOUND) && (equation.charAt(start) == '(') && (equation.charAt(end) == ')')) {
			return ENCLOSED_IN_BRACKETS;
		}


		return location;
	}

	//provided for testing purposes
	public static void main(String[] args) {

		String equation = getInput();
		ExpressionSimplifier es = new ExpressionSimplifier();
		double result = (double) es.simplifyExpression(equation);
		System.out.println(result);

	}

	//provided for testing purposes
	private static String getInput() {
		String input = "";
		try {
			System.out.print("Enter an expression:");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			input = reader.readLine();
			reader.close();
		} catch (Exception e) {
		}
		return input;
	}
	
	private static String insertImplicitMultiplication(String expression) { //helper method to place multiplication symbols where needed

	    String result = "";

	    for (int i = 0; i < expression.length() - 1; i++) {

	        char current = expression.charAt(i);
	        char next = expression.charAt(i + 1);
	        result += current;

	        boolean currentCanMultiply = Character.isDigit(current) || current == ')' || current == '.';
	        boolean nextCanMultiply = next == '(' || Character.isLetter(next); //checking for x's

	        if (currentCanMultiply && nextCanMultiply) {
	            result += "*";
	        }
	        if (current == ')' &&
	            (Character.isDigit(next) || next == '.')) {

	            result += "*";
	        }
	    }

	    result += expression.charAt(expression.length() - 1);

	    return result;
	}


}
