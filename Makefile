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