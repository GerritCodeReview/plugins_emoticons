Build
=====

This plugin can be built with Buck.


Two build modes are supported: Standalone and in Gerrit tree.
The standalone build mode is recommended, as this mode doesn't require
the Gerrit tree to exist locally.


Build standalone
----------------

Clone bucklets library:

```
  git clone https://gerrit.googlesource.com/bucklets

```
and link it to reviewers plugin directory:

```
  cd reviewers && ln -s ../bucklets .
```

Add link to the .buckversion file:

```
  cd reviewers && ln -s bucklets/buckversion .buckversion
```

To build the plugin, issue the following command:


```
  buck build plugin
```

The output is created in

```
  buck-out/gen/imagare.jar
```

Build in Gerrit tree
--------------------

Clone or link this plugin to the plugins directory of Gerrit tree
and issue the command:

```
  buck build plugins/imagare
```

The output is created in

```
  buck-out/gen/plugins/imagare/imagare.jar
```

This project can be imported into the Eclipse IDE:

```
  ./tools/eclipse/project.py
```


