package org.x.net;

public class Msg {

	public static final int DataPacketLimit = 2048;

	public static enum Compress {
		none, snappy;
	}

	public static enum DataType {
		json, bytes, string,error
	}

	public static enum Device {
		mobile(3), tablet(4), android(5), ios(6);

		private final int data;

		Device(int value) {
			this.data = value;
		}

		public int getData() {
			return this.data;
		}
	}

	public static enum Scope {
		app(1);
		private final int data;

		Scope(int value) {
			this.data = value;
		}

		public int getData() {
			return this.data;
		}
	}

	public static enum Service {
		mobile(31);
		private final int data;

		Service(int value) {
			this.data = value;
		}

		public int getData() {
			return this.data;
		}
	}

	public static enum Version {
		socket(1);
		private final int data;

		Version(int value) {
			this.data = value;
		}

		public int getData() {
			return this.data;
		}
	}

	public static enum Action {
		socket(21);
		private final int data;

		Action(int value) {
			this.data = value;
		}

		public int getData() {
			return this.data;
		}

	}

	public static enum Mode {
		Async(0), Sync(1);
		private final int data;

		Mode(int value) {
			this.data = value;
		}

		public int getData() {
			return this.data;
		}
	}

}
