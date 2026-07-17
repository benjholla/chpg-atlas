# chpg-atlas
CHPG support for Atlas

## Building the Plugin

This repository uses a Maven Tycho headless build system to build the Eclipse plugins. The dependencies are pulled in from a Git submodule.

1. Ensure the Git submodule is initialized:
   ```bash
   git submodule update --init --recursive
   ```

2. Build the plugins using Maven:
   ```bash
   mvn clean verify
   ```

   *Note: This process will automatically build the required `pg-exporter.jar` from the submodule using Gradle and wrap it into an OSGi bundle before building `chpg.atlas`.*