package com.zving.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 聊天服务端
 */
public class ChatServer {
	private int port; // 服务器端端口号

	public ChatServer() {
		this.port = 666;
	}

	public ChatServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) {
					/*
					 * 我们往 Pipeline
					 * 链中添加了处理字符串的编码器和解码器，它们加入到 Pipeline 链中后会自动工作，使得我
					 * 们在服务器端读写字符串数据时更加方便（不用人工处理 ByteBuf）。
					 */
					ChannelPipeline pipeline = ch.pipeline(); // 得到 Pipeline 链
					// 往 Pipeline 链中添加一个解码器
					pipeline.addLast("decoder", new StringDecoder());
					// 往 Pipeline 链中添加一个编码器
					pipeline.addLast("encoder", new StringEncoder());
					// 往 Pipeline 链中添加一个自定义的业务处理对象
					pipeline.addLast("handler", new ChatServerHandler());
				}
			}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
			System.out.println("ChatServer Start......");
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			System.out.println("ChatServer Close......");
		}
	}

	public static void main(String[] args) throws Exception {
		new ChatServer().run();
	}
}