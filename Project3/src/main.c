#include <stddef.h>
#include <stdio.h>
#include "algorithms.h"
#include "common.h"
#include "compare.h"

void printStudent(struct Student student) {
	printf("{%d, %d, %d}", student.id, student.courseType, student.grade);
}

void printStudents(struct Student* students, size_t num) {
	printf("{");
	for (size_t i = 0; i < num - 1; i++) {
		printStudent(students[i]);
		printf(", ");
	}
	printStudent(students[num - 1]);
	printf("}\n");
}

int main() {
	struct Student students1[] = {
		{ 8,	 BACHELOR,			   11 },
		{ 4,	 MASTER,				 10 },
		{ 4,	 MASTER,				 10 },
		{ 5,	 BACHELOR,			   13 },
		{ 12, NON_DEGREE_GRANTING, 10 },
		{ 6,	 BACHELOR,			   16 },
		{ 7,	 MASTER,				 17 },
		{ 9,	 MASTER,				 12 },
		{ 2,	 BACHELOR,			   14 },
		{ 10, MASTER,			  14 },
		{ 10, BACHELOR,			14 },
		{ 11, NON_DEGREE_GRANTING, 15 },
		{ 1,	 NON_DEGREE_GRANTING, 14 },
		{ 3,	 BACHELOR,			   10 },
	};
	size_t num1 = sizeof(students1) / sizeof(struct Student);

	struct Student students2[] = {
		{ 8,	 BACHELOR,			   11 },
		{ 4,	 MASTER,				 10 },
		{ 4,	 MASTER,				 10 },
		{ 5,	 BACHELOR,			   13 },
		{ 12, NON_DEGREE_GRANTING, 10 },
		{ 6,	 BACHELOR,			   16 },
		{ 7,	 MASTER,				 17 },
		{ 9,	 MASTER,				 12 },
		{ 2,	 BACHELOR,			   14 },
		{ 10, MASTER,			  14 },
		{ 10, BACHELOR,			14 },
		{ 11, NON_DEGREE_GRANTING, 15 },
		{ 1,	 NON_DEGREE_GRANTING, 14 },
		{ 3,	 BACHELOR,			   10 },
	};
	size_t num2 = sizeof(students1) / sizeof(struct Student);

	struct Student students3[] = {
		{ 8,	 BACHELOR,			   11 },
		{ 4,	 MASTER,				 10 },
		{ 4,	 MASTER,				 10 },
		{ 5,	 BACHELOR,			   13 },
		{ 12, NON_DEGREE_GRANTING, 10 },
		{ 6,	 BACHELOR,			   16 },
		{ 7,	 MASTER,				 17 },
		{ 9,	 MASTER,				 12 },
		{ 2,	 BACHELOR,			   14 },
		{ 10, MASTER,			  14 },
		{ 10, BACHELOR,			14 },
		{ 11, NON_DEGREE_GRANTING, 15 },
		{ 1,	 NON_DEGREE_GRANTING, 14 },
		{ 3,	 BACHELOR,			   10 },
	};
	size_t num3 = sizeof(students1) / sizeof(struct Student);

	printStudents(students1, num1);
	bubbleSort(students1, num1, compare1);
	printStudents(students1, num1);

	printf("\n--------------\n\n");

	printStudents(students2, num2);
	bubbleSort(students2, num2, compare2);
	printStudents(students2, num2);

	printf("\n--------------\n\n");

	printStudents(students3, num3);
	bubbleSort(students3, num3, compare3);
	printStudents(students3, num3);

	return 0;
}
