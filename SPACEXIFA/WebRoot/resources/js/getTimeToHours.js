$(function(){
	getOldTime();
	getNewTime();
})

function getOldTime(){
	//获取当前时间
	var now = new Date();  
	now.setDate(now.getDate());//获取当前的日期 
    var year = now.getFullYear();//年  
    var month = now.getMonth() + 1;//月  
    var day = now.getDate();//日
    
    var oldtime = year + "-";
      
    if(month < 10){
        oldtime += "0";
    }
    oldtime += month + "-";
      
    if(day < 10){
        oldtime += "0";
    }          
    oldtime += day + " 00:00:00";
	$("#dtoTime1").datetimebox('setValue',oldtime);
}

function getNewTime(){
	//获取当前时间
	var now = new Date();  
    
    var year = now.getFullYear();//年  
    var month = now.getMonth() + 1;//月  
    var day = now.getDate();//日
    var hh = now.getHours();//时
    
    var nowtime = year + "-";
      
    if(month < 10){
        nowtime += "0";
    }
    nowtime += month + "-";
      
    if(day < 10){
        nowtime += "0";
    }          
    nowtime += day + " ";
    
    if(hh < 10){
        nowtime += "0";
    }
    nowtime += hh + ":00:00";
	$("#dtoTime2").datetimebox('setValue',nowtime);
}