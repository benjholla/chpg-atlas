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

## Installation

Once the plugin is built, you can install it into Eclipse as a local update site.

1. In Eclipse, open the `Help` menu and select `Install New Software...`.
2. Click the `Add...` button near the top right of the dialog.
3. Click the `Local...` button and select the `chpg.atlas/target/` directory from this repository.
4. Provide a name for the repository (e.g., "CHPG Atlas Local Update Site") and click `Add`.
5. Select the plugin from the list, click `Next`, and follow the prompts to complete the installation.
6. Restart Eclipse when prompted.