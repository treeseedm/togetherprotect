package org.x.net.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import org.x.net.Client;
import org.x.net.Msg;
import org.x.net.Msg.DataType;

public class SocketDecoder extends MessageToMessageDecoder<ByteBuf> {
	protected static Integer jobCount = 0;
	protected Client client = null;

	public SocketDecoder(Client client) {
		this.client = client;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
			List<Object> out) throws Exception {
		buf.discardReadBytes();
		if (buf.capacity() <= 12) {
			return;
		}
		DataType type = DataType.values()[buf.readInt()];
		String jobId = SocketUtil.readString(buf);
		SocketResponse response = new SocketResponse(type, jobId);
		int compress = buf.readInt();
		int size = buf.readInt();
		byte[] bytes = new byte[size];
		buf.readBytes(bytes, 0, size);
		response.read(Msg.Compress.values()[compress], bytes);
		out.add(response);
	}

}
