from typing import Callable
from time import time
from os import path
import random
import csv


def exhaustiveSolution(input: list[int], k: int) -> bool:
    for i in input:
        for j in input:
            if i + j == k:
                return True

    return False


def orderedSolution(input: list[int], k: int) -> bool:
    input.sort()

    endIndex: int = len(input) - 1
    startIndex: int = 0
    for _ in range(len(input) - 1):
        if startIndex >= endIndex:
            return False

        result: int = input[startIndex] + input[endIndex]
        if result == k:
            return True
        elif result > k:
            endIndex -= 1
            continue

        startIndex += 1

    return False


def ellaborateSolution(input: list[int], k: int) -> bool:
    inputSet: set[int] = set()

    for num in input:
        if k - num in inputSet:
            return True
        inputSet.add(num)

    return False


def testAlgorithms(
    algorithms: dict[str, Callable[[list[int], int], bool]],
    i: int,
    inputSize: int,
    input: list[int],
    isValid: bool,
    basePath: str,
) -> None:
    k: int = 0
    if isValid:
        # Existe exatamente 1 '0' em canda input, por isso se k for um
        # dos elementos do input, irá ser calculado 0 + k = k, tornando
        # o input válido
        k = random.randint(0, inputSize)
    else:
        # Não existem números negativos nos inputs, logo com este k, irá
        # ser retornado sempre "False"
        k = -1

    for fileNamePrefix, algorithm in algorithms.items():
        fileName: str = f"{fileNamePrefix}-{inputSize}-{i}-"
        if isValid:
            fileName += "Avg.txt"
        else:
            fileName += "Worst.txt"

        with open(path.join(basePath, "results", fileName), "w") as algorithmFile:
            algorithmFile.write(f"k = {k}\n")

            print(
                f"Começando o algoritmo {fileNamePrefix} com um input de {inputSize} elementos e k ",
                end="",
            )
            if isValid:
                print("válido")
            else:
                print("inválido")

            inputCopy: list[int] = input.copy()
            start: float = time()
            inputIsCorrect: bool = algorithm(inputCopy, k)
            end: float = time()
            ellapsedTimeSeconds: float = end - start

            algorithmFile.write(f"Resultado: {inputIsCorrect}\nTempo: {ellapsedTimeSeconds} s\n")


def main() -> None:
    basePath: str = path.dirname(__file__)  # Diretório onde está este script
    algorithms: dict[str, Callable[[list[int], int], bool]] = {
        "exhaustive": exhaustiveSolution,
        "ordered": orderedSolution,
        "ellaborate": ellaborateSolution,
    }

    inputsDict: dict[int, list[list[int]]] = {
        10_000: [],
        32_500: [],
        55_000: [],
        77_500: [],
        100_000: [],
    }
    for inputSize, inputs in inputsDict.items():
        for i in range(5):
            input: list[int] = random.sample(range(inputSize), inputSize)
            inputs.append(input)

            inputsFileName = f"{inputSize}_{i}.csv"
            with open(path.join(basePath, "inputs", inputsFileName), "w") as file:
                csv.writer(file).writerow(input)

        for i, input in enumerate(inputs):
            for isValid in (False, True):
                testAlgorithms(algorithms, i, inputSize, input, isValid, basePath)

    print("Terminou")


if __name__ == "__main__":
    main()
