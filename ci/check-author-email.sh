#!/bin/bash

echo 'Checking Author email'

git config --global --add safe.directory "*"

# Get the author's email
authorEmail=$(git log -1 --format=%ae)

# Check if the author's email ends with "@internetbrands.com"
if [[ "$authorEmail" == *@internetbrands.com ]]; then
    echo "Commit author's email ($authorEmail) ends with '@internetbrands.com'"
    exit 0
else
    echo "Commit author's email ($authorEmail) does not end with '@internetbrands.com'"
    exit 1
fi