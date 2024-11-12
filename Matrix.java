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
            result += Math.pow(-1, i) * matrix[0][i] * new Matrix(menor).det();
        }
        return result;
    }

    public double[][] menor(int y, int x) {
        double[][] result = new double[sizeX - 1][sizeY - 1];
        int positionX = 0, positionY = 0;

        for (int i = 0; i < sizeX; i++) {
            if (i == y)
                continue;
            positionX = 0;
            for (int j = 0; j < sizeY; j++) {
                if (j == x)
                    continue;
                result[positionY][positionX] = matrix[i][j];
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
        return this.map((value, position) -> this.cofactor(position[0], position[1]));
    }

    public double[][] adj() {
        return this.transpuesta(this.matrixCofactores());
    }

    public double[][] multiplicarPorConstante(double constante) {
        return this.map((value, position) -> value * constante);
    }

    public double[][] inversa() {
        double det = this.det();
        Matrix adj = new Matrix(adj());
        return adj.map((value, position) -> value * (1 / det));
    }

    public double[][] map(BiFunction<Double, int[], Double> callback) {
        double[][] result = new double[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int[] position = { i, j };
                result[i][j] = callback.apply(this.matrix[i][j], position);
            }
        }
        return result;
    }

    public static void pretty(double[][] _matrix) {
        StringBuilder prettyMatrix = new StringBuilder();
        for (int i = 0; i < _matrix.length; i++) {
            for (int j = 0; j < _matrix[i].length; j++) {
                if (j == 0)
                    prettyMatrix.append("[");
                prettyMatrix.append(String.format("%.2f", _matrix[i][j]));
                if (j < _matrix[i].length - 1)
                    prettyMatrix.append("\t");
                if (j == _matrix[i].length - 1)
                    prettyMatrix.append("]");
            }
            prettyMatrix.append("\n");
        }
        System.out.println(prettyMatrix);
    }

    public static void main(String[] args) {
        double[][] m = {
                { 1, 3, 4 },
                { 3, -1, 6 },
                { -1, 5, 1 }
        };
        Matrix matrix = new Matrix(m);
        double[][] m3 = matrix.map((v, p) -> v * 3);
        System.out.println("matrix * 3: " + Arrays.deepToString(m3));
        System.out.println("Menor de (1,0): " + Arrays.deepToString(matrix.menor(1, 0)));
        System.out.println("Determinante: " + matrix.det());
        System.out.println("Transpuesta: " + Arrays.deepToString(matrix.transpuesta(m)));
        System.out.println("matrix de Cofactores: " + Arrays.deepToString(matrix.matrixCofactores()));
        System.out.println("Multiplicada por constante 2: " + Arrays.deepToString(matrix.multiplicarPorConstante(2)));
        System.out.println("Inversa: " + Arrays.deepToString(matrix.inversa()));
        Matrix.pretty(m);
    }
}
