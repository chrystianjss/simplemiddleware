package distribution;

import infrastructure.ServerRequestHandler;

import java.io.IOException;

import distribution.pooling.CalculatorPool;

public class CalculatorInvoker {

	private CalculatorPool calculatorImplPool;
	private static final int TAM_POOL = 3;

	public CalculatorInvoker() {
		calculatorImplPool = new CalculatorPool(TAM_POOL);
	}

	public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(clientProxy.getPort());
		byte[] msgToBeUnmarshalled = null;
		byte[] msgMarshalled = null;
		Message msgUnmarshalled = new Message();
		Marshaller mrsh = new Marshaller();
		Termination ter = new Termination();

		// Inversion loop
		while (true) {
			// @ Receive Message
			msgToBeUnmarshalled = srh.receive();

			// @ Unmarshall received message
			msgUnmarshalled = mrsh.unmarshall(msgToBeUnmarshalled);

			// id - operation - parameters
			int objectId = msgUnmarshalled.getBody().getRequestHeader().getObjectKey(); 
			String operation = msgUnmarshalled.getBody().getRequestHeader().getOperation();
			Float param_1 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
			Float param_2 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(1);

			// Obtém o Objeto Remoto
			CalculatorImpl rObj = this.calculatorImplPool.obterObjeto(objectId);
			this.calculatorImplPool.retornarObjeto(rObj);
			
			CalculatorImpl rObj2 = this.calculatorImplPool.obterObjeto(objectId);
			CalculatorImpl rObj3 = this.calculatorImplPool.obterObjeto();

			switch (operation) {
			case "add":
				// @ Invokes the remote object
				ter.setResult(rObj.add(param_1, param_2));
				break;
			case "sub":
				ter.setResult(rObj.sub(param_1, param_2));
				break;
			case "div":
				ter.setResult(rObj.div(param_1, param_2));
				break;
			case "mul":
				ter.setResult(rObj.mul(param_1, param_2));
				break;
			}

			Message _add_msgToBeMarshalled = new Message(
					new MessageHeader("protocolo", 0, false, 0, 0), 
					new MessageBody(null, null, new ReplyHeader("", 0, 0), 
					new ReplyBody(ter.getResult())));

			// @ Marshall the response
			msgMarshalled = mrsh.marshall(_add_msgToBeMarshalled);

			// @ Send response
			srh.send(msgMarshalled);

			// Devolve objeto para o pool
			this.calculatorImplPool.retornarObjeto(rObj);
		}
	}

	public CalculatorPool getCalculatorImplPool() {
		return calculatorImplPool;
	}
}
