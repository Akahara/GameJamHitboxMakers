package fr.ttl.game;

public class Logger {

	// TODO This logger facility should really be replaced! an easy fix would be to use https://github.com/Akahara/fr.wonder.commons

	private static final boolean USE_ANSI = true;
	private static final String INFO_HEADER = USE_ANSI ? "\u001b[38;5;15mINFO ":"INFO ";
	private static final String WARN_HEADER = USE_ANSI ? "\u001b[38;5;11mWARN":"WARN";
	private static final String ERROR_HEADER = USE_ANSI ? "\u001b[38;5;196mERR_ ":"ERR_ ";
	
	public static synchronized void log(String s) {
		System.out.println(INFO_HEADER + Thread.currentThread().getName() + ": " + s);
	}

	public static synchronized void warn(String s) {
		System.out.println(WARN_HEADER + Thread.currentThread().getName() + ": " + s);
	}
	
	public static synchronized void err(String s) {
		System.out.println(ERROR_HEADER + Thread.currentThread().getName() + ": " + s);
	}

	public static synchronized void merr(Throwable e, String s) {
		System.out.println(ERROR_HEADER + Thread.currentThread().getName() + ": " + s);
		e.printStackTrace(System.out);
	}

}
