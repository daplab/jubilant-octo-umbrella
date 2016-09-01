package ch.daplab.jubilantoctoumbrella;

import rx.Observable;

public class EventGenerator {
	private RandomGeneratorService generator;
	public EventGenerator(RandomGeneratorService generator) {
		this.generator = generator;
	}
	
	public Observable<Transaction> getTransactions() {
		return Observable.create(observer -> {
			try {
	            if (!observer.isUnsubscribed()) {
	                while (true) {
	                    observer.onNext(generator.getRndTransaction());
	                }
	            }
	        } catch (Exception e) {
	            observer.onError(e);
	        }
		});
	}
}
