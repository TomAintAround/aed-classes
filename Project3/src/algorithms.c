#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include "../lib/algorithms.h"
#include "../lib/common.h"

void bubbleSort(struct Student* students, size_t start, size_t end,
				int (*compareAlgo)(struct Student*, struct Student*)) {
	while (end > 0) {
		bool swapped = false;
		for (size_t i = start; i <= end - 1; i++) {
			swapped = false;

			if (compareAlgo(&students[i], &students[i + 1]) > 0) {
				struct Student tmp = students[i];
				students[i] = students[i + 1];
				students[i + 1] = tmp;
				swapped = true;
			}
		}
		if (!swapped) return;
		end--;
	}
}

void mergeSort(struct Student* students, size_t start, size_t end,
			   int (*compareAlgo)(struct Student*, struct Student*)) {
	if (start < end) {
		size_t middle = start + ((end - start) / 2);
		mergeSort(students, start, middle, compareAlgo);
		mergeSort(students, middle + 1, end, compareAlgo);

		// Para evitar stack overflows, decidi criar arrays na heap e não na stack
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

static void heapify(struct Student* students, size_t start, size_t end, size_t root,
					int (*compareAlgo)(struct Student*, struct Student*)) {
	while (true) {
		size_t largest = root;
		size_t left = (2 * (root - start)) + 1 + start;
		size_t right = (2 * (root - start)) + 2 + start;

		if (left <= end && compareAlgo(&students[left], &students[largest]) > 0)
			largest = left;

		if (right <= end && compareAlgo(&students[right], &students[largest]) > 0)
			largest = right;

		if (largest == root) break;

		struct Student temp = students[root];
		students[root] = students[largest];
		students[largest] = temp;
		root = largest;
	}
}

void heapSort(struct Student* students, size_t start, size_t end,
			  int (*compareAlgo)(struct Student*, struct Student*)) {
	size_t size = end + 1 - start;
	for (long i = (long)((size / 2) - 1 + start); i >= (long)start; i--)
		heapify(students, start, end, i, compareAlgo);

	for (size_t i = end; i > start; i--) {
		struct Student tmp = students[start];
		students[start] = students[i];
		students[i] = tmp;
		heapify(students, start, i - 1, start, compareAlgo);
	}
}
