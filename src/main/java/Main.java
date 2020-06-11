import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Tree> list = new ArrayList<>();//用来存放数据
    private static Integer id = 0;//因为测试使用，当初主键id来用
    public static String root = "D:\\autoback";
    public static void main(String[] args) {
        // 创建网络客户端

        String server = "http://127.0.0.1:9999";
        NetClient client = new NetClientImpl(server, root);
        client.init();

        while (true) {
            try {
                Thread.sleep(5000);
                client.sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
