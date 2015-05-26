package org.x.net.socket;

import org.x.net.Msg;
import org.x.net.Msg.Compress;
import org.x.net.Msg.DataType;

import com.mongodb.BasicDBObject;

public class SocketMsg {
	public String id = null;
	public String tag = "";
	public String to = "";
	public byte[] bytes = null;
	public DataType type = null;
	public String content = null;

	public SocketMsg(DataType type, String id) {
		this.type = type;
		this.id = id;
	}

	public SocketMsg() {
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

	public void write(byte[] bytes) {
		this.type = Msg.DataType.bytes;
		this.bytes = bytes;
	}

	public void write(String key, BasicDBObject oData) {
		this.type = Msg.DataType.json;
		String content = oData.toString();
		content = SocketUtil.easyEncrypt(key, content);
		this.bytes = SocketUtil.toBytes(content);
	}

}
