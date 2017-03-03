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

public class BuildParams {
    private String pipelineName;
    private String stageName;
    private String jobName;
    private String pipelineLabel;
    private String stageCounter;

    public BuildParams(String pipelineName, String pipelineLabel, String stageName, String stageCounter, String jobName) {
        this.pipelineName = pipelineName;
        this.pipelineLabel = pipelineLabel;
        this.stageName = stageName;
        this.stageCounter = stageCounter;
        this.jobName = jobName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setPipelineLabel(String pipelineLabel) {
        this.pipelineLabel = pipelineLabel;
    }

    public void setStageCounter(String stageCounter) {
        this.stageCounter = stageCounter;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public String getStageName() {
        return stageName;
    }

    public String getJobName() {
        return jobName;
    }

    public String getPipelineLabel() {
        return pipelineLabel;
    }

    public String getStageCounter() {
        return stageCounter;
    }

    @Override
    public String toString() {
        return "BuildParams{" +
                "pipelineName='" + pipelineName + '\'' +
                ", stageName='" + stageName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", pipelineLabel='" + pipelineLabel + '\'' +
                ", stageCounter='" + stageCounter + '\'' +
                '}';
    }

    public boolean isValid() {
        return pipelineName != null && pipelineLabel != null && stageName != null && stageCounter != null && jobName != null;
    }
}
