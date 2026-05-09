#ifndef COMMON_H
#define COMMON_H

enum CourseType { BACHELOR = 0, MASTER = 1, NON_DEGREE_GRANTING = 2 };

struct Student {
	unsigned int id;
	enum CourseType courseType; // 0 (Licenciatura), 1 (Mestrado), 2 (Não conferente de grau)
	int grade;					// 10 - 20
};

#endif
