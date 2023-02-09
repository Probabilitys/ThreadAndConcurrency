/**
 * Multiplying Two Matrix Instances with Help from the Fork/Join Framework
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MatrixMult extends RecursiveAction
{
    private final Matrix a, b, c;
    private final int row;

    public MatrixMult(Matrix a, Matrix b, Matrix c)
    {
        this(a, b, c, -1);
    }

    public MatrixMult(Matrix a, Matrix b, Matrix c, int row)
    {
        if (a.getCols() != b.getRows())
            throw new IllegalArgumentException("rows/columns mismatch");
        this.a = a;
        this.b = b;
        this.c = c;
        this.row = row;
    }

    @Override
    public void compute()
    {
        if (row == -1)
        {
            List<MatrixMult> tasks = new ArrayList<>();
            for (int row = 0; row < a.getRows(); row++)
                tasks.add(new MatrixMult(a, b, c, row));
            invokeAll(tasks);
        }
        else
            multiplyRowByColumn(a, b, c, row);
    }

    public static void multiplyRowByColumn(Matrix a, Matrix b, Matrix c,
                                           int row)
    {
        for (int j = 0; j < b.getCols(); j++)
            for (int k = 0; k < a.getCols(); k++)
                c.setValue(row, j, c.getValue(row, j) +
                        a.getValue(row, k) * b.getValue(k, j));
    }

    public static void dump(Matrix m)
    {
        for (int i = 0; i < m.getRows(); i++)
        {
            for (int j = 0; j < m.getCols(); j++)
                System.out.print(m.getValue(i, j) + " ");
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args)
    {
        Matrix a = new Matrix(2, 3);
        a.setValue(0, 0, 1); // | 1 2 3 |
        a.setValue(0, 1, 2); // | 4 5 6 |
        a.setValue(0, 2, 3);
        a.setValue(1, 0, 4);
        a.setValue(1, 1, 5);
        a.setValue(1, 2, 6);
        dump(a);
        Matrix b = new Matrix(3, 2);
        b.setValue(0, 0, 7); // | 7 1 |
        b.setValue(1, 0, 8); // | 8 2 |
        b.setValue(2, 0, 9); // | 9 3 |
        b.setValue(0, 1, 1);
        b.setValue(1, 1, 2);
        b.setValue(2, 1, 3);
        dump(b);
        Matrix c = new Matrix(2, 2);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new MatrixMult(a, b, c));
        dump(c);
    }
}

/**
 * A Class for Representing a Two-Dimensional Table
 */
class Matrix
{
    private final int[][] matrix;
    public Matrix(int nrows, int ncols)
    {
        matrix = new int[nrows][ncols];
    }
    public int getCols()
    {
        return matrix[0].length;
    }
    public int getRows()
    {
        return matrix.length;
    }
    public int getValue(int row, int col)
    {
        return matrix[row][col];
    }
    public void setValue(int row, int col, int value)
    {
        matrix[row][col] = value;
    }
}