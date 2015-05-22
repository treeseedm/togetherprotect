package org.x.net.socket;

import org.x.net.Msg.Compress;
import org.x.net.Msg.DataType;

public class SocketResponse {
	public String id = null;
	public byte[] bytes = null;
	public DataType type = null;
	public String content = null;

	public SocketResponse(DataType type, String id) {
		this.type = type;
		this.id = id;
	}

	public SocketResponse() {
		this.type = DataType.json;
		this.id = SocketUtil.newId();
	}

	public void read(Compress compress, byte[] bytes) {
		if (compress == Compress.snappy) {
			bytes = SocketUtil.uncompress(bytes);
		}
		if (type == DataType.bytes) {
			this.bytes = bytes;
		} else {
			this.content = SocketUtil.toString(bytes);
		}
	}

}
