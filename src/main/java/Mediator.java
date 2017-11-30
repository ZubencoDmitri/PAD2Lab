import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Mediator {
    private static final String tcpUnicastAdress="localhost";
    private static int tcpUnicastPort=1488;                                 //default
    public static final int PORT = 1488;
    //private static final String tcpGetAllNodesEmployeeLists="get all data";

    private Socket serverSocketToConnect;
    private PrintWriter pwrite;
    private OutputStream ostream;
    private InputStream istream;
    //private List<Employee>employeesList;

    public Mediator(int tcpUnicastPort) throws IOException {
        this.tcpUnicastPort=tcpUnicastPort;
        serverSocketToConnect= new Socket(tcpUnicastAdress, tcpUnicastPort); //new client socket - host + port
        ostream = serverSocketToConnect.getOutputStream();
        pwrite = new PrintWriter(ostream, true);
        istream = serverSocketToConnect.getInputStream();
    }

    public void connect() throws IOException{}

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT);
            System.out.println("Mediator started, waiting for connection!");
            Socket socket = serverSocket.accept();
            System.out.println("Accepted:"+socket.getInetAddress());

            try(InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream()){

                byte[] buf = new byte[32 * 1024];
                int readBufs = in.read(buf);
                String line = new String(buf, 0, readBufs);


                System.out.println("----------Mediator started----------");
                System.out.println("--------------UDP-----------------");
                DiscoveryClient discoveryClient=new DiscoveryClient();
                discoveryClient.connect();
                int maxNodesTcpPort=discoveryClient.getMaxNodesTcpPort();               //get the TCP PORT of the node,which
                System.out.println("Was chosen node with maximum nodes.It's port is "
                        +maxNodesTcpPort+" to get all data");

                System.out.println("--------------TCP-----------------");
                TransportClient transportClient=new TransportClient(maxNodesTcpPort);
                transportClient.connect();
                System.out.println("--------------FILTER--------------");
                //filter(transportClient.getData());
                test(line, transportClient.getData());


                //-----------
                System.out.println("Client's filter: "+ line +" "+test(line, transportClient.getData()));
                String str = test(line, transportClient.getData());
                byte[] buffer = str.getBytes(Charset.forName("UTF-8"));
                out.write(buffer);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("----------Mediator started----------");
//        System.out.println("--------------UDP-----------------");
//        DiscoveryClient discoveryClient=new DiscoveryClient();
//        discoveryClient.connect();
//        int maxNodesTcpPort=discoveryClient.getMaxNodesTcpPort();               //get the TCP PORT of the node,which
//        System.out.println("Was chosen node with maximum nodes.It's port is "
//                +maxNodesTcpPort+" to get all data");
//
//        System.out.println("--------------TCP-----------------");
//        TransportClient transportClient=new TransportClient(maxNodesTcpPort);
//        transportClient.connect();
//        System.out.println("--------------");
//        transportClient.getData();                                              //get the whole data from this node
//        System.out.println("--------------");

//        System.out.println("--------------FILTER--------------");
//        filter(transportClient.getData());
    }

    public static String test(String testString, List<Employee> employeeList){
        Pattern more = Pattern.compile("salary>.+");
        Pattern equal = Pattern.compile("salary=.+");
        Matcher m = more.matcher(testString);
        Matcher eq = equal.matcher(testString);
        String str = null;
        int salary= Integer.parseInt(testString.substring(7));
        if (m.matches()) {
            str = "Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() > salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString();
        }
        else if (eq.matches()) {
           str = "Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() == salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString();

        }
        return str;
    }

    public static void filter(List<Employee> employeeList) throws IOException {
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input the rule for salary:");
        String sign = bf.readLine();
        int salary= Integer.parseInt(bf.readLine());
//        System.out.println(sign.trim());
//        char c = sign.charAt(0);
        if (sign.trim().equals(">")){
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() > salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals("<")){
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() < salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals("=")){
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() == salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals("<>")){
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() != salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else System.out.println("Unknown rule");
    }

    }

