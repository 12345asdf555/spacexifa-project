package com;

import java.util.concurrent.TimeUnit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
@Sharable
public class TcpClientHandler extends SimpleChannelInboundHandler{

	public Client client;
	
	public TcpClientHandler(Client client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}
	
	public TcpClientHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
      ctx.channel().close();  
      ctx.close();
      ctx.disconnect();
      ctx.flush();
    }  
	
	@Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
	
      final EventLoop eventLoop = ctx.channel().eventLoop();  
      eventLoop.schedule(new Runnable() {  
    	@Override  
        public void run() {
    		client.createBootstrap(new Bootstrap(),eventLoop);
        }  
      }, 1L, TimeUnit.SECONDS);
      super.channelInactive(ctx);  
    }  
	
}
