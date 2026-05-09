#include <stddef.h>
#include <stdio.h>
#include "../lib/algorithms.h"
#include "../lib/common.h"

void bubbleSort(struct Student* students, size_t num,
				int (*compareAlgo)(struct Student*, struct Student*)) {
	size_t lastElem = num - 1;
	while (lastElem > 0) {
		for (size_t i = 0; i <= lastElem - 1; i++) {
			if (compareAlgo(&students[i], &students[i + 1]) > 0) {
				struct Student tmp = students[i];
				students[i] = students[i + 1];
				students[i + 1] = tmp;
			}
		}
		lastElem--;
	}
}
