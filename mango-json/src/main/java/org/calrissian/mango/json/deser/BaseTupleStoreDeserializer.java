package org.calrissian.mango.json.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleStore;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Iterables.concat;

public abstract class BaseTupleStoreDeserializer<T extends TupleStore> extends JsonDeserializer<T> {

    private static final TypeReference TR = new TypeReference<Map<String, Collection<Tuple>>>(){};
    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        T tupleStore = deserialize(root);

        ObjectNode tuplesObject = (ObjectNode) root.get("tuples");

        Map<String, Collection<Tuple>> tuples =
                jsonParser.getCodec().readValue(jsonParser.getCodec().treeAsTokens(tuplesObject), TR);

        tupleStore.putAll(concat(tuples.values()));

        return tupleStore;


    }

    public abstract T deserialize(JsonNode root);
}
