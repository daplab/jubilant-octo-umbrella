package ch.daplab.jubilantoctoumbrella;

import ch.daplab.jubilantoctoumbrella.model.Transaction;

public class DummyRandomGenerator implements RandomGeneratorService {

	@Override
	public Transaction getRndTransaction() {
		return new Transaction(false, "abc", "abc", "abc", "abc", 0, "abc", 0, (byte) 123, "abc", "abc", "abc", "abc");
	}

}
