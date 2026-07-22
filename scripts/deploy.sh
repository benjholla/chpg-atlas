#!/bin/bash
set -euo pipefail

# Change to the project root directory (one level up from scripts directory)
cd "$(dirname "$0")/.."

echo "Updating submodules..."
git submodule update --init --recursive

echo "Building with Maven..."
mvn clean verify

# Retrieve the current commit SHA and origin remote URL
CURRENT_COMMIT=$(git rev-parse HEAD)
REMOTE_URL=$(git remote get-url origin)

echo "Deploying to preview branch..."
# Navigate to the built update site directory
cd dev.chpg.pg.atlas.site/target/repository

# Initialize a new git repository
git init

# Change branch to preview
git checkout -b preview

# Add all files and commit
git add .
git commit -m "Preview update site of $CURRENT_COMMIT"

# Force push to the preview branch of the remote repository
git push --force "$REMOTE_URL" preview

echo "Deployment complete!"
