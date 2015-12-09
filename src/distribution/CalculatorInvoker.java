package distribution;

import infrastructure.ServerRequestHandler;

import java.io.IOException;

import distribution.pooling.CalculatorPool;
import distribution.pooling.exception.PoolException;

public class CalculatorInvoker {

	private CalculatorPool calculatorImplPool;
	private static final int TAM_POOL = 3;

	public CalculatorInvoker() {
		calculatorImplPool = new CalculatorPool(TAM_POOL);
	}

	public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(clientProxy.getPort());
		byte[] msgToBeUnmarshalled = null;

		// Inversion loop
		while (true) {
			// @ Receive Message
			msgToBeUnmarshalled = srh.receive();

			CalculatorInvokerThread calculatorInvokerThread = new CalculatorInvokerThread(srh, msgToBeUnmarshalled, this.calculatorImplPool); 
			new Thread(calculatorInvokerThread).start();
		}
	}

	public class CalculatorInvokerThread implements Runnable {
		ServerRequestHandler srh = null;
		byte[] msgToBeUnmarshalled = null;
		CalculatorPool calculatorImplPool = null;
		
		public CalculatorInvokerThread(ServerRequestHandler srh, byte[] msgToBeUnmarshalled, CalculatorPool calculatorImplPool) {
			this.srh = srh;
			this.msgToBeUnmarshalled = msgToBeUnmarshalled;
			this.calculatorImplPool = calculatorImplPool; 
		}
				
		@Override
		public void run() {
			byte[] msgMarshalled = null;
			Message msgUnmarshalled = new Message();
			Marshaller mrsh = new Marshaller();
			Termination ter = new Termination();
			
			// @ Unmarshall received message
			msgUnmarshalled = mrsh.unmarshall(msgToBeUnmarshalled);

			// id - operation - parameters
			int objectId = msgUnmarshalled.getBody().getRequestHeader().getObjectKey(); 
			String operation = msgUnmarshalled.getBody().getRequestHeader().getOperation();
			Float param_1 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(0);
			Float param_2 = (Float) msgUnmarshalled.getBody().getRequestBody().getParameters().get(1);

			// Obtém o Objeto Remoto
			CalculatorImpl rObj;
			try {
				rObj = this.calculatorImplPool.obterObjeto(objectId);

//				this.calculatorImplPool.retornarObjeto(rObj);

//				CalculatorImpl rObj2 = this.calculatorImplPool.obterObjeto(objectId);
//				CalculatorImpl rObj3 = this.calculatorImplPool.obterObjeto();

				switch (operation) {
				case "add":
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
				this.srh.send(msgMarshalled);

				// Devolve objeto para o pool
				this.calculatorImplPool.retornarObjeto(rObj);
			} catch (PoolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public CalculatorPool getCalculatorImplPool() {
		return calculatorImplPool;
	}
}
