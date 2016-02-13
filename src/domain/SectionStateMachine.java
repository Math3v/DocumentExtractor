package domain;

public class SectionStateMachine {
	
	private static boolean usage = false;
	private static boolean section = false;
	private static boolean activeSubstance = false;
	
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
	
	protected static boolean isActiveSubstance() {
		return activeSubstance;
	}
	
	protected static void startActiveSubstance() {
		activeSubstance = true;
	}
	
	protected static void endActiveSubstance() {
		activeSubstance = false;
	}

}
