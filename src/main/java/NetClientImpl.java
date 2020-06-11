import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import jdk.nashorn.internal.parser.JSONParser;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NetClientImpl implements NetClient {


    private String server;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public NetClientImpl(String root) {
        this.root = root;
    }

    public NetClientImpl(String server, String root) {
        this.server = server;
        this.root = root;
    }

    private String root;

    @Override
    public void init() {
        Synchronize();

    }

    @Override
    public void startHeat() {
        System.out.println("TODO 启动心跳服务");
//        new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(3000);
//                        System.out.println("I am alive.");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.run();
    }

    //  Synchronize 实际进行同步的方法
    // 1. 获取本地所有的文件 发给服务器
    // 2. 得到需要上传的文件
    // 3. 重新上传目标文件
    private void Synchronize() {
        // 得到树形结构
        List<Tree> list = FileUtil.getAllNodes(root);

        System.out.println("数据上报...");
        System.out.printf("文件数[%s]\n", list.size());
        String actual = JSON.toJSONString(list);

        String response = sendPost(server + "/sync", actual);
        Object json = JSON.parse(response);
        System.out.println("得到服务器的结果" + response);
        Result result = JSONObject.parseObject(response, Result.class);
        try {
            command(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // command 接收服务器的命令之后处理文件上传
    private void command(Result result) throws IOException {


        if (result.isOk()) {
            // 上传文件
            for (Tree tree : result.getData()) {
                if(tree.isLeft()){
                    Client client = new Client(); // 启动客户端连接
                    client.sendFile(Main.root+tree.getPath()); // 传输文件
                    client.close();
                }

            }
        }


    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void sync() {
        Synchronize();
    }

}
