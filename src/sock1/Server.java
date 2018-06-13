package sock1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ServerSocket {
  private static final int SERVER_PORT = 2333;
  
  public Server() throws IOException {
    super(SERVER_PORT);
    
    while (true) {
      Socket socket = accept();
      new CreateServerThread(socket);
    }
  }
  
  class CreateServerThread extends Thread{
    private Socket client;
    private BufferedReader br;
    private PrintStream ps;
    private BufferedReader brConsole = new BufferedReader(new InputStreamReader(System.in));
    
    public CreateServerThread(Socket s) throws IOException {
      client = s;
      br = new BufferedReader(new InputStreamReader(client.getInputStream()));
      ps = new PrintStream(client.getOutputStream());
      System.out.println("Client("+getName()+") come in¡­¡­");
      
      start();
    }
    
    public void run() {
      try {
        String line = "OK";
        while (!line.equals("bye")) {
          line = br.readLine();
          System.out.println("CLient("+getName()+") say:"+line);
          line = brConsole.readLine();
          ps.println(line);
          ps.flush();
        }
        ps.println("bye,Client("+getName()+")!");
        System.out.println("Client("+getName()+") exit!");
        ps.close();
        br.close();
        client.close();
        
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void main(String[] args) throws IOException {
    new Server();
  }
}
