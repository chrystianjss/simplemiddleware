package distribution;

import infrastructure.ServerRequestHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.pool2.impl.GenericObjectPool;

import distribution.pooling.CalculatorImplPool;
import distribution.pooling.CalculatorImplPooledFactory;

public class CalculatorInvoker {

	private CalculatorImplPool calculatorImplPool = null;
	private static final int TAM_MAX_POOL = 3;
	private static final int TEMPO_MAX_PARA_BUSCAR_NOVO_OBJETO = 3000; // Neste caso, 3 segundos

	// Método construtor
	public CalculatorInvoker() throws NoSuchElementException, IOException {
		// Cria o Pool de objetos CalculatorImpl
		List<CalculatorImpl> calculatorImplList = new ArrayList<CalculatorImpl>();
		GenericObjectPool<CalculatorImpl> genericObjectPool = new GenericObjectPool<CalculatorImpl>(new CalculatorImplPooledFactory());
		genericObjectPool.setMaxTotal(TAM_MAX_POOL);
		genericObjectPool.setMaxWaitMillis(TEMPO_MAX_PARA_BUSCAR_NOVO_OBJETO);
		this.calculatorImplPool = new CalculatorImplPool(genericObjectPool);

		// Ajusta quantidade inicial de objetos no Pool
		for (int i = 0; i < TAM_MAX_POOL; i++) {
			calculatorImplList.add(this.calculatorImplPool.getObjeto());
		}
		for (CalculatorImpl c: calculatorImplList) {
			this.calculatorImplPool.retornaObjeto(c);
		}
	}

	public void invoke(ClientProxy clientProxy) throws IOException, Throwable {
		ServerRequestHandler srh = new ServerRequestHandler(clientProxy.getPort());
		byte[] msgToBeUnmarshalled = null;
		byte[] msgMarshalled = null;
		Message msgUnmarshalled = new Message();
		Marshaller mrsh = new Marshaller();
		Termination ter = new Termination();

		// Create remote object
		CalculatorImpl rObj = this.calculatorImplPool.getObjeto();

		// Inversion loop
		while (true) {

			// @ Receive Message
			msgToBeUnmarshalled = srh.receive();

			// @ Unmarshall received message
			msgUnmarshalled = mrsh.unmarshall(msgToBeUnmarshalled);
			
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
			
			this.calculatorImplPool.retornaObjeto(rObj);
		}
	}
}
