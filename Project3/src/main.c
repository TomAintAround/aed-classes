#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "../lib/algorithms.h"
#include "../lib/common.h"
#include "../lib/compare.h"

static int binomialDistribution(int n, int p) {
	int result = 0;

	for (int i = 0; i < n; i++) {
		if ((rand() % p) == 0) result++;
	}

	return result;
}

static enum CourseType randomCourse() {
	return (enum CourseType)(rand() % 3);
}

static void fillAscending(struct Student* students, size_t size) {
	size_t numDuplicates = (size_t)(0.05 * (double)size);
	size_t currId = 1;

	size_t i = 0;
	while (i < size) {
		int repetitions = 1;
		if (numDuplicates > 0 && (rand() % 100) < 5) {
			repetitions = 2 + (rand() % 2);
			numDuplicates--;
		}

		for (int r = 0; r < repetitions && i < size; r++, i++) {
			students[i].id = currId;
			students[i].courseType = randomCourse();
			students[i].grade = 10 + binomialDistribution(10, 3);
		}

		currId++;
	}
}

static void fillDescending(struct Student* students, size_t size) {
	size_t numDuplicates = (size_t)(0.05 * (double)size);
	size_t currId = size;

	size_t i = 0;
	while (i < size) {
		int repetitions = 1;
		if (numDuplicates > 0 && (rand() % 100) < 5) {
			repetitions = 2 + (rand() % 2);
			numDuplicates--;
		}

		for (int r = 0; r < repetitions && i < size; r++, i++) {
			students[i].id = currId;
			students[i].courseType = randomCourse();
			students[i].grade = 10 + binomialDistribution(10, 3);
		}

		if (currId > 1) currId--;
	}
}

static void fillRandomly(struct Student* students, size_t size) {
	fillAscending(students, size);

	for (size_t i = size - 1; i > 0; i--) {
		size_t j = rand() % (i + 1);
		struct Student tmp = students[i];
		students[i] = students[j];
		students[j] = tmp;
	}
}

static double elapsedSeconds(struct timespec* start, struct timespec* end) {
	time_t seconds = end->tv_sec - start->tv_sec;
	long nanoseconds = end->tv_nsec - start->tv_nsec;
	if (nanoseconds < 0) {
		seconds--;
		nanoseconds += 1'000'000'000L;
	}
	return (double)seconds + ((double)nanoseconds / 1e9);
}

static void logMessage(const char* message, FILE* file) {
	fprintf(file, "%s", message);
	printf("%s", message);
}

static void testAlgos(SortAlgo algo, char* algoName, struct Student** arrays, char* arrayType,
					  Comparator comparator, int comparatorNum, FILE* logFile,
					  FILE* csvResult, size_t* sizes, int numSizes, int numReps) {
	char message[LOG_SIZE];

	int currPos = 0;
	for (int size = 0; size < numSizes; size++) {
		for (int rep = 0; rep < numReps; rep++) {
			struct Student* studentsCopy = malloc(sizeof(struct Student) * sizes[size]);
			if (studentsCopy == NULL) {
				fclose(logFile);
				fclose(csvResult);
				fprintf(stderr, "Erro ao fazer malloc para um array de "
								"cópia\n");
				exit(1);
			}
			memcpy(studentsCopy, arrays[currPos++], sizes[size] * sizeof(struct Student));

			struct timespec start, end;
			snprintf(message, LOG_SIZE, "\t\t%zu estudantes (%d): ", sizes[size], rep + 1);
			logMessage(message, logFile);
			fflush(logFile);
			if (!timespec_get(&start, TIME_UTC)) perror("timespec_get failed");
			algo(studentsCopy, 0, sizes[size] - 1, comparator);
			if (!timespec_get(&end, TIME_UTC)) perror("timespec_get failed");
			double elapsed = elapsedSeconds(&start, &end);
			snprintf(message, LOG_SIZE, "%.9f segundos\n", elapsed);
			logMessage(message, logFile);
			fflush(logFile);

			fprintf(csvResult, "%s,%s,%d,%zu,%d,%.9f\n", algoName, arrayType,
					comparatorNum, sizes[size], rep + 1, elapsed);
			fflush(csvResult);

			free(studentsCopy);
		}
	}
}

int main() {
	srand(time(NULL));
	FILE* logFile = fopen("project3.log", "w");
	if (logFile == NULL) {
		perror("Não foi possível escrever no ficheiro log");
		return 1;
	}
	FILE* csvResult = fopen("result.csv", "w");
	if (csvResult == NULL) {
		fclose(logFile);
		perror("Não foi possível escrever no ficheiro de resultados");
		return 1;
	}
	fprintf(csvResult,
			"algorithm,arrayType,comparatorNum,size,rep,timeSeconds\n");
	fflush(csvResult);

	int numReps = 3;
	size_t sizes[] = { 1'000, 10'000, 100'000, 1'000'000 };
	int numSizes = sizeof(sizes) / sizeof(sizes[0]);

	int currPos = 0;
	struct Student* studentsAscId[numSizes * numReps];
	for (int size = 0; size < numSizes; size++) {
		for (int rep = 0; rep < numReps; rep++) {
			char filename[64];
			snprintf(filename, 64, "inputs/asc/%zu_%d.csv", sizes[size], rep + 1);
			FILE* csvInput = fopen(filename, "w");
			if (csvInput == NULL) {
				fclose(logFile);
				fclose(csvResult);
				perror("Não foi possível escrever num ficheiro de input");
				return 1;
			}
			fprintf(csvInput, "id,courseType,grade\n");

			studentsAscId[currPos] = malloc(sizeof(struct Student) * sizes[size]);
			if (studentsAscId[currPos] == NULL) {
				fclose(logFile);
				fclose(csvResult);
				fclose(csvInput);
				fprintf(stderr, "Erro ao fazer malloc ao criar array com IDs "
								"ascendentes\n");
				return 1;
			}

			fillAscending(studentsAscId[currPos], sizes[size]);
			for (size_t i = 0; i < sizes[size]; i++) {
				struct Student student = studentsAscId[currPos][i];
				fprintf(csvInput, "%zu,%d,%d\n", student.id, student.courseType,
						student.grade);
			}

			fclose(csvInput);
			currPos++;
		}
	}

	currPos = 0;
	struct Student* studentsDesId[numSizes * numReps];
	for (int size = 0; size < numSizes; size++) {
		for (int rep = 0; rep < numReps; rep++) {
			char filename[64];
			snprintf(filename, 64, "inputs/desc/%zu_%d.csv", sizes[size], rep + 1);
			FILE* csvInput = fopen(filename, "w");
			if (csvInput == NULL) {
				fclose(logFile);
				fclose(csvResult);
				perror("Não foi possível escrever num ficheiro de input");
				return 1;
			}
			fprintf(csvInput, "id,courseType,grade\n");

			studentsDesId[currPos] = malloc(sizeof(struct Student) * sizes[size]);
			if (studentsDesId[currPos] == NULL) {
				fclose(logFile);
				fclose(csvResult);
				fclose(csvInput);
				fprintf(stderr, "Erro ao fazer malloc ao criar array com IDs "
								"descendentes\n");
				return 1;
			}

			fillDescending(studentsDesId[currPos], sizes[size]);
			for (size_t i = 0; i < sizes[size]; i++) {
				struct Student student = studentsDesId[currPos][i];
				fprintf(csvInput, "%zu,%d,%d\n", student.id, student.courseType,
						student.grade);
			}

			fclose(csvInput);
			currPos++;
		}
	}

	currPos = 0;
	struct Student* studentsRandId[numSizes * numReps];
	for (int size = 0; size < numSizes; size++) {
		for (int rep = 0; rep < numReps; rep++) {
			char filename[64];
			snprintf(filename, 64, "inputs/rand/%zu_%d.csv", sizes[size], rep + 1);
			FILE* csvInput = fopen(filename, "w");
			if (csvInput == NULL) {
				fclose(logFile);
				fclose(csvResult);
				perror("Não foi possível escrever num ficheiro de input");
				return 1;
			}
			fprintf(csvInput, "id,courseType,grade\n");

			studentsRandId[currPos] = malloc(sizeof(struct Student) * sizes[size]);
			if (studentsRandId[currPos] == NULL) {
				fclose(logFile);
				fclose(csvResult);
				fclose(csvInput);
				fprintf(stderr, "Erro ao fazer malloc ao criar array com IDs "
								"espalhados\n");
				return 1;
			}

			fillRandomly(studentsRandId[currPos], sizes[size]);
			for (size_t i = 0; i < sizes[size]; i++) {
				struct Student student = studentsRandId[currPos][i];
				fprintf(csvInput, "%zu,%d,%d\n", student.id, student.courseType,
						student.grade);
			}

			fclose(csvInput);
			currPos++;
		}
	}

	SortAlgo algos[] = { heapSort, mergeSort, bubbleSort };
	char algosName[][8] = { "Heap", "Merge", "Bubble" };
	int numAlgos = sizeof(algos) / sizeof(algos[0]);

	Comparator comparators[] = { compare1, compare2, compare3 };
	int numComparators = sizeof(comparators) / sizeof(comparators[0]);

	struct Student** typesArrays[] = { studentsAscId, studentsDesId, studentsRandId };
	char typesMessages[][16] = { "ascendente", "descendente", "espalhado" };
	int numTypesArrays = sizeof(typesArrays) / sizeof(typesArrays[0]);

	char message[LOG_SIZE];
	for (int algo = 0; algo < numAlgos; algo++) {
		snprintf(message, LOG_SIZE, "Testando algorítmo: %s Sort\n", algosName[algo]);
		logMessage(message, logFile);
		fflush(logFile);

		for (int type = 0; type < numTypesArrays; type++) {
			snprintf(message, LOG_SIZE, "Testando com conjunto de dados com ID %s:\n",
					 typesMessages[type]);
			logMessage(message, logFile);
			fflush(logFile);

			for (int comparator = 0; comparator < numComparators; comparator++) {
				snprintf(message, LOG_SIZE,
						 "\tTestando algoritmo de comparação %d:\n", comparator + 1);
				logMessage(message, logFile);
				fflush(logFile);

				testAlgos(algos[algo], algosName[algo], typesArrays[type],
						  typesMessages[type], comparators[comparator], comparator + 1,
						  logFile, csvResult, sizes, numSizes, numReps);
			}
		}
	}

	for (int i = 0; i < numSizes * numReps; i++) {
		free(studentsAscId[i]);
		free(studentsDesId[i]);
		free(studentsRandId[i]);
	}
	fclose(logFile);
	fclose(csvResult);
	return 0;
}
