include_defs('//bucklets/gerrit_plugin.bucklet')

MODULE = 'com.googlesource.gerrit.plugins.emoticons.Emoticons'

gerrit_plugin(
  name = 'emoticons',
  srcs = glob(['src/main/java/**/*.java']),
  resources = glob(['src/main/**/*']),
  gwt_module = MODULE,
  manifest_entries = [
    'Gerrit-PluginName: emoticons',
    'Gerrit-Module: com.googlesource.gerrit.plugins.emoticons.Module',
    'Gerrit-HttpModule: com.googlesource.gerrit.plugins.emoticons.HttpModule',
  ]
)

java_library(
  name = 'classpath',
  deps = GERRIT_GWT_API + GERRIT_PLUGIN_API + [
    ':emoticons__plugin',
    '//lib/gwt:user',
  ],
)
