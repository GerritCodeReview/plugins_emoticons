workspace(name = "emoticons")

load("//:bazlets.bzl", "load_bazlets")

load_bazlets(
    commit = "22b242638bd36017790d94ffcc18a57f223bda2f",
    #local_path = "/home/<user>/projects/bazlets",
)

# Snapshot Plugin API
#load(
#    "@com_googlesource_gerrit_bazlets//:gerrit_api_maven_local.bzl",
#    "gerrit_api_maven_local",
#)

# Load snapshot Plugin API
#gerrit_api_maven_local()

# Release Plugin API
load(
    "@com_googlesource_gerrit_bazlets//:gerrit_api.bzl",
    "gerrit_api",
)

# Load release Plugin API
gerrit_api()

load(
    "@com_googlesource_gerrit_bazlets//:gerrit_gwt.bzl",
    "gerrit_gwt",
)

gerrit_gwt()
