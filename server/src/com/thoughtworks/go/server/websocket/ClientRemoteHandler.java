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

import com.thoughtworks.go.domain.ConsoleOut;
import com.thoughtworks.go.domain.JobIdentifier;
import com.thoughtworks.go.server.service.ConsoleService;
import com.thoughtworks.go.server.service.RestfulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientRemoteHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRemoteHandler.class);


    @Autowired
    private RestfulService restfulService;
    private ConsoleService consoleService;

    @Autowired
    public ClientRemoteHandler(RestfulService restfulService, ConsoleService consoleService) {
        this.restfulService = restfulService;
        this.consoleService = consoleService;
    }

    public String process(BuildParams params) throws Exception {
        JobIdentifier identifier = restfulService.findJob(params.getPipelineName(), params.getPipelineLabel(), params.getStageName(), params.getStageCounter(),
                params.getJobName());
        int startLine = 0;
        ConsoleOut consoleOut = consoleService.getConsoleOut(identifier, startLine);
        startLine = consoleOut.calculateNextStart();
        return consoleOut.output();
    }
}
