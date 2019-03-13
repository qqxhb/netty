package com.zving.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 客户端业务处理类，继承 ChannelInboundHandlerAdapter ，并分别重
 * 写了通道就绪、通道读取数据、数据读取 、异常发生方法
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 通道就绪
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Hello NettyServer", CharsetUtil.UTF_8));
	}

	/**
	 * 通道读取数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		System.out.println("NettyServer : " + in.toString(CharsetUtil.UTF_8));
	}

	/**
	 * 数据读取完毕
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	/**
	 * 异常发生
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
		cause.printStackTrace();
	}
}