package sock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * 通过修改主机地址实现同一个局域网中两个主机间的点对点聊天(一对一)
 * @author MaXU
 *
 */
public class SocketClient {
  public static void main(String[] args)
      throws UnknownHostException, IOException {
    Socket socket = new Socket("127.0.0.1", 2333);
    socket.setSoTimeout(60000);
    // 得到socket的输出流，并且构建PrintStream对象用于向该流写入内容
    PrintStream ps = new PrintStream(socket.getOutputStream());
    // 得到从键盘的输入流，并将该输入流的内容输入到socket的输出流中
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    // 得到socket的输入流(即是客户端的输出流)，并将该流中的内容输出到控制台
    BufferedReader brServerIn = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));

    String result = "OK";
    while (!result.equals("quit")) {
      
      ps.println(br.readLine());
      // 刷新输出流(即使服务端的输入流),使服务端立刻得到该内容
      ps.flush();
      result = brServerIn.readLine();
      System.out.println("server say:" + result);
      
    }

    // 关闭socket
    ps.close();
    br.close();
    ps.close();
    socket.close();
  }
}
