package sock2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ��ʵ�ֶ����������Ϣ���� ������˼·���ڷ���˿�һ���߳�������Ϣ�Ĵ�ӡ�����������Ϣ��������Ϣ�ͽ��ù�����Ϣ�������������������ͻ���
 * Ȼ��ÿ���ڿͻ��˽���ʱ���¿�һ���߳����ڴ���ÿͻ��ˣ������ǽ��ÿͻ��˵�������Ϣ��ȡ��������Ϣ���棬���Ҹ��ݿͻ����������Ϣ����Ƿ���Ҫ��ֹ���߳�
 * 
 * @author MaXU
 *
 */
public class Server extends ServerSocket {
  private static final String END_MARK = "quit";
  private static final int SERVER_PORT = 2333;
  private static final String VIEW_USER = "viewusers";

  private List<String> userList = new LinkedList<>();
  private List<Task> threadList = new LinkedList<>();
  private BlockingQueue<String> msgQueue = new ArrayBlockingQueue<>(20);

  public Server() throws IOException {
    super(SERVER_PORT);
    load();
  }

  public void load() throws IOException {
    new Thread(new SendMsgTask()).start();

    while (true) {
      Socket socket = this.accept();
      new Thread(new Task(socket)).start();
    }
  }

  class SendMsgTask implements Runnable {
    @Override
    public void run() {
      String msg = null;
      while (true) {
        try {
          msg = msgQueue.take();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        if (msg != null) {
          for (Task thread : threadList) {
            thread.sendMsg(msg);
          }
        }
      }
    }
  }

  class Task implements Runnable {
    private Socket socket;
    private BufferedReader buff;
    private BufferedWriter writer;
    private String userName;

    public Task(Socket socket) throws IOException {
      this.socket = socket;
      try {
        this.buff = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream()));
      } catch (Exception e) {
        e.printStackTrace();
      }
      sendMsg("[�������������]");
      this.userName = buff.readLine();
      userList.add(this.userName);
      threadList.add(this);
      storeMassage("[" + this.userName + "������������]");
      System.out.println("[" + this.userName + "������������]");

    }

    @Override
    public void run() {
      try {
        while (true) {
          String msg;
          msg = buff.readLine();
          if (VIEW_USER.equals(msg)) {
            sendMsg(onlineUsers());
          } else if (msg.equals(END_MARK)) {
            sendMsg(END_MARK);
            break;
          } else {
            storeMassage("[" + userName + "˵]��" + msg);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          writer.close();
          buff.close();
          socket.close();
        } catch (Exception e2) {
          e2.printStackTrace();
        }
      }
    }
    public void storeMassage(String msg) {
      try {
        msgQueue.put(msg);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    public void sendMsg(String msg) {
      try {
        writer.write(msg);
        writer.write("\n");
        writer.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    private String onlineUsers() {
      StringBuffer sbf = new StringBuffer();
      sbf.append("======== ���߳�Ա�б�(").append(userList.size())
          .append(") ========\n");
      for (int i = 0; i < userList.size(); i++) {
        sbf.append("[" + userList.get(i) + "]\n");
      }
      sbf.append("===============================");
      return sbf.toString();
    }
  }

  public static void main(String[] args) {
    try {
      Server server = new Server();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
