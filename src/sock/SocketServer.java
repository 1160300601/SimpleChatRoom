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
      // ����2333�˿ڵ�Socket���ӣ���������������һ��Socket
      Socket socket = serverSocket.accept();
      // �ɸյõ���Socket������������(���ͻ��˵������),�����������������������̨
      BufferedReader br = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));
      // �õ�Socket����������(���ͻ��˵�������),�������д������
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
