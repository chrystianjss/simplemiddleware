package distribution;
 
import java.io.IOException;
 
import commonservices.naming.NamingProxy;
import distribution.CalculatorInvoker;
import distribution.CalculatorProxy;
 
public class CalculatorServer {
 
    public static void main(String[] args) throws IOException, Throwable {
        CalculatorInvoker invoker = new CalculatorInvoker();
 
        // remote object
        CalculatorProxy calculator = new CalculatorProxy();
 
        // obtain instance of Naming Service
        NamingProxy namingService = new NamingProxy("localhost", 1313);
 
        // register calculator in Naming service
        namingService.bind("Calculator1", calculator);
 
        // register calculator in Naming service
        namingService.bind("Calculator2", calculator);
         
        // invoke Invoker
        invoker.invoke(calculator);
    }
}