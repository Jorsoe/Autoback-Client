public interface NetClient {

    //  初始化方法
    void init();

    // 开启心跳包 心跳包每3秒执行一次  向服务器报告当前状态
    void startHeat();


    // 更新方法
    void sync();


}
