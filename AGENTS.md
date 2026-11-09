# AGENTS.md

## Project Context

This repository contains the JoshHack Java project used for COSC 442 Software Quality Assurance and Testing.

The project uses:

- Java 17
- Maven / Maven Wrapper
- JUnit 5
- PIT mutation testing
- SpotBugs static analysis
- GitHub Actions for continuous integration

Production code is located under `src/main/java`.

Test code is located under `src/test/java`.

## General Expectations

When making changes:

- Preserve existing required behavior unless the requested task explicitly changes that behavior.
- Keep changes focused on the requested task.
- Avoid modifying unrelated files or performing unnecessary refactoring.
- Prefer small, reviewable changes over large rewrites.

## Testing and Quality

Before considering work complete:

- Run the relevant JUnit tests.
- Run the complete regression test suite when appropriate.
- Use coverage, PIT, or SpotBugs when they are relevant to the requested quality improvement.
- Do not remove, disable, weaken, or bypass tests simply to make the build pass.
- Do not reduce test assertions or quality checks merely to obtain better reported results.

## Production-Code Changes

- Test-code changes may be made when they are within the requested task.
- Do not modify production code unless the user explicitly approves the production-code change.
- If a production-code change appears necessary, explain why and request approval before making it.

## Verification

When finished:

- Summarize the files changed.
- Summarize the tests or quality tools that were run.
- Report whether those checks passed.
- Identify any remaining concerns or work requiring human judgment.

## Repository Actions

- Do not push commits automatically.
- Do not merge branches or pull requests automatically.
- Do not modify GitHub Actions, Maven configuration, or other project infrastructure unless the requested task requires it.
- Leave final review, commit, push, and merge decisions to the human developer.