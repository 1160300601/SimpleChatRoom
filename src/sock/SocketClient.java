package sock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
/**
 * ͨ���޸�������ַʵ��ͬһ��������������������ĵ�Ե�����(һ��һ)
 * @author MaXU
 *
 */
public class SocketClient {
  public static void main(String[] args)
      throws UnknownHostException, IOException {
    Socket socket = new Socket("127.0.0.1", 2333);
    socket.setSoTimeout(60000);
    // �õ�socket������������ҹ���PrintStream�������������д������
    PrintStream ps = new PrintStream(socket.getOutputStream());
    // �õ��Ӽ��̵������������������������������뵽socket���������
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    // �õ�socket��������(���ǿͻ��˵������)�����������е��������������̨
    BufferedReader brServerIn = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));

    String result = "OK";
    while (!result.equals("quit")) {
      
      ps.println(br.readLine());
      // ˢ�������(��ʹ����˵�������),ʹ��������̵õ�������
      ps.flush();
      result = brServerIn.readLine();
      System.out.println("server say:" + result);
      
    }

    // �ر�socket
    ps.close();
    br.close();
    ps.close();
    socket.close();
  }
}
