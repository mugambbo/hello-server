package com.pangaea.helloserver.controllers;

import com.pangaea.helloserver.model.SubscribeReqBody;
import com.pangaea.helloserver.model.SubscribeResBody;
import com.pangaea.helloserver.service.ProducerMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class PubSubController {

    private static final Logger logger = LogManager.getLogger(PubSubController.class);
    private final ProducerMessageService helloMessageService;

    @Autowired
    public PubSubController(ProducerMessageService helloMessageService) {
        this.helloMessageService = helloMessageService;
    }

    @RequestMapping(value = "/subscribe/{topic}", method = RequestMethod.POST)
    public ResponseEntity<SubscribeResBody> postSubscribeTopic(@PathVariable String topic, @RequestBody SubscribeReqBody body){
        logger.info("About to subscribe to topic "+topic);
        if (body.getUrl() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        SubscribeResBody res = new SubscribeResBody(topic, body.getUrl());
        Long reply = helloMessageService.subscribeTopic(res);
        if (reply > -1) return new ResponseEntity<>(res, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/publish/{topic}", method = RequestMethod.POST)
    public ResponseEntity<Object> postMessage(@PathVariable String topic, @RequestBody Map<String, Object> body){
        logger.info("About to publish message to subscribers "+topic+", "+body);
        helloMessageService.sendMessage(topic, body);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @RequestMapping(value = "*")
    public ResponseEntity<String> any(){
        logger.info("You don't belong here");
        return new ResponseEntity<>("Welcome to Hello World Server. How you got here remains a mystery", HttpStatus.OK);
    }
}
