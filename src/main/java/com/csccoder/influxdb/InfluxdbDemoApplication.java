package com.csccoder.influxdb;

import com.csccoder.influxdb.entity.LogInfo;
import org.influxdb.InfluxDB;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class InfluxdbDemoApplication {

    @Resource
    private InfluxDB influxDB;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(InfluxdbDemoApplication.class, args);


        InfluxdbDemoApplication bean = applicationContext.getBean(InfluxdbDemoApplication.class);
        //bean.insertDB();
        bean.insertBatchDB();
        //bean.query();
    }

    private void insertDB() {
        System.out.println("InfluxdbDemoApplication.insertDB");
        LogInfo logInfo = new LogInfo();
        logInfo.setDeviceId("deviceA");
        logInfo.setLevel("ERROR");
        logInfo.setModule("order");
        logInfo.setMsg("order msg");
        logInfo.setSeq(1);

        long milli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(milli);

        Point point = Point.measurementByPOJO(logInfo.getClass())
                .addFieldsFromPOJO(logInfo)
                .time(milli, TimeUnit.MILLISECONDS)
                .build();

        influxDB.write(point);
    }

    private void insertBatchDB() {
        System.out.println("InfluxdbDemoApplication.insertBatchDB");
        LogInfo logInfo1 = new LogInfo();
        logInfo1.setDeviceId("deviceB");
        logInfo1.setLevel("ERROR22");
        logInfo1.setModule("order");
        logInfo1.setMsg("order msg");
        logInfo1.setSeq(177);

        LogInfo logInfo2 = new LogInfo();
        logInfo2.setDeviceId("deviceB");
        logInfo2.setLevel("DEBUG11");
        logInfo2.setModule("order");
        logInfo2.setMsg("order msg");
        logInfo2.setSeq(188);

        List<LogInfo> infoList = new ArrayList<>();
        infoList.add(logInfo1);
        infoList.add(logInfo2);


        BatchPoints batchPoints = BatchPoints.builder()
                .consistency(InfluxDB.ConsistencyLevel.ONE)
                .build();
        infoList.stream().forEach(logInfo -> {
            long milli = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            System.out.println(milli);

            Point point = Point.measurementByPOJO(logInfo.getClass())
                    .addFieldsFromPOJO(logInfo)
                    .time(milli, TimeUnit.MILLISECONDS)
                    .build();
            batchPoints.point(point);
        });

        influxDB.write(batchPoints);
    }

    public void query() {
        System.out.println("InfluxdbDemoApplication.query");
        Query query = new Query("SELECT * FROM " + LogInfo.class.getAnnotation(Measurement.class).name());
        QueryResult queryResult = influxDB.query(query);
        List<QueryResult.Result> results = queryResult.getResults();
        System.out.println(results);
        List<QueryResult.Series> series = results.get(0).getSeries();
        System.out.println(series);
        //1596347386946000000
    }
}
