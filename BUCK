include_defs('//bucklets/gerrit_plugin.bucklet')

MODULE = 'com.googlesource.gerrit.plugins.emoticons.Emoticons'

if STANDALONE_MODE:
  SILK_ICONS = '//lib/silk-icons:silk_icons_lib'
else:
  SILK_ICONS = '//plugins/emoticons/lib/silk-icons:silk_icons_lib'

gerrit_plugin(
  name = 'emoticons',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/**/*']),
  gwt_module = MODULE,
  manifest_entries = [
    'Gerrit-PluginName: emoticons',
    'Gerrit-Module: com.googlesource.gerrit.plugins.emoticons.Module',
    'Gerrit-HttpModule: com.googlesource.gerrit.plugins.emoticons.HttpModule',
  ],
  deps = [
    SILK_ICONS,
  ],
)

java_library(
  name = 'classpath',
  deps = GERRIT_GWT_API + GERRIT_PLUGIN_API + [
    ':emoticons__plugin',
    '//lib/gwt:user',
  ],
)
