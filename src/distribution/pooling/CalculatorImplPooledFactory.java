package distribution.pooling;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import distribution.CalculatorImpl;

public class CalculatorImplPooledFactory extends BasePooledObjectFactory<CalculatorImpl> {

	@Override
	public CalculatorImpl create() throws Exception {
		return new CalculatorImpl();
	}

	@Override
	public PooledObject<CalculatorImpl> wrap(CalculatorImpl calculatorImpl) {
		 return new DefaultPooledObject<CalculatorImpl>(calculatorImpl);
	}

}