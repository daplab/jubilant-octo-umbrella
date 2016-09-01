package ch.daplab.jubilantoctoumbrella;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestEventGenerator {

	private EventGenerator generator;
	private EventGenerator generator2;

	@Before
	public void initObjects() {
		generator = new EventGenerator(new DummyRandomGenerator());
		generator2 = new EventGenerator(new RandomGenerator());
	}

	@Test
	public void shouldGenerateInfiniteTransactionStream() {
		ArrayList<String> transactions = new ArrayList<String>();
		generator2.getTransactions().take(5).subscribe(transaction -> {
			transactions.add(transaction.getSeq());
		}, err -> fail(), () -> assertEquals(transactions.size(), 5));
	}

}
