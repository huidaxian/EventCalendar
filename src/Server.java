

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class ClientThread implements Runnable{
    Socket socket;
    EventCalendar ecar;
    ClientThread(Socket socket, EventCalendar ecar){
        this.socket = socket;
        this.ecar=ecar;
    }
    @Override
    public void run() {
        ecar = new EventCalendar();
        InputStream in =null;
        OutputStream out = null;
        ObjectInputStream objIn = null;
        ObjectOutputStream objOut = null;
        try{
            EventCalendar ear = new EventCalendar();
            in = socket.getInputStream();

            objIn = new ObjectInputStream(new BufferedInputStream(in));
            ear = (EventCalendar)objIn.readObject();
            socket.shutdownInput();
            System.out.println(ear.items.get(0).get(0).getComponent(0));




            /*out = socket.getOutputStream();
            objOut = new ObjectOutputStream(new BufferedOutputStream(out));
            objOut.writeObject(ecar);
            out.close();
            socket.shutdownOutput();*/
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
public class Server {
    ServerSocket server ;
    EventCalendar ecar;
    Server() throws IOException {
        try {
            server = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ecar = new EventCalendar();

    }
    void listen(){
        while(true){
            try {
                Socket socket = server.accept();
                Thread thread= new Thread(new ClientThread(socket,ecar));
                thread.start();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new Server().listen();
    }
}