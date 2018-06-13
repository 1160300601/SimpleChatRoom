package sock2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 简单实现多个客户端的共享
 * @author MaXU
 *
 */
public class Client {
  public static final String SERVER_IP = "127.0.0.1";
  public static final int SERVER_PORT = 2333;
  public static final String END_MARK = "quit";
  
  private Socket client = null;
  BufferedWriter out = null;
  BufferedReader inConsol = null;
  
  public Client() throws UnknownHostException, IOException {
    this.client = new Socket(SERVER_IP, SERVER_PORT);
    System.out.println("[You enter chartroom successfully!]");
    new Thread(new lisentPort()).start();;
    printMsg();
  }
  
  public void printMsg() throws IOException {
    out = new BufferedWriter(new OutputStreamWriter(this.client.getOutputStream()));
    inConsol = new BufferedReader(new InputStreamReader(System.in));
    String write;
    while (true) {
      write = inConsol.readLine();
      out.write(write);
      out.write("\n");
      out.flush();
    }
  }
  
  
  private class lisentPort implements Runnable {
    private String result;
    private BufferedReader inServer;
    
    @Override
    public void run() {
      try {
        inServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while (true) {
          result = inServer.readLine();
          if(result.equals(END_MARK)) {
            System.out.println("[You quit chartroom successfully!]");  
            break;
          }else {
            System.out.println(result);
          }
          
        }
      } catch (IOException e) {
        e.printStackTrace();
      }finally {
        try {
          inServer.close();
          inConsol.close();
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  
  public static void main(String[] args) throws UnknownHostException, IOException {
    new Client();
  }
  
}
