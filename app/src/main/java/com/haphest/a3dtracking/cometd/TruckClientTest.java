package com.haphest.a3dtracking.cometd;


import android.os.Build;

import androidx.annotation.RequiresApi;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.type.TypeReference;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class TruckClientTest {

    public static class ZonedDateTimeJsonSerializer extends JsonSerializer<ZonedDateTime> {
        @Override
        public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            //jsonGenerator.writeNumber(zonedDateTime.toInstant().toEpochMilli());
            jsonGenerator.writeString(zonedDateTime.toString());
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public Class<ZonedDateTime> handledType() {
            return ZonedDateTime.class;
        }

    }

    public static class DateJsonDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String text = jp.getText();
            return new Date();
        }
    }

    protected String cometdURL;

    protected HttpClient httpClient;

    private final ClientSessionChannel.MessageListener truckEventListener = new TruckEventListener();

    private final ClientSessionChannel.MessageListener subscribeCallback = new ClientSessionChannel.MessageListener() {
        @Override
        public void onMessage(ClientSessionChannel channel, Message message) {
            if (!message.isSuccessful()) {
                System.out.println("Subscribe failed.");
            }
        }
    };

    public TruckClientTest() {
        String contextPath = "";
        int port = 9999;
        String cometdServletPath = "/cometd";
        //cometdURL = "http://localhost:" + port + contextPath + cometdServletPath;
        // ///////////////////////////////////////////
        //使用外网测试地址
        cometdURL = "http://yangjiefeng.natapp1.cc" + cometdServletPath;
        // ///////////////////////////////////////////
        System.out.println("CometD URL: " + cometdURL);

        httpClient = new HttpClient();


        Map<String, Object> transportOptions = new HashMap<String, Object>();
        // Use the Jackson implementation
        //JSONContext.Client jsonContext = new JacksonJSONContextClient();
        //transportOptions.put(ClientTransport.JSON_CONTEXT, jsonContext);

        try {
            httpClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final BayeuxClient client = newBayeuxClient(transportOptions);
        client.handshake();//握手（建立连接）

        boolean handShaken = client.waitFor(10000, BayeuxClient.State.CONNECTED);
        if (handShaken) {//如果握手成功
            String truckId = UUID.randomUUID().toString();

            client.getChannel("/trucks/" + truckId).subscribe(truckEventListener, subscribeCallback);

            // /////////////////////////////////////////////////////////////////
            // 我们可以定义一些有更明确业务含义的 POJO（静态类型）。
            // 比如这里的 TruckEvent 类型。
            // //////////////////////////////////////////////////////////////////
            TruckEvent truckEvent = getTestTruckEvent(truckId);
            // //////////////////////////////////////////////////////////////
            // Publish 到服务端以及从服务端推送过来的数据一般使用 Map 类型。
            // 示例使用 Jackson ObjectMapper 实现 Map 与 POJO 之间互转。
            // Map 的 Value 类型为对应到 JSON 类型（String、Number 等）的“简单” Java 类型。
            // 当然不使用 Jackson ObjectMapper 也可以。
            // ////////////////////////////////////////////////////////////
            ObjectMapper objectMapper = getObjectMapper();
            TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<Map<String, Object>>() {
            };
            try {
                //                String jsonStr = objectMapper.writeValueAsString(truckEvent);
                //                Map data = objectMapper.readValue(jsonStr, Map.class);
                // //////////////////////////////////////////////////////////////////
                // 客户端把消息发布到“服务”通道，服务端接收到消息后，会进行进一步处理。
                // 服务端可能会将消息经过处理后广播出去，
                // 也可能将消息转发给特定客户端（“私聊”），等等。
                // /////////////////////////////////////////////////////////////////
                // 这是一个服务通道：/service/trucks/{truckId}
                // 默认路径中带着 service 前缀的通道为服务通道
                // /////////////////////////////////////////////////////////////////
                Map data = objectMapper.convertValue(truckEvent, mapTypeReference);
                System.out.println("-------------------------------------------");
                System.out.println("Publish to service channel...");
                client.getChannel("/service/trucks/" + truckId).publish(data);
            } catch (Exception e) {
                //todo
                e.printStackTrace();
            }

            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // /////////////// 发布到“广播”通道（channel）//////////////////////
            TruckEvent truckEvent2 = getTestTruckEvent(truckId);
            Map<String, Object> data2 = objectMapper.convertValue(truckEvent2, mapTypeReference);
            System.out.println("------------------------------------------------");
            System.out.println("Publish to broadcast channel...");
            // 这是一个广播通道：/trucks/{truckId}
            client.getChannel("/trucks/" + truckId).publish(data2);
            // /////////////////////////////////////////////////////////////////////////
            // 对于“广播”通道的消息，服务端的默认行为是转发给所有订阅了该通道的客户端。
            // /////////////////////////////////////////////////////////////////////////

//            Map<String, Object> truckEvent = new HashMap<>();
//            truckEvent.put("class", "org.dddml.wms.cometd.TruckEvent");
//            truckEvent.put("eventId", UUID.randomUUID().toString());
//            truckEvent.put("eventType", "CLAMPED");
//            truckEvent.put("truckId", truckId);
//            truckEvent.put("occurredAt", ZonedDateTime.now().toInstant().toEpochMilli());
////            ObjectMapper objectMapper = new ObjectMapper();
////            try {
////                truckEvent.put("goodLocation", objectMapper.writeValueAsString(new Point3D(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE)));
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            Map<Object, Object> locMap = new HashMap<>();
//            locMap.put("x", BigDecimal.ONE);
//            locMap.put("y", BigDecimal.TEN);
//            locMap.put("z", BigDecimal.ONE);
//            truckEvent.put("goodLocation", locMap);
//            client.getChannel("/service/trucks/" + truckId).publish(truckEvent);


        }


    }

    private TruckEvent getTestTruckEvent(String truckId) {
        TruckEvent truckEvent = new TruckEvent();
        truckEvent.setEventId(UUID.randomUUID().toString());
        truckEvent.setEventType(TruckEvent.EVENT_TYPE_CLAMPED);
        truckEvent.setTruckId(truckId);
        truckEvent.setOccurredAt(new Date());
        truckEvent.setGoodLocation(new Point3D(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ONE));
        return truckEvent;
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("truckEventModule", Version.unknownVersion());
        module.addSerializer(new ZonedDateTimeJsonSerializer());
        module.addDeserializer(Date.class, new DateJsonDeserializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    public BayeuxClient newBayeuxClient(Map<String, Object> transportOptions) {
        BayeuxClient client = new BayeuxClient(cometdURL, new LongPollingTransport(transportOptions, httpClient));
        //client.setDebugEnabled(debugTests());
        return client;
    }

    private class TruckEventListener implements ClientSessionChannel.MessageListener {

        public void onMessage(ClientSessionChannel channel, Message message) {
            //从服务端推送过来的数据一般是 Map 类型。
            Map<String, Object> data = message.getDataAsMap();
            System.out.println(data);
            //使用 Jackson ObjectMapper 将 Map 类型转换为 POJO（静态类型）
            TruckEvent truckEvent = getObjectMapper().convertValue(data, TruckEvent.class);
            System.out.println(truckEvent);
//            TruckEvent input = (TruckEvent)message.getData();
//            System.out.println(input);

        }
    }

}
