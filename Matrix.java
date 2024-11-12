import java.util.Arrays;
import java.util.function.BiFunction;

public class Matrix {
    private double[][] matrix;
    private int sizeX;
    private int sizeY;

    public Matrix(double[][] _matrix) {
        this.matrix = _matrix;
        this.sizeX = this.matrix.length;
        this.sizeY = this.matrix[0].length;
    }

    public double det() {
        if (this.sizeX == 2) {
            return this.matrix[0][0] * this.matrix[1][1] - this.matrix[0][1] * this.matrix[1][0];
        }

        double result = 0;

        for (int i = 0; i < this.sizeX; i++) {
            double[][] menor = this.menor(0, i);
            result += Math.pow(-1, i) * this.matrix[0][i] * new Matrix(menor).det();
        }
        return result;
    }

    public static double[] mulFila(double[] fila, double k){
        for (int i = 0; i < fila.length; i++){
            fila[i] *= k;
        }
        return fila;
    }

    public static double[] difFilas(double[] fila1, double[] fila2){
        double[] result = new double[fila1.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = fila1[i] - fila2[i];
        }
        return result;
    }

    public double[][] menor(int y, int x) {
        double[][] result = new double[this.sizeX - 1][this.sizeY - 1];
        int positionX = 0, positionY = 0;

        for (int i = 0; i < this.sizeX; i++) {
            if (i == y)
                continue;
            positionX = 0;
            for (int j = 0; j < sizeY; j++) {
                if (j == x)
                    continue;
                result[positionY][positionX] = this.matrix[i][j];
                positionX++;
            }
            positionY++;
        }
        return result;
    }

    public double[][] transpuesta(double[][] matrix) {
        int sizeX = matrix.length;
        int sizeY = matrix[0].length;
        double[][] result = new double[sizeY][sizeX];
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    public double cofactor(int x, int y) {
        double[][] menor = this.menor(x, y);
        return Math.pow(-1, x + y) * new Matrix(menor).det();
    }

    public double[][] matrixCofactores() {
        return this.map((v, p) -> this.cofactor(p[0], p[1]));
    }

    public double[][] adj() {
        return this.transpuesta(this.matrixCofactores());
    }

    public double[][] inversa() {
        double det = this.det();
        Matrix adj = new Matrix(adj());
        return adj.map((v, p) -> v * (1 / det));
    }

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

    public static String pretty(double[][] _matrix) {
        StringBuilder prettyMatrix = new StringBuilder();
        for (int i = 0; i < _matrix.length; i++) {
            for (int j = 0; j < _matrix[i].length; j++) {
                if (j == 0)
                    prettyMatrix.append("[");
                prettyMatrix.append(String.format((j < _matrix[i].length - 1) ? "%.2f, " : "%.2f", _matrix[i][j]));
                if (j == _matrix[i].length - 1)
                    prettyMatrix.append("]");
            }
            prettyMatrix.append((i != _matrix[i].length - 1) ? "\n " : "\n");
        }
        return prettyMatrix.toString();
    }

    public static void main(String[] args) {
        double[][] A = {
                { 1, 3, 4 },
                { 3, -1, 6 },
                { -1, 5, 1 }
        };
        double[][] B = {
            {1,2,-1,2,1},
            {2,4,1,-2,3},
            {3,6,2,-6,5}
        };
        System.out.printf("A:\n %s", Matrix.pretty(A));
        Matrix matrix = new Matrix(A);
        double[][] m3 = matrix.map((v, p) -> v * 3);
        System.out.printf("A * 3:\n %s", Matrix.pretty(m3));
        System.out.printf("Menor de (1,0):\n %s", Matrix.pretty(matrix.menor(1, 0)));
        System.out.printf("Determinante: %.2f \n", matrix.det());
        System.out.printf("Transpuesta:\n %s", Matrix.pretty(matrix.transpuesta(A)));
        System.out.printf("Matriz de Cofactores:\n %s", Matrix.pretty(matrix.matrixCofactores()));
        System.out.printf("Inversa:\n %s", Matrix.pretty(matrix.inversa()));
        System.out.printf("B:\n %s", Matrix.pretty(B));
        System.out.printf("De B, Fila2 - 2Fila1: \n %s \n", Arrays.toString(difFilas(B[1], Matrix.mulFila(B[0], 2))));
    }
}
