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

package com.googlesource.gerrit.plugins.emoticons;

import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.account.AccountResource;
import com.google.gerrit.server.config.ConfigResource;
import com.google.gerrit.server.project.ProjectCache;
import com.google.inject.Inject;
import com.google.inject.Provider;

import com.googlesource.gerrit.plugins.emoticons.GetConfig.ConfigInfo;

import org.eclipse.jgit.lib.Config;

public class GetPreference implements RestReadView<AccountResource> {
  public static final String PREFERENCE = "preference";
  public static final String KEY_SHOW_EMOTICONS = "showEmoticons";

  private final Provider<IdentifiedUser> self;
  private final ProjectCache projectCache;
  private final String pluginName;
  private final Provider<GetConfig> getConfig;

  @Inject
  GetPreference(Provider<IdentifiedUser> self,
      ProjectCache projectCache,
      @PluginName String pluginName,
      Provider<GetConfig> getConfig) {
    this.self = self;
    this.projectCache = projectCache;
    this.pluginName = pluginName;
    this.getConfig = getConfig;
  }

  @Override
  public ConfigInfo apply(AccountResource rsrc) throws AuthException {
    if (self.get() != rsrc.getUser()
        && !self.get().getCapabilities().canAdministrateServer()) {
      throw new AuthException("not allowed to get preference");
    }

    ConfigInfo globalCfg = getConfig.get().apply(new ConfigResource());
    Config db =
        projectCache.getAllProjects().getConfig(pluginName + ".config").get();
    ConfigInfo info = new ConfigInfo();
    info.showEmoticons =
        db.getBoolean(PREFERENCE, self.get().getUserName(), KEY_SHOW_EMOTICONS,
            (globalCfg.showEmoticons != null ? globalCfg.showEmoticons : true));
    if (!info.showEmoticons) {
      info.showEmoticons = null;
    }
    return info;
  }
}
