package ch.daplab.yarn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rx.functions.Func1;
import ch.daplab.jubilantoctoumbrella.model.Transaction;

/**
 * Created by bperroud on 01/09/16.
 */
public class TransactionToJson implements Func1<Transaction, byte[]> {

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] call(Transaction tx) {

        try {
            return mapper.writeValueAsBytes(tx);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Oups", e);
        }

    }
}
