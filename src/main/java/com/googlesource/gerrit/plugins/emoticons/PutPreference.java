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

import static com.google.gerrit.server.permissions.GlobalPermission.ADMINISTRATE_SERVER;
import static com.googlesource.gerrit.plugins.emoticons.GetPreference.KEY_SHOW_EMOTICONS;
import static com.googlesource.gerrit.plugins.emoticons.GetPreference.PREFERENCE;

import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.extensions.restapi.AuthException;
import com.google.gerrit.extensions.restapi.Response;
import com.google.gerrit.extensions.restapi.RestModifyView;
import com.google.gerrit.extensions.restapi.UnprocessableEntityException;
import com.google.gerrit.server.IdentifiedUser;
import com.google.gerrit.server.account.AccountResource;
import com.google.gerrit.server.git.MetaDataUpdate;
import com.google.gerrit.server.git.ProjectLevelConfig;
import com.google.gerrit.server.permissions.PermissionBackend;
import com.google.gerrit.server.permissions.PermissionBackendException;
import com.google.gerrit.server.project.ProjectCache;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.googlesource.gerrit.plugins.emoticons.PutConfig.Input;
import java.io.IOException;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Config;

public class PutPreference implements RestModifyView<AccountResource, Input> {
  private final Provider<IdentifiedUser> self;
  private final ProjectCache projectCache;
  private final MetaDataUpdate.User metaDataUpdateFactory;
  private final String pluginName;
  private final PermissionBackend permissionBackend;

  @Inject
  PutPreference(
      Provider<IdentifiedUser> self,
      ProjectCache projectCache,
      MetaDataUpdate.User metaDataUpdateFactory,
      @PluginName String pluginName,
      PermissionBackend permissionBackend) {
    this.self = self;
    this.projectCache = projectCache;
    this.metaDataUpdateFactory = metaDataUpdateFactory;
    this.pluginName = pluginName;
    this.permissionBackend = permissionBackend;
  }

  @Override
  public Response<String> apply(AccountResource rsrc, Input input)
      throws AuthException, RepositoryNotFoundException, IOException, UnprocessableEntityException,
          PermissionBackendException {
    if (self.get() != rsrc.getUser()) {
      permissionBackend.user(self).check(ADMINISTRATE_SERVER);
    }
    if (input == null) {
      input = new Input();
    }

    String username = self.get().getUserName();

    ProjectLevelConfig storage = projectCache.getAllProjects().getConfig(pluginName + ".config");
    Config db = storage.get();
    boolean modified = false;

    boolean showEmoticons = db.getBoolean(PREFERENCE, username, KEY_SHOW_EMOTICONS, true);
    if (input.showEmoticons != null) {
      if (input.showEmoticons != showEmoticons) {
        db.setBoolean(PREFERENCE, username, KEY_SHOW_EMOTICONS, input.showEmoticons);
        modified = true;
      }
    } else {
      if (!showEmoticons) {
        db.unset(PREFERENCE, username, KEY_SHOW_EMOTICONS);
        modified = true;
      }
    }

    if (modified) {
      MetaDataUpdate md =
          metaDataUpdateFactory.create(projectCache.getAllProjects().getProject().getNameKey());
      md.setMessage("Update " + pluginName + " Preferences for '" + username + "'\n");
      storage.commit(md);
    }

    return Response.<String>ok("OK");
  }
}
