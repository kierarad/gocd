/*
 * Copyright 2019 ThoughtWorks, Inc.
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

import * as m from "mithril";
import {GitMaterialAttributes, Material} from "models/materials/types";
import {PipelineConfig} from "models/pipeline_configs/pipeline_config";
import {Stage} from "models/pipeline_configs/stage";
import * as s from "underscore.string";
import {Page, PageState} from "views/pages/page";
import {PipelineActions} from "views/pages/pipelines/actions";
import {AdvancedSettings} from "views/pages/pipelines/advanced_settings";
import {ConceptDiagram} from "views/pages/pipelines/concept_diagram";
import {FillableSection} from "views/pages/pipelines/fillable_section";
import {MaterialEditor} from "views/pages/pipelines/material_editor";
import {PipelineInfoEditor} from "views/pages/pipelines/pipeline_info_editor";
import {StageEditor} from "views/pages/pipelines/stage_editor";
import {UserInputPane} from "views/pages/pipelines/user_input_pane";

const materialImg = require("../../../app/assets/images/concept_diagrams/concept_material.svg");
const pipelineImg = require("../../../app/assets/images/concept_diagrams/concept_pipeline.svg");
const stageImg    = require("../../../app/assets/images/concept_diagrams/concept_stage.svg");
const jobImg      = require("../../../app/assets/images/concept_diagrams/concept_job.svg");

export class PipelineCreatePage extends Page {
  private material: Material = new Material("git", new GitMaterialAttributes());
  private model: PipelineConfig = new PipelineConfig("", [this.material], [new Stage()]);

  pageName(): string {
    return "Add a New Pipeline";
  }

  oninit(vnode: m.Vnode) {
    this.pageState = PageState.OK;
    const group = m.parseQueryString(window.location.search).group;
    if (!s.isBlank(group)) {
      this.model.group(group);
    }
  }

  componentToDisplay(vnode: m.Vnode): m.Children {
    return [
      <FillableSection sectionId="material">
        <UserInputPane heading="Part 1: Material">
          <MaterialEditor material={this.material}/>
        </UserInputPane>
        <ConceptDiagram image={materialImg}>
          A <strong>material</strong> triggers your pipeline to run. Typically this is a
          <strong>source repository</strong> or an <strong>upstream pipeline</strong>.
        </ConceptDiagram>
      </FillableSection>,

      <FillableSection sectionId="pipeline">
        <UserInputPane heading="Part 2: Pipeline Name">
          <PipelineInfoEditor pipelineConfig={this.model}/>
        </UserInputPane>
        <ConceptDiagram image={pipelineImg}>
          In GoCD, a <strong>pipeline</strong> is a representation of a <strong>workflow</strong>.
          Pipelines consist of one or more <strong>stages</strong>.
        </ConceptDiagram>
      </FillableSection>,

      <FillableSection sectionId="stage">
        <UserInputPane heading="Part 3: Stage Details">
          <StageEditor stage={this.model.stages()[0]} />
        </UserInputPane>
        <ConceptDiagram image={stageImg}>
          A <strong>stage</strong> is a group of jobs, and a <strong>job</strong> is a
          piece of work to execute.
        </ConceptDiagram>
      </FillableSection>,

      <FillableSection sectionId="job">
        <UserInputPane heading="Part 4: Job and Tasks">
          <p>Form fields go here</p>
          <AdvancedSettings>
            More to come...
          </AdvancedSettings>
        </UserInputPane>
        <ConceptDiagram image={jobImg}>
          A <strong>job</strong> is like a script, where each sequential step is called
          a <strong>task</strong>. Typically, a task is a single command.
        </ConceptDiagram>
      </FillableSection>,

      <PipelineActions pipelineConfig={this.model}/>

    ];
  }

  fetchData(vnode: m.Vnode): Promise<any> {
    return new Promise(() => null);
  }
}
