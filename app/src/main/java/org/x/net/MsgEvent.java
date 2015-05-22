package org.x.net;

public interface MsgEvent {

	public void onResponse(Msg.DataType type, String action, String key,
			Object data);

}
