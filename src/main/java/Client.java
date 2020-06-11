import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

//客户端
public class Client extends Socket {
    private final String SERVER_IP = "127.0.0.1";
    private final int SERVER_PORT = 8999;
    private Socket client;
    private FileInputStream fis;
    private DataOutputStream dos;

    //创建客户端，并指定接收的服务端IP和端口号
    public Client() throws IOException {
        this.client = new Socket(SERVER_IP, SERVER_PORT);
        System.out.println("成功连接服务端..." + SERVER_IP);
    }

    //向服务端传输文件
    public void sendFile(String url) throws IOException {
        File file = new File(url);
        if (!file.canRead()) {
            return;
        }
        try {
            fis = new FileInputStream(file);
            dos = new DataOutputStream(client.getOutputStream());//client.getOutputStream()返回此套接字的输出流
            //文件名、大小等属性
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();
            dos.writeUTF(url);
            dos.flush();
            // 开始传输文件
            System.out.println("======== 开始传输文件 ========" + file.getName());
            byte[] bytes = new byte[1024];
            int length = 0;

            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
            }
            System.out.println("======== 文件传输成功 ========");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("客户端文件传输异常");
        }
    }

    public void close() {
        try {
            this.fis.close();
            this.dos.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}