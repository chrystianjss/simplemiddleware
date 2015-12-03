package distribution.pooling;

import java.io.IOException;

import org.apache.commons.pool2.ObjectPool;

import distribution.CalculatorImpl;

public class CalculatorImplPool {

	private ObjectPool<CalculatorImpl> pool;

	public CalculatorImplPool(ObjectPool<CalculatorImpl> pool) {
		this.pool = pool;
	}

	public CalculatorImpl getObjeto() throws IOException {
		CalculatorImpl obj = null;
		try {
			obj = pool.borrowObject();
			return obj;
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Não foi possível obter um objeto do pool" + e.toString());
		} 
	}

	public void retornaObjeto(CalculatorImpl obj) {
		try {
			if (null != obj) {
				pool.returnObject(obj);
			}
		} catch (Exception e) {}
	}

	public ObjectPool<CalculatorImpl> getPool() {
		return pool;
	}

	public void setPool(ObjectPool<CalculatorImpl> pool) {
		this.pool = pool;
	}

}