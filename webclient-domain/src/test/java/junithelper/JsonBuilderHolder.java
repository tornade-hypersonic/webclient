package junithelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * JsonBuilderを保持する
 * 
 */
public class JsonBuilderHolder {

	private static Deque<JsonBuilder> stack = new ArrayDeque<>();

	public static void push(JsonBuilder json) {
		stack.push(json);
	}
	
	public static void pop() {
		stack.pop();
	}
	
	public static JsonBuilder peek() {
		return stack.peek();
	}
	
}
