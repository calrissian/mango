package org.calrissian.mango.types.serialization;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Date: 9/12/12
 * Time: 2:52 PM
 */
@JsonSerialize(using = TypedTupleSerializer.class)
@JsonDeserialize(using = TypedTupleDeserializer.class)
public interface TypedTupleMixin {
}
