$ErrorActionPreference = 'Stop'

# Helper function to check exit code of external commands
function Invoke-CommandOrThrow {
    param (
        [scriptblock]$Command
    )
    & $Command
    if ($LASTEXITCODE -ne 0) {
        throw "Command failed with exit code $LASTEXITCODE"
    }
}

# Change to the project root directory
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location -Path "$ScriptDir\.."

Write-Host "Updating submodules..."
Invoke-CommandOrThrow { git submodule update --init --recursive }

Write-Host "Building with Maven..."
Invoke-CommandOrThrow { mvn clean verify }

# Retrieve the current commit SHA and origin remote URL
$CURRENT_COMMIT = (git rev-parse HEAD).Trim()
$REMOTE_URL = (git remote get-url origin).Trim()

Write-Host "Deploying to preview branch..."
# Navigate to the built update site directory
Set-Location -Path "dev.chpg.pg.atlas.site/target/repository"

# Initialize a new git repository
Invoke-CommandOrThrow { git init }

# Change branch to preview
Invoke-CommandOrThrow { git checkout -b preview }

# Add all files and commit
Invoke-CommandOrThrow { git add . }
Invoke-CommandOrThrow { git commit -m "Preview update site of $CURRENT_COMMIT" }

# Force push to the preview branch of the remote repository
Invoke-CommandOrThrow { git push --force "$REMOTE_URL" preview }

Write-Host "Deployment complete!"
