package com.pangaea.helloserver.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PubSubControllerTest {

    @Autowired
    private PubSubController pubSubController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        assertThat(pubSubController).isNotNull();
    }

    @Test
    public void postSubscribeTopicShouldReturnRequestPayload() throws Exception {
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("url", "https://localhost:9000/test1");

        ObjectNode expectedNode = new ObjectNode(JsonNodeFactory.instance);
        expectedNode.put("url", "https://localhost:9000/test1");
        expectedNode.put("topic", "topic1");


        this.mockMvc.perform(
                post("/subscribe/topic1")
                            .content(node.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedNode.toString())));
    }

    @Test
    public void postMessageTestShouldReturnRequestPayload() throws Exception {
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("url", "https://localhost:9000/test1");

        this.mockMvc.perform(
                post("/publish/topic1")
                            .content(node.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(node.toString())));
    }
}