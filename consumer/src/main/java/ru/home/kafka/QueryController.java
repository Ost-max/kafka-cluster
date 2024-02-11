package ru.home.kafka;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.internals.InMemoryKeyValueStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.home.kafka.dto.MLoseMessage;

import java.util.List;

@RestController
public class QueryController {

    private final KafkaStreams kafkaStreams;

    public QueryController(KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;

    }

    @GetMapping("/allReady")
    public List<MLoseMessage> getWordCount() {
        InMemoryKeyValueStore<Long, MLoseMessage> mlosed = kafkaStreams.query(
                StoreQueryParameters.fromNameAndType("mlosed", QueryableStoreTypes.keyValueStore())
        );
        return counts.get(word);
    }


}
