load("//tools/bzl:plugin.bzl", "gerrit_plugin")

gerrit_plugin(
    name = "emoticons",
    srcs = glob(["src/main/java/**/*.java"]),
    gwt_module = "com.googlesource.gerrit.plugins.emoticons.Emoticons",
    manifest_entries = [
        "Gerrit-PluginName: emoticons",
        "Gerrit-Module: com.googlesource.gerrit.plugins.emoticons.Module",
        "Gerrit-HttpModule: com.googlesource.gerrit.plugins.emoticons.HttpModule",
    ],
    resources = glob(
        ["src/main/**/*"],
        exclude = ["**/*.java"],
    ),
)
