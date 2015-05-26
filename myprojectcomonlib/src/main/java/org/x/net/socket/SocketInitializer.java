package org.x.net.socket;

import org.x.net.Client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class SocketInitializer extends ChannelInitializer<SocketChannel> {

	protected Client client = null;
	protected static Integer FrameSize = 30 * 1024 * 1024;

	public SocketInitializer(Client client) {
		this.client = client;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoderLengthField",
				new LengthFieldBasedFrameDecoder(FrameSize, 0, 4, 0, 4));
		pipeline.addLast("encoderLengthField", new LengthFieldPrepender(4));
		pipeline.addLast("idleStateHandler", new IdleStateHandler(
				Client.IDLE_SECONDS, Client.IDLE_SECONDS,
				Client.IDLE_SECONDS));
		pipeline.addLast("decoder", new SocketDecoder(this.client));
		pipeline.addLast("encoder", new SocketEncoder(this.client));
		pipeline.addLast("handler", new SocketHandler(this.client));
	}

}
