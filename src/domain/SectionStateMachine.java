package domain;

public class SectionStateMachine {
	
	private static boolean usage = false;
	
	protected static boolean isUsage() {
		return usage;
	}
	
	protected static void startUsage() {
		usage = true;
	}
	
	protected static void endUsage() {
		usage = false;
	}

}
