package org.calrissian.criteria.serialization;

import org.calrissian.criteria.serialization.NodeDeserializer;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Date: 11/15/12
 * Time: 9:59 AM
 */
@JsonSerialize(using = NodeSerializer.class)
@JsonDeserialize(using = NodeDeserializer.class)
public interface NodeMixin {
}
