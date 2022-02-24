package org.example.netty.chat;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
//        System.out.println("[SERVER]" + incoming.remoteAddress() + " has joined!\n");
//         ctx.writeAndFlush("[SERVER]" + incoming.remoteAddress() + " has joined!\n");
         channels.add(ctx.channel());
        for (Channel channel : channels){
            channel.writeAndFlush("[SERVER]" + incoming.remoteAddress() + " has joined!\n");
        }
//        channels.add(ctx.channel());
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println("[SERVER]" + ctx.channel().remoteAddress() + " has joined!\n");
//        ChannelFuture f = ctx.writeAndFlush("123123123");
////        f.addListener(ChannelFutureListener.CLOSE);
//    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels){
            channel.write("[SERVER]" + incoming.remoteAddress() + " has left!\n");
        }
        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels){
            if (channel != incoming){
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + msg + "\n");
            }
        }
//        for (Channel c: channels) {
//            if (c != ctx.channel()) {
//                c.writeAndFlush("[" + ctx.channel().remoteAddress() + "] " + msg + '\n');
//            } else {
//                c.writeAndFlush("[you] " + msg + '\n');
//            }
//        }
    }
}
