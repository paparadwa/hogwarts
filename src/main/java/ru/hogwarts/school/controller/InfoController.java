package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
public class InfoController {
    @Value("${server.port}")
    private Integer port;

    @GetMapping("/port")
    public ResponseEntity<Integer> getPort() {
        return ResponseEntity.ok(port);
    }

    @GetMapping("/optimize")
    public long tryToOptimizeComputing() {
        return Stream.iterate(1L, a -> a + 1)
                .parallel()
                .limit(1_000_000)
                .reduce(0L, Long::sum);
    }
}
