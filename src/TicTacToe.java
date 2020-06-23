import java.util.Scanner;


public class TicTacToe {

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame(){

        int FIELD_SIZE = 5;
        int LINE_SIZE = 4;

        char[] CELLTYPE= {'X','O','•'};
        int CELLTYPE_EMPTY = 2;

        boolean[] isHumanPlayer = new boolean[2];


        Scanner scanner = new Scanner(System.in);

        selectPlayers(isHumanPlayer, scanner);

        char[][] field = new char[FIELD_SIZE][FIELD_SIZE];
        clearField(field, CELLTYPE[CELLTYPE_EMPTY]);

        gameLoop(field, isHumanPlayer, LINE_SIZE, CELLTYPE, scanner);


    }

    public static void gameLoop(char[][] field, boolean[] isHumanPlayer, int lineSize, char[] cellType, Scanner scanner ){
        int CELLTYPE_EMPTY = 2;
        int currentPlayer = 0;

        while (true){

            System.out.printf("Ход %d игрока!\n", (currentPlayer+1));
            drawField(field);

            if (checkDraw(field, cellType[CELLTYPE_EMPTY])){
                System.out.println("Ничья!");
                break;
            }

            if (isHumanPlayer[currentPlayer]){
                humanMove(field, cellType[CELLTYPE_EMPTY], cellType[currentPlayer], scanner);
            } else {
                computerMove(field, cellType[CELLTYPE_EMPTY], cellType[currentPlayer]);
            }

            if (checkWin(field, cellType[currentPlayer], lineSize)){
                System.out.printf("Игрок %d победил!\n", (currentPlayer+1));
                drawField(field);
                break;
            }

            currentPlayer = currentPlayer == 1 ? 0 : 1;

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
            System.out.println("Два компьютерных ИИ будут сражаться между собой. Нажимайте Enter для перехода хода от одного к другому");
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
     *
     * @param field матрица символов, содержащая игровое поле
     * @param emptyCellChar символ по умолчанию, не являющийся фишкой первого или второго игрока
     * @return true если на игровом поле ситуация ничьей
     */
    public static boolean checkDraw(char[][] field, char emptyCellChar){
        int size = field.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (emptyCellChar == field[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * Проверка, есть ли на игровой доске победная ситуация для одного или другого игрока
     *
     * @param field матрица символов, содержащая игровое поле
     * @param checkingChar символ, являющийся фишкой игрока, победу которого мы хотим определить
     * @param lineSize размер непрерывной линии фишек, при достижении которой засчитывается победа
     * @return true если проверяемый игрок победил
     */
    public static boolean checkWin(char[][] field, char checkingChar, int lineSize){
        int size = field.length;
        int checkedIndex = size - lineSize;
        int loops = 0;
        //Переменные указывают, может ли из точки y,x теоретически быть построена выигрышная комбинация
        boolean horizontalWinPossible;
        boolean verticalWinPossible;
        boolean principalDiagonalWinPossible;
        boolean secondaryDiagonalWinPossible;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                //Если поиск для текущих X и Y для выбранного направления валиден - ставим true
                //Поиск идет вниз, вправо, влево вниз и вправо вниз от точки y,x
                horizontalWinPossible = x <= checkedIndex;
                verticalWinPossible = y <= checkedIndex;
                principalDiagonalWinPossible = (y <= checkedIndex) && (x <= checkedIndex);
                secondaryDiagonalWinPossible = (y <= checkedIndex) && (x >= (lineSize - 1));

                //l - цикл для проверки всех точек линии в 4х направлениях с начальными координатами y,x
                for (int l = 0; l < lineSize && (horizontalWinPossible || verticalWinPossible || principalDiagonalWinPossible || secondaryDiagonalWinPossible); l++) {

                    //Помним, что операции сравнения - ленивые. Можно проверять выход за границы массива и элемент массива в одном if
                    if (horizontalWinPossible && (checkingChar != field[y][x + l])) horizontalWinPossible = false;
                    if (verticalWinPossible   && (checkingChar != field[y + l][x])) verticalWinPossible = false;
                    if (principalDiagonalWinPossible  && (checkingChar != field[y + l][x + l])) principalDiagonalWinPossible = false;
                    if (secondaryDiagonalWinPossible  && (checkingChar != field[y + l][x - l])) secondaryDiagonalWinPossible = false;

                    loops++;
                }
                //Победная комбинация символов в одном из направлений была достигнута
                if (horizontalWinPossible || verticalWinPossible || principalDiagonalWinPossible || secondaryDiagonalWinPossible) {
                    System.out.println(loops);
                    return true;
                }
            }
        }
        System.out.println(loops);
        return false;
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


    public static void computerMove(char[][] field, char emptyCellChar, char playerChar) {
        //TODO: do magic
    }
}
