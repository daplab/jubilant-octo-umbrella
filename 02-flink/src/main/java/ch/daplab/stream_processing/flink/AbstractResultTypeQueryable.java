package ch.daplab.stream_processing.flink;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public abstract class AbstractResultTypeQueryable<T> implements ResultTypeQueryable<T> {
    public TypeInformation<T> getProducedType() {
        return TypeExtractor.createTypeInfo(AbstractResultTypeQueryable.class, this.getClass(), 0, (TypeInformation)null, (TypeInformation)null);
    }
}
