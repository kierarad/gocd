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

package com.thoughtworks.go.websocket;

import com.google.gson.annotations.Expose;
import com.thoughtworks.go.domain.JobIdentifier;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class ConsoleTransmission implements Serializable {
    @Expose
    private JobIdentifier jobIdentifier;
    @Expose
    private String line;

    public ConsoleTransmission(String line, JobIdentifier jobIdentifier) {
        this.line = line;
        this.jobIdentifier = jobIdentifier;
    }

    public InputStream getLine() throws IOException {
        return IOUtils.toInputStream(line + "\n", "UTF-8");
    }

    public JobIdentifier getJobIdentifier() {
        return jobIdentifier;
    }
}
