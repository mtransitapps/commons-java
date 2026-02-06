# GitHub Actions Workflows

This directory contains GitHub Actions workflows for the commons-java repository.

## Workflows

### `java-to-kotlin-weekly.yml` - Convert Java to Kotlin Weekly

**Schedule:** Every Wednesday at 0:09 GMT

**Purpose:** Automatically converts one Java class to Kotlin every week to gradually migrate the codebase.

**How it works:**
1. The workflow runs on a weekly schedule (Wednesday at 0:09 GMT)
2. It scans the repository for Java files (excluding build directories)
3. Selects one Java file based on the current week number (rotates through all files)
4. Creates a GitHub issue with @copilot tagged to request the conversion
5. GitHub Copilot in Issues will then convert the file and create a pull request

**Manual Triggering:** 
You can manually trigger this workflow from the Actions tab in GitHub:
1. Go to Actions → "Convert Java to Kotlin Weekly"
2. Click "Run workflow"
3. Select the branch and click "Run workflow"

**Labels:** The created issues are tagged with:
- `automation` - Indicates this is an automated task
- `kotlin` - Related to Kotlin migration
- `refactoring` - Code refactoring task

**Algorithm:** The workflow uses a deterministic algorithm to select files:
- Files are sorted alphabetically
- The selection rotates based on week number: `(week_number - 1) % total_files + 1`
- This ensures all files will eventually be converted and prevents duplicates

**Requirements:** The conversion process maintains:
- All functionality and logic from Java
- Kotlin idioms and best practices
- Same package structure
- Proper null safety annotations
- Compatibility with existing tests

---

### `mt-build.yml` - MT Build

Runs the main build workflow for MonTransit projects.

### `mt-trigger-main-repo-build.yml` - MT Trigger Main Repo Build

Triggers the build of the main MonTransit repository when changes are pushed to master.
