package com;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Websocket {

    Timestamp timesql1;
    Timestamp timesql2;
    Timestamp timesql3;
    private String limit;
    private String connet;
    private String strsend = "";
    private String strdata;
    private SocketChannel chweb;
    private String websocketfail;
    public ArrayList<String> listarray1;
    public ArrayList<String> listarray2;
    public ArrayList<String> listarray3;
    public ArrayList<String> taskarray = new ArrayList<String>();
    private boolean datawritetype = false;
    private HashMap<String, Socket> websocket;
    private HashMap<String, SocketChannel> websocketlist = null;
    public ArrayList<String> dbdata = new ArrayList<String>();
    public MyMqttClient mqtt;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
    private static final SimpleDateFormat sdftime = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    public void Websocketbase(String str) {
        Date time;
        Timestamp timesql = null;
        String cardid = "0000";
        String wpsid = "0000";
        String productid = "0000";
        String workprocedureid = "0000";
        String workstepid = "0000";
        if (str.length() == 596) {
            //前端实时数据处理
            String check1 = str.substring(0, 2);
            String check11 = str.substring(594, 596);
            if (check1.equals("7E") && check11.equals("7D")) {
                String weldid = "0000";//焊机id
                String welderno = String.valueOf(Long.parseLong(str.substring(40, 44), 16));//焊工号
                if (welderno.length() < 5) {
                    int lenth = 5 - welderno.length();
                    for (int i = 0; i < lenth; i++) {
                        welderno = "0" + welderno;
                    }
                }
                String gatherno = String.valueOf(Long.parseLong(str.substring(16, 20), 16));//采集模块编号
                if (gatherno.length() < 4) {
                    int lenth = 4 - gatherno.length();
                    for (int i = 0; i < lenth; i++) {
                        gatherno = "0" + gatherno;
                    }
                }
                String itemins = Integer.valueOf(str.substring(592, 594)).toString();//组织id
                if (itemins.length() < 4) {
                    int lenth = 4 - itemins.length();
                    for (int i = 0; i < lenth; i++) {
                        itemins = "0" + itemins;
                    }
                }
                String weldmodel = Integer.valueOf(str.substring(12, 14), 16).toString();//焊机型号
                if (weldmodel.length() < 4) {
                    int lenth = 4 - weldmodel.length();
                    for (int i = 0; i < lenth; i++) {
                        weldmodel = "0" + weldmodel;
                    }
                }

                for (int a = 0; a < 365; a += 182) {
                    try {
                        String ins = "0000";//焊机组织id
                        String welderid = "0";
                        String junctionNo = Integer.valueOf(str.substring(76 + a, 84 + a), 16).toString();//焊口号(焊缝编号)
                        if (junctionNo.length() < 8) {
                            int lenth = 8 - junctionNo.length();
                            for (int i = 0; i < lenth; i++) {
                                junctionNo = "0" + junctionNo;
                            }
                        }
                        String electricity = Integer.valueOf(str.substring(56 + a, 60 + a), 16).toString();//实际电流
                        if (electricity.length() < 4) {
                            int lenth = 4 - electricity.length();
                            for (int i = 0; i < lenth; i++) {
                                electricity = "0" + electricity;
                            }
                        }
                        String voltage = Integer.valueOf(str.substring(60 + a, 64 + a), 16).toString();//实际电压
                        if (voltage.length() < 4) {
                            int lenth = 4 - voltage.length();
                            for (int i = 0; i < lenth; i++) {
                                voltage = "0" + voltage;
                            }
                        }
                        String speed = Integer.valueOf(str.substring(64 + a, 68 + a), 16).toString();//送丝速度
                        if (speed.length() < 4) {
                            int lenth = 4 - speed.length();
                            for (int i = 0; i < lenth; i++) {
                                speed = "0" + speed;
                            }
                        }
                        String setelectricity = Integer.valueOf(str.substring(68 + a, 72 + a), 16).toString();//给定电流
                        if (setelectricity.length() < 4) {
                            int lenth = 4 - setelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                setelectricity = "0" + setelectricity;
                            }
                        }
                        String setvoltage = Integer.valueOf(str.substring(72 + a, 76 + a), 16).toString();//给定电压
                        if (setvoltage.length() < 4) {
                            int lenth = 4 - setvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                setvoltage = "0" + setvoltage;
                            }
                        }
                        String status = Integer.valueOf(str.substring(84 + a, 86 + a), 16).toString();//报警信息
                        if (status.length() < 2) {
                            int lenth = 2 - status.length();
                            for (int i = 0; i < lenth; i++) {
                                status = "0" + status;
                            }
                        }
                        String year = Integer.valueOf(str.substring(44 + a, 46 + a), 16).toString();
                        String month = Integer.valueOf(str.substring(46 + a, 48 + a), 16).toString();
                        String day = Integer.valueOf(str.substring(48 + a, 50 + a), 16).toString();
                        String hour = Integer.valueOf(str.substring(50 + a, 52 + a), 16).toString();
                        String minute = Integer.valueOf(str.substring(52 + a, 54 + a), 16).toString();
                        String second = Integer.valueOf(str.substring(54 + a, 56 + a), 16).toString();
                        String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        try {
                            String date = year + "-" + month + "-" + day;
                            String nowdatetime = sdf.format(System.currentTimeMillis());//当前系统时间
                            if (!date.equals(nowdatetime)) {
                                strdate = sdftime.format(System.currentTimeMillis());
                            }
                            time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                            timesql = new Timestamp(time.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String channel = Integer.valueOf(str.substring(106 + a, 108 + a), 16).toString();//通道号
                        if (channel.length() < 4) {
                            int lenth = 4 - channel.length();
                            for (int i = 0; i < lenth; i++) {
                                channel = "0" + channel;
                            }
                        }
                        String maxelectricity = Integer.valueOf(str.substring(90 + a, 94 + a), 16).toString();
                        if (maxelectricity.length() < 4) {
                            int lenth = 4 - maxelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                maxelectricity = "0" + maxelectricity;
                            }
                        }
                        String minelectricity = Integer.valueOf(str.substring(94 + a, 98 + a), 16).toString();
                        if (minelectricity.length() < 4) {
                            int lenth = 4 - minelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                minelectricity = "0" + minelectricity;
                            }
                        }
                        String maxvoltage = Integer.valueOf(str.substring(98 + a, 102 + a), 16).toString();
                        if (maxvoltage.length() < 4) {
                            int lenth = 4 - maxvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                maxvoltage = "0" + maxvoltage;
                            }
                        }
                        String minvoltage = Integer.valueOf(str.substring(102 + a, 106 + a), 16).toString();
                        if (minvoltage.length() < 4) {
                            int lenth = 4 - minvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                minvoltage = "0" + minvoltage;
                            }
                        }
                        //焊工信息
                        for (int i = 0; i < listarray1.size(); i += 3) {
                            if (String.valueOf(("0090"+welderno)).equals(listarray1.get(i + 1))) {
                                welderno = listarray1.get(i + 1);//焊工编号
                                welderid = listarray1.get(i);//焊工id
                                break;
                            }
                        }
                        //焊机信息
                        for (int i = 0; i < listarray2.size(); i += 4) {
                            //采集模块编号判断
                            if (String.valueOf(Integer.parseInt(gatherno)).equals(listarray2.get(i + 2))) {
                                weldid = listarray2.get(i);//焊机id
                                ins = listarray2.get(i + 3);//焊机组织id
                                break;
                            }
                        }
                        //判断手持终端是否下发了任务信息
                        if (!taskarray.isEmpty()) {
                            if (taskarray.contains("m-" + gatherno)) {
                                int index = taskarray.indexOf("m-" + gatherno);
                                //welderid = taskarray.get(index + 1);//焊工id
                                cardid = taskarray.get(index + 2);//电子跟踪卡id
                                wpsid = taskarray.get(index + 3);//工艺id
                                productid = taskarray.get(index + 4);//产品号id
                                workprocedureid = taskarray.get(index + 6);//工步号id
                                workstepid = taskarray.get(index + 7);//焊缝号id
                            }
                        }
                        if (welderid.length() < 4) {
                            int lenth = 4 - welderid.length();
                            for (int m = 0; m < lenth; m++) {
                                welderid = "0" + welderid;
                            }
                        }
                        if (weldid.length() < 4) {
                            int lenth = 4 - weldid.length();
                            for (int m = 0; m < lenth; m++) {
                                weldid = "0" + weldid;
                            }
                        }
                        if (cardid.length() < 4) {
                            int lenth = 4 - cardid.length();
                            for (int i1 = 0; i1 < lenth; i1++) {
                                cardid = "0" + cardid;
                            }
                        }

                        if (wpsid.length() < 4) {
                            int lenth = 4 - wpsid.length();
                            for (int i1 = 0; i1 < lenth; i1++) {
                                wpsid = "0" + wpsid;
                            }
                        }
                        if (productid.length() < 4) {
                            int lenth = 4 - productid.length();
                            for (int i1 = 0; i1 < lenth; i1++) {
                                productid = "0" + productid;
                            }
                        }
                        if (workprocedureid.length() < 4) {
                            int lenth = 4 - workprocedureid.length();
                            for (int i1 = 0; i1 < lenth; i1++) {
                                workprocedureid = "0" + workprocedureid;
                            }
                        }
                        if (workstepid.length() < 4) {
                            int lenth = 4 - workstepid.length();
                            for (int i1 = 0; i1 < lenth; i1++) {
                                workstepid = "0" + workstepid;
                            }
                        }
                        if (ins.length() < 4) {
                            int lenth = 4 - ins.length();
                            for (int i = 0; i < lenth; i++) {
                                ins = "0" + ins;
                            }
                        }
                        String gasflow = Integer.valueOf(str.substring(108 + a, 112 + a), 16).toString();
                        if (gasflow.length() < 4) {
                            int lenth = 4 - gasflow.length();
                            for (int i = 0; i < lenth; i++) {
                                gasflow = "0" + gasflow;
                            }
                        }
                        String weld_speed = Integer.valueOf(str.substring(112 + a, 116 + a), 16).toString();
                        if (weld_speed.length() < 4) {
                            int lenth = 4 - weld_speed.length();
                            for (int i = 0; i < lenth; i++) {
                                weld_speed = "0" + weld_speed;
                            }
                        }
                        String lon_air_flow = Integer.valueOf(str.substring(116 + a, 120 + a), 16).toString();
                        if (lon_air_flow.length() < 4) {
                            int lenth = 4 - lon_air_flow.length();
                            for (int i = 0; i < lenth; i++) {
                                lon_air_flow = "0" + lon_air_flow;
                            }
                        }
                        String hatwire_current = Integer.valueOf(str.substring(120 + a, 124 + a), 16).toString();
                        if (hatwire_current.length() < 4) {
                            int lenth = 4 - hatwire_current.length();
                            for (int i = 0; i < lenth; i++) {
                                hatwire_current = "0" + hatwire_current;
                            }
                        }
                        String laser_power = Integer.valueOf(str.substring(136 + a, 140 + a), 16).toString();
                        if (laser_power.length() < 4) {
                            int lenth = 4 - laser_power.length();
                            for (int i = 0; i < lenth; i++) {
                                laser_power = "0" + laser_power;
                            }
                        }
                        strsend = strsend + welderid + weldid + gatherno + junctionNo + gasflow + ins + itemins + weldmodel + status + electricity +
                                voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + speed + cardid +
                                wpsid + productid + workprocedureid + workstepid + weld_speed + lon_air_flow + hatwire_current + laser_power;

                        String strstrsend = welderid + weldid + gatherno + junctionNo + gasflow + ins + itemins + weldmodel + status + electricity +
                                voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + speed + cardid +
                                wpsid + productid + workprocedureid + workstepid + weld_speed + lon_air_flow + hatwire_current + laser_power;

                        //焊工id、焊机id、采集编号、焊口号(8位)、保护气流量、焊机组织id、组织id（配置文件）、焊机型号（值）、报警信息、焊接电流、焊接电压、给定电流、给定电压、
                        //焊机工作时间、最大电流、最小电流、最大电压、最小电压、通道号、送丝速度、电子跟踪卡id（工作号id/工票id）、
                        //工艺id、产品号id、工步号id、焊缝号id、焊机速度、离子气流量、热丝电流、激光功率
                        //0000000000610000000100000000001700000000000000019001902021-01-12 16:57:32.0024001400240014000000000000000000000000000000000000000000000
                        //0000000001140000000100000000001700000000000000019001902021-01-12 16:57:32.0024001400240014000000000000000000000000000000000000000000000
                        //0000000000670000000100000000001700000000000000019001902021-01-12 16:57:32.0024001400240014000000000000000000000000000000000000000000000
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("字节码解析异常：" + e);
                    }
                }
                //MQTT处理,发送到前端(length:405)
                mqtt.publishMessage("weldmesrealdata", strsend, 0);
                strsend = "";
            }
        }
    }


    public void Websocketrun(String str, ArrayList<String> listarray2, ArrayList<String> listarray3, HashMap<String, SocketChannel> websocketlist) {
        this.strdata = str;
        try {

            //閺冪姷鏁ら幋鐤箾閹恒儲妞傛晶鐐插缂佺喕顓搁悞濠冩簚瀹搞儰缍旈弮鍫曟？
            if (websocketlist == null || websocketlist.isEmpty()) {

            } else {
                if (str.length() == 170) {

                    String check1 = str.substring(0, 2);
                    String check11 = str.substring(168, 170);
                    if (check1.equals("FA") && check11.equals("F5")) {

                        int check2 = str.length();
                        if (check2 == 170) {

                            String check3 = str.substring(2, 164);
                            String check5 = "";
                            int check4 = 0;
                            for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                                String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                                check4 += Integer.valueOf(tstr1, 16);
                            }
                            if ((Integer.toHexString(check4)).toUpperCase().length() == 2) {
                                check5 = ((Integer.toHexString(check4)).toUpperCase());
                            } else {
                                check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                            }
                            String check6 = str.substring(164, 166);
                            if (check5.equals(check6)) {

                                //System.out.println("2");

                                strdata = str;
                                //String weldname = strdata.substring(10,14);
                                int weldname1 = Integer.valueOf(strdata.subSequence(10, 14).toString(), 16);
                                String weldname = String.valueOf(weldname1);
                                if (weldname.length() != 4) {
                                    int lenth = 4 - weldname.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldname = "0" + weldname;
                                    }
                                }

                                int welder1 = Integer.valueOf(strdata.subSequence(14, 18).toString(), 16);
                                String welder = String.valueOf(welder1);
                                if (welder.length() != 4) {
                                    int lenth = 4 - welder.length();
                                    for (int i = 0; i < lenth; i++) {
                                        welder = "0" + welder;
                                    }
                                }

                                //String code = strdata.substring(18,26);
                                long code1 = Integer.valueOf(strdata.subSequence(18, 26).toString(), 16);
                                String code = String.valueOf(code1);
                                if (code.length() != 8) {
                                    int lenth = 8 - code.length();
                                    for (int i = 0; i < lenth; i++) {
                                        code = "0" + code;
                                    }
                                }

                                int electricity11 = Integer.valueOf(strdata.subSequence(26, 30).toString(), 16);
                                String electricity1 = String.valueOf(electricity11);
                                if (electricity1.length() != 4) {
                                    int lenth = 4 - electricity1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity1 = "0" + electricity1;
                                    }
                                }

                                int voltage11 = Integer.valueOf(strdata.subSequence(30, 34).toString(), 16);
                                String voltage1 = String.valueOf(voltage11);
                                if (voltage1.length() != 4) {
                                    int lenth = 4 - voltage1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage1 = "0" + voltage1;
                                    }
                                }

                                String status1 = Integer.valueOf(strdata.substring(38, 40), 16).toString();
                                if (status1.length() != 2) {
                                    int lenth = 2 - status1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status1 = "0" + status1;
                                    }
                                }


                                int wirefeedrate11 = Integer.valueOf(strdata.subSequence(40, 44).toString(), 16);
                                String wirefeedrate1 = String.valueOf(wirefeedrate11);
                                if (wirefeedrate1.length() != 4) {
                                    int lenth = 4 - wirefeedrate1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        wirefeedrate1 = "0" + wirefeedrate1;
                                    }
                                }
                                int weldingrate11 = Integer.valueOf(strdata.subSequence(40, 48).toString(), 16);
                                String weldingrate1 = String.valueOf(weldingrate11);
                                if (weldingrate1.length() != 4) {
                                    int lenth = 4 - weldingrate1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldingrate1 = "0" + weldingrate1;
                                    }
                                }
                                int weldheatinput11 = Integer.valueOf(strdata.subSequence(48, 52).toString(), 16);
                                String weldheatinput1 = String.valueOf(weldheatinput11);
                                if (weldheatinput1.length() != 4) {
                                    int lenth = 4 - weldheatinput1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldheatinput1 = "0" + weldheatinput1;
                                    }
                                }
                                int hatwirecurrent11 = Integer.valueOf(strdata.subSequence(52, 56).toString(), 16);
                                String hatwirecurrent1 = String.valueOf(hatwirecurrent11);
                                if (hatwirecurrent1.length() != 4) {
                                    int lenth = 4 - hatwirecurrent1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hatwirecurrent1 = "0" + hatwirecurrent1;
                                    }
                                }
                                int vibrafrequency11 = Integer.valueOf(strdata.subSequence(56, 60).toString(), 16);
                                String vibrafrequency1 = String.valueOf(vibrafrequency11);
                                if (vibrafrequency1.length() != 4) {
                                    int lenth = 4 - vibrafrequency1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        vibrafrequency1 = "0" + vibrafrequency1;
                                    }
                                }


                                long year1 = Integer.valueOf(str.subSequence(60, 62).toString(), 16);
                                String yearstr1 = String.valueOf(year1);
                                long month1 = Integer.valueOf(str.subSequence(62, 64).toString(), 16);
                                String monthstr1 = String.valueOf(month1);
                                if (monthstr1.length() != 2) {
                                    int lenth = 2 - monthstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr1 = "0" + monthstr1;
                                    }
                                }
                                long day1 = Integer.valueOf(str.subSequence(64, 66).toString(), 16);
                                String daystr1 = String.valueOf(day1);
                                if (daystr1.length() != 2) {
                                    int lenth = 2 - daystr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr1 = "0" + daystr1;
                                    }
                                }
                                long hour1 = Integer.valueOf(str.subSequence(66, 68).toString(), 16);
                                String hourstr1 = String.valueOf(hour1);
                                if (hourstr1.length() != 2) {
                                    int lenth = 2 - hourstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr1 = "0" + hourstr1;
                                    }
                                }
                                long minute1 = Integer.valueOf(str.subSequence(68, 70).toString(), 16);
                                String minutestr1 = String.valueOf(minute1);
                                if (minutestr1.length() != 2) {
                                    int lenth = 2 - minutestr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr1 = "0" + minutestr1;
                                    }
                                }
                                long second1 = Integer.valueOf(str.subSequence(70, 72).toString(), 16);
                                String secondstr1 = String.valueOf(second1);
                                if (secondstr1.length() != 2) {
                                    int lenth = 2 - secondstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr1 = "0" + secondstr1;
                                    }
                                }

                                String timestr1 = yearstr1 + "-" + monthstr1 + "-" + daystr1 + " " + hourstr1 + ":" + minutestr1 + ":" + secondstr1;
                                SimpleDateFormat timeshow1 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time1 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr1);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql1 = new Timestamp(time1.getTime());

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                int electricity22 = Integer.valueOf(strdata.subSequence(72, 76).toString(), 16);
                                String electricity2 = String.valueOf(electricity22);
                                if (electricity2.length() != 4) {
                                    int lenth = 4 - electricity2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity2 = "0" + electricity2;
                                    }
                                }

                                int voltage22 = Integer.valueOf(strdata.subSequence(76, 80).toString(), 16);
                                String voltage2 = String.valueOf(voltage22);
                                if (voltage2.length() != 4) {
                                    int lenth = 4 - voltage2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage2 = "0" + voltage2;
                                    }
                                }

                                String status2 = Integer.valueOf(strdata.substring(84, 86), 16).toString();
                                if (status2.length() != 2) {
                                    int lenth = 2 - status2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status2 = "0" + status2;
                                    }
                                }

                                int wirefeedrate22 = Integer.valueOf(strdata.subSequence(86, 90).toString(), 16);
                                String wirefeedrate2 = String.valueOf(wirefeedrate22);
                                if (wirefeedrate2.length() != 4) {
                                    int lenth = 4 - wirefeedrate2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        wirefeedrate2 = "0" + wirefeedrate2;
                                    }
                                }
                                int weldingrate22 = Integer.valueOf(strdata.subSequence(90, 94).toString(), 16);
                                String weldingrate2 = String.valueOf(weldingrate22);
                                if (weldingrate2.length() != 4) {
                                    int lenth = 4 - weldingrate2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldingrate2 = "0" + weldingrate2;
                                    }
                                }
                                int weldheatinput22 = Integer.valueOf(strdata.subSequence(94, 98).toString(), 16);
                                String weldheatinput2 = String.valueOf(weldheatinput22);
                                if (weldheatinput2.length() != 4) {
                                    int lenth = 4 - weldheatinput2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldheatinput2 = "0" + weldheatinput2;
                                    }
                                }
                                int hatwirecurrent22 = Integer.valueOf(strdata.subSequence(98, 102).toString(), 16);
                                String hatwirecurrent2 = String.valueOf(hatwirecurrent22);
                                if (hatwirecurrent2.length() != 4) {
                                    int lenth = 4 - hatwirecurrent2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hatwirecurrent2 = "0" + hatwirecurrent2;
                                    }
                                }
                                int vibrafrequency22 = Integer.valueOf(strdata.subSequence(102, 106).toString(), 16);
                                String vibrafrequency2 = String.valueOf(vibrafrequency22);
                                if (vibrafrequency2.length() != 4) {
                                    int lenth = 4 - vibrafrequency2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        vibrafrequency2 = "0" + vibrafrequency2;
                                    }
                                }


                                long year2 = Integer.valueOf(str.subSequence(106, 108).toString(), 16);
                                String yearstr2 = String.valueOf(year2);
                                long month2 = Integer.valueOf(str.subSequence(108, 110).toString(), 16);
                                String monthstr2 = String.valueOf(month2);
                                if (monthstr2.length() != 2) {
                                    int lenth = 2 - monthstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr2 = "0" + monthstr2;
                                    }
                                }
                                long day2 = Integer.valueOf(str.subSequence(110, 112).toString(), 16);
                                String daystr2 = String.valueOf(day2);
                                if (daystr2.length() != 2) {
                                    int lenth = 2 - daystr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr2 = "0" + daystr2;
                                    }
                                }
                                long hour2 = Integer.valueOf(str.subSequence(112, 114).toString(), 16);
                                String hourstr2 = String.valueOf(hour2);
                                if (hourstr2.length() != 2) {
                                    int lenth = 2 - hourstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr2 = "0" + hourstr2;
                                    }
                                }
                                long minute2 = Integer.valueOf(str.subSequence(114, 116).toString(), 16);
                                String minutestr2 = String.valueOf(minute2);
                                if (minutestr2.length() != 2) {
                                    int lenth = 2 - minutestr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr2 = "0" + minutestr2;
                                    }
                                }
                                long second2 = Integer.valueOf(str.subSequence(116, 118).toString(), 16);
                                String secondstr2 = String.valueOf(second2);
                                if (secondstr2.length() != 2) {
                                    int lenth = 2 - secondstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr2 = "0" + secondstr2;
                                    }
                                }

                                String timestr2 = yearstr2 + "-" + monthstr2 + "-" + daystr2 + " " + hourstr2 + ":" + minutestr2 + ":" + secondstr2;
                                SimpleDateFormat timeshow2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time2 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr2);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql2 = new Timestamp(time2.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                int electricity33 = Integer.valueOf(strdata.subSequence(118, 122).toString(), 16);
                                String electricity3 = String.valueOf(electricity33);
                                if (electricity3.length() != 4) {
                                    int lenth = 4 - electricity3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity3 = "0" + electricity3;
                                    }
                                }

                                int voltage33 = Integer.valueOf(strdata.subSequence(122, 126).toString(), 16);
                                String voltage3 = String.valueOf(voltage33);
                                if (voltage3.length() != 4) {
                                    int lenth = 4 - voltage3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage3 = "0" + voltage3;
                                    }
                                }
                                String status3 = Integer.valueOf(strdata.substring(130, 132), 16).toString();
                                if (status3.length() != 2) {
                                    int lenth = 2 - status3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status3 = "0" + status3;
                                    }
                                }

                                int wirefeedrate33 = Integer.valueOf(strdata.subSequence(132, 136).toString(), 16);
                                String wirefeedrate3 = String.valueOf(wirefeedrate33);
                                if (wirefeedrate3.length() != 4) {
                                    int lenth = 4 - wirefeedrate3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        wirefeedrate3 = "0" + wirefeedrate3;
                                    }
                                }
                                int weldingrate33 = Integer.valueOf(strdata.subSequence(136, 140).toString(), 16);
                                String weldingrate3 = String.valueOf(weldingrate33);
                                if (weldingrate3.length() != 4) {
                                    int lenth = 4 - weldingrate3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldingrate3 = "0" + weldingrate3;
                                    }
                                }
                                int weldheatinput33 = Integer.valueOf(strdata.subSequence(140, 144).toString(), 16);
                                String weldheatinput3 = String.valueOf(weldheatinput33);
                                if (weldheatinput3.length() != 4) {
                                    int lenth = 4 - weldheatinput3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldheatinput3 = "0" + weldheatinput3;
                                    }
                                }
                                int hatwirecurrent33 = Integer.valueOf(strdata.subSequence(144, 148).toString(), 16);
                                String hatwirecurrent3 = String.valueOf(hatwirecurrent33);
                                if (hatwirecurrent3.length() != 4) {
                                    int lenth = 4 - hatwirecurrent3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hatwirecurrent3 = "0" + hatwirecurrent3;
                                    }
                                }
                                int vibrafrequency33 = Integer.valueOf(strdata.subSequence(148, 152).toString(), 16);
                                String vibrafrequency3 = String.valueOf(vibrafrequency33);
                                if (vibrafrequency3.length() != 4) {
                                    int lenth = 4 - vibrafrequency3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        vibrafrequency3 = "0" + vibrafrequency3;
                                    }
                                }


                                long year3 = Integer.valueOf(str.subSequence(152, 154).toString(), 16);
                                String yearstr3 = String.valueOf(year3);
                                long month3 = Integer.valueOf(str.subSequence(154, 156).toString(), 16);
                                String monthstr3 = String.valueOf(month3);
                                if (monthstr3.length() != 2) {
                                    int lenth = 2 - monthstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr3 = "0" + monthstr3;
                                    }
                                }
                                long day3 = Integer.valueOf(str.subSequence(156, 158).toString(), 16);
                                String daystr3 = String.valueOf(day3);
                                if (daystr3.length() != 2) {
                                    int lenth = 2 - daystr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr3 = "0" + daystr3;
                                    }
                                }
                                long hour3 = Integer.valueOf(str.subSequence(158, 160).toString(), 16);
                                String hourstr3 = String.valueOf(hour3);
                                if (hourstr3.length() != 2) {
                                    int lenth = 2 - hourstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr3 = "0" + hourstr3;
                                    }
                                }
                                long minute3 = Integer.valueOf(str.subSequence(160, 162).toString(), 16);
                                String minutestr3 = String.valueOf(minute3);
                                if (minutestr3.length() != 2) {
                                    int lenth = 2 - minutestr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr3 = "0" + minutestr3;
                                    }
                                }
                                long second3 = Integer.valueOf(str.subSequence(162, 164).toString(), 16);
                                String secondstr3 = String.valueOf(second3);
                                if (secondstr3.length() != 2) {
                                    int lenth = 2 - secondstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr3 = "0" + secondstr3;
                                    }
                                }

                                String timestr3 = yearstr3 + "-" + monthstr3 + "-" + daystr3 + " " + hourstr3 + ":" + minutestr3 + ":" + secondstr3;
                                SimpleDateFormat timeshow3 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {
                                    Date time3 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr3);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql3 = new Timestamp(time3.getTime());
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                //System.out.println("3");

                                try {

                                    for (int i = 0; i < listarray3.size(); i += 5) {
                                        String weldjunction = listarray3.get(i);
                                        if (weldjunction.equals(code)) {
                                            String maxe = listarray3.get(i + 1);
                                            String mixe = listarray3.get(i + 2);
                                            String maxv = listarray3.get(i + 3);
                                            String mixv = listarray3.get(i + 4);
                                            limit = maxe + mixe + maxv + mixv;
                                        }
                                    }

                                    for (int i = 0; i < listarray2.size(); i += 4) {
                                        String fequipment_no = listarray2.get(i);
                                        String fgather_no = listarray2.get(i + 2);
                                        String finsframework_id = listarray2.get(i + 3);
                                        if (weldname.equals(fgather_no)) {
                                            try {
                                                if (fequipment_no.length() != 4) {
                                                    int lenth = 4 - fequipment_no.length();
                                                    for (int i1 = 0; i1 < lenth; i1++) {
                                                        fequipment_no = "0" + fequipment_no;
                                                    }
                                                }
                                                if (weldname.equals(fgather_no)) {
                                                    if (finsframework_id == null || finsframework_id == "") {
                                                        finsframework_id = "nu";
                                                        strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + wirefeedrate1 + weldingrate1 + weldheatinput1 + hatwirecurrent1 + vibrafrequency1 + timesql1 + limit + "00:00:00" + "00:00:00" + code
                                                                + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + wirefeedrate2 + weldingrate2 + weldheatinput2 + hatwirecurrent2 + vibrafrequency2 + timesql2 + limit + "00:00:00" + "00:00:00" + code
                                                                + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + wirefeedrate3 + weldingrate3 + weldheatinput3 + hatwirecurrent3 + vibrafrequency3 + timesql3 + limit + "00:00:00" + "00:00:00" + code;
                                                    } else {
                                                        strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + wirefeedrate1 + weldingrate1 + weldheatinput1 + hatwirecurrent1 + vibrafrequency1 + timesql1 + limit + "00:00:00" + "00:00:00" + code
                                                                + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + wirefeedrate2 + weldingrate2 + weldheatinput2 + hatwirecurrent2 + vibrafrequency2 + timesql2 + limit + "00:00:00" + "00:00:00" + code
                                                                + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + wirefeedrate3 + weldingrate3 + weldheatinput3 + hatwirecurrent3 + vibrafrequency3 + timesql3 + limit + "00:00:00" + "00:00:00" + code;
                                                    }
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                e.getStackTrace();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    System.out.println("鏁版嵁搴撹鍙栭敊璇�");
                                    e.printStackTrace();
                                }
                                datawritetype = true;
                                synchronized (websocketlist) {
                                    ArrayList<String> listarraybuf = new ArrayList<String>();
                                    boolean ifdo = false;
                                    Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                                    while (webiter.hasNext()) {
                                        try {
                                            Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                                            websocketfail = entry.getKey();
                                            SocketChannel websocketcon = entry.getValue();
                                            websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();
                                        } catch (Exception e) {
                                            listarraybuf.add(websocketfail);
                                            ifdo = true;
                                        }
                                    }
                                    if (ifdo) {
                                        for (int i = 0; i < listarraybuf.size(); i++) {
                                            websocketlist.remove(listarraybuf.get(i));
                                        }
                                    }

                                }
                                strsend = "";
                            } else {
                                System.out.println("鏍￠獙浣嶉敊璇�");
                                str = "";
                            }

                        } else {
                            System.out.println("闀垮害閿欒");
                            str = "";
                        }

                    } else {
                        System.out.println("棣栨湯浣嶉敊璇�");
                        str = "";
                    }


                } else if (str.length() == 110) {
                    String check1 = str.substring(0, 2);
                    String check11 = str.substring(108, 110);
                    if (check1.equals("FA") && check11.equals("F5")) {

                        //閺嶏繝鐛欓梹鍨
                        int check2 = str.length();
                        if (check2 == 110) {

                            //閺嶏繝鐛欐担宥嗙墡妤�?
                            String check3 = str.substring(2, 104);
                            String check5 = "";
                            int check4 = 0;
                            for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                                String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                                check4 += Integer.valueOf(tstr1, 16);
                            }
                            if ((Integer.toHexString(check4)).toUpperCase().length() == 2) {
                                check5 = ((Integer.toHexString(check4)).toUpperCase());
                            } else {
                                check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                            }
                            String check6 = str.substring(104, 106);
                            if (check5.equals(check6)) {

                                //System.out.println("2");

                                strdata = str;
                                //String weldname = strdata.substring(10,14);
                                int weldname1 = Integer.valueOf(strdata.subSequence(10, 14).toString(), 16);
                                String weldname = String.valueOf(weldname1);
                                if (weldname.length() != 4) {
                                    int lenth = 4 - weldname.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldname = "0" + weldname;
                                    }
                                }

                                int welder1 = Integer.valueOf(strdata.subSequence(14, 18).toString(), 16);
                                String welder = String.valueOf(welder1);
                                if (welder.length() != 4) {
                                    int lenth = 4 - welder.length();
                                    for (int i = 0; i < lenth; i++) {
                                        welder = "0" + welder;
                                    }
                                }

                                //String code = strdata.substring(18,26);
                                long code1 = Integer.valueOf(strdata.subSequence(18, 26).toString(), 16);
                                String code = String.valueOf(code1);
                                if (code.length() != 8) {
                                    int lenth = 8 - code.length();
                                    for (int i = 0; i < lenth; i++) {
                                        code = "0" + code;
                                    }
                                }

                                int electricity11 = Integer.valueOf(strdata.subSequence(26, 30).toString(), 16);
                                String electricity1 = String.valueOf(electricity11);
                                if (electricity1.length() != 4) {
                                    int lenth = 4 - electricity1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity1 = "0" + electricity1;
                                    }
                                }

                                int voltage11 = Integer.valueOf(strdata.subSequence(30, 34).toString(), 16);
                                String voltage1 = String.valueOf(voltage11);
                                if (voltage1.length() != 4) {
                                    int lenth = 4 - voltage1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage1 = "0" + voltage1;
                                    }
                                }

                                String status1 = Integer.valueOf(strdata.substring(38, 40), 16).toString();
                                if (status1.length() != 2) {
                                    int lenth = 2 - status1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status1 = "0" + status1;
                                    }
                                }

                                long year1 = Integer.valueOf(str.subSequence(40, 42).toString(), 16);
                                String yearstr1 = String.valueOf(year1);
                                long month1 = Integer.valueOf(str.subSequence(42, 44).toString(), 16);
                                String monthstr1 = String.valueOf(month1);
                                if (monthstr1.length() != 2) {
                                    int lenth = 2 - monthstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr1 = "0" + monthstr1;
                                    }
                                }
                                long day1 = Integer.valueOf(str.subSequence(44, 46).toString(), 16);
                                String daystr1 = String.valueOf(day1);
                                if (daystr1.length() != 2) {
                                    int lenth = 2 - daystr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr1 = "0" + daystr1;
                                    }
                                }
                                long hour1 = Integer.valueOf(str.subSequence(46, 48).toString(), 16);
                                String hourstr1 = String.valueOf(hour1);
                                if (hourstr1.length() != 2) {
                                    int lenth = 2 - hourstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr1 = "0" + hourstr1;
                                    }
                                }
                                long minute1 = Integer.valueOf(str.subSequence(48, 50).toString(), 16);
                                String minutestr1 = String.valueOf(minute1);
                                if (minutestr1.length() != 2) {
                                    int lenth = 2 - minutestr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr1 = "0" + minutestr1;
                                    }
                                }
                                long second1 = Integer.valueOf(str.subSequence(50, 52).toString(), 16);
                                String secondstr1 = String.valueOf(second1);
                                if (secondstr1.length() != 2) {
                                    int lenth = 2 - secondstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr1 = "0" + secondstr1;
                                    }
                                }

                                String timestr1 = yearstr1 + "-" + monthstr1 + "-" + daystr1 + " " + hourstr1 + ":" + minutestr1 + ":" + secondstr1;
                                SimpleDateFormat timeshow1 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time1 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr1);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql1 = new Timestamp(time1.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                int electricity22 = Integer.valueOf(strdata.subSequence(52, 56).toString(), 16);
                                String electricity2 = String.valueOf(electricity22);
                                if (electricity2.length() != 4) {
                                    int lenth = 4 - electricity2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity2 = "0" + electricity2;
                                    }
                                }

                                int voltage22 = Integer.valueOf(strdata.subSequence(56, 60).toString(), 16);
                                String voltage2 = String.valueOf(voltage22);
                                if (voltage2.length() != 4) {
                                    int lenth = 4 - voltage2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage2 = "0" + voltage2;
                                    }
                                }

                                String status2 = Integer.valueOf(strdata.substring(64, 66), 16).toString();
                                if (status2.length() != 2) {
                                    int lenth = 2 - status2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status2 = "0" + status2;
                                    }
                                }

                                long year2 = Integer.valueOf(str.subSequence(66, 68).toString(), 16);
                                String yearstr2 = String.valueOf(year2);
                                long month2 = Integer.valueOf(str.subSequence(68, 70).toString(), 16);
                                String monthstr2 = String.valueOf(month2);
                                if (monthstr2.length() != 2) {
                                    int lenth = 2 - monthstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr2 = "0" + monthstr2;
                                    }
                                }
                                long day2 = Integer.valueOf(str.subSequence(70, 72).toString(), 16);
                                String daystr2 = String.valueOf(day2);
                                if (daystr2.length() != 2) {
                                    int lenth = 2 - daystr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr2 = "0" + daystr2;
                                    }
                                }
                                long hour2 = Integer.valueOf(str.subSequence(72, 74).toString(), 16);
                                String hourstr2 = String.valueOf(hour2);
                                if (hourstr2.length() != 2) {
                                    int lenth = 2 - hourstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr2 = "0" + hourstr2;
                                    }
                                }
                                long minute2 = Integer.valueOf(str.subSequence(74, 76).toString(), 16);
                                String minutestr2 = String.valueOf(minute2);
                                if (minutestr2.length() != 2) {
                                    int lenth = 2 - minutestr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr2 = "0" + minutestr2;
                                    }
                                }
                                long second2 = Integer.valueOf(str.subSequence(76, 78).toString(), 16);
                                String secondstr2 = String.valueOf(second2);
                                if (secondstr2.length() != 2) {
                                    int lenth = 2 - secondstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr2 = "0" + secondstr2;
                                    }
                                }

                                String timestr2 = yearstr2 + "-" + monthstr2 + "-" + daystr2 + " " + hourstr2 + ":" + minutestr2 + ":" + secondstr2;
                                SimpleDateFormat timeshow2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time2 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr2);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql2 = new Timestamp(time2.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                int electricity33 = Integer.valueOf(strdata.subSequence(52, 56).toString(), 16);
                                String electricity3 = String.valueOf(electricity33);
                                if (electricity3.length() != 4) {
                                    int lenth = 4 - electricity3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity3 = "0" + electricity3;
                                    }
                                }

                                int voltage33 = Integer.valueOf(strdata.subSequence(56, 60).toString(), 16);
                                String voltage3 = String.valueOf(voltage33);
                                if (voltage3.length() != 4) {
                                    int lenth = 4 - voltage3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage3 = "0" + voltage3;
                                    }
                                }
                                String status3 = Integer.valueOf(strdata.substring(90, 92), 16).toString();
                                if (status3.length() != 2) {
                                    int lenth = 2 - status3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status3 = "0" + status3;
                                    }
                                }

                                long year3 = Integer.valueOf(str.subSequence(92, 94).toString(), 16);
                                String yearstr3 = String.valueOf(year3);
                                long month3 = Integer.valueOf(str.subSequence(94, 96).toString(), 16);
                                String monthstr3 = String.valueOf(month3);
                                if (monthstr3.length() != 2) {
                                    int lenth = 2 - monthstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr3 = "0" + monthstr3;
                                    }
                                }
                                long day3 = Integer.valueOf(str.subSequence(96, 98).toString(), 16);
                                String daystr3 = String.valueOf(day3);
                                if (daystr3.length() != 2) {
                                    int lenth = 2 - daystr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr3 = "0" + daystr3;
                                    }
                                }
                                long hour3 = Integer.valueOf(str.subSequence(98, 100).toString(), 16);
                                String hourstr3 = String.valueOf(hour3);
                                if (hourstr3.length() != 2) {
                                    int lenth = 2 - hourstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr3 = "0" + hourstr3;
                                    }
                                }
                                long minute3 = Integer.valueOf(str.subSequence(100, 102).toString(), 16);
                                String minutestr3 = String.valueOf(minute3);
                                if (minutestr3.length() != 2) {
                                    int lenth = 2 - minutestr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr3 = "0" + minutestr3;
                                    }
                                }
                                long second3 = Integer.valueOf(str.subSequence(102, 104).toString(), 16);
                                String secondstr3 = String.valueOf(second3);
                                if (secondstr3.length() != 2) {
                                    int lenth = 2 - secondstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr3 = "0" + secondstr3;
                                    }
                                }

                                String timestr3 = yearstr3 + "-" + monthstr3 + "-" + daystr3 + " " + hourstr3 + ":" + minutestr3 + ":" + secondstr3;
                                SimpleDateFormat timeshow3 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {
                                    Date time3 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr3);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql3 = new Timestamp(time3.getTime());
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                //System.out.println("3");

                                try {

                                    for (int i = 0; i < listarray3.size(); i += 5) {
                                        String weldjunction = listarray3.get(i);
                                        if (weldjunction.equals(code)) {
                                            String maxe = listarray3.get(i + 1);
                                            String mixe = listarray3.get(i + 2);
                                            String maxv = listarray3.get(i + 3);
                                            String mixv = listarray3.get(i + 4);
                                            limit = maxe + mixe + maxv + mixv;
                                        }
                                    }

                                    //System.out.println("4");

									/*String worktime = "";
		                   	 String totaltime = "";
		                   	 String worktime1 = "";
		                   	 String totaltime1 = "";
		                   	 String workhour1,workminute1,worksecond1;
		                   	 String workhour2,workminute2,worksecond2;*/
                                    for (int i = 0; i < listarray2.size(); i += 4) {
                                        //娣囶喗鏁奸崣鎴︹偓浣哄墾閺堣櫣绱崣铚傝礋閻掑﹥婧�id
                                        //String fequipment_no = listarray2.get(i+1);
                                        String fequipment_no = listarray2.get(i);
                                        String fgather_no = listarray2.get(i + 2);
                                        String finsframework_id = listarray2.get(i + 3);

                                        //System.out.print(fequipment_no+" ");

                                        if (weldname.equals(fgather_no)) {

                                            try {
                                                if (fequipment_no.length() != 4) {
                                                    int lenth = 4 - fequipment_no.length();
                                                    for (int i1 = 0; i1 < lenth; i1++) {
                                                        fequipment_no = "0" + fequipment_no;
                                                    }
                                                }

                                                //姹熷崡
                                                if (weldname.equals(fgather_no)) {
                                                    if (finsframework_id == null || finsframework_id == "") {
                                                        finsframework_id = "nu";
                                                        strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "300050045005" + "00:00:00" + "00:00:00";
                                                    } else {
                                                        strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "300050045005" + "00:00:00" + "00:00:00";
                                                    }
                                                    break;
                                                }
                                            } catch (Exception e) {
                                                e.getStackTrace();
                                            }

                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("鏁版嵁搴撻敊璇�");
                                    e.printStackTrace();
                                }

                                datawritetype = true;

                                synchronized (websocketlist) {

                                    ArrayList<String> listarraybuf = new ArrayList<String>();
                                    boolean ifdo = false;

                                    Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                                    while (webiter.hasNext()) {
                                        try {
                                            Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                                            websocketfail = entry.getKey();
                                            SocketChannel websocketcon = entry.getValue();
                                            websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();

                                        } catch (Exception e) {

                                            listarraybuf.add(websocketfail);
                                            ifdo = true;

                                        }
                                    }

                                    if (ifdo) {
                                        for (int i = 0; i < listarraybuf.size(); i++) {
                                            websocketlist.remove(listarraybuf.get(i));
                                        }
                                    }

                                }

                                strsend = "";
                            } else {
                                System.out.println("鏍￠獙浣嶉敊璇�");
                                str = "";
                            }

                        } else {
                            System.out.println("闀垮害閿欒");
                            str = "";
                        }

                    } else {
                        System.out.println("棣栨湯浣嶉敊璇�");
                        str = "";
                    }

                } else {

                    str = "";

                }
            }
        } catch (Exception e) {

            if (datawritetype = true) {

                e.printStackTrace();

            }
        }
    }

    public Websocket(String str, java.sql.Statement stmt, HashMap<String, Socket> websocket, ArrayList<String> listarray2, ArrayList<String> listarray3, HashMap<String, SocketChannel> websocketlist, ArrayList<String> dbdata) {
        this.strdata = str;
        this.websocket = websocket;
        this.listarray2 = listarray2;
        this.listarray3 = listarray3;
        this.websocketlist = websocketlist;
        this.dbdata = dbdata;

        try {

            if (websocketlist == null || websocketlist.isEmpty()) {

            } else {
                if (str.length() == 110) {

                    //鏍￠獙绗竴浣嶆槸鍚︿负FA鏈綅鏄惁涓篎5
                    String check1 = str.substring(0, 2);
                    String check11 = str.substring(108, 110);
                    if (check1.equals("FA") && check11.equals("F5")) {

                        //鏍￠獙闀垮害
                        int check2 = str.length();
                        if (check2 == 110) {

                            //鏍￠獙浣嶆牎楠�
                            String check3 = str.substring(2, 104);
                            String check5 = "";
                            int check4 = 0;
                            for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                                String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                                check4 += Integer.valueOf(tstr1, 16);
                            }
                            if ((Integer.toHexString(check4)).toUpperCase().length() == 2) {
                                check5 = ((Integer.toHexString(check4)).toUpperCase());
                            } else {
                                check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                            }
                            String check6 = str.substring(104, 106);
                            if (check5.equals(check6)) {

                                strdata = str;
                                //String weldname = strdata.substring(10,14);
                                int weldname1 = Integer.valueOf(strdata.subSequence(10, 14).toString(), 16);
                                String weldname = String.valueOf(weldname1);
                                if (weldname.length() != 4) {
                                    int lenth = 4 - weldname.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldname = "0" + weldname;
                                    }
                                }

                                int welder1 = Integer.valueOf(strdata.subSequence(14, 18).toString(), 16);
                                String welder = String.valueOf(welder1);
                                if (welder.length() != 4) {
                                    int lenth = 4 - welder.length();
                                    for (int i = 0; i < lenth; i++) {
                                        welder = "0" + welder;
                                    }
                                }

                                //String code = strdata.substring(18,26);
                                long code1 = Integer.valueOf(strdata.subSequence(18, 26).toString(), 16);
                                String code = String.valueOf(code1);
                                if (code.length() != 8) {
                                    int lenth = 8 - code.length();
                                    for (int i = 0; i < lenth; i++) {
                                        code = "0" + code;
                                    }
                                }

                                int electricity11 = Integer.valueOf(strdata.subSequence(26, 30).toString(), 16);
                                String electricity1 = String.valueOf(electricity11);
                                if (electricity1.length() != 4) {
                                    int lenth = 4 - electricity1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity1 = "0" + electricity1;
                                    }
                                }

                                int voltage11 = Integer.valueOf(strdata.subSequence(30, 34).toString(), 16);
                                String voltage1 = String.valueOf(voltage11);
                                if (voltage1.length() != 4) {
                                    int lenth = 4 - voltage1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage1 = "0" + voltage1;
                                    }
                                }

                                String status1 = strdata.substring(38, 40);

                                long year1 = Integer.valueOf(str.subSequence(40, 42).toString(), 16);
                                String yearstr1 = String.valueOf(year1);
                                long month1 = Integer.valueOf(str.subSequence(42, 44).toString(), 16);
                                String monthstr1 = String.valueOf(month1);
                                if (monthstr1.length() != 2) {
                                    int lenth = 2 - monthstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr1 = "0" + monthstr1;
                                    }
                                }
                                long day1 = Integer.valueOf(str.subSequence(44, 46).toString(), 16);
                                String daystr1 = String.valueOf(day1);
                                if (daystr1.length() != 2) {
                                    int lenth = 2 - daystr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr1 = "0" + daystr1;
                                    }
                                }
                                long hour1 = Integer.valueOf(str.subSequence(46, 48).toString(), 16);
                                String hourstr1 = String.valueOf(hour1);
                                if (hourstr1.length() != 2) {
                                    int lenth = 2 - hourstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr1 = "0" + hourstr1;
                                    }
                                }
                                long minute1 = Integer.valueOf(str.subSequence(48, 50).toString(), 16);
                                String minutestr1 = String.valueOf(minute1);
                                if (minutestr1.length() != 2) {
                                    int lenth = 2 - minutestr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr1 = "0" + minutestr1;
                                    }
                                }
                                long second1 = Integer.valueOf(str.subSequence(50, 52).toString(), 16);
                                String secondstr1 = String.valueOf(second1);
                                if (secondstr1.length() != 2) {
                                    int lenth = 2 - secondstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr1 = "0" + secondstr1;
                                    }
                                }

                                String timestr1 = yearstr1 + "-" + monthstr1 + "-" + daystr1 + " " + hourstr1 + ":" + minutestr1 + ":" + secondstr1;
                                SimpleDateFormat timeshow1 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time1 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr1);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql1 = new Timestamp(time1.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                int electricity22 = Integer.valueOf(strdata.subSequence(52, 56).toString(), 16);
                                String electricity2 = String.valueOf(electricity22);
                                if (electricity2.length() != 4) {
                                    int lenth = 4 - electricity2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity2 = "0" + electricity2;
                                    }
                                }

                                int voltage22 = Integer.valueOf(strdata.subSequence(56, 60).toString(), 16);
                                String voltage2 = String.valueOf(voltage22);
                                if (voltage2.length() != 4) {
                                    int lenth = 4 - voltage2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage2 = "0" + voltage2;
                                    }
                                }
                                String status2 = strdata.substring(64, 66);

                                long year2 = Integer.valueOf(str.subSequence(66, 68).toString(), 16);
                                String yearstr2 = String.valueOf(year2);
                                long month2 = Integer.valueOf(str.subSequence(68, 70).toString(), 16);
                                String monthstr2 = String.valueOf(month2);
                                if (monthstr2.length() != 2) {
                                    int lenth = 2 - monthstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr2 = "0" + monthstr2;
                                    }
                                }
                                long day2 = Integer.valueOf(str.subSequence(70, 72).toString(), 16);
                                String daystr2 = String.valueOf(day2);
                                if (daystr2.length() != 2) {
                                    int lenth = 2 - daystr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr2 = "0" + daystr2;
                                    }
                                }
                                long hour2 = Integer.valueOf(str.subSequence(72, 74).toString(), 16);
                                String hourstr2 = String.valueOf(hour2);
                                if (hourstr2.length() != 2) {
                                    int lenth = 2 - hourstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr2 = "0" + hourstr2;
                                    }
                                }
                                long minute2 = Integer.valueOf(str.subSequence(74, 76).toString(), 16);
                                String minutestr2 = String.valueOf(minute2);
                                if (minutestr2.length() != 2) {
                                    int lenth = 2 - minutestr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr2 = "0" + minutestr2;
                                    }
                                }
                                long second2 = Integer.valueOf(str.subSequence(76, 78).toString(), 16);
                                String secondstr2 = String.valueOf(second2);
                                if (secondstr2.length() != 2) {
                                    int lenth = 2 - secondstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr2 = "0" + secondstr2;
                                    }
                                }

                                String timestr2 = yearstr2 + "-" + monthstr2 + "-" + daystr2 + " " + hourstr2 + ":" + minutestr2 + ":" + secondstr2;
                                SimpleDateFormat timeshow2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time2 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr2);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql2 = new Timestamp(time2.getTime());

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                                int electricity33 = Integer.valueOf(strdata.subSequence(52, 56).toString(), 16);
                                String electricity3 = String.valueOf(electricity33);
                                if (electricity3.length() != 4) {
                                    int lenth = 4 - electricity3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity3 = "0" + electricity3;
                                    }
                                }

                                int voltage33 = Integer.valueOf(strdata.subSequence(56, 60).toString(), 16);
                                String voltage3 = String.valueOf(voltage33);
                                if (voltage3.length() != 4) {
                                    int lenth = 4 - voltage3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage3 = "0" + voltage3;
                                    }
                                }
                                String status3 = strdata.substring(90, 92);

                                long year3 = Integer.valueOf(str.subSequence(92, 94).toString(), 16);
                                String yearstr3 = String.valueOf(year3);
                                long month3 = Integer.valueOf(str.subSequence(94, 96).toString(), 16);
                                String monthstr3 = String.valueOf(month3);
                                if (monthstr3.length() != 2) {
                                    int lenth = 2 - monthstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr3 = "0" + monthstr3;
                                    }
                                }
                                long day3 = Integer.valueOf(str.subSequence(96, 98).toString(), 16);
                                String daystr3 = String.valueOf(day3);
                                if (daystr3.length() != 2) {
                                    int lenth = 2 - daystr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr3 = "0" + daystr3;
                                    }
                                }
                                long hour3 = Integer.valueOf(str.subSequence(98, 100).toString(), 16);
                                String hourstr3 = String.valueOf(hour3);
                                if (hourstr3.length() != 2) {
                                    int lenth = 2 - hourstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr3 = "0" + hourstr3;
                                    }
                                }
                                long minute3 = Integer.valueOf(str.subSequence(100, 102).toString(), 16);
                                String minutestr3 = String.valueOf(minute3);
                                if (minutestr3.length() != 2) {
                                    int lenth = 2 - minutestr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr3 = "0" + minutestr3;
                                    }
                                }
                                long second3 = Integer.valueOf(str.subSequence(102, 104).toString(), 16);
                                String secondstr3 = String.valueOf(second3);
                                if (secondstr3.length() != 2) {
                                    int lenth = 2 - secondstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr3 = "0" + secondstr3;
                                    }
                                }

                                String timestr3 = yearstr3 + "-" + monthstr3 + "-" + daystr3 + " " + hourstr3 + ":" + minutestr3 + ":" + secondstr3;
                                SimpleDateFormat timeshow3 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {
                                    Date time3 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr3);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql3 = new Timestamp(time3.getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                try {

                                    for (int i = 0; i < listarray3.size(); i += 5) {
                                        String weldjunction = listarray3.get(i);
                                        if (weldjunction.equals(code)) {
                                            String maxe = listarray3.get(i + 1);
                                            String mixe = listarray3.get(i + 2);
                                            String maxv = listarray3.get(i + 3);
                                            String mixv = listarray3.get(i + 4);
                                            limit = maxe + mixe + maxv + mixv;
                                        }
                                    }

                                    for (int i = 0; i < listarray2.size(); i += 4) {
                                        String fequipment_no = listarray2.get(i);
                                        String fgather_no = listarray2.get(i + 2);
                                        String finsframework_id = listarray2.get(i + 3);

                                        if (weldname.equals(fgather_no)) {

                                            if (fequipment_no.length() != 4) {
                                                int lenth = 4 - fequipment_no.length();
                                                for (int i1 = 0; i1 < lenth; i1++) {
                                                    fequipment_no = "0" + fequipment_no;
                                                }
                                            }

                                            if (weldname.equals(fgather_no)) {

                                                //姹熷崡
                                                if (finsframework_id == null || finsframework_id == "") {
                                                    finsframework_id = "nu";
                                                    strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "000000000000" + "00:00:00" + "00:00:00";
                                                } else {
                                                    strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "000000000000" + "00:00:00" + "00:00:00";
                                                }

                                            } else {
                                                if (finsframework_id == null || finsframework_id == "") {
                                                    finsframework_id = "nu";
                                                    strsend += "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00";
                                                } else {
                                                    strsend += "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00";
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    System.out.println("鏁版嵁搴撹鍙栨暟鎹敊璇�");
                                    e.printStackTrace();
                                }
                                datawritetype = true;
                                Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                                while (webiter.hasNext()) {
                                    try {
                                        Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                                        websocketfail = entry.getKey();
                                        SocketChannel websocketcon = entry.getValue();
                                        websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();
                                    } catch (Exception e) {
                                        if (datawritetype = true) {
                                            websocketlist.remove(websocketfail);
                                            webiter = websocketlist.entrySet().iterator();
                                            datawritetype = false;
                                        }
                                    }
                                }
                                strsend = "";
                            } else {
                                //鏍￠獙浣嶉敊璇�
                                System.out.println("鏁版嵁鎺ユ敹鏍￠獙浣嶉敊璇�");
                                str = "";
                            }

                        } else {
                            //闀垮害閿欒
                            System.out.println("鏁版嵁鎺ユ敹闀垮害閿欒");
                            str = "";
                        }

                    } else {
                        //棣栦綅涓嶆槸FE
                        System.out.println("鏁版嵁鎺ユ敹棣栨湯浣嶉敊璇�");
                        str = "";
                    }


                } else {

                    str = "";

                }
            }
        } catch (Exception e) {

            if (datawritetype = true) {

                chweb = null;
                websocket.remove(websocketfail);
                datawritetype = false;

            }
        }

    }

    public Websocket() {

    }


    public void Websocketohwh(String str, ArrayList<String> listarray22, ArrayList<String> listarray32,
                              HashMap<String, SocketChannel> websocketlist2) {
        Date time;
        Timestamp timesql = null;

        if (websocketlist2 == null || websocketlist2.isEmpty()) {
        } else {
            String welderid = Integer.valueOf(str.substring(70, 74)).toString();
            if (welderid.length() != 4) {
                int lenth = 4 - welderid.length();
                for (int i = 0; i < lenth; i++) {
                    welderid = "0" + welderid;
                }
            }
            String weldid = Integer.valueOf(str.substring(20, 24)).toString();
            if (weldid.length() != 4) {
                int lenth = 4 - weldid.length();
                for (int i = 0; i < lenth; i++) {
                    weldid = "0" + weldid;
                }
            }
            String gatherid = Integer.valueOf(str.substring(16, 20)).toString();
            if (gatherid.length() != 4) {
                int lenth = 4 - gatherid.length();
                for (int i = 0; i < lenth; i++) {
                    gatherid = "0" + gatherid;
                }
            }
            String itemins = Integer.valueOf(str.substring(316, 318)).toString();
            if (itemins.length() != 4) {
                int lenth = 4 - itemins.length();
                for (int i = 0; i < lenth; i++) {
                    itemins = "0" + itemins;
                }
            }
            String weldmodel = Integer.valueOf(str.subSequence(42, 44).toString(), 16).toString();
            if (weldmodel.length() != 4) {
                int lenth = 4 - weldmodel.length();
                for (int i = 0; i < lenth; i++) {
                    weldmodel = "0" + weldmodel;
                }
            }

            for (int a = 0; a < 161; a += 80) {
                try {
                    String welderins = "0000";
                    String junctionins = "0000";
                    String ins = "0000";

                    String junctionid = Integer.valueOf(str.substring(106 + a, 114 + a)).toString();
                    if (junctionid.length() != 4) {
                        int lenth = 4 - junctionid.length();
                        for (int i = 0; i < lenth; i++) {
                            junctionid = "0" + junctionid;
                        }
                    }
                    String electricity = Integer.valueOf(str.subSequence(86 + a, 90 + a).toString(), 16).toString();
                    if (electricity.length() != 4) {
                        int lenth = 4 - electricity.length();
                        for (int i = 0; i < lenth; i++) {
                            electricity = "0" + electricity;
                        }
                    }
                    String voltage = Integer.valueOf(str.subSequence(90 + a, 94 + a).toString(), 16).toString();
                    if (voltage.length() != 4) {
                        int lenth = 4 - voltage.length();
                        for (int i = 0; i < lenth; i++) {
                            voltage = "0" + voltage;
                        }
                    }
                    String speed = Integer.valueOf(str.subSequence(94 + a, 98 + a).toString(), 16).toString();
                    if (speed.length() != 4) {
                        int lenth = 4 - speed.length();
                        for (int i = 0; i < lenth; i++) {
                            speed = "0" + speed;
                        }
                    }
                    String setelectricity = Integer.valueOf(str.subSequence(98 + a, 102 + a).toString(), 16).toString();
                    if (setelectricity.length() != 4) {
                        int lenth = 4 - setelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            setelectricity = "0" + setelectricity;
                        }
                    }
                    String setvoltage = Integer.valueOf(str.subSequence(102 + a, 106 + a).toString(), 16).toString();
                    if (setvoltage.length() != 4) {
                        int lenth = 4 - setvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            setvoltage = "0" + setvoltage;
                        }
                    }
                    String status = Integer.valueOf(str.subSequence(114 + a, 116 + a).toString(), 16).toString();
                    if (status.length() != 2) {
                        int lenth = 2 - status.length();
                        for (int i = 0; i < lenth; i++) {
                            status = "0" + status;
                        }
                    }

                    String year = Integer.valueOf(str.subSequence(74 + a, 76 + a).toString(), 16).toString();
                    String month = Integer.valueOf(str.subSequence(76 + a, 78 + a).toString(), 16).toString();
                    String day = Integer.valueOf(str.subSequence(78 + a, 80 + a).toString(), 16).toString();
                    String hour = Integer.valueOf(str.subSequence(80 + a, 82 + a).toString(), 16).toString();
                    String minute = Integer.valueOf(str.subSequence(82 + a, 84 + a).toString(), 16).toString();
                    String second = Integer.valueOf(str.subSequence(84 + a, 86 + a).toString(), 16).toString();
                    String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    try {
                        time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                        timesql = new Timestamp(time.getTime());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    String channel = Integer.valueOf(str.subSequence(136 + a, 138 + a).toString(), 16).toString();
                    if (channel.length() != 4) {
                        int lenth = 4 - channel.length();
                        for (int i = 0; i < lenth; i++) {
                            channel = "0" + channel;
                        }
                    }
                    String maxelectricity = Integer.valueOf(str.subSequence(120 + a, 124 + a).toString(), 16).toString();
                    if (maxelectricity.length() != 4) {
                        int lenth = 4 - maxelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            maxelectricity = "0" + maxelectricity;
                        }
                    }
                    String minelectricity = Integer.valueOf(str.subSequence(124 + a, 128 + a).toString(), 16).toString();
                    if (minelectricity.length() != 4) {
                        int lenth = 4 - minelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            minelectricity = "0" + minelectricity;
                        }
                    }
                    String maxvoltage = Integer.valueOf(str.subSequence(128 + a, 132 + a).toString(), 16).toString();
                    if (maxvoltage.length() != 4) {
                        int lenth = 4 - maxvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            maxvoltage = "0" + maxvoltage;
                        }
                    }
                    String minvoltage = Integer.valueOf(str.subSequence(132 + a, 136 + a).toString(), 16).toString();
                    if (minvoltage.length() != 4) {
                        int lenth = 4 - minvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            minvoltage = "0" + minvoltage;
                        }
                    }

                    for (int i = 0; i < listarray1.size(); i += 3) {
                        if (Integer.valueOf(welderid) == Integer.valueOf(listarray1.get(i))) {
                            welderins = listarray1.get(i + 2);
                            if (welderins.equals(null) || welderins.equals("null")) {
                                break;
                            } else {
                                if (welderins.length() != 4) {
                                    int lenth = 4 - welderins.length();
                                    for (int i1 = 0; i1 < lenth; i1++) {
                                        welderins = "0" + welderins;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < listarray32.size(); i += 7) {
                        if (Integer.valueOf(junctionid) == Integer.valueOf(listarray32.get(i + 5))) {
                            junctionins = listarray32.get(i + 6);
                            if (junctionins.equals(null) || junctionins.equals("null")) {
                                break;
                            } else {
                                if (junctionins.length() != 4) {
                                    int lenth = 4 - junctionins.length();
                                    for (int i1 = 0; i1 < lenth; i1++) {
                                        junctionins = "0" + junctionins;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < listarray22.size(); i += 4) {
                        if (Integer.valueOf(gatherid) == Integer.valueOf(listarray22.get(i))) {
                            ins = listarray22.get(i + 3);
                            if (ins == null || ins.equals("null")) {
                                break;
                            } else {
                                if (ins.length() != 4) {
                                    int lenth = 4 - ins.length();
                                    for (int i1 = 0; i1 < lenth; i1++) {
                                        ins = "0" + ins;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (ins == null || ins.equals("null")) {
                        ins = "0000";
                    }
                    if (junctionins.equals(null) || junctionins.equals("null")) {
                        junctionins = "0000";
                    }
                    if (welderins.equals(null) || welderins.equals("null")) {
                        welderins = "0000";
                    }

                    //鍗楅�氬幓闄ゆ姤璀�
					/*if(status.equals("98") || status.equals("99")){
            	status = "03";
            }*/

                    //strsend = strsend + welderid + weldid + gatherid + junctionid + welderins + junctionins + ins + itemins + weldmodel + status + electricity + voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel;
                    strsend = strsend + welderid + weldid + gatherid + junctionid + welderins + junctionins + ins + itemins + weldmodel + status + electricity + voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + speed;
                } catch (Exception e) {
                    System.out.println(str);
                    System.out.println(str.substring(76 + a, 84 + a));
                }
            }
            synchronized (websocketlist2) {

                ArrayList<String> listarraybuf = new ArrayList<String>();
                boolean ifdo = false;

                Iterator<Entry<String, SocketChannel>> webiter = websocketlist2.entrySet().iterator();
                while (webiter.hasNext()) {
                    try {
                        Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                        websocketfail = entry.getKey();
                        SocketChannel websocketcon = entry.getValue();
                        websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();
                    } catch (Exception e) {
                        listarraybuf.add(websocketfail);
                        ifdo = true;
                    }
                }

                if (ifdo) {
                    for (int i = 0; i < listarraybuf.size(); i++) {
                        websocketlist2.remove(listarraybuf.get(i));
                    }
                }
                strsend = "";
            }
        }
    }
}
