package app;
//version 2.0
class FindKValue {

    protected double[][] findValue(double[][] distValue) {
        double[][] tempArray = distValue;
        double[][] arrayResult = new double[tempArray[0].length][tempArray[0].length];

        for (int x = 0; x < tempArray[0].length; x++) {
            for (int y = x; y < tempArray[0].length; y++) {
                if (x != y) {
                    arrayResult[x][y] = arrayResult[y][x] = -(.75) * (Math.log1p((-1.3333333333333333333333333333333333333) * tempArray[x][y]));
                }
            }
        }

        return arrayResult;
    }
}