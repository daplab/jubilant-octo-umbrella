package ch.daplab.jubilantoctoumbrella;

import ch.daplab.jubilantoctoumbrella.model.Transaction;
import rx.Observable;

public class EventGenerator {
	private RandomGeneratorService generator;

	public EventGenerator(RandomGeneratorService generator) {
		this.generator = generator;
	}

	public Observable<Transaction> getTransactions() {
		return Observable.create(observer -> {
			try {
				while (true) {
					if (!observer.isUnsubscribed()) {
						observer.onNext(generator.getRndTransaction());
					} else {
						observer.onCompleted();
						break;
					}
				}
			} catch (Exception e) {
				observer.onError(e);
			}
		});
	}
}
