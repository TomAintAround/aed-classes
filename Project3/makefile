CC = gcc
SRC_DIR = src
BUILD_DIR = build
# Todos os ficheiros C
SRC = $(wildcard $(SRC_DIR)/*.c)
# Todos os ficheiros objetos resultantes
OBJ = $(patsubst $(SRC_DIR)/%.c,$(BUILD_DIR)/%.o,$(SRC))
TARGET = $(BUILD_DIR)/project3
TARGET_DEBUG = $(BUILD_DIR)/project3-debug

BASE_FLAGS = -std=c23 -Wall -Wextra -Wfloat-equal -Wundef -Wshadow -Wpointer-arith -Wcast-align
CFLAGS = $(BASE_FLAGS) -O2

.PHONY: all debug clean 

all: $(BUILD_DIR) $(TARGET)

debug: CFLAGS := $(BASE_FLAGS) -g
debug: $(BUILD_DIR) $(TARGET_DEBUG)

clean:
	rm -rf $(BUILD_DIR)

$(BUILD_DIR):
	mkdir -p ./$(BUILD_DIR)

$(TARGET): $(OBJ)
	$(CC) $^ -o $@
	strip $@

$(TARGET_DEBUG): $(OBJ)
	$(CC) $^ -o $@

$(BUILD_DIR)/%.o: $(SRC_DIR)/%.c
	$(CC) $(CFLAGS) -c $^ -o $@
