package com.yang.serialport.ui;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import javax.swing.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

//接收焊机数据处理（数据采集）
@Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public String ip;
    public String connet;
    public String fitemid;
    public Thread workThread;
    public HashMap<String, Socket> websocket;
    public ArrayList<String> listjunction = new ArrayList<String>();
    public ArrayList<String> listwelder = new ArrayList<String>();
    public ArrayList<String> listweld = new ArrayList<String>();
    public ArrayList<String> listarrayJN = new ArrayList<String>();  //任务、焊工、焊机、状态
    public JTextArea dataView = new JTextArea();
    public SocketChannel chcli = null;
    public SocketChannel chclitest = null;
    public Date timetran;
    public long timetran1;
    public Date time11;
    public long timetran2;
    public Date time22;
    public long timetran3;
    public Date time33;

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;
        byte[] req = null;
        String str = "";
        try {
            buf = (ByteBuf) msg;
            req = new byte[buf.readableBytes()];
            buf.readBytes(req);
//            Workspace ws = new Workspace(req);
//            workThread = new Thread(ws);
//            workThread.setDaemon(true);
//            workThread.start();
            /**
             * 144:实时数据包
             * 11：控制命令返回，密码返回
             * 56：索取返回
             * 12：下发返回
             * 13：白名单下发返回
             */
//            synchronized (this) {
                if (req.length == 144 || req.length == 56 || req.length == 12 || req.length == 11 || req.length == 13) {
                    for (int i = 0; i < req.length; i++) {
                        //判断为数字还是字母，若为字母+256取正数
                        if (req[i] < 0) {
                            String r = Integer.toHexString(req[i] + 256);
                            String rr = r.toUpperCase();
                            //数字补为两位数
                            if (rr.length() == 1) {
                                rr = '0' + rr;
                            }
                            //strdata为总接收数据
                            str += rr;

                        } else {
                            String r = Integer.toHexString(req[i]);
                            if (r.length() == 1)
                                r = '0' + r;
                            r = r.toUpperCase();
                            str += r;
                        }
                    }
                    if (req.length == 56) {
                        System.out.println("索取返回");
                    }
                    if (req.length == 12) {
                        System.out.println("下发返回");
                    }
                }

                if (str.length() >= 6) {
                    //基本版
                    if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22") || str.substring(10, 12).equals("23")) && str.length() == 288) {
                        //str = trans(str);
                        /**
                         * 数据发送
                         * str。length:596
                         */
                        String charStr = "";
                        for (int i = 0; i < 102; i++) {
                            charStr += 0;
                        }
                        String headData = str.substring(0, 44);                    //头部数据
                        String oneData = str.substring(44, 124) + charStr;        //第一组数据
                        String towData = str.substring(124, 204) + charStr;    //第二组数据
                        String threeData = str.substring(204, 284) + charStr;    //第三组数据
                        String footerData = str.substring(284, 286);                //尾部数据

                        str = headData + oneData + towData + threeData + footerData + fitemid + "7D";

                        try {
                            chcli.writeAndFlush(str);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        str = "";
                        //西安版本
                    } else {
                        /**
                         * 索取返回，下发返回，控制命令返回，白名单返回
                         */
                        try {
                            chcli.writeAndFlush(str);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        str = "";
                    }
                }
//            }
            //ctx.flush();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("OTC-Netty-远程关闭一个连接:" + e);
        } finally {
            //内存释放
            ReferenceCountUtil.release(req);
            ReferenceCountUtil.release(buf);
        }
    }

    public class Workspace implements Runnable {

        private byte[] req;
        public String str = "";

        public Workspace(byte[] req) {
            this.req = req;
        }

        @Override
        public void run() {
            try {
                /**
                 * 144:实时数据包
                 * 11：控制命令返回，密码返回
                 * 56：索取返回
                 * 12：下发返回
                 * 13：白名单下发返回
                 */
                synchronized (this) {
                    if (req.length == 144 || req.length == 56 || req.length == 12 || req.length == 11 || req.length == 13) {
                        for (int i = 0; i < req.length; i++) {
                            //判断为数字还是字母，若为字母+256取正数
                            if (req[i] < 0) {
                                String r = Integer.toHexString(req[i] + 256);
                                String rr = r.toUpperCase();
                                //数字补为两位数
                                if (rr.length() == 1) {
                                    rr = '0' + rr;
                                }
                                //strdata为总接收数据
                                str += rr;

                            } else {
                                String r = Integer.toHexString(req[i]);
                                if (r.length() == 1)
                                    r = '0' + r;
                                r = r.toUpperCase();
                                str += r;
                            }
                        }
                    }
//                    else {
//                        str = new String(req);
//                    }

                    if (str.length() >= 6) {
                        //基本版
                        if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22") || str.substring(10, 12).equals("23")) && str.length() == 288) {
                            //str = trans(str);
                            /**
                             * 数据发送
                             * str。length:596
                             */
                            String charStr = "";
                            for (int i = 0; i < 102; i++) {
                                charStr += 0;
                            }
                            String headData = str.substring(0, 44);                    //头部数据
                            String oneData = str.substring(44, 124) + charStr;        //第一组数据
                            String towData = str.substring(124, 204) + charStr;    //第二组数据
                            String threeData = str.substring(204, 284) + charStr;    //第三组数据
                            String footerData = str.substring(284, 286);                //尾部数据

                            str = headData + oneData + towData + threeData + footerData + fitemid + "7D";

                            try {
                                chcli.writeAndFlush(str).sync();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            str = "";

                            //西安版本
                        } else if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22") || str.substring(10, 12).equals("23")) && str.length() == 594) {

                            //str = transxian(str);
                            str = str.substring(0, 592) + fitemid + "7D";

                            try {
                                //发送到转发器
                                chcli.writeAndFlush(str).sync();
                                //chclitest.writeAndFlush(str).sync();
                                //dataView.append(" " + str + "\r\n");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //dataView.append(" " + str + "\r\n");
                                //dataView.setText("服务器未开启" + "\r\n");
                            }

                            str = "";

                            //欧华纬华
                        } else if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22")) && str.length() == 318) {

//                            str = transohwh(str);
                            //str = transOTC(str);
                            //str = transJN(str);
                            str = str.substring(0, 316) + fitemid + "7D";

                            try {
                                chcli.writeAndFlush(str).sync();
                                //dataView.append(" " + str + "\r\n");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                //dataView.append(" " + str + "\r\n");
                                //dataView.setText("服务器未开启" + "\r\n");
                            }

                            str = "";

                            //核五
                        } else if (str.substring(0, 2).equals("FA")) {
                            str = str.substring(0, 106) + fitemid + "F5";
                            try {
                                chcli.writeAndFlush(str).sync();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            str = "";
                        } else {
                            /**
                             * 索取返回，下发返回，控制命令返回，白名单返回
                             */
                            try {
                                chcli.writeAndFlush(str).sync();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            str = "";
                        }
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                //dataView.append("数据接收错误" + "\r\n");
            }
        }

        private String trans(String str) {
            // TODO Auto-generated method stub

            if (str.length() == 288) {

                //校验第一位是否为FA末位是否为F5
                String check1 = str.substring(0, 2);
                String check11 = str.substring(286, 288);
                if (check1.equals("7E") && check11.equals("7D")) {

                    //校验位校验
                    String check3 = str.substring(0, 284);
                    String check5 = "";
                    int check4 = 0;
                    for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                        String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                        check4 += Integer.valueOf(tstr1, 16);
                    }
                    if ((Integer.toHexString(check4)).toUpperCase().length() == 3) {
                        check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                    } else {
                        check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(2, 4);
                    }
                    String check6 = str.substring(284, 286);
                    if (check5.equals(check6)) {

                        StringBuilder sb = new StringBuilder(str);

                        String weld = str.substring(16, 20);
                        String welder = str.substring(40, 44);
                        String junction1 = str.substring(76, 84);
                        String junction2 = str.substring(156, 164);
                        String junction3 = str.substring(236, 244);

                        //江南任务模式
                        if (Integer.parseInt(welder, 16) == 0 && Integer.parseInt(junction1, 16) == 0 && Integer.parseInt(junction2, 16) == 0 && Integer.parseInt(junction3, 16) == 0) {

                            //焊机编号对应id
                            int countweld = 0;
                            String weldid = "";
                            if (listweld.size() == 0) {
                                sb.replace(16, 20, "0000");
                                sb.replace(20, 24, "0000");
                            } else {
                                for (int a = 0; a < listweld.size(); a += 4) {
                                    if (Integer.valueOf(listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                                        String gatherid = listweld.get(a);
                                        weldid = listweld.get(a + 2);

                                        if (gatherid.length() != 4) {
                                            int length = 4 - gatherid.length();
                                            for (int b = 0; b < length; b++) {
                                                gatherid = "0" + gatherid;
                                            }
                                        }

                                        if (weldid.length() != 4) {
                                            int length = 4 - weldid.length();
                                            for (int b = 0; b < length; b++) {
                                                weldid = "0" + weldid;
                                            }
                                        }

                                        sb.replace(16, 20, gatherid);
                                        sb.replace(20, 24, weldid);
                                        countweld = 0;

                                        break;
                                    } else {
                                        countweld++;
                                        if (countweld == listweld.size() / 4) {
                                            sb.replace(16, 20, "0000");
                                            sb.replace(20, 24, "0000");
                                            countweld = 0;
                                        }
                                    }
                                }
                            }

                            //江南任务下发,重置焊工id,焊口(任务id)
                            if (listarrayJN.size() == 0) {
                                sb.replace(40, 44, "0000");
                                sb.replace(76, 84, "00000000");
                                sb.replace(156, 164, "00000000");
                                sb.replace(236, 244, "00000000");
                            } else {
                                sb.replace(40, 44, "0000");
                                sb.replace(76, 84, "00000000");
                                sb.replace(156, 164, "00000000");
                                sb.replace(236, 244, "00000000");
                                String welder1 = "0000";
                                String code = "00000000";
                                for (int i = 0; i < listarrayJN.size(); i += 5) {
                                    if (weldid.equals("")) {
                                        welder1 = "0000";
                                        code = "00000000";
                                    } else {
                                        if (Integer.valueOf(weldid).toString().equals(listarrayJN.get(i + 2))) {
                                            welder1 = listarrayJN.get(i + 1);
                                            if (welder1 != "") {
                                                if (welder1.length() < 4) {
                                                    int length = 4 - welder1.length();
                                                    for (int j = 0; j < length; j++) {
                                                        welder1 = "0" + welder1;
                                                    }
                                                }
                                            } else {
                                                welder1 = "0000";
                                            }

                                            code = listarrayJN.get(i);
                                            if (code != "") {
                                                if (code.length() != 8) {
                                                    int length = 8 - code.length();
                                                    for (int i1 = 0; i1 < length; i1++) {
                                                        code = "0" + code;
                                                    }
                                                }
                                                code.toUpperCase();
                                            } else {
                                                code = "00000000";
                                            }

                                            break;
                                        }
                                    }
                                }
                                sb.replace(40, 44, welder1);
                                sb.replace(76, 84, code);
                                sb.replace(156, 164, code);
                                sb.replace(236, 244, code);
                            }

                            str = sb.toString();

                        } else {

                            //无任务下发模式
                            int countweld = 0;
                            int countwelder = 0;
                            int countjunction = 0;

                            //焊机编号对应id
                            if (listweld.size() == 0) {
                                sb.replace(16, 20, "0000");
                                sb.replace(20, 24, "0000");
                            } else {
                                for (int a = 0; a < listweld.size(); a += 4) {
                                    if (Integer.valueOf(listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                                        String gatherid = listweld.get(a);
                                        String weldid = listweld.get(a + 2);

                                        if (gatherid.length() != 4) {
                                            int length = 4 - gatherid.length();
                                            for (int b = 0; b < length; b++) {
                                                gatherid = "0" + gatherid;
                                            }
                                        }

                                        if (weldid.length() != 4) {
                                            int length = 4 - weldid.length();
                                            for (int b = 0; b < length; b++) {
                                                weldid = "0" + weldid;
                                            }
                                        }

                                        sb.replace(16, 20, gatherid);
                                        sb.replace(20, 24, weldid);
                                        countweld = 0;

                                    } else {
                                        countweld++;
                                        if (countweld == listweld.size() / 4) {
                                            sb.replace(16, 20, "0000");
                                            sb.replace(20, 24, "0000");
                                            countweld = 0;
                                        }
                                    }
                                }
                            }

                            //焊工编号对应id
                            if (listwelder.size() == 0) {
                                sb.replace(40, 44, "0000");
                            } else {
                                sb.replace(40, 44, "0000");
								/*for(int a=0;a<listwelder.size();a+=2){
									if(Integer.valueOf(listwelder.get(a+1)) == (Integer.parseInt(welder,16))){
										String welderid = listwelder.get(a);

										if(welderid.length() != 4){
											int length = 4 - welderid.length();
											for(int b=0;b<length;b++){
												welderid = "0" + welderid;
											}
										}

										sb.replace(40, 44, welderid);
										countwelder = 0;

									}else{
										countwelder++;
										if(countwelder == listwelder.size()/2){
											sb.replace(40, 44, "0000");
											countwelder = 0;
										}
									}
								}*/
                            }

                            //焊口编号对应id(有三组数据的焊口)
                            if (listjunction.size() == 0) {
                                sb.replace(76, 84, "00000000");
                                sb.replace(156, 164, "00000000");
                                sb.replace(236, 244, "00000000");
                            } else {
                                sb.replace(76, 84, "00000000");
                                sb.replace(156, 164, "00000000");
                                sb.replace(236, 244, "00000000");
								/*for(int a=0;a<listjunction.size();a+=2){
									if(Integer.valueOf(listjunction.get(a+1)) == (Integer.parseInt(junction1,16))){
										String junctionid = listjunction.get(a);

										if(junctionid.length() != 8){
											int length = 8 - junctionid.length();
											for(int b=0;b<length;b++){
												junctionid = "0" + junctionid;
											}
										}

										sb.replace(76, 84, junctionid);
										countjunction = 0;

									}else{
										countjunction++;
										if(countjunction == listjunction.size()/2){
											sb.replace(76, 84, "00000000");
											countjunction = 0;
										}
									}
								}

								for(int a=0;a<listjunction.size();a+=2){
									if(Integer.valueOf(listjunction.get(a+1)) == (Integer.parseInt(junction2,16))){
										String junctionid = listjunction.get(a);

										if(junctionid.length() != 8){
											int length = 8 - junctionid.length();
											for(int b=0;b<length;b++){
												junctionid = "0" + junctionid;
											}
										}

										sb.replace(156, 164, junctionid);
										countjunction = 0;

									}else{
										countjunction++;
										if(countjunction == listjunction.size()/2){
											sb.replace(156, 164, "00000000");
											countjunction = 0;
										}
									}
								}

								for(int a=0;a<listjunction.size();a+=2){
									if(Integer.valueOf(listjunction.get(a+1)) == (Integer.parseInt(junction3,16))){
										String junctionid = listjunction.get(a);

										if(junctionid.length() != 8){
											int length = 8 - junctionid.length();
											for(int b=0;b<length;b++){
												junctionid = "0" + junctionid;
											}
										}

										sb.replace(236, 244, junctionid);
										countjunction = 0;

									}else{
										countjunction++;
										if(countjunction == listjunction.size()/2){
											sb.replace(236, 244, "00000000");
											countjunction = 0;
										}
									}
								}*/
                            }
                        }

                        str = sb.toString();

                    }
                }
            }
            return str;
        }

        private String transxian(String str) {
            // TODO Auto-generated method stub

            if (str.length() == 594) {

                StringBuilder sb = new StringBuilder(str);

                String weld = str.substring(16, 20);
                String welder = str.substring(40, 44);
                String junction1 = str.substring(76, 84);
                String junction2 = str.substring(258, 266);
                String junction3 = str.substring(440, 448);

                //无任务下发模式
                int countweld = 0;
                int countwelder = 0;
                int countjunction = 0;

                //焊机编号对应id
                if (listweld.size() == 0) {
                    sb.replace(16, 20, "0000");
                    sb.replace(20, 24, "0000");
                } else {
                    for (int a = 0; a < listweld.size(); a += 4) {
                        if (Integer.valueOf(listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                            String gatherid = listweld.get(a);
                            String weldid = listweld.get(a + 2);

                            if (gatherid.length() != 4) {
                                int length = 4 - gatherid.length();
                                for (int b = 0; b < length; b++) {
                                    gatherid = "0" + gatherid;
                                }
                            }

                            if (weldid.length() != 4) {
                                int length = 4 - weldid.length();
                                for (int b = 0; b < length; b++) {
                                    weldid = "0" + weldid;
                                }
                            }

                            sb.replace(16, 20, gatherid);
                            sb.replace(20, 24, weldid);
                            countweld = 0;

                        } else {
                            countweld++;
                            if (countweld == listweld.size() / 4) {
                                sb.replace(16, 20, "0000");
                                sb.replace(20, 24, "0000");
                                countweld = 0;
                            }
                        }
                    }
                }

                //焊工编号对应id
                if (listwelder.size() == 0) {
                    sb.replace(40, 44, "0000");
                } else {
                    for (int a = 0; a < listwelder.size(); a += 2) {
                        if (Integer.valueOf(listwelder.get(a + 1)) == (Integer.parseInt(welder, 16))) {
                            String welderid = listwelder.get(a);

                            if (welderid.length() != 4) {
                                int length = 4 - welderid.length();
                                for (int b = 0; b < length; b++) {
                                    welderid = "0" + welderid;
                                }
                            }

                            sb.replace(40, 44, welderid);
                            countwelder = 0;

                        } else {
                            countwelder++;
                            if (countwelder == listwelder.size() / 2) {
                                sb.replace(40, 44, "0000");
                                countwelder = 0;
                            }
                        }
                    }
                }

                //焊口编号对应id(有三组数据的焊口)
                if (listjunction.size() == 0) {
                    sb.replace(76, 84, "00000000");
                    sb.replace(258, 266, "00000000");
                    sb.replace(440, 448, "00000000");
                } else {
                    sb.replace(76, 84, "00000000");
                    sb.replace(258, 266, "00000000");
                    sb.replace(440, 448, "00000000");
                }

                str = sb.toString();

            }
            return str;
        }

        private String transohwh(String str) {
            // TODO Auto-generated method stub

            if (str.length() == 318) {

                //校验第一位是否为FA末位是否为F5
                String check1 = str.substring(0, 2);
                String check11 = str.substring(316, 318);
                if (check1.equals("7E") && check11.equals("7D")) {

                    StringBuilder sb = new StringBuilder(str);

                    String weld = str.substring(16, 20);
                    String welder = str.substring(70, 74);
                    String junction1 = str.substring(106, 114);
                    String junction2 = str.substring(186, 194);
                    String junction3 = str.substring(266, 274);

                    //无任务下发模式
                    int countweld = 0;
                    int countwelder = 0;
                    int countjunction = 0;

                    //焊机编号对应id
                    if (listweld.size() == 0) {
                        sb.replace(16, 20, "0000");
                        sb.replace(20, 24, "0000");
                    } else {
                        for (int a = 0; a < listweld.size(); a += 4) {
                            if (Integer.valueOf(listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                                String gatherid = listweld.get(a);
                                String weldid = listweld.get(a + 2);

                                if (gatherid.length() != 4) {
                                    int length = 4 - gatherid.length();
                                    for (int b = 0; b < length; b++) {
                                        gatherid = "0" + gatherid;
                                    }
                                }

                                if (weldid.length() != 4) {
                                    int length = 4 - weldid.length();
                                    for (int b = 0; b < length; b++) {
                                        weldid = "0" + weldid;
                                    }
                                }

                                sb.replace(16, 20, gatherid);
                                sb.replace(20, 24, weldid);
                                countweld = 0;

                            } else {
                                countweld++;
                                if (countweld == listweld.size() / 4) {
                                    sb.replace(16, 20, "0000");
                                    sb.replace(20, 24, "0000");
                                    countweld = 0;
                                }
                            }
                        }
                    }

                    //焊工编号对应id
                    if (listwelder.size() == 0) {
                        sb.replace(70, 74, "0000");
                    } else {
                        for (int a = 0; a < listwelder.size(); a += 2) {
                            if (Integer.valueOf(listwelder.get(a + 1)) == (Integer.parseInt(welder, 16))) {
                                String welderid = listwelder.get(a);

                                if (welderid.length() != 4) {
                                    int length = 4 - welderid.length();
                                    for (int b = 0; b < length; b++) {
                                        welderid = "0" + welderid;
                                    }
                                }

                                sb.replace(70, 74, welderid);
                                countwelder = 0;

                            } else {
                                countwelder++;
                                if (countwelder == listwelder.size() / 2) {
                                    sb.replace(70, 74, "0000");
                                    countwelder = 0;
                                }
                            }
                        }
                    }

                    //焊口编号对应id(有三组数据的焊口)
                    if (listjunction.size() == 0) {
                        sb.replace(106, 114, "00000000");
                        sb.replace(186, 194, "00000000");
                        sb.replace(266, 274, "00000000");
                    } else {
                        for (int a = 0; a < listjunction.size(); a += 2) {
                            if (Integer.valueOf(listjunction.get(a + 1)) == (Integer.parseInt(junction1, 16))) {
                                String junctionid = listjunction.get(a);

                                if (junctionid.length() != 8) {
                                    int length = 8 - junctionid.length();
                                    for (int b = 0; b < length; b++) {
                                        junctionid = "0" + junctionid;
                                    }
                                }

                                sb.replace(106, 114, junctionid);
                                countjunction = 0;

                            } else {
                                countjunction++;
                                if (countjunction == listjunction.size() / 2) {
                                    sb.replace(106, 114, "00000000");
                                    countjunction = 0;
                                }
                            }
                        }

                        for (int a = 0; a < listjunction.size(); a += 2) {
                            if (Integer.valueOf(listjunction.get(a + 1)) == (Integer.parseInt(junction2, 16))) {
                                String junctionid = listjunction.get(a);

                                if (junctionid.length() != 8) {
                                    int length = 8 - junctionid.length();
                                    for (int b = 0; b < length; b++) {
                                        junctionid = "0" + junctionid;
                                    }
                                }

                                sb.replace(186, 194, junctionid);
                                countjunction = 0;

                            } else {
                                countjunction++;
                                if (countjunction == listjunction.size() / 2) {
                                    sb.replace(186, 194, "00000000");
                                    countjunction = 0;
                                }
                            }
                        }

                        for (int a = 0; a < listjunction.size(); a += 2) {
                            if (Integer.valueOf(listjunction.get(a + 1)) == (Integer.parseInt(junction3, 16))) {
                                String junctionid = listjunction.get(a);

                                if (junctionid.length() != 8) {
                                    int length = 8 - junctionid.length();
                                    for (int b = 0; b < length; b++) {
                                        junctionid = "0" + junctionid;
                                    }
                                }

                                sb.replace(266, 274, junctionid);
                                countjunction = 0;

                            } else {
                                countjunction++;
                                if (countjunction == listjunction.size() / 2) {
                                    sb.replace(266, 274, "00000000");
                                    countjunction = 0;
                                }
                            }
                        }
                    }


                    str = sb.toString();

                }
            }
            return str;
        }

        private String transJN(String str) {
            // TODO Auto-generated method stub
            String strdata1 = str;
            String strdata2 = strdata1.replaceAll("7C20", "00");
            String strdata3 = strdata2.replaceAll("7C5E", "7E");
            String strdata4 = strdata3.replaceAll("7C5C", "7C");
            String strdata = strdata4.replaceAll("7C5D", "7D");

            //String weld = Integer.toString(Integer.valueOf(strdata.substring(2,4), 16));
            String weld1 = strdata.substring(6, 8);
            String weld2 = strdata.substring(2, 4);
            String weld = weld1 + weld2;
			/*String weld = Integer.toString(Integer.valueOf(weld1+weld2, 16));
            if(weld.length()<4){
            	int length = 4 - weld.length();
            	for(int i=0;i<length;i++){
            		weld = "0" + weld;
            	}
            }*/

            //江南任务下发
            String welder = "0000";
            String code = "00000000";
            for (int i = 0; i < listarrayJN.size(); i += 5) {
                if (weld.equals(listarrayJN.get(i + 4))) {
                    welder = listarrayJN.get(i + 1);
                    if (welder != "") {
                        welder = Integer.toHexString(Integer.valueOf(welder));
                        if (welder.length() < 4) {
                            int length = 4 - welder.length();
                            for (int j = 0; j < length; j++) {
                                welder = "0" + welder;
                            }
                        }
                    } else {
                        welder = "0000";
                    }

                    code = listarrayJN.get(i);
                    if (code != "") {
                        code = Integer.toHexString(Integer.valueOf(code));
                        if (code.length() != 8) {
                            int length = 8 - code.length();
                            for (int i1 = 0; i1 < length; i1++) {
                                code = "0" + code;
                            }
                        }
                        code.toUpperCase();
                    } else {
                        code = "00000000";
                    }
                }
            }

            String electricity1 = strdata.substring(28, 32);

            String electricity2 = strdata.substring(78, 82);

            String electricity3 = strdata.substring(128, 132);

            String voltage1 = strdata.substring(32, 36);

            String voltage2 = strdata.substring(82, 86);

            String voltage3 = strdata.substring(132, 136);

            //String code = strdata.substring(48,56);

            String status1 = strdata.substring(56, 58);

            String status2 = strdata.substring(106, 108);

            String status3 = strdata.substring(156, 158);

            timetran = new Date();
            timetran1 = timetran.getTime();
            time11 = new Date(timetran1);
            timetran2 = timetran1 + 1000;
            time22 = new Date(timetran2);
            timetran3 = timetran2 + 1000;
            time33 = new Date(timetran3);

            String time1 = DateTools.format("yyMMddHHmmss", time11);
            String time2 = DateTools.format("yyMMddHHmmss", time22);
            String time3 = DateTools.format("yyMMddHHmmss", time33);

            String year1 = time1.substring(0, 2);
            String year161 = Integer.toHexString(Integer.valueOf(year1));
            year161 = year161.toUpperCase();
            if (year161.length() == 1) {
                year161 = '0' + year161;
            }
            String month1 = time1.substring(2, 4);
            String month161 = Integer.toHexString(Integer.valueOf(month1));
            month161 = month161.toUpperCase();
            if (month161.length() == 1) {
                month161 = '0' + month161;
            }
            String day1 = time1.substring(4, 6);
            String day161 = Integer.toHexString(Integer.valueOf(day1));
            day161 = day161.toUpperCase();
            if (day161.length() == 1) {
                day161 = '0' + day161;
            }
            String hour1 = time1.substring(6, 8);
            String hour161 = Integer.toHexString(Integer.valueOf(hour1));
            hour161 = hour161.toUpperCase();
            if (hour161.length() == 1) {
                hour161 = '0' + hour161;
            }
            String minute1 = time1.substring(8, 10);
            String minute161 = Integer.toHexString(Integer.valueOf(minute1));
            minute161 = minute161.toUpperCase();
            if (minute161.length() == 1) {
                minute161 = '0' + minute161;
            }
            String second1 = time1.substring(10, 12);
            String second161 = Integer.toHexString(Integer.valueOf(second1));
            second161 = second161.toUpperCase();
            if (second161.length() == 1) {
                second161 = '0' + second161;
            }

            String year2 = time2.substring(0, 2);
            String year162 = Integer.toHexString(Integer.valueOf(year2));
            year162 = year162.toUpperCase();
            if (year162.length() == 1) {
                year162 = '0' + year162;
            }
            String month2 = time2.substring(2, 4);
            String month162 = Integer.toHexString(Integer.valueOf(month2));
            month162 = month162.toUpperCase();
            if (month162.length() == 1) {
                month162 = '0' + month162;
            }
            String day2 = time2.substring(4, 6);
            String day162 = Integer.toHexString(Integer.valueOf(day2));
            day162 = day162.toUpperCase();
            if (day162.length() == 1) {
                day162 = '0' + day162;
            }
            String hour2 = time2.substring(6, 8);
            String hour162 = Integer.toHexString(Integer.valueOf(hour2));
            hour162 = hour162.toUpperCase();
            if (hour162.length() == 1) {
                hour162 = '0' + hour162;
            }
            String minute2 = time2.substring(8, 10);
            String minute162 = Integer.toHexString(Integer.valueOf(minute2));
            minute162 = minute162.toUpperCase();
            if (minute162.length() == 1) {
                minute162 = '0' + minute162;
            }
            String second2 = time2.substring(10, 12);
            String second162 = Integer.toHexString(Integer.valueOf(second2));
            second162 = second162.toUpperCase();
            if (second162.length() == 1) {
                second162 = '0' + second162;
            }

            String year3 = time3.substring(0, 2);
            String year163 = Integer.toHexString(Integer.valueOf(year3));
            year163 = year163.toUpperCase();
            if (year163.length() == 1) {
                year163 = '0' + year163;
            }
            String month3 = time3.substring(2, 4);
            String month163 = Integer.toHexString(Integer.valueOf(month3));
            month163 = month163.toUpperCase();
            if (month163.length() == 1) {
                month163 = '0' + month163;
            }
            String day3 = time3.substring(4, 6);
            String day163 = Integer.toHexString(Integer.valueOf(day3));
            day163 = day163.toUpperCase();
            if (day163.length() == 1) {
                day163 = '0' + day163;
            }
            String hour3 = time3.substring(6, 8);
            String hour163 = Integer.toHexString(Integer.valueOf(hour3));
            hour163 = hour163.toUpperCase();
            if (hour163.length() == 1) {
                hour163 = '0' + hour163;
            }
            String minute3 = time3.substring(8, 10);
            String minute163 = Integer.toHexString(Integer.valueOf(minute3));
            minute163 = minute163.toUpperCase();
            if (minute163.length() == 1) {
                minute163 = '0' + minute163;
            }
            String second3 = time3.substring(10, 12);
            String second163 = Integer.toHexString(Integer.valueOf(second3));
            second163 = second163.toUpperCase();
            if (second163.length() == 1) {
                second163 = '0' + second163;
            }

            String datesend = "00003101" + weld + welder + code
                    + electricity1 + voltage1 + "0000" + status1 + year161 + month161 + day161 + hour161 + minute161 + second161
                    + electricity2 + voltage2 + "0000" + status2 + year162 + month162 + day162 + hour162 + minute162 + second162
                    + electricity3 + voltage3 + "0000" + status3 + year163 + month163 + day163 + hour163 + minute163 + second163;

            int check = 0;
            byte[] data1 = new byte[datesend.length() / 2];
            for (int i = 0; i < data1.length; i++) {
                String tstr1 = datesend.substring(i * 2, i * 2 + 2);
                Integer k = Integer.valueOf(tstr1, 16);
                check += k;
            }

            String checksend = Integer.toHexString(check);
            int a = checksend.length();
            checksend = checksend.substring(a - 2, a);
            checksend = checksend.toUpperCase();

            datesend = "FA" + datesend + checksend + "F5";
            datesend = datesend.toUpperCase();

            return datesend;
        }

        private String transOTC(String str) {
            // TODO Auto-generated method stub
            String strdata1 = str;
            String strdata2 = strdata1.replaceAll("7C20", "00");
            String strdata3 = strdata2.replaceAll("7C5E", "7E");
            String strdata4 = strdata3.replaceAll("7C5C", "7C");
            String strdata = strdata4.replaceAll("7C5D", "7D");

            String weld1 = strdata.substring(6, 8);
            String weld2 = strdata.substring(2, 4);
            String weld = weld1 + weld2;
            if (weld.length() < 4) {
                int length = 4 - weld.length();
                for (int i = 0; i < length; i++) {
                    weld = "0" + weld;
                }
            }

            String welder1 = Integer.valueOf(strdata.substring(20, 22), 16).toString();
            String welder2 = Integer.valueOf(strdata.substring(22, 24), 16).toString();
            String welder3 = Integer.valueOf(strdata.substring(24, 26), 16).toString();
            String welder4 = Integer.valueOf(strdata.substring(26, 28), 16).toString();
            String welder = welder1 + "," + welder2 + "," + welder3 + "," + welder4;
            StringBuffer sbu = new StringBuffer();
            String[] chars = welder.split(",");
            for (int i = 0; i < chars.length; i++) {
                sbu.append((char) Integer.parseInt(chars[i]));
            }
            welder = Integer.toHexString(Integer.valueOf(sbu.toString()));
            if (welder.length() != 4) {
                int lenth = 4 - welder.length();
                for (int i = 0; i < lenth; i++) {
                    welder = "0" + welder;
                }
            }

            String electricity1 = strdata.substring(28, 32);

            String electricity2 = strdata.substring(78, 82);

            String electricity3 = strdata.substring(128, 132);

            String voltage1 = strdata.substring(32, 36);

            String voltage2 = strdata.substring(82, 86);

            String voltage3 = strdata.substring(132, 136);

            String code = strdata.substring(48, 56);

            String status1 = strdata.substring(56, 58);

            String status2 = strdata.substring(106, 108);

            String status3 = strdata.substring(156, 158);

			/*if(First){
            	timetran = new Date();
            	timetran1 = timetran.getTime();
                time11 = new Date(timetran1);
                timetran2 = timetran1 + 1000;
                time22 = new Date(timetran2);
                timetran3 = timetran2 + 1000;
                time33 = new Date(timetran3);
                timetran1 = timetran3;
            	First = false;
            }else{
            	timetran1 = timetran1 + 1000;
                time11 = new Date(timetran1);
            	timetran2 = timetran1 + 1000;
                time22 = new Date(timetran2);
                timetran3 = timetran2 + 1000;
                time33 = new Date(timetran3);
                timetran1 = timetran3;
            }*/

            timetran = new Date();
            timetran1 = timetran.getTime();
            time11 = new Date(timetran1);
            timetran2 = timetran1 + 1000;
            time22 = new Date(timetran2);
            timetran3 = timetran2 + 1000;
            time33 = new Date(timetran3);

            String time1 = DateTools.format("yyMMddHHmmss", time11);
            String time2 = DateTools.format("yyMMddHHmmss", time22);
            String time3 = DateTools.format("yyMMddHHmmss", time33);

            String year1 = time1.substring(0, 2);
            String year161 = Integer.toHexString(Integer.valueOf(year1));
            year161 = year161.toUpperCase();
            if (year161.length() == 1) {
                year161 = '0' + year161;
            }
            String month1 = time1.substring(2, 4);
            String month161 = Integer.toHexString(Integer.valueOf(month1));
            month161 = month161.toUpperCase();
            if (month161.length() == 1) {
                month161 = '0' + month161;
            }
            String day1 = time1.substring(4, 6);
            String day161 = Integer.toHexString(Integer.valueOf(day1));
            day161 = day161.toUpperCase();
            if (day161.length() == 1) {
                day161 = '0' + day161;
            }
            String hour1 = time1.substring(6, 8);
            String hour161 = Integer.toHexString(Integer.valueOf(hour1));
            hour161 = hour161.toUpperCase();
            if (hour161.length() == 1) {
                hour161 = '0' + hour161;
            }
            String minute1 = time1.substring(8, 10);
            String minute161 = Integer.toHexString(Integer.valueOf(minute1));
            minute161 = minute161.toUpperCase();
            if (minute161.length() == 1) {
                minute161 = '0' + minute161;
            }
            String second1 = time1.substring(10, 12);
            String second161 = Integer.toHexString(Integer.valueOf(second1));
            second161 = second161.toUpperCase();
            if (second161.length() == 1) {
                second161 = '0' + second161;
            }

            String year2 = time2.substring(0, 2);
            String year162 = Integer.toHexString(Integer.valueOf(year2));
            year162 = year162.toUpperCase();
            if (year162.length() == 1) {
                year162 = '0' + year162;
            }
            String month2 = time2.substring(2, 4);
            String month162 = Integer.toHexString(Integer.valueOf(month2));
            month162 = month162.toUpperCase();
            if (month162.length() == 1) {
                month162 = '0' + month162;
            }
            String day2 = time2.substring(4, 6);
            String day162 = Integer.toHexString(Integer.valueOf(day2));
            day162 = day162.toUpperCase();
            if (day162.length() == 1) {
                day162 = '0' + day162;
            }
            String hour2 = time2.substring(6, 8);
            String hour162 = Integer.toHexString(Integer.valueOf(hour2));
            hour162 = hour162.toUpperCase();
            if (hour162.length() == 1) {
                hour162 = '0' + hour162;
            }
            String minute2 = time2.substring(8, 10);
            String minute162 = Integer.toHexString(Integer.valueOf(minute2));
            minute162 = minute162.toUpperCase();
            if (minute162.length() == 1) {
                minute162 = '0' + minute162;
            }
            String second2 = time2.substring(10, 12);
            String second162 = Integer.toHexString(Integer.valueOf(second2));
            second162 = second162.toUpperCase();
            if (second162.length() == 1) {
                second162 = '0' + second162;
            }

            String year3 = time3.substring(0, 2);
            String year163 = Integer.toHexString(Integer.valueOf(year3));
            year163 = year163.toUpperCase();
            if (year163.length() == 1) {
                year163 = '0' + year163;
            }
            String month3 = time3.substring(2, 4);
            String month163 = Integer.toHexString(Integer.valueOf(month3));
            month163 = month163.toUpperCase();
            if (month163.length() == 1) {
                month163 = '0' + month163;
            }
            String day3 = time3.substring(4, 6);
            String day163 = Integer.toHexString(Integer.valueOf(day3));
            day163 = day163.toUpperCase();
            if (day163.length() == 1) {
                day163 = '0' + day163;
            }
            String hour3 = time3.substring(6, 8);
            String hour163 = Integer.toHexString(Integer.valueOf(hour3));
            hour163 = hour163.toUpperCase();
            if (hour163.length() == 1) {
                hour163 = '0' + hour163;
            }
            String minute3 = time3.substring(8, 10);
            String minute163 = Integer.toHexString(Integer.valueOf(minute3));
            minute163 = minute163.toUpperCase();
            if (minute163.length() == 1) {
                minute163 = '0' + minute163;
            }
            String second3 = time3.substring(10, 12);
            String second163 = Integer.toHexString(Integer.valueOf(second3));
            second163 = second163.toUpperCase();
            if (second163.length() == 1) {
                second163 = '0' + second163;
            }

			/*String status1 = "00";
            String status2 = "00";
            String status3 = "00";
            if(electricityint1 != 0){
            	status1 = "03";
            }else if(electricityint2 != 0){
            	status2 = "03";
            }else if(electricityint3 != 0){
            	status3 = "03";
            }*/

            String datesend = "00003101" + weld + welder + code
                    + electricity1 + voltage1 + "0000" + status1 + year161 + month161 + day161 + hour161 + minute161 + second161
                    + electricity2 + voltage2 + "0000" + status2 + year162 + month162 + day162 + hour162 + minute162 + second162
                    + electricity3 + voltage3 + "0000" + status3 + year163 + month163 + day163 + hour163 + minute163 + second163;

            int check = 0;
            byte[] data1 = new byte[datesend.length() / 2];
            for (int i = 0; i < data1.length; i++) {
                String tstr1 = datesend.substring(i * 2, i * 2 + 2);
                Integer k = Integer.valueOf(tstr1, 16);
                check += k;
            }

            String checksend = Integer.toHexString(check);
            int a = checksend.length();
            checksend = checksend.substring(a - 2, a);
            checksend = checksend.toUpperCase();

            datesend = "FA" + datesend + checksend + "F5";
            datesend = datesend.toUpperCase();

            return datesend;
        }
    }


    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
        System.out.println("异常：" + cause);
        ctx.close();
    }

}
