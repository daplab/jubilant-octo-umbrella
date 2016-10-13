package ch.daplab.stream_processing.flink;

import ch.daplab.jubilantoctoumbrella.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.util.serialization.AbstractDeserializationSchema;

import java.io.IOException;

public class TransactionDeserializer extends AbstractDeserializationSchema<Transaction> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Transaction deserialize(byte[] bytes) throws IOException {
        return mapper.readValue(bytes, Transaction.class);
    }

}
