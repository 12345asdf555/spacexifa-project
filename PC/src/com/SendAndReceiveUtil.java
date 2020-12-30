package com;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
  
public class SendAndReceiveUtil {  
    public static String receiveData(SocketChannel channel) {  
        // TODO Auto-generated method stub  
        // TODO Auto-generated method stub  
        ByteBuffer bb = ByteBuffer.allocate(65536);  
        StringBuilder msg = new StringBuilder();  
        Charset charset = Charset.forName("UTF-8");    
        //CharsetDecoder decoder = charset.newDecoder();  
        try {  
            while( (channel.read(bb) ) > 0 ){  
                bb.flip();        
                msg.append(charset.decode(bb).toString()); 
                //msg.append(decoder.decode(bb).toString());  
                //System.out.println(msg.toString());  
      
                bb.clear();  
            }  
            return msg.toString();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
        	System.out.println("5-����");
            e.printStackTrace();  
        }  
        return null;  
    }  
    public static  void sendData(SocketChannel socketChannel, String msg) {  
        // TODO Auto-generated method stub  
        try {  
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
}  
