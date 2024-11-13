public class Inversa extends Matrix {

    Inversa(double[][] _matrix){
        super(_matrix);
    }
    /**
     * Calcula la inversa de la matriz actual.
     * 
     * @return La matriz inversa de tipo double[][]
     * 
     *         Fórmula:
     *         inv(A) = (1/det(A)) * adj(A)
     */
    public double[][] inversa() {
        double det = this.det();
        Matrix adj = new Matrix(adj());
        return adj.map((v, p) -> v * (1 / det));
    }
}
