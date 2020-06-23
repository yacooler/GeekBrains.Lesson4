import java.util.Scanner;


public class TicTacToe {

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame(){

        int FIELD_SIZE = 5;
        int LINE_SIZE = 4;
        char CELLTYPE_EMPTY = '•';
        char CELLTYPE_X = 'X';
        char CELLTYPE_O = 'O';

        boolean[] isHumanPlayer = new boolean[2];
        int currentPlayer = 1;

        Scanner scanner = new Scanner(System.in);

        selectPlayers(isHumanPlayer, scanner);

        char[][] field = new char[FIELD_SIZE][FIELD_SIZE];
        clearField(field, CELLTYPE_EMPTY);

//        char[][] field = {
//                {'X','X','X','x','X'},
//                {'.','X','x','X','X'},
//                {'.','x','x','X','X'},
//                {'.','x','X','X','X'},
//                {'.','X','X','X','X'}};

        drawField(field);

        System.out.println(checkWin(field,'.',4));

    }


    /**
     * Выбор типа игрока - человек true, компьютерный оппонент - false
     * @param isHumanPlayer массив, содержащий признак игрока - человека
     * @param scanner ссылка на сканер для ввода данных
     */
    public static void selectPlayers(boolean[] isHumanPlayer, Scanner scanner){
        //Для большего количества можно было бы использовать цикл
        System.out.println("Введите тип первого и второго игрока");
        System.out.println("Первый игрок - человек или ИИ? true - человек, false - ИИ");
        isHumanPlayer[0] = scanner.nextBoolean();

        System.out.println("Второй игрок - человек или ИИ? true - человек, false - ИИ");
        isHumanPlayer[1] = scanner.nextBoolean();

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
            System.out.print(" " + i + " ");
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.print(i);
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
        boolean horizontalWin;
        boolean verticalWin;
        boolean principalDiagonalWin;
        boolean secondaryDiagonalWin;

        /*Проверка на наличие полностью заполненных линий.
        y,x содержат начальную координату для проверки
        l - цикл по координатам от начальной точки проверки до длины линии
        */
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                /*
                    Инициализируем булевые переменные.
                    если поиск для текущих X и Y валиден - ставим true.
                    В цикле поиска если встретим запрещенный символ - ставим false
                 */
                horizontalWin = x <= checkedIndex;
                verticalWin = y <= checkedIndex;
                principalDiagonalWin = (y <= checkedIndex) && (x <= checkedIndex);
                secondaryDiagonalWin = (y <= checkedIndex) && (x >= (lineSize - 1));

                for (int l = 0; l < lineSize; l++) {
                    //Помним, что операции сравнения - ленивые. Можно проверять выход за границы массива и элемент массива разом
                    //Проверка горизонтали. Перебираем все стартовые значения по Y, и только часть стартовых значений по X
                    if ((x <= checkedIndex) && (checkingChar != field[y][x + l])) horizontalWin = false;
                    //Проверка горизонтали. Перебираем часть стартовых значений по Y, и все значения по X
                    if ((y <= checkedIndex) && (checkingChar != field[y + l][x])) verticalWin = false;
                    //Проверка главной диагонали
                    if ((y <= checkedIndex) && (x <= checkedIndex) && (checkingChar != field[y + l][x + l]))
                        principalDiagonalWin = false;
                    //Проверка побочной диагонали
                    if ((y <= checkedIndex) && (x >= (lineSize - 1)) && (checkingChar != field[y + l][x - l]))
                        secondaryDiagonalWin = false;
                }

                System.out.println("Y:" + y + " X:" + x);

                //Если нашли победу - выходим и дальше не сканируем
                if (horizontalWin || verticalWin || principalDiagonalWin || secondaryDiagonalWin) {
                    System.out.println("horizontalWin " + horizontalWin);
                    System.out.println("verticalWin " + verticalWin);
                    System.out.println("principalDiagonalWin " + principalDiagonalWin);
                    System.out.println("secondaryDiagonalWin " + secondaryDiagonalWin);


                    return true;
                }
            }
        }
        return false;
    }

}
