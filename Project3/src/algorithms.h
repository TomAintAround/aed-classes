#ifndef ALGORITHMS_H
#define ALGORITHMS_H

#include <stddef.h>
#include "common.h"

void bubbleSort(struct Student* students, size_t num,
				int(compareAlgo)(struct Student*, struct Student*));

#endif
