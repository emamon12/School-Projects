package app;

class FindDistValues{

    protected double[][] findValue(int numSpecies, String[] sequenceString){
        double[][] distValues = new double[numSpecies][numSpecies];

        for (int x = 0; x < numSpecies; x++) {
            for (int y = x; y < numSpecies; y++) {
                if (x != y) {
                    double denom = 0;
                    double nom = 0;
                    for (int currChar = 0; currChar < sequenceString[x].length() - 1; currChar++) {
                        if (sequenceString[x].charAt(currChar) != '-' && sequenceString[y].charAt(currChar) != '-') {
                            denom++;
                            if (sequenceString[x].charAt(currChar) == sequenceString[y].charAt(currChar)) {
                                nom++;
                            }
                        }
                    }
                    distValues[x][y] = distValues[y][x] = nom / denom;
                } else {
                    distValues[x][y] = 0;
                }
            }
        }


        return distValues;
    }
}