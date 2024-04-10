#!/bin/bash
source .env
# Fetch the latest tag from ECR
latest_tag=$(aws ecr list-images --repository-name ${ECR_REPO_NAME} | \
             jq -r '.imageIds | map(.imageTag) | sort | .[-1]')
latest_tag="${latest_tag#v}"

# Set a default value if latest_tag is null
if [ "$latest_tag" == "null" ]; then
    latest_tag="v1.0.0"
    echo ::set-output name=lastest_tag::$latest_tag
    exit 0
fi

# Extract major, minor, and patch version numbers
IFS='.' read -r major minor patch <<< "$latest_tag"

# Increment the version and update the tag
if [ "$patch" -eq 9 ]; then
    if [ "$minor" -eq 9 ]; then
        new_major=$((major + 1))
        new_minor=0
        new_patch=0
    else
        new_major=$major
        new_minor=$((minor + 1))
        new_patch=0
    fi
else
    new_major=$major
    new_minor=$minor
    new_patch=$((patch + 1))
fi

latest_tag="v${new_major}.${new_minor}.${new_patch}"
echo ::set-output name=lastest_tag::$latest_tag
