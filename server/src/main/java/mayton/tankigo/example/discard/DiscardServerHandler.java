package mayton.tankigo.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a part of http://netty.io/wiki/user-guide-for-4.x.html
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter{

    static Logger logger = LoggerFactory.getLogger(DiscardServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        try{
            StringBuilder sb = new StringBuilder();
            while (bb.isReadable()) { // (1)
                byte b = bb.readByte();
                if (b >= 32 && b < 128) {
                    sb.append((char)b);
                } else {
                    sb.append(String.format("%X02",b));
                }
            }
            logger.info("Message received: {}", sb.toString());
        } finally {
            bb.release();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.toString());
        ctx.close();
    }

}
