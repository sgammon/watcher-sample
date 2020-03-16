
APP ?= //:app
IBAZEL ?= $(shell which ibazel)
BAZELISK ?= $(shell which bazelisk)


all: build watch


build:
	@echo "Building sample..."
	$(BAZELISK) build $(APP)

run:
	@echo "Running sample app..."
	$(BAZELISK) run $(APP) --

watch:
	@echo "Starting watcher..."
	$(IBAZEL) run $(APP) --define=LIVE=1 --


.PHONY: all build run watch

