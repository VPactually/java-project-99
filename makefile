build:
	cd app/ && ./gradlew build

report:
	cd app/ && ./gradlew jacocoTestReport

.PHONY: build