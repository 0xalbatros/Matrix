import java.util.function.BiFunction;

public class Matrix {
    public double[][] matrix;
    private int sizeX;
    private int sizeY;

    /**
     * Constructor que inicializa la matriz y sus dimensiones
     * 
     * @param _matrix La matriz bidimensional de tipo double[][]
     */
    public Matrix(double[][] _matrix) {
        this.matrix = _matrix;
        this.sizeX = this.matrix.length;
        this.sizeY = this.matrix[0].length;
    }

    /**
     * Crea una matriz aumentada a partir de una matriz de coeficientes y un arreglo
     * de términos independientes.
     * La matriz aumentada incluye los valores de la matriz `matrizA` y añade una
     * columna adicional con los valores de `arreglo`.
     *
     * @param matrizA Matriz de coeficientes (de tamaño m x n) que representa el
     *                sistema de ecuaciones.
     * @param arreglo Vector de términos independientes, uno por cada fila de
     *                `matrizA`.
     * @return La matriz aumentada, de dimensiones m x (n + 1), donde la última
     *         columna es el vector de términos independientes.
     */
    public static double[][] matrizAum(double[][] matrizA, double[] arreglo) {
        int col = matrizA.length;
        int filas = matrizA[0].length;
        double[][] matrizAum = new double[filas][col + 1];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < filas; j++) {
                matrizAum[i][j] = matrizA[i][j];
            }
        }

        for (int k = 0; k < filas; k++) {
            matrizAum[k][matrizAum[0].length - 1] = arreglo[k];
        }

        return matrizAum;
    }

    /**
     * Calcula el determinante de la matriz usando la expansión de cofactores.
     * 
     * @return El determinante de la matriz.
     * 
     *         Fórmula general:
     *         Para una matriz 2x2:
     *         det(A) = a11 * a22 - a12 * a21
     * 
     *         Para una matriz NxN (expansión por cofactores en la primera fila):
     *         det(A) = Σ (-1)^i * a1i * det(M1i)
     *         donde M1i es el menor de a1i.
     */
    public double det() {
        if (this.sizeX == 2) { // Caso base para una matriz 2x2
            return this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0];
        }

        double result = 0;
        for (int i = 0; i < this.sizeX; i++) {
            double[][] menor = this.menor(0, i);
            result += Math.pow(-1, i) * this.matrix[0][i] * new Matrix(menor).det();
        }
        return result;
    }

    /**
     * Genera la submatriz "menor" excluyendo la fila y columna dadas
     * 
     * @param x La fila a excluir
     * @param y La columna a excluir
     * @return Una nueva matriz de tipo double[][] que representa el menor de la
     *         posición (y, x)
     */
    public double[][] menor(int x, int y) {
        double[][] result = new double[this.sizeX - 1][this.sizeY - 1];
        int positionX = 0, positionY = 0;

        for (int i = 0; i < this.sizeX; i++) {
            if (i == x)
                continue;
            positionX = 0;
            for (int j = 0; j < sizeY; j++) {
                if (j == y)
                    continue;
                result[positionY][positionX] = this.matrix[i][j];
                positionX++;
            }
            positionY++;
        }
        return result;
    }

    /**
     * Calcula la matriz transpuesta de la matriz dada
     * 
     * @param matrix La matriz original de tipo double[][]
     * @return La matriz transpuesta de tipo double[][]
     */
    public double[][] transpuesta(double[][] _matrix) {
        int sizeX = _matrix.length;
        int sizeY = _matrix[0].length;
        double[][] result = new double[sizeY][sizeX];
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                result[j][i] = _matrix[i][j];
            }
        }
        return result;
    }

    /**
     * Calcula el cofactor de la posición (x, y) de la matriz.
     * 
     * @param x La fila de la posición.
     * @param y La columna de la posición.
     * @return El cofactor de tipo double.
     * 
     *         Fórmula:
     *         C_ij = (-1)^(i+j) * det(M_ij)
     *         donde M_ij es el menor de la posición (i, j).
     */
    public double cofactor(int x, int y) {
        double[][] menor = this.menor(x, y);
        return Math.pow(-1, x + y) * new Matrix(menor).det();
    }

    /**
     * Calcula la matriz de cofactores de la matriz actual
     * 
     * @return Una nueva matriz de tipo double[][] que representa los cofactores
     */
    public double[][] matrixCofactores() {
        return this.map((v, p) -> this.cofactor(p[0], p[1]));
    }

    /**
     * Calcula la matriz adjunta de la matriz actual.
     * 
     * @return La matriz adjunta de tipo double[][]
     * 
     *         Fórmula:
     *         adj(A) = Traspuesta de la Matriz de Cofactores
     */
    public double[][] adj() {
        return this.transpuesta(this.matrixCofactores());
    }

    /**
     * Aplica una función a cada elemento de la matriz
     * 
     * @param callback Una función que toma un valor y su posición y retorna un
     *                 valor modificado
     * @return Una nueva matriz con los valores modificados
     */
    public double[][] map(BiFunction<Double, int[], Double> callback) {
        double[][] result = new double[this.sizeX][this.sizeY];
        for (int i = 0; i < this.sizeX; i++) {
            for (int j = 0; j < this.sizeY; j++) {
                int[] position = { i, j };
                result[i][j] = callback.apply(this.matrix[i][j], position);
            }
        }
        return result;
    }
}
