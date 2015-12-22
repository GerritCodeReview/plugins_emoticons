// Copyright (C) 2015 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.emoticons.client;

import com.google.gerrit.plugin.client.rpc.RestApi;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class EmoticonsConfigScreen extends VerticalPanel {
  private final RestApi restApi;

  private CheckBox showEmoticonsBox;
  private Button saveButton;

  protected EmoticonsConfigScreen(RestApi restApi) {
    this.restApi = restApi;
    setStyleName("emoticons-config-screen");
    restApi.get(new AsyncCallback<ConfigInfo>() {
        @Override
        public void onSuccess(ConfigInfo info) {
          display(info);
        }

        @Override
        public void onFailure(Throwable caught) {
          // never invoked
        }
      });
  }

  protected void display(ConfigInfo info) {
    HorizontalPanel p = new HorizontalPanel();
    p.setStyleName("emoticons-label-panel");
    showEmoticonsBox = new CheckBox("Show emoticons as images");
    showEmoticonsBox.setValue(info.showEmoticons());
    p.add(showEmoticonsBox);
    Image stageInfo = new Image(EmoticonsPlugin.RESOURCES.information());
    stageInfo.setTitle("Emoticons in comments are replaced by images.");
    p.add(stageInfo);
    add(p);

    HorizontalPanel buttons = new HorizontalPanel();
    add(buttons);

    saveButton = new Button("Save");
    saveButton.setStyleName("emoticons-save-button");
    saveButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(final ClickEvent event) {
        doSave();
      }
    });
    buttons.add(saveButton);
    saveButton.setEnabled(false);
    new OnEditEnabler(saveButton, showEmoticonsBox);

    showEmoticonsBox.setFocus(true);
    saveButton.setEnabled(false);
  }

  private void doSave() {
    ConfigInfo in = ConfigInfo.create();
    in.setShowEmoticons(showEmoticonsBox.getValue());
    restApi.put(in, new AsyncCallback<JavaScriptObject>() {
        @Override
        public void onSuccess(JavaScriptObject result) {
          saveButton.setEnabled(false);
          onSave();
        }

        @Override
        public void onFailure(Throwable caught) {
          // never invoked
        }
      });
  }

  protected void onSave() {
  }
}
