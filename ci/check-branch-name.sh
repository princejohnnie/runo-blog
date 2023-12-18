#!/bin/bash

echo 'Checking MR Branch Name'

echo "BRANCH_NAME - $CHANGE_BRANCH"

# Declare a list of conventional commit types
conventions=("feat" "fix" "chore" "build" "ci" "docs" "style" "refactor" "perf" "test")

# Check if the branch name starts with any of the conventions
for convention in "${conventions[@]}"; do
    if [[ $CHANGE_BRANCH == "${convention}"* ]]; then
        echo "Branch '$CHANGE_BRANCH' starts with one of the conventional commit types."
        exit 0
    fi
done

echo "Error: Branch '$CHANGE_BRANCH' does not start with any of the conventional commit types."
exit 1