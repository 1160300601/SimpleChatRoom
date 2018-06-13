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
 * 简单实现多个聊天室消息共享 ，总体思路：在服务端开一个线程用于消息的打印，如果共享消息里面有消息就将该共享消息里面的内容输出给各个客户端
 * 然后每次在客户端接入时就新开一个线程用于处理该客户端，具体是将该客户端的输入消息提取到共享消息里面，并且根据客户端输入的消息监控是否需要终止该线程
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
      sendMsg("[请输入你的名字]");
      this.userName = buff.readLine();
      userList.add(this.userName);
      threadList.add(this);
      storeMassage("[" + this.userName + "进入了聊天室]");
      System.out.println("[" + this.userName + "进入了聊天室]");

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
            storeMassage("[" + userName + "说]：" + msg);
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
      sbf.append("======== 在线成员列表(").append(userList.size())
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
