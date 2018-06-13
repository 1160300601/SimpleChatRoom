package sock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(2333);
    while (true) {
      // 监听2333端口的Socket连接，如果请求到来则产生一个Socket
      Socket socket = serverSocket.accept();
      // 由刚得到的Socket对象获得输入流(即客户端的输出流),并将读到的数据输出到控制台
      BufferedReader br = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));
      // 得到Socket对象的输出流(即客户端的输入流),并向该流写入数据
      PrintStream ps = new PrintStream(socket.getOutputStream());
      BufferedReader brConsole = new BufferedReader(
          new InputStreamReader(System.in));
      String result = "OK";
      String get = null;
      while (!result.equals("quit")) {
        result = br.readLine();
        System.out.println("Client say:" + result);
        get = brConsole.readLine();
        ps.println(get);
        ps.flush();
      }

      ps.close();
      br.close();
      brConsole.close();
      socket.close();
    }
  }
}
