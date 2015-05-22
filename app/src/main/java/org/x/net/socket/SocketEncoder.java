package org.x.net.socket;

import org.x.net.Client;
import org.x.net.Msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SocketEncoder extends MessageToByteEncoder<SocketMsg> {

	protected Client client = null;

	public SocketEncoder(Client client) {
		this.client = client;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, SocketMsg msg, ByteBuf buf)
			throws Exception {
		if (!(msg instanceof SocketMsg)) {
			return;
		}
		buf.writeInt(19750);
		buf.writeInt(424);
		buf.writeInt(msg.type.ordinal());
		buf.writeInt(Msg.Device.android.getData());
		SocketUtil.writeBytes(buf, client.localNameBytes);
		SocketUtil.writeBytes(buf, client.localIPBytes);
		SocketUtil.writeString(buf, String.valueOf(client.userId));
		SocketUtil.writeString(buf, Msg.Service.mobile.name());
		buf.writeInt(Msg.Service.mobile.getData());
		buf.writeInt(Msg.Scope.app.getData());

		buf.writeInt(Msg.Action.socket.getData());
		buf.writeInt(Msg.Version.socket.getData());
		buf.writeInt(Msg.Mode.Async.getData());

		SocketUtil.writeString(buf, msg.id);
		SocketUtil.writeString(buf, msg.tag);
		SocketUtil.writeString(buf, msg.to);
		buf.writeLong(System.currentTimeMillis());

		byte[] bytes = null;
		if (msg.bytes.length > Msg.DataPacketLimit) {
			buf.writeInt(Msg.Compress.snappy.ordinal());
			bytes = SocketUtil.compress(msg.bytes);
		} else {
			buf.writeInt(Msg.Compress.none.ordinal());
			bytes = msg.bytes;
		}
		buf.writeInt(bytes.length);
		buf.writeBytes(bytes);
	}

}
