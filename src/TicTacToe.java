import java.util.Random;
import java.util.Scanner;


public class TicTacToe {

    public static void main(String[] args) {
        startGame();
    }


    /**
     * Основной игровой цикл
     */
    public static void startGame(){

        //TODO: Отрефакторить, когда разрешат поля класса и классы.

        int FIELD_SIZE = 7;
        int LINE_SIZE = 4;
        char[] CELLTYPE= {'X','O','•'};
        int CELLTYPE_EMPTY = 2;

        boolean[] isHumanPlayer = new boolean[2];

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        char[][] field = new char[FIELD_SIZE][FIELD_SIZE];

        do {
            selectPlayers(isHumanPlayer, scanner);
            clearField(field, CELLTYPE[CELLTYPE_EMPTY]);
            gameLoop(field, isHumanPlayer, LINE_SIZE, CELLTYPE, scanner, random);
            System.out.println("Хотите сыграть ещё одну игру? 1 - да, 0 - нет");
        } while (scanner.nextInt() == 1);

    }


    public static void gameLoop(char[][] field, boolean[] isHumanPlayer, int lineSize, char[] cellType, Scanner scanner, Random random){
        int CELLTYPE_EMPTY = 2;
        int currentPlayer = 0;
        int moveCounter = 1; //Номер хода
        int maxMovesCount = field.length * field.length;

        while (true){

            drawField(field);
            System.out.printf("======================================");
            System.out.printf("Ход %d игрока!\n", (currentPlayer+1));

            if (isHumanPlayer[currentPlayer]){
                humanMove(field, cellType[CELLTYPE_EMPTY], cellType[currentPlayer], scanner);
            } else {
                if(moveCounter <= 2){
                    firstComputerMove(field, cellType[CELLTYPE_EMPTY], cellType[currentPlayer], random);
                } else {
                    computerMove(field, cellType[CELLTYPE_EMPTY], cellType[currentPlayer], cellType[currentPlayer == 1 ? 0 : 1], lineSize, random);
                }
            }

            if (checkWin(field, cellType[currentPlayer], lineSize) > 0){
                System.out.printf("Игрок %d победил!\n", (currentPlayer+1));
                System.out.println("Победная комбинация:");
                drawField(field);
                break;
            }

            if (checkDraw(moveCounter, maxMovesCount)){
                System.out.println("Ничья!");
                break;
            }
            currentPlayer = currentPlayer == 1 ? 0 : 1;
            moveCounter++;
        }
    }


    /**
     * Выбор типа игрока - человек true, компьютерный оппонент - false
     * @param isHumanPlayer массив, содержащий признак игрока - человека
     * @param scanner сканер для ввода данных
     */
    public static void selectPlayers(boolean[] isHumanPlayer, Scanner scanner){
        //Для большего количества можно было бы использовать цикл
        System.out.println("Введите тип первого и второго игрока");
        System.out.println("Первый игрок - человек или ИИ? 1 - человек, 2 - ИИ");
        isHumanPlayer[0] = 1 == scanner.nextInt();

        System.out.println("Второй игрок - человек или ИИ? 1 - человек, 2 - ИИ");
        isHumanPlayer[1] = 1 == scanner.nextInt();

        if (isHumanPlayer[0] && isHumanPlayer[1]){
            System.out.println("По очереди вводите координаты ячеек, в которые хотите сделать ход");
        } else if (!isHumanPlayer[0] && !isHumanPlayer[1]){
            System.out.println("Два компьютерных ИИ будут сражаться между собой");
        } else if(isHumanPlayer[0]){
            System.out.println("Игрок ходит первым, ИИ - вторым. Вводите координаты, в которые хотите поставить X");
        } else if(isHumanPlayer[1]){
            System.out.println("Игрок ходит вторым. После первого хода ИИ нажмите Enter и вводите координаты, в которые хотите поставить O");
        }

    }

    /**
     * Очистка игрового поля - заполнение символом по умолчанию
     * @param field матрица символов, содержащая игровое поле
     * @param cellType символ по умолчанию, не являющийся фишкой первого или второго игрока
     */
    public static void clearField(char[][] field, char cellType) {
        int size = field.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = cellType;
            }
        }
    }


    /**
     * Печать поля field на экран
     * @param field матрица символов, содержащая игровое поле
     */
    public static void drawField(char[][] field){
        int size = field.length;

        System.out.print(" ");
        for (int i = 0; i < size; i++) {
            System.out.print(" " + (i+1) + " ");
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.print(i+1);
            for (int j = 0; j < size; j++) {
                System.out.print(" " + field[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Проверка игрового поля на состояние ничьей. Должна осуществляться после проверки на победу
     * т.к. проверяет только наличие свободных ячеек
     * @param currentMove Номер текущего хода
     * @param maxMoves Максимальное количество возможных фишек на игровом поле
     * @return true если на игровом поле ситуация ничьей
     */
    public static boolean checkDraw(int currentMove, int maxMoves){
        return currentMove >= maxMoves;
    }

    /**
     * Проверка, есть ли на игровой доске победная ситуация для одного или другого игрока
     * @param field матрица символов, содержащая игровое поле
     * @param checkingChar символ, являющийся фишкой игрока, победу которого мы хотим определить
     * @param lineSize размер непрерывной линии фишек, при достижении которой засчитывается победа
     * @return количество победных комбинаций на поле для проверяемого игрока
     */
    public static int checkWin(char[][] field, char checkingChar, int lineSize){
        int size = field.length;
        int checkedIndex = size - lineSize;
        //Переменные указывают, может ли из точки y,x теоретически быть построена выигрышная комбинация
        boolean horizontalWinPossible;
        boolean verticalWinPossible;
        boolean principalDiagonalWinPossible;
        boolean secondaryDiagonalWinPossible;
        char gapChar = '•';
        int retCount = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                //Если поиск для текущих X и Y для выбранного направления валиден - ставим true
                horizontalWinPossible        = (x <= checkedIndex);
                verticalWinPossible          = (y <= checkedIndex);
                principalDiagonalWinPossible = (x <= checkedIndex && y <= checkedIndex);
                secondaryDiagonalWinPossible = (x >= lineSize - 1) && (y <= checkedIndex);


                retCount += getWinCombinationCount(field, gapChar, checkingChar, lineSize, 0, horizontalWinPossible,
                        verticalWinPossible, principalDiagonalWinPossible, secondaryDiagonalWinPossible, y, x);
            }
        }
        return retCount;
    }

    /**
     * Вычисляет количество выигрышных ситуаций для части игрового поля - матрицы,
     * начальными координатами которой на поле являются y,x и конечными y + lineSize - 1, x + lineSize - 1
     * @param field Матрица, содержащая игровое поле
     * @param emptyCellChar символ, означающий незанятую клетку
     * @param checkingChar символ, являющийся фишкой игрока, победу которого мы хотим определить
     * @param lineSize размер непрерывной линии фишек, при достижении которой засчитывается победа
     * @param gapCount Допустимое количество замен фишек игрока на пустые клетки
     * @param horizontalWinPossible для данных y,x доступна проверка по горизонтали
     * @param verticalWinPossible для данных y,x досиупна проверка по вертикали
     * @param principalDiagonalWinPossible для данных y,x доступна проверка главной диагонали
     * @param secondaryDiagonalWinPossible для данных y,x доступна проверка побочной диагонали
     * @param y начальная координата по вертикали
     * @param x начальная координата по горизонтали
     * @return количество победных комбинаций для  для проверяемого игрока
     */
    public static int getWinCombinationCount(char[][] field, char emptyCellChar, char checkingChar,  int lineSize, int gapCount,
                                             boolean horizontalWinPossible,
                                             boolean verticalWinPossible,
                                             boolean principalDiagonalWinPossible,
                                             boolean secondaryDiagonalWinPossible,
                                             int y, int x) {

        int l  = 0;
        int retCount = 0;
        int horizontalGapCount = gapCount;
        int verticalGapCount = gapCount;
        int principalDiagonalGapCount = gapCount;
        int secondaryDiagonalGapCount = gapCount;

        //l - цикл для проверки всех точек линии в 4х направлениях с начальными координатами y,x
        while (l < lineSize && (horizontalWinPossible || verticalWinPossible || principalDiagonalWinPossible || secondaryDiagonalWinPossible)) {
            //Помним, что операции сравнения - ленивые. Можно проверять выход за границы массива и элемент массива в одном if
            if (horizontalWinPossible && (checkingChar != field[y][x + l])) horizontalWinPossible = false;
            if (verticalWinPossible && (checkingChar != field[y + l][x])) verticalWinPossible = false;
            if (principalDiagonalWinPossible && (checkingChar != field[y + l][x + l]))
                principalDiagonalWinPossible = false;
            if (secondaryDiagonalWinPossible && (checkingChar != field[y + l][x - l]))
                secondaryDiagonalWinPossible = false;
            l++;
        }
        //Победная комбинация символов в одном из направлений была достигнута
        if (horizontalWinPossible) retCount++;
        if (verticalWinPossible) retCount++;
        if (principalDiagonalWinPossible) retCount++;
        if (secondaryDiagonalWinPossible) retCount++;
        return retCount;
    }


    /**
     * Ход игрока - человека. До хода нужно проверить состояние игрового поля на ничью
     * @param field Матрица, содержащая игровое поле
     * @param emptyCellChar Символ, означающий незанятую клетку
     * @param playerChar Символ - фишка, принадлежащий игроку
     */
    public static void humanMove(char[][] field, char emptyCellChar, char playerChar, Scanner scanner){
        int x;
        int y;
        int size = field.length;

        while (true) {
            System.out.println("Введите X координату");
            x = humanInputCoordinate(field, scanner);

            System.out.println("Введите Y координату");
            y = humanInputCoordinate(field, scanner);

            if (field[y-1][x-1] == emptyCellChar){
                field[y-1][x-1] = playerChar;
                return;
            }

            System.out.printf("Клетка для установки [%s] должна быть свободна\n", playerChar);
            drawField(field);
        }
    }

    /**
     * Цикл запроса координат у человека
     *
     * @param field Матрица, содержащая игровое поле
     * @param scanner сканер для ввода данных
     * @return Координата, введенная пользователем
     */
    public static int humanInputCoordinate(char[][] field, Scanner scanner){
        int coord;
        while (true){
            coord = scanner.nextInt();
            if (coord < 1 || coord > field.length){
                System.out.printf("Значение должно лежать в диапазоне от 1 до %d\n Введите значение:", field.length);
            } else {
                return coord;
            }
        }
    }

    /**
     * Функция клонирования матрицы (clone() пока использовать запрещено)
     * @param matrix Исходная матрица для клонирования
     * @return Клон исходной матрицы
     */
    public static char[][] cloneMatrix(char[][] matrix){
        char[][] retMatrix = new char[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                retMatrix[i][j] = matrix[i][j];
            }
        }
        return retMatrix;
    }

    /**
     * Первый ход компьютера всегда осуществляется в центральные клетки доски
     * @param field field Матрица, содержащая игровое поле
     * @param emptyCellChar Символ, означающий незанятую клетку
     * @param playerChar Символ - фишка, принадлежащий игроку
     * @param random Генератор случайных чисел
     */
    public static void firstComputerMove(char[][] field, char emptyCellChar, char playerChar, Random random) {
        int x;
        int y;
        while (true){
            //Пытаемся занять центральную ячейку
            x = field.length / 2;
            y = field.length / 2;

            if (field[x][y] == emptyCellChar) break;

            //Если центральная ячейка занята
            x = 1 + x  - random.nextInt(3);
            y = 1 + y  - random.nextInt(3);
            if (field[x][y] == emptyCellChar) break;
        }
        field[x][y] = playerChar;
    }

    /**
     * Ход компьютерного оппонента с расчетом игровой позиции и установкой фишки
     * @param field field Матрица, содержащая игровое поле
     * @param emptyCellChar Символ, означающий незанятую клетку
     * @param playerChar Символ - фишка, принадлежащий игроку
     * @param random Генератор случайных чисел
     */
    public static void computerMove(char[][] field, char emptyCellChar, char playerChar, char opponentChar, int lineSize, Random random) {
        int[] bestMove = miniMax(field, emptyCellChar, playerChar, playerChar, opponentChar, lineSize, 0, 0, random);
        field[bestMove[1]][bestMove[2]] = playerChar;
        System.out.printf("Введите X координату\n %d\nВведите Y координату\n %d\n", bestMove[2] + 1, bestMove[1] + 1);
    }


    /**
     * Установка фишки компьютерным оппонентом на поле во втором и последующих ходах
     * @param field Матрица, содержащая игровое поле
     * @param emptyCellChar Символ, означающий незанятую клетку
     * @return Массив из всех доступных для установки фишки клеток. [0][0] хранит количество n, [n][0] - y, [n][1] - x
     */
    public static int[][] getComputerPossibleCoordinates(char[][] field, char emptyCellChar, Random random){
        int tail = (field.length + 1) % 2;
        int[][] retCoords = new int[field.length*field.length][2];
        int counter = 0;
        int i;
        int j;


        //Ищем место для фишки из центра по спирали
        for (i = (field.length + 1) / 2 - 1; i >= 0; i--) {
            for (j = 0; j < tail; j++) {

                if (field[i][i+j] == emptyCellChar){
                    counter++;
                    retCoords[counter][0] = i;
                    retCoords[counter][1] = i+j;
                }
                if (field[i+j][field.length-i-1] == emptyCellChar){
                    counter++;
                    retCoords[counter][0] = i+j;
                    retCoords[counter][1] = field.length-i-1;
                }
                if (field[field.length-i-1][field.length-i-1-j] == emptyCellChar) {
                    counter++;
                    retCoords[counter][0] = field.length-i-1;
                    retCoords[counter][1] = field.length-i-1-j;
                }
                if (field[field.length-i-1-j][i] == emptyCellChar) {
                    counter++;
                    retCoords[counter][0] = field.length-i-1-j;
                    retCoords[counter][1] = i;
                }

            }
            tail += 2;
        }
        retCoords[0][0] = counter;

        //Немного рандома
        if (counter > 10) {
            shuffleArray(retCoords, 1, counter - 1, field.length, random);
        }

        return retCoords;
    }

    /**
     * Функция случайной позиционной перестановки элементов матрицы int[random<->random][]
     * @param array Матрица, элементы первой размерности которой будут случайным образом поменяны
     * @param firstIndex Индекс первого элемента для перестановок
     * @param lastIndex Индекс последнего элемента для перестановок
     * @param shuffleLoops Количество перестановок
     * @param random Генератор случайных чисел
     */
    public static void shuffleArray(int[][] array, int firstIndex, int lastIndex, int shuffleLoops, Random random){
        int index1;
        int index2;
        int value;

        for (int i = 0; i < shuffleLoops; i++) {
            index1 = random.nextInt(lastIndex - firstIndex + 1) + firstIndex;
            index2 = random.nextInt(lastIndex - firstIndex + 1) + firstIndex;
            for (int j = 0; j < array[0].length; j++) {
                value = array[index1][j];
                array[index1][j] = array[index2][j];
                array[index2][j] = value;
            }
        }
    }


      /**
     *
     * @param field Матрица, содержащая игровое поле
     * @param playerChar Фишка текущего игрока
     * @param opponentChar Фишка оппонента
     * @param lineSize Размер непрерывной линии фишек, при достижении которой засчитывается победа
     * @param score Счет на предыдущей итерации
     * @param depth Глубина рекурсии
     * @return Весовой коэффициент для позиции текущего игрока
     */
    public static int[] miniMax(char[][] field, char emptyCellChar, char checkedPlayer, char playerChar, char opponentChar, int lineSize, int score, int depth, Random random){
        char[][][] cloneField;
        char switchChar;
        int[][] possibleCoordinates;
        int MAXDEPTH = 4;
        int bestMoveScore;
        int bestMovePosition = 0;
        int[] moveScore;
        int[] retMoveCoordScore = {0,0,0};
        int[] getMoveCoordScore;

        depth++;

        //Получаем список возможных ходов
        possibleCoordinates = getComputerPossibleCoordinates(field, emptyCellChar, random);

        bestMoveScore = Integer.MIN_VALUE;
        if (possibleCoordinates[0][0] == 0){
            //Тут ничья
            retMoveCoordScore[0] = bestMoveScore;
            return retMoveCoordScore;
        }

        cloneField = new char[possibleCoordinates[0][0]][][];
        moveScore = new int[possibleCoordinates[0][0]];

        //Формируем список возможных полей на текущей глубине
        for (int i = 1; i <= possibleCoordinates[0][0]; i++) {
            cloneField[i-1] = cloneMatrix(field);
            cloneField[i-1][possibleCoordinates[i][0]][possibleCoordinates[i][1]] = playerChar;
            //Запомнили количество очков за расклад
            moveScore[i-1] = getFieldScore(cloneField[i-1], checkedPlayer, playerChar, opponentChar, lineSize);
            if (moveScore[i-1] > bestMoveScore){
                bestMoveScore = moveScore[i-1];
                bestMovePosition = i;
            }
        }

        //Если лучший ход < 0 мы всё равно проигрываем, глубже смотреть смысла нет
        //Если глубина максимальная - глубже смотреть нам запрещено
        if (depth >= MAXDEPTH || (depth <= 2) && (bestMoveScore >= 1)){
            retMoveCoordScore[0] = bestMoveScore + score;
            retMoveCoordScore[1] = possibleCoordinates[bestMovePosition][0];
            retMoveCoordScore[2] = possibleCoordinates[bestMovePosition][1];
            return retMoveCoordScore;
        }

        //Меняем сторону расчета
        switchChar = playerChar;
        playerChar = opponentChar;
        opponentChar = switchChar;
        //Запускаем в цикле обход каждого созданного поля с очками >= 0
        retMoveCoordScore[0] = Integer.MIN_VALUE;

        for (int i = 1; i <= possibleCoordinates[0][0]; i++) {
            if (moveScore[i-1] < 0) continue;
            getMoveCoordScore = miniMax(cloneField[i-1], emptyCellChar, checkedPlayer, playerChar, opponentChar, lineSize, moveScore[i-1] + score, depth, random);
            if (getMoveCoordScore[0] > retMoveCoordScore[0]){
                retMoveCoordScore[0] = getMoveCoordScore[0];
                retMoveCoordScore[1] = getMoveCoordScore[1];
                retMoveCoordScore[2] = getMoveCoordScore[2];
            }
        }

        return retMoveCoordScore;
    }


    /**
     * Функция оценки текущего состояния игрового поля
     * @param cloneField Клон игрового поля
     * @param checkedPlayer Фишка игрока - протагониста
     * @param playerChar Текущий активный игрок, поставивший свою фишку и оценивающий ситуаци.
     * @param opponentChar Оппонент, ход к которому перейдет после завершения хода активного игрока
     * @param lineSize Размер победной линии
     * @return Счёт для протагониста
     */
    public static int getFieldScore(char[][] cloneField, char checkedPlayer, char playerChar, char opponentChar, int lineSize) {
        int retScore;
        //Проверяем, есть ли победа в этом ходу для активного игрока
        retScore = checkWin(cloneField, playerChar, lineSize);
        if (retScore > 0) {
            return 1;
        }

        //Проверяем, есть ли победа в этом ходу для неактивного игрока
        retScore = checkWin(cloneField, opponentChar, lineSize);
        if (retScore > 0) {
            return 1;
        }

        return 0;
    }

}
