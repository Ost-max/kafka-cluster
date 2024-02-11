package ru.home.kafka.proccessor;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import ru.home.kafka.dto.MLoseMessage;
import ru.home.kafka.dto.MotherMessage;
import ru.home.kafka.dto.OruMessage;

@Component
@Slf4j
public class StreamProcessors {

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Bean
    public Serde<OruMessage> oruSerde() {

     /*   new JsonSerde<>(MyKeyType.class)
                .forKeys()
                .noTypeInfo()*/
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(OruMessage.class));
    }

    @Bean
    public Serde<MotherMessage> mapiSerde() {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(MotherMessage.class));
    }

    @Bean
    public KStream<Long, MLoseMessage> mLoseStream(StreamsBuilder kStreamBuilder) {
        var oruStream =   kStreamBuilder
               .stream("oru", Consumed.with(Serdes.Long(), oruSerde()))
               .mapValues( oru ->  MLoseMessage.builder()
                       .status(oru.getStatus())
                       .amount(oru.getAmount())
                       .orderId(oru.getOrderId())
                       .serviceName(oru.getServiceName())
                       .partnerName(oru.getPartnerName())
                       .build()
               );

        var mapiStream =   kStreamBuilder
                .stream("mapi", Consumed.with(Serdes.Long(), mapiSerde()))
                .mapValues( mapi ->  MLoseMessage.builder()
                        .status(mapi.getStatus())
                        .amount(mapi.getAmount())
                        .orderId(mapi.getOrderId())
                        .build()
                );

        return mapiStream.merge(oruStream);
    }


    @Bean
    public KTable<Long, MLoseMessage> kTable(KStream<Long, MLoseMessage> mLoseStream, KafkaStreams kafkaStreams) {


        Materialized<Long, MLoseMessage, KeyValueStore<Bytes, byte[]>> materialized = Materialized.as("mlosed");
        log.info("!!!!!!!!!!!!!!!!!! materialized {}", materialized.storeType);
        return mLoseStream.groupByKey().reduce( (existMsg, newMsg ) -> {
            if(newMsg.getAmount() != null) {
                existMsg.setAmount(newMsg.getAmount());
            }

            if(newMsg.getPartnerName() != null) {
                existMsg.setPartnerName(newMsg.getPartnerName());
            }

            if(newMsg.getServiceName() != null) {
                existMsg.setServiceName(newMsg.getServiceName());
            }

            if(newMsg.getStatus() != null) {
                existMsg.setStatus(newMsg.getStatus());
            }

            return existMsg;
        }, materialized);
    }





}
