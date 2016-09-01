package ch.daplab.jubilantoctoumbrella;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TestEventGenerator {

	private EventGenerator generator;

	@Before
	public void initObjects() {
		generator = new EventGenerator(new DummyRandomGenerator());
	}

	@Test
	public void shouldGenerateInfiniteTransactionStream() {
		ArrayList<String> transactions = new ArrayList<String>();
		generator.getTransactions().take(5).subscribe(transaction -> {
			transactions.add(transaction.getSeq());
		}, err -> fail(), () -> assertEquals(transactions.size(), 5));
	}

}
