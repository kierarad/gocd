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
import com.thoughtworks.go.domain.JobInstance;
import com.thoughtworks.go.domain.JobState;
import com.thoughtworks.go.plugin.api.task.Console;
import com.thoughtworks.go.server.service.ConsoleService;
import com.thoughtworks.go.server.service.JobDetailService;
import com.thoughtworks.go.server.service.RestfulService;
import com.thoughtworks.go.util.command.ConsoleResult;
import org.h2.expression.ConditionIn;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class ClientRemoteHandlerTest {
    ClientRemoteHandler clientRemoteHandler;
    private RestfulService restfulService;
    private ConsoleService consoleService;
    private JobDetailService jobDetailService;
    private ClientRemoteSocket clientRemoteSocket;
    private BuildParams buildParams;
    private JobIdentifier jobIdentifier;
    private ConsoleOut consoleOut;
    private JobInstance jobInstance;

    @Before
    public void setUp() throws Exception {
        buildParams = new BuildParams("pipelineName", "pipelineLabel", "stageName", "stageCounter", "jobName");
        restfulService = mock(RestfulService.class);
        consoleService = mock(ConsoleService.class);
        jobDetailService = mock(JobDetailService.class);
        clientRemoteSocket = mock(ClientRemoteSocket.class);
        clientRemoteHandler = new ClientRemoteHandler(restfulService, consoleService, jobDetailService);
        consoleOut = mock(ConsoleOut.class);
        jobIdentifier = mock(JobIdentifier.class);
        jobInstance = mock(JobInstance.class);
        when(restfulService.findJob(buildParams.getPipelineName(), buildParams.getPipelineLabel(),
                buildParams.getStageName(), buildParams.getStageCounter(),
                buildParams.getJobName())).thenReturn(jobIdentifier);
        when(jobDetailService.findMostRecentBuild(jobIdentifier)).thenReturn(jobInstance);
        when(consoleService.getConsoleOut(eq(jobIdentifier), anyInt())).thenReturn(consoleOut);
    }

    @Test
    public void shouldSendConsoleLog() throws Exception {
        when(jobInstance.isCompleted()).thenReturn(true);
        when(consoleOut.output()).thenReturn("Expected output for this test");

        clientRemoteHandler.process(clientRemoteSocket, buildParams);

        verify(clientRemoteSocket).send("Expected output for this test");
    }

    @Test
    public void shouldSendConsoleLogInMultipleMessagesIfBuildInProgress() throws Exception {
        when(jobInstance.isCompleted()).thenReturn(false).thenReturn(true);
        when(consoleOut.output()).thenReturn("First Output").thenReturn("Second Output");;
        when(consoleOut.calculateNextStart()).thenReturn(1);

        clientRemoteHandler.process(clientRemoteSocket, buildParams);

        verify(consoleService, times(1)).getConsoleOut(jobIdentifier, 0);
        verify(consoleService, times(1)).getConsoleOut(jobIdentifier, 1);
        verify(clientRemoteSocket, times(1)).send("First Output");
        verify(clientRemoteSocket, times(1)).send("Second Output");
    }

    @Test
    public void shouldNotSendMessagesWhenOutputHasNotAdvanced() throws Exception {
        when(jobInstance.isCompleted()).thenReturn(false).thenReturn(true);
        when(consoleOut.calculateNextStart()).thenReturn(0).thenReturn(0);

        clientRemoteHandler.process(clientRemoteSocket, buildParams);

        verify(clientRemoteSocket, times(1)).send(anyString());
    }

    @Test
    public void shouldCloseSocketAfterProcessingMessage() throws Exception {
        when(jobInstance.isCompleted()).thenReturn(true);
        clientRemoteHandler.process(clientRemoteSocket, buildParams);

        verify(clientRemoteSocket).close();
    }

    @Test
    public void shouldHandleAnInvalidPipelineName() throws Exception {
        buildParams.setPipelineName(null);
        clientRemoteHandler.process(clientRemoteSocket, buildParams);
        verify(clientRemoteSocket).send("Build not found. Console log unavailable.");
    }

    @Test
    public void shouldHandleAnInvalidPipelineLabel() throws Exception {
        buildParams.setPipelineLabel(null);
        clientRemoteHandler.process(clientRemoteSocket, buildParams);
        verify(clientRemoteSocket).send("Build not found. Console log unavailable.");
    }

    @Test
    public void shouldHandleAnInvalidStageName() throws Exception {
        buildParams.setStageName(null);
        clientRemoteHandler.process(clientRemoteSocket, buildParams);
        verify(clientRemoteSocket).send("Build not found. Console log unavailable.");
    }

    @Test
    public void shouldHandleAnInvalidJobName() throws Exception {
        buildParams.setJobName(null);
        clientRemoteHandler.process(clientRemoteSocket, buildParams);
        verify(clientRemoteSocket).send("Build not found. Console log unavailable.");
    }

    @Test
    public void shouldHandleAnInvalidStageCounter() throws Exception {
        buildParams.setStageCounter(null);
        clientRemoteHandler.process(clientRemoteSocket, buildParams);
        verify(clientRemoteSocket).send("Build not found. Console log unavailable.");
    }
}