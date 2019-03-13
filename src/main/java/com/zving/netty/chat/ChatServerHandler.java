package com.zving.netty.chat;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务器端业务处理类，继承 ChannelInboundHandlerAdapter，并分别重
 * 写了读取、读取完成、发生异常三个方法
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
	public static List<Channel> channels = new ArrayList<>();

	/**
	 * 通道就绪
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		channels.add(incoming);
		System.out.println("ChatServer :" + incoming.remoteAddress().toString().substring(1) + "上线。。");
	}

	/**
	 * 通道未就绪
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		Channel incoming = ctx.channel();
		channels.remove(incoming);
		System.out.println("ChatServer :" + incoming.remoteAddress().toString().substring(1) + "下线了。。");
	}

	/**
	 * 读取数据
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) {
		Channel incoming = ctx.channel();
		for (Channel channel : channels) {
			if (channel != incoming) { // 排除当前通道
				channel.writeAndFlush(incoming.remoteAddress().toString().substring(1) + " : " + s + "\n");
			}
		}
	}

	/**
	 * 发生异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		Channel incoming = ctx.channel();
		System.out.println("ChatServer :" + incoming.remoteAddress().toString().substring(1) + "异常");
		ctx.close();
	}
}