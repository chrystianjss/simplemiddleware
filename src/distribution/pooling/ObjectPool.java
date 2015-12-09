package distribution.pooling;

import java.util.concurrent.ConcurrentLinkedQueue;
import distribution.pooling.exception.PoolException;

public abstract class ObjectPool<T> {

	private ConcurrentLinkedQueue<T> pool;
	
	/**
	 * Cria o pool informando o n�mero de objetos a serem criados.
	 */
	public ObjectPool(final int tamanho) {
		pool = new ConcurrentLinkedQueue<T>();
		for (int i = 0; i < tamanho; i++) {
			pool.add(criarObjeto());
		}
	}

	protected abstract T criarObjeto();

	/**
	 * Obt�m o pr�ximo objeto do pool. 
	 * Se for tentado obter um objeto e n�o houver algum presente no pool, uma exce��o � lan�ada.
	 */
	public T obterObjeto() throws PoolException {
		T objeto;
		
		if ((objeto = pool.poll()) == null) {
			throw new PoolException("N�o h� mais elementos presentes no pool.");
		} else {
			return objeto;
		}
	}

	public void retornarObjeto(T objeto) {
		if (objeto == null) {
			return;
		}
		this.pool.offer(objeto);
	}

	public ConcurrentLinkedQueue<T> getPool() {
		return pool;
	}

	public void setPool(ConcurrentLinkedQueue<T> pool) {
		this.pool = pool;
	}
}