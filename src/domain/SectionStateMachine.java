package domain;

public class SectionStateMachine {
	
	private static boolean usage = false;
	private static boolean section = false;
	
	protected static boolean isUsage() {
		return usage;
	}
	
	protected static void startUsage() {
		usage = true;
	}
	
	protected static void endUsage() {
		usage = false;
	}
	
	protected static boolean isSection() {
		return section;
	}
	
	protected static void startSection() {
		section = true;
	}
	
	protected static void endSection() {
		section = false;
	}

}
