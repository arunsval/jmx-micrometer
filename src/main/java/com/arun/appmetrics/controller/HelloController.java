package com.arun.appmetrics.controller;

import com.codahale.metrics.Meter;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {
    private final Timer responseTimer;
    private final Counter counter;

    @Autowired
    public HelloController(MeterRegistry meterRegistry) {
        this.responseTimer = meterRegistry.timer("helloController.reponseTime");
        this.counter = meterRegistry.counter("helloController.numberOfHits");
    }

    @Timed
    @GetMapping("/hello")
    public String sayHello() {
        long startTime = System.currentTimeMillis();
        try {
            counter.increment();
            return "Hello";
        } finally {
            long endTime = System.currentTimeMillis();
            responseTimer.record(endTime - startTime, TimeUnit.MILLISECONDS);
        }
    }
}
