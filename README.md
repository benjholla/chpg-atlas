# dev-chpg-pg-atlas
[![Build Plugin](https://github.com/benjholla/dev-chpg-pg-atlas/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/benjholla/dev-chpg-pg-atlas/actions/workflows/build.yml)

CHPG support for Atlas

## Installation

Once the plugin is built, you can install it into Eclipse as an update site.

### Preview Builds (from main)

To install the latest preview build generated from the `main` branch, you can use the GitHub Pages hosted update site.

1. In Eclipse, open the `Help` menu and select `Install New Software...`.
2. Click the `Add...` button near the top right of the dialog.
3. Provide a name for the repository (e.g., "CHPG Atlas Preview") and enter the following Location: `https://ben-holland.com/dev-chpg-pg-atlas/`
4. Click `Add`.
5. Select the plugin from the list, click `Next`, and follow the prompts to complete the installation.
6. Restart Eclipse when prompted.

### Local Installation

If you prefer to build and install from source locally:

1. In Eclipse, open the `Help` menu and select `Install New Software...`.
2. Click the `Add...` button near the top right of the dialog.
3. Click the `Local...` button and select the `dev.chpg.pg.atlas.site/target/repository/` directory from this repository.
4. Provide a name for the repository (e.g., "CHPG Atlas Local Update Site") and click `Add`.
5. Select the plugin from the list, click `Next`, and follow the prompts to complete the installation.
6. Restart Eclipse when prompted.

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

   *Note: This process will automatically build the required `pg-exporter.jar` from the submodule using Gradle and wrap it into an OSGi bundle before building `dev.chpg.pg.atlas`, the feature, and the update site repository.*
