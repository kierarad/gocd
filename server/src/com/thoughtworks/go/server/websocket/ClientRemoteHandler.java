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

import com.thoughtworks.go.domain.AgentInstance;
import com.thoughtworks.go.domain.JobIdentifier;
import com.thoughtworks.go.domain.JobInstance;
import com.thoughtworks.go.remote.AgentInstruction;
import com.thoughtworks.go.remote.BuildRepositoryRemote;
import com.thoughtworks.go.server.service.AgentRuntimeInfo;
import com.thoughtworks.go.server.service.AgentService;
import com.thoughtworks.go.server.service.ConsoleService;
import com.thoughtworks.go.server.service.JobInstanceService;
import com.thoughtworks.go.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClientRemoteHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRemoteHandler.class);

    @Autowired
    private JobInstanceService jobInstanceService;
    private ConsoleService consoleService;

    @Autowired
    public ClientRemoteHandler(JobInstanceService jobInstanceService, ConsoleService consoleService) {
        this.jobInstanceService = jobInstanceService;
        this.consoleService = consoleService;
    }

    public String process(BuildParams params) throws Exception {
        return params.toString();
    }
}
