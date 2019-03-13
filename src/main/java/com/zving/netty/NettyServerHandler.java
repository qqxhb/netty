package com.zving.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务器端业务处理类，继承 ChannelInboundHandlerAdapter，并分别重
 * 写了读取、读取完成、发生异常三个方法
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	@Override // 读取数据事件
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf buf = (ByteBuf) msg;
		System.out.println("NettyClient : " + buf.toString(CharsetUtil.UTF_8));
	}

	@Override // 数据读取完毕事件
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Hello NettyClient", CharsetUtil.UTF_8));
	}

	@Override // 异常发生事件
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
