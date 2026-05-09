#include "../lib/compare.h"

int compare1(struct Student* first, struct Student* second) {
	if (first->id < second->id) return -1;
	if (first->id > second->id) return 1;
	if (first->courseType < second->courseType) return -1;
	if (first->courseType > second->courseType) return 1;
	return 0;
}

int compare2(struct Student* first, struct Student* second) {
	if (first->courseType < second->courseType) return -1;
	if (first->courseType > second->courseType) return 1;
	return 0;
}

int compare3(struct Student* first, struct Student* second) {
	if (first->grade > second->grade) return -1;
	if (first->grade < second->grade) return 1;
	return 0;
}
