package ShardingWithRunnable;

import java.io.*;
import java.net.*;

import static ShardingWithRunnable.Configuration.*;

/**
 * Created by seven on 1/21/15.
 */
public class Server {

    public static int port;

    // Master sends the number of port where to store the object
    public static void tellPortNumber(){
        try (ServerSocket server = new ServerSocket(PORT_MASTER)){
            System.out.println("Master waiting for a client");
            System.out.println();

            while (true) {
                try (Socket clientSockect = server.accept();
                     DataInputStream reader = new DataInputStream(clientSockect.getInputStream());
                     DataOutputStream writer = new DataOutputStream(clientSockect.getOutputStream())) {
                    System.out.println("Master got a Client.");
                    int code = reader.readInt();
                    if (code % 2 == 0)
                        port = PORT_SHARD2;
                    else port = PORT_SHARD1;

                    writer.writeInt(port);
                    writer.flush();

                    System.out.println("Master: Object with hashCode " + code + " should be stored in the Shard with port: " + port + ".");
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // Saving the object to the appropriate shard
    public static void storeObject(int portNumber, String fileName){
        try (ServerSocket serv = new ServerSocket(portNumber)){
            System.out.println("Shard (port " + portNumber + ") waiting for a client");
            System.out.println();

            while (true) {
                try (Socket client = serv.accept();
                     DataInputStream reader = new DataInputStream(client.getInputStream());
                     ObjectInputStream objin = new ObjectInputStream(client.getInputStream());
                     ObjectOutputStream objout = new ObjectOutputStream(new FileOutputStream(fileName, true));
                     DataOutputStream writer = new DataOutputStream(client.getOutputStream())) {
                    System.out.println("Shard with the port " + portNumber + " got a Client.");

                    int commandCode = reader.readInt();
                    System.out.println("Command: " + Command.getCommandByCode(commandCode));

                    TargetObj ob = (TargetObj) objin.readObject();
                    objout.writeObject(ob);

                    writer.writeInt(Status.OK.getCode());
                    writer.flush();
                    System.out.println("Shard " + portNumber + ": The object was saved to file: " + fileName + ". Status: " + Status.OK.getDescription());
                    System.out.println();

                } catch (Exception e) {
                    e.printStackTrace();
/*                    writer.writeInt(Status.FAILED.getCode());
                    writer.flush();*/
                    System.out.println("Shard " + portNumber + ": The object was saved to file: " + fileName + ". Status: " + Status.FAILED.getDescription());
                    System.out.println();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){

        Thread masterThread = new Thread(() -> new Server().tellPortNumber());
        masterThread.start();

        Thread firstShardThread = new Thread(() -> new Server().storeObject(PORT_SHARD1, FILE1));
        firstShardThread.start();

        Thread secondShardThread = new Thread(() -> new Server().storeObject(PORT_SHARD2, FILE2));
        secondShardThread.start();
    }
}


// Process finished with exit code 137 ?????