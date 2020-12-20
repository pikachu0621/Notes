package com.pikachu.record.monitor;


//用于eventbus传递事件
public class DataSynEvent {

    //传递变量
    private String msg;
    //接口 传递事件
    private OnEvent onEvent;
    

    //静态方法 （方便使用）也可以用 构造方法
    public static DataSynEvent setNewMsg(String msg,OnEvent onEvent){
        DataSynEvent dataSynEvent = new DataSynEvent(msg,onEvent) ;
        return dataSynEvent;
    }
    public DataSynEvent (String msg,OnEvent onEvent){
        this.msg=msg;
        this.onEvent=onEvent;
    }
    public DataSynEvent (){}
    //获取传递的String信息
    public String getMsg() {
        return msg;
    }
    //设置传递的信息
    public void setMsg(String msg) {
        this.msg = msg;
    }

    //获取事件接口
    public OnEvent getOnEvent() {
        return onEvent;
    }

    //设置事件
    public void setOnEvent(OnEvent onEvent) {
        this.onEvent = onEvent;
    }

    //事件接口
    public interface OnEvent{
        void onClick(DataSynEvent dataSynEvent);
    }

}
