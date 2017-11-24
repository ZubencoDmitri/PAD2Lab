import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Mediator {
    public static void main(String[] args) throws IOException {
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
//        System.out.println("--------------");
//        transportClient.getData();                                              //get the whole data from this node
//        System.out.println("--------------");

        System.out.println("--------------FILTER--------------");
        filter(transportClient.getData());
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

