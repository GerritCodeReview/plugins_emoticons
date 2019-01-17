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

Gerrit.install(function(self) {

    function get(url, callback) {
      if (window.Polymer)
        self.restApi().get(url).then(callback)
      else
        Gerrit.get(url, callback)
    }

    function onComment(e) {
      var prefs = getPrefsFromCookie();
      if (prefs !== null) {
        insertEmoticons(e, prefs)
      } else {
        get('/accounts/self/' + self.getPluginName()
            + '~preference', function(prefs) {
          storePrefsInCookie(prefs);
          insertEmoticons(e, prefs)
        });
      }
    }

    function storePrefsInCookie(prefs) {
      var date = new Date();
      date.setTime(date.getTime() + (1 * 24 * 60 * 60 * 1000)); // 1 day
      document.cookie = getCookieName()
          + "="
          + JSON.stringify(prefs)
          + "; expires=" + date.toGMTString()
          + "; path=/";
    }

    function getPrefsFromCookie() {
      var cookie = document.cookie;
      if (cookie.length > 0) {
        var cookieName = getCookieName();
        var start = cookie.indexOf(cookieName + "=");
        if (start != -1) {
            start = start + cookieName.length + 1;
            var end = document.cookie.indexOf(";", start);
            if (end == -1) {
                end = document.cookie.length;
            }
            return JSON.parse(unescape(document.cookie.substring(start, end)));
        }
      }
      return null;
    }

    function getCookieName() {
      return self.getPluginName() + "~prefs";
    }

    function insertEmoticons(e, prefs) {
      if (!prefs.show_emoticons) {
        return;
      }

      e.innerHTML = replaceEmoticons(e.innerHTML);
    }

    function replaceEmoticons(text) {
      var emoticons = {
        '&gt;:)' : 'emoticon_evilgrin.png',
        '&gt;:-)': 'emoticon_evilgrin.png',
        ':-D' : 'emoticon_grin.png',
        ':D'  : 'emoticon_grin.png',
        '^o^' : 'emoticon_happy.png',
        ':-)' : 'emoticon_smile.png',
        ':)'  : 'emoticon_smile.png',
        ':-o' : 'emoticon_surprised.png',
        ':o'  : 'emoticon_surprised.png',
        ':-p' : 'emoticon_tongue.png',
        ':p'  : 'emoticon_tongue.png',
        ':-(' : 'emoticon_unhappy.png',
        ':('  : 'emoticon_unhappy.png',
        ':-3' : 'emoticon_waii.png',
        ':3'  : 'emoticon_waii.png',
        ';-D' : 'emoticon_wink.png',
        ';D'  : 'emoticon_wink.png',
        ';-)' : 'emoticon_wink.png',
        ';)'  : 'emoticon_wink.png'
      };
      var url = "plugins/" + self.getPluginName() + "/static/silk_icons/";
      return text.replace(/[&gt:;\^\-)Dop(3]+/g, function (match) {
        return typeof emoticons[match] != 'undefined'
           ? '<img src="' + url + emoticons[match]
               + '" style="position: relative; top: 3px;"'
               + ' alt="' + match + '"/>'
           : match;
      });
    }

    self.on('comment', onComment);
  });
