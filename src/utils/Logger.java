package utils;

public class Logger {
	
	private int mask;
	
	/* Info log level */
	public static final int INF = 2;
	/* Warning log level */
	public static final int WRN = 4;
	/* Critical log level */
	public static final int CRI = 8;
	/* Error log level */
	public static final int ERR = 16;
	
	public Logger( int mask ) {
		this.mask = mask;
	}
	
	public void logln( String s ) {
		System.out.println( s );
	}
	
	public void log( String s ) {
		System.out.print( s );
	}
	
	public void logln( String s, int level ) {
		if( (mask & level) > 0 ) {
			System.out.println( s );
		}
	}
	
	public void log( String s, int level ) {
		if( (mask & level) > 0 ) {
			System.out.print( s );
		}
	}

}
