package ShardingWithRunnable;

import java.io.*;
import java.net.*;

import static ShardingWithRunnable.Configuration.*;

/**
 * Created by seven on 1/20/15.
 */
public class Client {

    public static int port;
    public static String status;

    // Receive the number of port of the shard by hashcode
    public static int getPortNumber(int hashCode){

        try (Socket socket = new Socket(IP_ADRESS, PORT_MASTER);
             DataInputStream reader = new DataInputStream(socket.getInputStream());
             DataOutputStream writer = new DataOutputStream(socket.getOutputStream());)
        {
            writer.writeInt(hashCode);
            writer.flush();

            port = reader.readInt();

        }catch (Exception e){
            e.printStackTrace();
        }
        return port;
    }

    // Send the object to the appropriate shard with the command: Create
    public static String sendObject(TargetObj obj, int p, Command com){

        try (Socket socket = new Socket(IP_ADRESS, p);
             ObjectOutputStream objout = new ObjectOutputStream(socket.getOutputStream());
             DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
             DataInputStream reader = new DataInputStream(socket.getInputStream()); )
        {
            writer.writeInt(com.getCode());
            writer.flush();
            objout.writeObject(obj);
            objout.flush();

            int statusCode = reader.readInt();
            status = Status.getStatusByCode(statusCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

    public static void main(String[] args) {

        for(int i=0; i<10; i++) {
            TargetObj ob = new TargetObj();
            int code = ob.hashCode();

            int portNumber = getPortNumber(code);

            String command = Command.CREAT.getDescription();
            String mess = sendObject(ob, portNumber, Command.CREAT);
            System.out.println("The object with hashcode " + code + " was saved to the shard with the port " +
                    portNumber + ". Command: " + command + ". Status: "  + mess);
            System.out.println();

        }

    }
}

