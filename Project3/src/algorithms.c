#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
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

void mergeSort(struct Student* students, size_t start, size_t end,
			   int (*compareAlgo)(struct Student*, struct Student*)) {
	if (start < end) {
		size_t middle = start + ((end - start) / 2);
		mergeSort(students, start, middle, compareAlgo);
		mergeSort(students, middle + 1, end, compareAlgo);

		// Para evitar stack overflows, decidi criar arrays na heap
		size_t leftSize = middle - start + 1;
		size_t rightSize = end - middle;

		struct Student* leftStudents = malloc(sizeof(struct Student) * leftSize);
		struct Student* rightStudents = malloc(sizeof(struct Student) * rightSize);

		for (size_t i = 0; i < leftSize; i++)
			leftStudents[i] = students[start + i];
		for (size_t i = 0; i < rightSize; i++)
			rightStudents[i] = students[middle + 1 + i];

		size_t i = 0, j = 0;
		size_t progress = start;
		while (i < leftSize && j < rightSize) {
			if (compareAlgo(&leftStudents[i], &rightStudents[j]) <= 0)
				students[progress++] = leftStudents[i++];
			else
				students[progress++] = rightStudents[j++];
		}

		while (i < leftSize) students[progress++] = leftStudents[i++];

		while (j < rightSize) students[progress++] = rightStudents[j++];

		free(leftStudents);
		free(rightStudents);
	}
}
