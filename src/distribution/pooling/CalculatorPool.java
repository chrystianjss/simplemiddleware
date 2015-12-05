package distribution.pooling;

import distribution.CalculatorImpl;

public class CalculatorPool extends ObjectPool<CalculatorImpl> {

	CalculatorImpl calculatorImpl;

	public CalculatorPool(int tamanho) {
		super(tamanho);
	}

	@Override
	public CalculatorImpl criarObjeto() {
		return calculatorImpl = new CalculatorImpl(); 
	}
}