package ch.daplab.jubilantoctoumbrella;

import rx.Observable;

import ch.daplab.jubilantoctoumbrella.RandomGenerator;

public class EventGenerator {
	private RandomGenerator generator;
	public EventGenerator(RandomGenerator generator) {
		this.generator = generator;
	}
	
	public Observable<Transaction> getTransactions() {
		return Observable.create(observer -> {
			try {
	            if (!observer.isUnsubscribed()) {
	                while (true) {
	                    observer.onNext(generator.getRndTransaction());
	                }
	                observer.onCompleted();
	            }
	        } catch (Exception e) {
	            observer.onError(e);
	        }
		});
	}
}
