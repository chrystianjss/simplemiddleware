package distribution;

import java.io.IOException;

public interface ICalculator {

	public float add(float x, float y) throws IOException,
			InterruptedException, Throwable;

	public float sub(float x, float y) throws IOException,
			InterruptedException, Throwable;

	public float mul(float x, float y) throws IOException,
			InterruptedException, Throwable;

	public float div(float x, float y) throws IOException,
			InterruptedException, Throwable;
}
