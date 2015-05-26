package org.x.conf;

import java.util.ResourceBundle;

public class Const {
	public static PropertyFile rockProperty = null;

	public static final int HEARTBEAT_LIVENESS = 5;
	public static final int HEARTBEAT_INTERVAL = 3000;

	public static long Hour = 3600000;
	public static long Day1Milis = 24 * Hour;
	public static long Day2Milis = 48 * Hour;
	public static long Day3Milis = 72 * Hour;
	public static String DBName = "yiqihi";

	public static boolean DebugMode = false;
	static {
		rockProperty = new PropertyFile(ResourceBundle.getBundle("rock"));
		DebugMode = rockProperty.getBooleanItem("DebugMode", false);
	}

}
