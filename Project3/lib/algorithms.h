#ifndef ALGORITHMS_H
#define ALGORITHMS_H

#include <stddef.h>
#include "common.h"

void bubbleSort(struct Student* students, size_t start, size_t end,
				int(compareAlgo)(struct Student*, struct Student*));
void mergeSort(struct Student* students, size_t start, size_t end,
			   int(compareAlgo)(struct Student*, struct Student*));
void heapSort(struct Student* students, size_t start, size_t end,
			  int(compareAlgo)(struct Student*, struct Student*));

#endif
