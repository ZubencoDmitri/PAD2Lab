//import discovery.DiscoveryClient;
//import model.Employee;
//import transport.TransportClient;

import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class ClientApp {
    public static void main(String[] args) throws IOException {

        System.out.println("----------Client started----------");
//        Mediator mediator = new Mediator(1488);
//        mediator.connect();

        System.out.println("--------------UDP-----------------");
        DiscoveryClient discoveryClient=new DiscoveryClient();
        discoveryClient.connect();
        int maxNodesTcpPort=discoveryClient.getMaxNodesTcpPort();               //get the TCP PORT of the node,which
        System.out.println("Was chosen node with maximum nodes.It's port is "
                +maxNodesTcpPort+" to get all data");                           //contains MAX Connected nodes

        System.out.println("--------------TCP-----------------");
        TransportClient transportClient=new TransportClient(maxNodesTcpPort);
        transportClient.connect();
//        System.out.println("--------------");
//        transportClient.getData();                                              //get the whole data from this node
//        System.out.println("--------------");

        System.out.println("--------------FILTER--------------");
        filter(transportClient.getData());
    }
    public static void filter(List<Employee>employeeList) throws IOException {
        BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input the field and rule:");
        String field = bf.readLine();
        String sign = bf.readLine();
        String value = bf.readLine();
//        System.out.println(sign.trim());
//        char c = sign.charAt(0);
        if (sign.trim().equals("=")&&field.trim().equals("firstname")){
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getFirstName() == value)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals("<>")&&field.trim().equals("firstname")){
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getFirstName() != value)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals(">")&&field.trim().equals("salary")){
            int salary = Integer.parseInt(value);
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() > salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals("<")&&field.trim().equals("salary")){
            int salary = Integer.parseInt(value);
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() < salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals("=")&&field.trim().equals("salary")){
            int salary = Integer.parseInt(value);
            System.out.println("Discovered employees: " +
                    employeeList.stream()
                            .filter(e -> e.getSalary() == salary)
                            .sorted(Comparator.comparing(Employee::getLastName))
                            .collect(Collectors.groupingBy(Employee::getDepartment))
                            .toString()
            );
        }
        else if (sign.trim().equals("<>")&&field.trim().equals("salary")){
            int salary = Integer.parseInt(value);
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
