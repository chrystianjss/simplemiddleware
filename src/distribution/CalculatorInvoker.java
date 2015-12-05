package distribution;

import infrastructure.ServerRequestHandler;

import java.io.IOException;

import distribution.pooling.CalculatorPool;

public class CalculatorInvoker {

	private CalculatorPool calculatorImplPool;
	private static final int TAM_POOL = 3;
	
	public CalculatorInvoker() {
		// Cria o pool de objetos de CalculatorImpl
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
			
			// Obtém o Objeto Remoto
			CalculatorImpl rObj = this.calculatorImplPool.obterObjeto();
			
			switch (msgUnmarshalled.getBody().getRequestHeader().getOperation()) {

				case "add":
					// @ Invokes the remote object
					Float _add_p1 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
					Float _add_p2 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(1);
					ter.setResult(rObj.add(_add_p1, _add_p2));
	
					Message _add_msgToBeMarshalled = new Message(
							new MessageHeader("protocolo", 0, false, 0, 0), 
							new MessageBody(null, null, new ReplyHeader("", 0, 0), 
							new ReplyBody(ter.getResult())));
	
					// @ Marshall the response
					msgMarshalled = mrsh.marshall(_add_msgToBeMarshalled);
	
					// @ Send response
					srh.send(msgMarshalled);
					break;
	
				case "sub":
					// @ Invokes the remote object
					Float _sub_p1 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
					Float _sub_p2 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(1);
					ter.setResult(rObj.sub(_sub_p1, _sub_p2));
	
					Message msgToBeMarshalled = new Message(
							new MessageHeader("protocolo", 0, false, 0, 0), 
							new MessageBody(null, null, new ReplyHeader("", 0, 0), 
							new ReplyBody(ter.getResult())));
	
					// @ Marshall the response
					msgMarshalled = mrsh.marshall(msgToBeMarshalled);
	
					// @ Send response
					srh.send(msgMarshalled);
					break;
	
				case "div":
					// @ Invokes the remote object
					Float _div_p1 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
					Float _div_p2 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(1);
					ter.setResult(rObj.div(_div_p1, _div_p2));
	
					Message _div_msgToBeMarshalled = new Message(
							new MessageHeader("protocolo", 0, false, 0, 0), 
							new MessageBody(null, null, new ReplyHeader("", 0, 0), 
							new ReplyBody(ter.getResult())));
	
					// @ Marshall the response
					msgMarshalled = mrsh.marshall(_div_msgToBeMarshalled);
	
					// @ Send response
					srh.send(msgMarshalled);
					break;
	
				case "mul":
					// @ Invokes the remote object
					Float _mul_p1 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
					Float _mul_p2 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(1);
					ter.setResult(rObj.mul(_mul_p1, _mul_p2));
	
					Message _mul_msgToBeMarshalled = new Message(
							new MessageHeader("protocolo", 0, false, 0, 0), 
							new MessageBody(null, null, new ReplyHeader("", 0, 0), 
							new ReplyBody(ter.getResult())));
	
					// @ Marshall the response
					msgMarshalled = mrsh.marshall(_mul_msgToBeMarshalled);
	
					// @ Send response
					srh.send(msgMarshalled);
					break;
			}
			
			this.calculatorImplPool.retornarObjeto(rObj);
		}
	}
}
