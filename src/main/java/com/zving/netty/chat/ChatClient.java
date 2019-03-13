package com.zving.netty.chat;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 聊天客户端
 */
public class ChatClient {
	private final String host; // 服务器端 IP 地址
	private final int port; // 服务器端端口号

	public ChatClient() {
		this.host = "127.0.0.1";
		this.port = 666;
	}

	public ChatClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
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
							pipeline.addLast("handler", new ChatClientHandler());
						}
					});
			Channel channel = bootstrap.connect(host, port).sync().channel();
			System.out.println("当前用户：" + channel.localAddress().toString().substring(1));
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String msg = scanner.nextLine();
				channel.writeAndFlush(msg + "\r\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new ChatClient().run();
	}
}