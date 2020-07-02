import java.util.Random;
import java.util.Scanner;


public class TicTacToe {

    final int FIELD_SIZE = 7;
    final int LINE_SIZE = 4;
    final char[] CELLTYPE= {'X','O','•'};
    final int CELLTYPE_EMPTY = 2;


    boolean[] isHumanPlayer;
    char[][] field;
    int currentPlayer;
    int moveCounter;
    int maxMovesCount;

    Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    public static void main(String[] args) {

        TicTacToe game = new TicTacToe();
        game.startGame();

    }


    /**
     * Основной игровой цикл
     */
    public void startGame(){

        //TODO: Отрефакторить, когда разрешат поля класса и классы.

        isHumanPlayer = new boolean[2];

        field = new char[FIELD_SIZE][FIELD_SIZE];

        do {
            selectPlayers();
            clearField();
            gameLoop();
            System.out.println("Хотите сыграть ещё одну игру? 1 - да, 0 - нет");
        } while (scanner.nextInt() == 1);

    }


    public void gameLoop(){

        currentPlayer = 0;
        moveCounter = 1; //Номер хода
        maxMovesCount = field.length * field.length;

        while (true){

            drawField();
            System.out.print("======================================");
            System.out.printf("Ход %d игрока!\n", (currentPlayer+1));

            if (isHumanPlayer[currentPlayer]){
                humanMove(CELLTYPE[currentPlayer]);
            } else {
                if(moveCounter <= 2){
                    firstComputerMove(CELLTYPE[currentPlayer]);
                } else {
                    computerMove(CELLTYPE[currentPlayer], CELLTYPE[currentPlayer == 1 ? 0 : 1]);
                }
            }

            if (checkWin(field, CELLTYPE[currentPlayer]) > 0){
                System.out.printf("Игрок %d победил!\n", (currentPlayer+1));
                System.out.println("Победная комбинация:");
                drawField();
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
     */
    public void selectPlayers(){
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
     */
    public void clearField() {
        int size = field.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = CELLTYPE[CELLTYPE_EMPTY];
            }
        }
    }


    /**
     * Печать поля field на экран
     */
    public void drawField(){

        System.out.print(" ");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print(" " + (i+1) + " ");
        }
        System.out.println();

        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.print(i+1);
            for (int j = 0; j < FIELD_SIZE; j++) {
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
     * @param fieldToCheck матрица символов, содержащая игровое поле
     * @param checkingChar символ, являющийся фишкой игрока, победу которого мы хотим определить
     * @return количество победных комбинаций на поле для проверяемого игрока
     */
    public int checkWin(char[][] fieldToCheck, char checkingChar){
        int checkedIndex = FIELD_SIZE - LINE_SIZE;
        //Переменные указывают, может ли из точки y,x теоретически быть построена выигрышная комбинация
        boolean horizontalWinPossible;
        boolean verticalWinPossible;
        boolean principalDiagonalWinPossible;
        boolean secondaryDiagonalWinPossible;

        int retCount = 0;
        for (int y = 0; y < FIELD_SIZE; y++) {
            for (int x = 0; x < FIELD_SIZE; x++) {

                //Если поиск для текущих X и Y для выбранного направления валиден - ставим true
                horizontalWinPossible        = (x <= checkedIndex);
                verticalWinPossible          = (y <= checkedIndex);
                principalDiagonalWinPossible = (x <= checkedIndex && y <= checkedIndex);
                secondaryDiagonalWinPossible = (x >= LINE_SIZE - 1) && (y <= checkedIndex);


                retCount += getWinCombinationCount(fieldToCheck, checkingChar, 0, horizontalWinPossible,
                        verticalWinPossible, principalDiagonalWinPossible, secondaryDiagonalWinPossible, y, x);
            }
        }
        return retCount;
    }

    /**
     * Вычисляет количество выигрышных ситуаций для части игрового поля - матрицы,
     * начальными координатами которой на поле являются y,x и конечными y + lineSize - 1, x + lineSize - 1
     * @param fieldToCheck Матрица, содержащая игровое поле
     * @param checkingChar символ, являющийся фишкой игрока, победу которого мы хотим определить
     * @param gapCount Допустимое количество замен фишек игрока на пустые клетки
     * @param horizontalWinPossible для данных y,x доступна проверка по горизонтали
     * @param verticalWinPossible для данных y,x досиупна проверка по вертикали
     * @param principalDiagonalWinPossible для данных y,x доступна проверка главной диагонали
     * @param secondaryDiagonalWinPossible для данных y,x доступна проверка побочной диагонали
     * @param y начальная координата по вертикали
     * @param x начальная координата по горизонтали
     * @return количество победных комбинаций для  для проверяемого игрока
     */
    public int getWinCombinationCount(char[][] fieldToCheck, char checkingChar,  int gapCount,
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
        while (l < LINE_SIZE && (horizontalWinPossible || verticalWinPossible || principalDiagonalWinPossible || secondaryDiagonalWinPossible)) {
            //Помним, что операции сравнения - ленивые. Можно проверять выход за границы массива и элемент массива в одном if
            if (horizontalWinPossible && (checkingChar != fieldToCheck[y][x + l])) horizontalWinPossible = false;
            if (verticalWinPossible && (checkingChar != fieldToCheck[y + l][x])) verticalWinPossible = false;
            if (principalDiagonalWinPossible && (checkingChar != fieldToCheck[y + l][x + l]))
                principalDiagonalWinPossible = false;
            if (secondaryDiagonalWinPossible && (checkingChar != fieldToCheck[y + l][x - l]))
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
     * @param playerChar Символ - фишка, принадлежащий игроку
     */
    public void humanMove(char playerChar){
        int x;
        int y;

        while (true) {
            System.out.println("Введите X координату");
            x = humanInputCoordinate();

            System.out.println("Введите Y координату");
            y = humanInputCoordinate();

            if (field[y-1][x-1] == CELLTYPE[CELLTYPE_EMPTY]){
                field[y-1][x-1] = playerChar;
                return;
            }

            System.out.printf("Клетка для установки [%s] должна быть свободна\n", playerChar);
            drawField();
        }
    }

    /**
     * Цикл запроса координат у человека
     *
     * @return Координата, введенная пользователем
     */
    public int humanInputCoordinate(){
        int coord;
        while (true){
            coord = scanner.nextInt();
            if (coord < 1 || coord > FIELD_SIZE){
                System.out.printf("Значение должно лежать в диапазоне от 1 до %d\n Введите значение:", FIELD_SIZE);
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
     * @param playerChar Символ - фишка, принадлежащий игроку
     */
    public void firstComputerMove(char playerChar) {
        int x;
        int y;
        while (true){
            //Пытаемся занять центральную ячейку
            x = FIELD_SIZE / 2;
            y = FIELD_SIZE / 2;

            if (field[x][y] == CELLTYPE[CELLTYPE_EMPTY]) break;

            //Если центральная ячейка занята
            x = 1 + x  - random.nextInt(3);
            y = 1 + y  - random.nextInt(3);
            if (field[x][y] == CELLTYPE[CELLTYPE_EMPTY]) break;
        }
        field[x][y] = playerChar;
    }

    /**
     * Ход компьютерного оппонента с расчетом игровой позиции и установкой фишки
     * @param playerChar Символ - фишка, принадлежащий игроку
     */
    public void computerMove(char playerChar, char opponentChar) {
        int[] bestMove = miniMax(field, playerChar, playerChar, opponentChar, 0, 0);
        field[bestMove[1]][bestMove[2]] = playerChar;
        System.out.printf("Введите X координату\n %d\nВведите Y координату\n %d\n", bestMove[2] + 1, bestMove[1] + 1);
    }


    /**
     * Установка фишки компьютерным оппонентом на поле во втором и последующих ходах
     * @param fieldToCheck Матрица, содержащая игровое поле
     * @return Массив из всех доступных для установки фишки клеток. [0][0] хранит количество n, [n][0] - y, [n][1] - x
     */
    public int[][] getComputerPossibleCoordinates(char[][] fieldToCheck){
        int tail = (FIELD_SIZE + 1) % 2;
        int[][] retCoords = new int[FIELD_SIZE*FIELD_SIZE][2];
        int counter = 0;
        int i;
        int j;


        //Ищем место для фишки из центра по спирали
        for (i = (fieldToCheck.length + 1) / 2 - 1; i >= 0; i--) {
            for (j = 0; j < tail; j++) {

                if (fieldToCheck[i][i+j] == CELLTYPE[CELLTYPE_EMPTY]){
                    counter++;
                    retCoords[counter][0] = i;
                    retCoords[counter][1] = i+j;
                }
                if (fieldToCheck[i+j][fieldToCheck.length-i-1] == CELLTYPE[CELLTYPE_EMPTY]){
                    counter++;
                    retCoords[counter][0] = i+j;
                    retCoords[counter][1] = fieldToCheck.length-i-1;
                }
                if (fieldToCheck[fieldToCheck.length-i-1][fieldToCheck.length-i-1-j] == CELLTYPE[CELLTYPE_EMPTY]) {
                    counter++;
                    retCoords[counter][0] = fieldToCheck.length-i-1;
                    retCoords[counter][1] = fieldToCheck.length-i-1-j;
                }
                if (fieldToCheck[fieldToCheck.length-i-1-j][i] == CELLTYPE[CELLTYPE_EMPTY]) {
                    counter++;
                    retCoords[counter][0] = fieldToCheck.length-i-1-j;
                    retCoords[counter][1] = i;
                }

            }
            tail += 2;
        }
        retCoords[0][0] = counter;

        //Немного рандома
        if (counter > 10) {
            shuffleArray(retCoords, 1, counter - 1, fieldToCheck.length, random);
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
     * @param fieldToCheck Матрица, содержащая игровое поле
     * @param playerChar Фишка текущего игрока
     * @param opponentChar Фишка оппонента
     * @param score Счет на предыдущей итерации
     * @param depth Глубина рекурсии
     * @return Весовой коэффициент для позиции текущего игрока
     */
    public int[] miniMax(char[][] fieldToCheck, char checkedPlayer, char playerChar, char opponentChar, int score, int depth){
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
        possibleCoordinates = getComputerPossibleCoordinates(fieldToCheck);

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
            cloneField[i-1] = cloneMatrix(fieldToCheck);
            cloneField[i-1][possibleCoordinates[i][0]][possibleCoordinates[i][1]] = playerChar;
            //Запомнили количество очков за расклад
            moveScore[i-1] = getFieldScore(cloneField[i-1], checkedPlayer, playerChar, opponentChar);
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
            getMoveCoordScore = miniMax(cloneField[i-1], checkedPlayer, playerChar, opponentChar,moveScore[i-1] + score, depth);
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
     * @param fieldToCheck Клон игрового поля
     * @param checkedPlayer Фишка игрока - протагониста
     * @param playerChar Текущий активный игрок, поставивший свою фишку и оценивающий ситуаци.
     * @param opponentChar Оппонент, ход к которому перейдет после завершения хода активного игрока
     * @return Счёт для протагониста
     */
    public int getFieldScore(char[][] fieldToCheck, char checkedPlayer, char playerChar, char opponentChar) {
        int retScore;
        //Проверяем, есть ли победа в этом ходу для активного игрока
        retScore = checkWin(fieldToCheck, playerChar);
        if (retScore > 0) {
            return 1;
        }

        //Проверяем, есть ли победа в этом ходу для неактивного игрока
        retScore = checkWin(fieldToCheck, opponentChar);
        if (retScore > 0) {
            return 1;
        }

        return 0;
    }

}
