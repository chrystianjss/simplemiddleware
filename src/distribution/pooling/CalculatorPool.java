package distribution.pooling;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import distribution.CalculatorImpl;
import distribution.pooling.exception.PoolException;

public class CalculatorPool extends ObjectPool<CalculatorImpl> {

	CalculatorImpl calculatorImpl;
	Map<Integer, CalculatorImpl> mapaCalculator = new HashMap<Integer, CalculatorImpl>(); 

	public CalculatorPool(int tamanho) {
		super(tamanho);
	}

	@Override
	public CalculatorImpl criarObjeto() {
		return calculatorImpl = new CalculatorImpl(); 
	}
	
	public CalculatorImpl obterObjeto(int objectId) throws PoolException {
		boolean encontrou = false;
		CalculatorImpl c = new CalculatorImpl();
		Iterator<CalculatorImpl> it = this.getPool().iterator();
		
		while(it.hasNext() && !encontrou){
			 c = it.next();
			 if(c.getId() == objectId){
				 encontrou = true;
			 } 
		}
		if(!encontrou){
			c = this.obterObjeto();
			c.setId(objectId);
		}
		return c;
	}
}