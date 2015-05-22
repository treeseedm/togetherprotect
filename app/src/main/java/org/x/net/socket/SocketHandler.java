package org.x.net.socket;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang.StringUtils;
import org.x.net.Client;
import org.x.net.Msg.DataType;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

@ChannelHandler.Sharable
public class SocketHandler extends ChannelDuplexHandler {
	protected BlockingQueue<SocketMsg> queue;
	protected ChannelHandlerContext ctx = null;
	protected Client client = null;

	public SocketHandler(Client c) {
		this.client = c;
	}

	@Override
	public synchronized void channelActive(ChannelHandlerContext ctx)
			throws Exception {
		this.ctx = ctx;
	}

	@Override
	public synchronized void channelInactive(ChannelHandlerContext ctx)
			throws Exception {
		this.client.privateKey = null;
		this.client.auth = false;
		this.client.event.onResponse(DataType.error, "disconnect", null,
				"channel close.");
		if (this.client.isBackground == false) {
			this.client.connect();
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		super.userEventTriggered(ctx, evt);
		if ((evt instanceof IdleStateEvent) == false) {
			return;
		}
		IdleStateEvent e = (IdleStateEvent) evt;
		if (e.state() == IdleState.ALL_IDLE) {
			if (this.client.isBackground == true) {
				this.client.event.onResponse(DataType.error, "idle", null,
						"channel idle.");
				ctx.close();
			} else {
				this.client.heartbeat();
			}
		}
	}

	public void channelRead(ChannelHandlerContext ctx, Object obj)
			throws Exception {
		if ((obj instanceof SocketResponse) == false) {
			return;
		}
		SocketResponse response = (SocketResponse) obj;
		String content = response.content;
		switch (response.type) {
		case string:
		case json:
			BasicDBObject oResult = (BasicDBObject) JSON.parse(content);
			String data = oResult.getString("data");
			try {
				if (StringUtils.isEmpty(data) == false) {
					content = SocketUtil.easyDecrypt(
							client.auth ? client.privateKey : client.publicKey,
							data);
					oResult = (BasicDBObject) JSON.parse(content);
				}
				String action = oResult.getString("action", "");
				client.event.onResponse(response.type, action,
						oResult.getString("key", ""), oResult);
			} catch (Exception e) {
				client.event.onResponse(response.type, "channelRead", "解密数据失败",
						new BasicDBObject().append("message", data));
			}
			break;
		case bytes:
			client.event.onResponse(response.type, "channelRead", null,
					response.bytes);
			break;
		case error:
			client.event.onResponse(response.type, "channelRead", null,
					response.content);
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		this.client.event.onResponse(DataType.error, "caught", null,
				cause.getLocalizedMessage());
	}

}
