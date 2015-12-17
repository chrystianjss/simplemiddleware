package distribution;

import infrastructure.ServerRequestHandler;

import java.io.IOException;

import utilsconf.UtilsConf;
import distribution.pooling.CalculatorPool;
import distribution.pooling.exception.TamanhoPoolException;

public class CalculatorInvoker {

	private CalculatorPool calculatorImplPool;
		
	public CalculatorInvoker() {
		// Cria o pool de objetos de CalculatorImpl
		calculatorImplPool = new CalculatorPool(UtilsConf.TAM_POOL);
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

			CalculatorImpl rObj = this.calculatorImplPool.obterObjeto();
			boolean encontrou = false;
			int qtdTentativas = 0;
			
			while (!encontrou) {
				try{
					// Obt�m o Objeto Remoto
					rObj = this.calculatorImplPool.obterObjeto();
					encontrou = true;
				} catch(TamanhoPoolException tpe) {
					qtdTentativas = qtdTentativas + 1;
					if (qtdTentativas == UtilsConf.QTD_MAX_TENTATIVAS){
						encontrou = true;
					}
					Thread.sleep(2000L); // 2 segundos para realizar a nova tentativa					
				}
			}

			if(qtdTentativas == UtilsConf.QTD_MAX_TENTATIVAS){
				Message _add_msgToBeMarshalled = new Message(
						new MessageHeader("protocolo", 0, false, 0, 0), 
						new MessageBody(null, null, new ReplyHeader("", 0, 0), 
								new ReplyBody(UtilsConf.MSG_ERRO_POOL)));

				// @ Marshall the response
				msgMarshalled = mrsh.marshall(_add_msgToBeMarshalled);

				// @ Send response
				srh.send(msgMarshalled);
			} else {

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
}
