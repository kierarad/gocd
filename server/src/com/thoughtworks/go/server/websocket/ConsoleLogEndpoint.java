/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thoughtworks.go.server.websocket;

import com.thoughtworks.go.domain.JobIdentifier;
import com.thoughtworks.go.server.service.RestfulService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

import javax.websocket.EndpointConfig;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(
        value = "/client-websocket/{pipelineName}/{pipelineLabel}/{stageName}/{stageCounter}/{jobName}",
        configurator = ConsoleLogEndpointConfigurator.class
)
public class ConsoleLogEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleLogEndpoint.class);
    private Session session;

    class MissingParametersException extends Exception {
        MissingParametersException(String message) {
            super(message);
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config,
                       @PathParam(value = "pipelineName") String pipelineName,
                       @PathParam(value = "pipelineLabel") String pipelineLabel,
                       @PathParam(value = "stageName") String stageName,
                       @PathParam(value = "stageCounter") String stageCounter,
                       @PathParam(value = "jobName") String jobName) throws Exception {
        this.session = session;
        LOGGER.debug("{} connected.", sessionName());

        validateParams(pipelineName, pipelineLabel, stageName, stageCounter, jobName);

        WebApplicationContext wac = (WebApplicationContext) config.getUserProperties().get("webAppContext");
        ClientRemoteHandler handler = (ClientRemoteHandler) config.getUserProperties().get("handler");
        JobIdentifier jobIdentifier = getJobIdentifier(config, pipelineName, pipelineLabel, stageName, stageCounter, jobName);
        handler.process(this, jobIdentifier);
    }


    public void send(String msg) throws IOException {
        LOGGER.debug("{} send message: {}", sessionName(), msg);
        session.getBasicRemote().sendText(msg);
    }

    public void close() {
        String name = sessionName();
        LOGGER.debug("closing session {}", name);
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.warn("Failed to close session " + name);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        LOGGER.error("Closing session because an error was thrown", error);
        close();
    }

    private JobIdentifier getJobIdentifier(EndpointConfig config, String pipelineName, String pipelineLabel, String stageName, String stageCounter, String jobName) {
        RestfulService rest = (RestfulService) config.getUserProperties().get("restfulService");
        return rest.findJob(pipelineName, pipelineLabel, stageName, stageCounter, jobName);
    }

    private void validateParams(String... params) throws MissingParametersException {
        for (String param : params) {
            if (null == param || "".equals(param)) {
                String message = String.format("Require the following params: {pipelineName}/{pipelineLabel}/{stageName}/{stageCounter}/{jobName}, but only received %s", StringUtils.join(params, "/"));
                throw new MissingParametersException(message);
            }
        }
    }

    private String sessionName() {
        return session == null ? "[No session initialized]" : "Session[" + session.getBasicRemote().toString() + "]";
    }

    @Override
    public String toString() {
        return "[ConsoleLogEndpoint: " + sessionName() + "]";
    }
}
