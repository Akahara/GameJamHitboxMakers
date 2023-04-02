package fr.ttl.game.math;

import java.util.Vector;

public class Utils {

	private static Vector<Runnable> pendingTasks = new Vector<>();
	
	public static void later(Runnable r, float delay) {
		new Thread(() -> {
			// bad, do not do this
			try {
				Thread.sleep((long) (delay*1000));
			} catch (InterruptedException e) {
			}
			pendingTasks.add(r);
		}).start();
	}
	
	public static void runTasks() {
		while(!pendingTasks.isEmpty())
			pendingTasks.remove(0).run();
	}

	public static boolean isInBox(float x, float y, float bx, float by, float bw, float bh) {
		return x >= bx && y >= by && x <= bx+bw && y <= by+bh;
	}
	
	public static <T> T pickAnim(T[] set, float time, float rotationTime, boolean excludeFirst) {
		int off = excludeFirst ? 1 : 0;
		return set[off + (int) (time/rotationTime) % set.length-off];
	}
	
	public static <T> T pickAnim(T[] set, float time, float rotationTime) {
		return pickAnim(set, time, rotationTime, false);
	}
	
}
