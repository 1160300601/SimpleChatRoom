package sock1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * 使用多线程处理多个客户端连入服务端，但是每个客户端不能进行信息共享，只有服务端能看到每个客户端发过来的东西
 * 
 * @author MaXU
 *
 */
public class SocketClient {
  public static void main(String[] args)
      throws UnknownHostException, IOException {
    Socket socket = new Socket("127.0.0.1", 2333);
    socket.setSoTimeout(60000);

    PrintStream ps = new PrintStream(socket.getOutputStream());
    BufferedReader br = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    BufferedReader brConsole = new BufferedReader(
        new InputStreamReader(System.in));
    String result = "OK";
    while (!result.equals("bye")) {
      result = brConsole.readLine();
      ps.println(result);
      ps.flush();

      result = br.readLine();
      System.out.println("Server say:" + result);
    }
    ps.close();
    br.close();
    brConsole.close();

  }

}
