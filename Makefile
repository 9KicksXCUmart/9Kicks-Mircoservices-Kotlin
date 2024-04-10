include .env
REGISTRY_ID := $(shell cat terraform.tfstate | jq '.resources[] | select(.name == "ecr_repo") | .instances[].attributes.registry_id')
REGISTRY_URL := $(shell cat terraform.tfstate | jq '.resources[] | select(.name == "ecr_repo") | .instances[].attributes.repository_url' | tr -d '"')
IMAGE_TAG := $(shell bash ./scripts/update_tag.sh)
.DEFAULT_GOAL := run

.PHONY: build clean test run

build:
	./gradlew build

clean:
	./gradlew clean

test:
	./gradlew test

run: build
	./gradlew bootRun

docker-build:
	docker build -t spring-boot-docker .

docker-run:
	docker run -d -p 8080:8080 --rm spring-boot-docker

docker-stop:
	docker stop $$(docker ps -q --filter ancestor=spring-boot-docker)

tf-plan:
	TF_VAR_repository_name=${ECR_REPO_NAME} terraform plan -out=tfplan

tf-apply:
	TF_VAR_repository_name=${ECR_REPO_NAME} terraform apply -auto-approve
	
1push-image:
	aws ecr get-login-password --region ${AWS_REGION_NAME} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION_NAME}.amazonaws.com
	docker build -t ${REGISTRY}/${ECR_REPO_NAME}:${IMAGE_TAG} . --platform=linux/amd64 
	docker push ${REGISTRY}/${ECR_REPO_NAME}:${IMAGE_TAG}

push-image:
	aws ecr get-login-password --region ${AWS_REGION_NAME} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION_NAME}.amazonaws.com
	docker build -t ${ECR_REPO_NAME}:${IMAGE_TAG} . --platform=linux/amd64 
	docker tag ${ECR_REPO_NAME}:${IMAGE_TAG} ${REGISTRY_URL}:${IMAGE_TAG} 
	docker push ${REGISTRY_URL}:${IMAGE_TAG}
