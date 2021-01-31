package com.yang.serialport.ui;

import com.alibaba.fastjson.JSONArray;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.sf.json.JSONObject;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;
import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;

public class MainFrame {

    Connection c = null;
    Statement stmt = null;
    // 输出流对象
    OutputStream outputStream;
    public String IP;
    public Server server;
    public Otcserver otcserver;
    public String str = "";
    public String responstr = "";
    public String datesend = "";
    public String msg;
    public String fitemid;
    public NettyServerHandler NS = new NettyServerHandler();
    public Clientconnect clientconnect = new Clientconnect(NS, this);
    public ClientconnectTest clientconnecttest = new ClientconnectTest(NS, this);
    public TcpClientHandler TC = new TcpClientHandler();
    public HashMap<String, SocketChannel> socketlist = new HashMap();
    public int socketcount = 0;

    /**
     * 程序界面宽度
     */
    public static final int WIDTH = 500;

    /**
     * 程序界面高度
     */
    public static final int HEIGHT = 360;

//	public JTextArea dataView = new JTextArea();
//	private JScrollPane scrollDataView = new JScrollPane(dataView);
//
//	// 串口设置面板
//	private JPanel serialPortPanel = new JPanel();
//	private JLabel baudrateLabel = new JLabel("波特率");
//	private JComboBox commChoice = new JComboBox();
//	private JComboBox baudrateChoice = new JComboBox();
//
//	// 操作面板
//	private JPanel operatePanel = new JPanel();
//	private JTextField dataInput = new JTextField();
//	private JButton serialPortOperate = new JButton("停止接收");
//	private JButton sendData = new JButton("开始接收");

    byte[] data;
    public int socketnortype = 0;
    public int sockettetype = 0;
    public int responsetype = 0;
    public int clientcount = 0;
    public boolean Firsttime = true;
    private String ip;
    public ArrayList<String> listarrayJN = new ArrayList<String>();
    ;

    private IsnullUtil iutil;
    private JaxWsDynamicClientFactory dcf;
    private Client client;

    public boolean iffirst = true;

    public MainFrame() {

        //获取服务器IP地址以及组织机构号
        try {
            File file = new File("OTCcom/IPconfig.txt");
//            FileInputStream in = new FileInputStream("IPconfig.txt");
            FileInputStream in = new FileInputStream(file.getCanonicalPath());
            InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
            BufferedReader bufReader = new BufferedReader(inReader);
            String line = null;
            int writetime = 0;

            while ((line = bufReader.readLine()) != null) {
                if (writetime == 0) {
                    ip = line;
                    writetime++;
                } else {
                    fitemid = line;
                    writetime = 0;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("获取PC地址异常");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取PC地址异常");
        }

        if (fitemid.length() != 2) {
            int count = 2 - fitemid.length();
            for (int i = 0; i < count; i++) {
                fitemid = "0" + fitemid;
            }
        }

        NS.fitemid = fitemid;

        //加载界面布局
        //initView();
        //initComponents();

        //webservice配置
        iutil = new IsnullUtil();
        dcf = JaxWsDynamicClientFactory.newInstance();
        client = dcf.createClient("http://" + ip + ":9090/CIWJN_Service/cIWJNWebService?wsdl");
        //client = dcf.createClient("http://" + "119.3.100.103" + ":9090/CIWJN_Service/cIWJNWebService?wsdl");
        iutil.Authority(client);

        //功能实现线程
        Timer tExit1 = null;
        tExit1 = new Timer();
        tExit1.schedule(new TimerTask() {
            @Override
            public void run() {
                ser();
            }
        }, 3000000, 3000000);

        //重置时间
        Timer tExit11 = null;
        tExit11 = new Timer();
        tExit11.schedule(new TimerTask() {
            private String socketfail;
            private String time;

            @Override
            public void run() {
                ser();

                //十分钟发送时间校准
                Calendar d = Calendar.getInstance();
                String year = Integer.toString(d.get(Calendar.YEAR));
                String month = Integer.toString(d.get(Calendar.MONTH) + 1);
                int length2 = 2 - month.length();
                if (length2 != 2) {
                    for (int i = 0; i < length2; i++) {
                        month = "0" + month;
                    }
                }
                String day = Integer.toString(d.get(Calendar.DAY_OF_MONTH));
                int length3 = 2 - day.length();
                if (length3 != 2) {
                    for (int i = 0; i < length3; i++) {
                        day = "0" + day;
                    }
                }
                String hour = Integer.toString(d.get(Calendar.HOUR_OF_DAY));
                int length4 = 2 - hour.length();
                if (length4 != 2) {
                    for (int i = 0; i < length4; i++) {
                        hour = "0" + hour;
                    }
                }
                String minutes = Integer.toString(d.get(Calendar.MINUTE));
                int length5 = 2 - minutes.length();
                if (length5 != 2) {
                    for (int i = 0; i < length5; i++) {
                        minutes = "0" + minutes;
                    }
                }
                String seconds = Integer.toString(d.get(Calendar.SECOND));
                int length6 = 2 - seconds.length();
                if (length6 != 2) {
                    for (int i = 0; i < length6; i++) {
                        seconds = "0" + seconds;
                    }
                }

                String time = "7E0F010101450000" + year + month + day + hour + minutes + seconds + "007D";

                synchronized (socketlist) {

                    ArrayList<String> listarraybuf = new ArrayList<String>();
                    boolean ifdo = false;

                    Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
                    while (webiter.hasNext()) {
                        try {

                            Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                            socketfail = entry.getKey();
                            SocketChannel socketcon = entry.getValue();

                            byte[] b = new byte[time.length() / 2];

                            for (int i = 0; i < time.length(); i += 2) {
                                if (i <= 14 || i >= 30) {
                                    String buf = time.substring(0 + i, 2 + i);
                                    b[i / 2] = (byte) Integer.parseInt(buf, 16);
                                } else {
                                    String buf = time.substring(0 + i, 2 + i);
                                    b[i / 2] = (byte) Integer.parseInt(buf);
                                }
                            }

                            ByteBuf byteBuf = Unpooled.buffer();
                            byteBuf.writeBytes(b);

                            try {
                                if (socketcon.isOpen() && socketcon.isActive() && socketcon.isWritable()) {
                                    socketcon.writeAndFlush(byteBuf).sync();
                                } else {
                                    listarraybuf.add(socketfail);
                                    ifdo = true;
                                }

                            } catch (Exception e) {
                                listarraybuf.add(socketfail);
                                ifdo = true;
                                e.printStackTrace();
                                System.out.println("listarraybuf:"+e.getMessage());
                            }

                        } catch (Exception e) {
                            //client.mainFrame.DateView("数据接收错误" + "\r\n");
                            //webiter = socketlist.entrySet().iterator();
                            e.printStackTrace();
                            System.out.println("Exception:"+e.getMessage());
                        }
                    }

                    //clientconnectTest.mainFrame.DateView(str);

                    if (ifdo) {
                        for (int i = 0; i < listarraybuf.size(); i++) {
                            socketlist.remove(listarraybuf.get(i));
                        }
                    }
                }

            }
        }, 600000, 600000);

        ser();

        //NS.dataView = this.dataView;
    }

    //webservice获取焊工、焊机(采集模块)、任务(包括下发任务)对应id值
    private void ser() {
        // TODO Auto-generated method stub
        try {

            try {
                File file = new File("OTCcom/IPconfig.txt");
                //FileInputStream in = new FileInputStream("IPconfig.txt");
                FileInputStream in = new FileInputStream(file.getCanonicalPath());
                InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
                BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                int writetime = 0;

                while ((line = bufReader.readLine()) != null) {
                    if (writetime == 0) {
                        ip = line;
                        writetime++;
                    }
                }

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //任务下发
            String obj1111 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"getWeldedJunctionAll\"}";
            Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                    new Object[]{obj1111});
            String restr = objects[0].toString();
            JSONArray ary = JSONArray.parseArray(restr);

            ArrayList<String> listarraybuf = new ArrayList<String>();

            synchronized (listarrayJN) {
                for (int i = 0; i < ary.size(); i++) {
                    String str = ary.getString(i);
                    JSONObject js = JSONObject.fromObject(str);

                    if (js.getString("OPERATESTATUS").equals("1")) {
                        listarraybuf.add(js.getString("ID"));
                    }
                }

                if (listarraybuf.size() == 0) {
                    for (int i = 0; i < ary.size(); i++) {
                        String str = ary.getString(i);
                        JSONObject js = JSONObject.fromObject(str);

                        if (js.getString("OPERATESTATUS").equals("0") || js.getString("OPERATESTATUS").equals("2")) {
                            if (listarrayJN.size() == 0) {
                                listarrayJN.add(js.getString("ID"));
                                listarrayJN.add(js.getString("REWELDERID"));
                                listarrayJN.add(js.getString("MACHINEID"));
                                listarrayJN.add(js.getString("OPERATESTATUS"));
                                listarrayJN.add(js.getString("MACHINENO"));
                            } else {
                                int count = 0;
                                for (int i1 = 0; i1 < listarrayJN.size(); i1 += 5) {
                                    if (!listarrayJN.get(i1).equals(js.getString("ID"))) {
                                        count++;
                                        if (count == listarrayJN.size() / 5) {
                                            listarrayJN.add(js.getString("ID"));
                                            listarrayJN.add(js.getString("REWELDERID"));
                                            listarrayJN.add(js.getString("MACHINEID"));
                                            listarrayJN.add(js.getString("OPERATESTATUS"));
                                            listarrayJN.add(js.getString("MACHINENO"));
                                            break;
                                        }
                                    } else if (listarrayJN.get(i1 + 3).equals("0") && js.getString("OPERATESTATUS").equals("2")) {
                                        for (int j = 0; j < 5; j++) {
                                            listarrayJN.remove(i1);
                                        }
                                        listarrayJN.add(js.getString("ID"));
                                        listarrayJN.add(js.getString("REWELDERID"));
                                        listarrayJN.add(js.getString("MACHINEID"));
                                        listarrayJN.add(js.getString("OPERATESTATUS"));
                                        listarrayJN.add(js.getString("MACHINENO"));
                                    }
                                }
                            }
                        }
                    }
                } else if (listarraybuf.size() != 0) {
                    for (int i = 0; i < ary.size(); i++) {
                        String str = ary.getString(i);
                        JSONObject js = JSONObject.fromObject(str);

                        int count1 = 0;
                        for (int l = 0; l < listarraybuf.size(); l++) {
                            if (listarraybuf.get(l).equals(js.getString("ID"))) {
                                break;
                            } else {
                                count1++;
                                if (count1 == listarraybuf.size()) {
                                    if (js.getString("OPERATESTATUS").equals("0") || js.getString("OPERATESTATUS").equals("2")) {
                                        if (listarrayJN.size() == 0) {
                                            listarrayJN.add(js.getString("ID"));
                                            listarrayJN.add(js.getString("REWELDERID"));
                                            listarrayJN.add(js.getString("MACHINEID"));
                                            listarrayJN.add(js.getString("OPERATESTATUS"));
                                            listarrayJN.add(js.getString("MACHINENO"));
                                        } else {
                                            int count = 0;
                                            for (int i1 = 0; i1 < listarrayJN.size(); i1 += 5) {
                                                if (!listarrayJN.get(i1).equals(js.getString("ID"))) {
                                                    count++;
                                                    if (count == listarrayJN.size() / 5) {
                                                        listarrayJN.add(js.getString("ID"));
                                                        listarrayJN.add(js.getString("REWELDERID"));
                                                        listarrayJN.add(js.getString("MACHINEID"));
                                                        listarrayJN.add(js.getString("OPERATESTATUS"));
                                                        listarrayJN.add(js.getString("MACHINENO"));
                                                        break;
                                                    }
                                                } else if (listarrayJN.get(i1 + 3).equals("0") && js.getString("OPERATESTATUS").equals("2")) {
                                                    for (int j = 0; j < 5; j++) {
                                                        listarrayJN.remove(i1);
                                                    }
                                                    listarrayJN.add(js.getString("ID"));
                                                    listarrayJN.add(js.getString("REWELDERID"));
                                                    listarrayJN.add(js.getString("MACHINEID"));
                                                    listarrayJN.add(js.getString("OPERATESTATUS"));
                                                    listarrayJN.add(js.getString("MACHINENO"));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                NS.listarrayJN = listarrayJN;
            }

            //任务编号对应
            String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"getWeldedJunctionAll\"}";
            Object[] objects1 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                    new Object[]{obj1});
            String restr1 = objects1[0].toString();
            JSONArray ary1 = JSONArray.parseArray(restr1);
            ArrayList<String> listjunction = new ArrayList<String>();
            for (int i = 0; i < ary1.size(); i++) {
                String str = ary1.getString(i);
                JSONObject js = JSONObject.fromObject(str);
                listjunction.add(js.getString("ID"));
                listjunction.add(js.getString("TASKNO"));
            }
            NS.listjunction = listjunction;

            //焊工
            String obj11 = "{\"CLASSNAME\":\"welderWebServiceImpl\",\"METHOD\":\"getWelderAll\"}";
            String obj22 = "{\"STR\":\"\"}";
            Object[] objects11 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"),
                    new Object[]{obj11, obj22});
            String restr11 = objects11[0].toString();
            JSONArray ary11 = JSONArray.parseArray(restr11);
            ArrayList<String> listwelder = new ArrayList<String>();
            for (int i = 0; i < ary11.size(); i++) {
                String str = ary11.getString(i);
                JSONObject js = JSONObject.fromObject(str);
                listwelder.add(js.getString("WELDERID"));
                listwelder.add(js.getString("WELDERNO"));
            }
            NS.listwelder = listwelder;

            //焊机
            String obj111 = "{\"CLASSNAME\":\"weldingMachineWebServiceImpl\",\"METHOD\":\"getGatherMachine\"}";
            Object[] objects111 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                    new Object[]{obj111});
            String restr111 = objects111[0].toString();
            JSONArray ary111 = JSONArray.parseArray(restr111);
            ArrayList<String> listweld = new ArrayList<String>();
            for (int i = 0; i < ary111.size(); i++) {
                String str = ary111.getString(i);
                JSONObject js = JSONObject.fromObject(str);
                listweld.add(js.getString("GATHERID"));
                listweld.add(js.getString("GATHERNO"));
                listweld.add(js.getString("MACHINEID"));
                listweld.add(js.getString("MACHINENO"));
            }
            NS.listweld = listweld;

            if (iffirst) {
                new Thread(work).start();
                new Thread(cli).start();
                //new Thread(clitest).start();
                iffirst = false;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            //dataView.append("Webservice未开启" + "\r\n");
            e.printStackTrace();
        }
    }

    //socket连接服务器
    public Runnable cli = new Runnable() {

        private String ip;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                clientconnect.run();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("socket连接服务器异常");
            }
        }
    };

    //socket连接服务器
    public Runnable clitest = new Runnable() {

        private String ip;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                clientconnecttest.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    //开启服务器供焊机连接
    public Runnable work = new Runnable() {

        int count = 0;

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        public void run() {

            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(NS);


                b = b.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel chsoc) throws Exception {
                        synchronized (socketlist) {
                            //编码解码
//						chsoc.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 1, 2, 0, 0));    
//	                	chsoc.pipeline().addLast("frameEncoder", new LengthFieldPrepender(1));
                            //加入编码解码之后,不能加入utf-8编码,加入之后0x80之后的数错误
                            //chsoc.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            //chsoc.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

                            //焊机连接上后,存入list数组中
                            chsoc.pipeline().addLast(NS);
                            socketcount++;
                            socketlist.put(Integer.toString(socketcount), chsoc);
                            TC.socketlist = socketlist;
                        }
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                //绑定端口，等待同步成功
                ChannelFuture f;
                f = b.bind(5555).sync();
//                f = b.bind(5555);
                //等待服务端关闭监听端口
                f.channel().closeFuture().sync();
//                f.channel().closeFuture();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("OTC服务端监听关闭:"+e.getMessage());
            } finally {
                //释放线程池资源
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    };

    public static void main(String args[]) {
        //new MainFrame().setVisible(true);
        new MainFrame();
    }

}  
	 
