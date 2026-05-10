#ifndef ALGORITHMS_H
#define ALGORITHMS_H

#include <stddef.h>
#include "common.h"
#include "compare.h"

typedef void (*SortAlgo)(struct Student*, size_t, size_t, Comparator);

void bubbleSort(struct Student* students, size_t start, size_t end, Comparator compareAlgo);
void mergeSort(struct Student* students, size_t start, size_t end, Comparator compareAlgo);
void heapSort(struct Student* students, size_t start, size_t end, Comparator compareAlgo);

#endif
