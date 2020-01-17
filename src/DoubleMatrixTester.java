import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InsufficientElementsException extends Exception {
    public InsufficientElementsException(){
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException extends Exception{
    public InvalidRowNumberException(){super("Invalid row number");}
}

class InvalidColumnNumberException extends Exception{
    public InvalidColumnNumberException(){super("Invalid column number");}
}

final class DoubleMatrix{
    private final double a[][];
    public DoubleMatrix(double a[], int m, int n) throws InsufficientElementsException {
        this.a = new double[m][n];
        if(a.length < m * n)
            throw new InsufficientElementsException();
        double [] tmp = Arrays.copyOfRange(a,a.length- m*n, a.length);
        //this.a = IntStream.range(0,m).mapToObj(j ->IntStream.range(0,n).mapToDouble(i -> tmp[i]).toArray()).toArray(double[][]::new);
        IntStream.range(0,m).forEach(j -> this.a[j] = Arrays.copyOfRange(tmp, j*n, n+n*j));
    }
    public DoubleMatrix(Double a[], int m, int n) throws InsufficientElementsException {
        if(a.length < m * n)
            throw new InsufficientElementsException();
        this.a = new double[m][n];
        double [] tmp = new double[a.length];
        for(int i=a.length - m*n;i<a.length;i++)
            tmp[i - (a.length - m*n)] = a[i];
        IntStream.range(0,m).forEach(j -> this.a[j] = Arrays.copyOfRange(tmp, j*n, n+n*j));
    }
    String getDimensions(){return "[" + this.a.length + " x " + this.a[0].length + "]";}
    int rows(){return this.a.length;}
    int columns(){return this.a[0].length;}
    double maxElementAtRow(int row) throws InvalidRowNumberException {
        if(row > this.a.length || row <= 0)
            throw new InvalidRowNumberException();
        return Arrays.stream(this.a[row-1]).max().getAsDouble();
    }
    double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if(column > this.a[0].length || column <= 0)
            throw new InvalidColumnNumberException();
        return IntStream.range(0,this.a.length).mapToDouble(i -> a[i][column - 1]).max().getAsDouble();
    }
    double sum(){
        return Arrays.stream(this.a).mapToDouble(array-> Arrays.stream(array).sum()).sum();
    }
    double [] toSortedArray(){
       double [] temp =  Arrays.stream(this.a).flatMapToDouble(array -> Arrays.stream(array)).sorted().toArray();
       IntStream.range(0,temp.length).forEach(i-> temp[i] = temp[i]*(-1));
       Arrays.sort(temp);
       IntStream.range(0,temp.length).forEach(i-> temp[i] = temp[i]*(-1));
       return temp;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00");
        StringBuilder sb = new StringBuilder();
        Arrays.stream(this.a).forEach(array -> {
            Arrays.stream(array).forEach(element -> sb.append(df.format(element) + "\t"));
            sb.replace(sb.length()-1, sb.length(),"\n");
            }
        );
        sb.delete(sb.length()-1,sb.length());
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(this.getClass()!=obj.getClass()) return false;
        DoubleMatrix other = (DoubleMatrix)obj;
        if(rows()!=((DoubleMatrix) obj).rows()) return false;
        if(columns()!=((DoubleMatrix) obj).columns()) return false;
        for(int i=0;i<rows();i++)
            for(int j=0;j<columns();j++)
                if(this.a[i][j]!= other.a[i][j])
                    return false;
        return true;
    }
    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(this.a);
        result = 31 * result + rows();
        result = 31 * result + columns();
        return result;
    }
}

class MatrixReader{
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException, IOException {
        Scanner in = new Scanner(new InputStreamReader(input));
        String []line = in.nextLine().split(" ");
        int m = Integer.parseInt(line[0]), n = Integer.parseInt(line[1]);
        Double [] array = new Double[m*n];
        IntStream.range(0,m*n).mapToDouble(i ->  in.nextDouble()).boxed().collect(Collectors.toList()).toArray(array);
        in.close(); input.close();
        return new DoubleMatrix(array,m,n);
    }
}


public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }
        scanner.close();
    }
}
