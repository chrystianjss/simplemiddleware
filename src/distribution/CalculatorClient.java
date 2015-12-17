package distribution;

import java.io.IOException;
import java.net.UnknownHostException;

import commonservices.naming.NamingProxy;

public class CalculatorClient {

	public static void main(String[] args) throws UnknownHostException,
	IOException, Throwable {

		// create an instance of Naming Service
		NamingProxy namingService = new NamingProxy("localhost", 1313);

		// check registered services
		System.out.println(namingService.list());

		// look for Calculator in Naming service
		CalculatorProxy calculatorProxy = (CalculatorProxy) namingService
				.lookup("Calculator");

		// invoke calculator
		//calculatorProxy.add(1, 3);
		try {
			System.out.println("Resultado da opera��o: "+calculatorProxy.add(1, 3));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}