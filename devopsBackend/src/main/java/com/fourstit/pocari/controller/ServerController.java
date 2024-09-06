package com.fourstit.pocari.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api")
//@CrossOrigin("http://localhost:5173/")
@CrossOrigin(origins = "*")
@Slf4j
public class ServerController {
    private String serverUuid;

    @PostConstruct
    public void init() {
        serverUuid = UUID.randomUUID().toString();
    }

    @GetMapping("/server-status")
    public ServerStatus getServerStatus() {
        log.info("Get Server Status : {}", serverUuid);
        return new ServerStatus(serverUuid);
    }

    public static class ServerStatus {
        private String serverUuid;

        public ServerStatus(String serverUuid) {
            this.serverUuid = serverUuid;
        }

        public String getServerUuid() {
            return serverUuid;
        }

        public void setServerUuid(String serverUuid) {
            this.serverUuid = serverUuid;
        }
    }
}
