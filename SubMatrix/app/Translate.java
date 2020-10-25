package app;

public class Translate {

    public static final String ALA = "A";
    public static final String ARG = "R";
    public static final String ASN = "N";
    public static final String ASP = "D";
    public static final String CYS = "C";
    public static final String GLN = "Q";
    public static final String GLU = "E";
    public static final String GLY = "G";
    public static final String HIS = "H";
    public static final String ILE = "I";
    public static final String LEU = "L";
    public static final String LYS = "K";
    public static final String MET = "M";
    public static final String PHE = "F";
    public static final String PRO = "P";
    public static final String SER = "S";
    public static final String THR = "T";
    public static final String TRP = "W";
    public static final String TYR = "Y";
    public static final String VAL = "V";
    public static final String ASX = "B";
    public static final String GLX = "Z";
    public static final String Xaa = "X";
    public static final String STOP = "STOP";

    protected String translate(String nucleotide, boolean sequenceIsTemplate) {
        String aminoAcid = "";
        String rnaNucleotide = "";

        if (sequenceIsTemplate) {
            for (int i = 0; i < nucleotide.length(); i++) {
                switch (nucleotide.charAt(nucleotide.length() - 1 - i)) {
                case 'A':
                    rnaNucleotide += 'U';
                    break;
                case 'C':
                    rnaNucleotide += 'G';
                    break;
                case 'T':
                    rnaNucleotide += 'A';
                    break;
                case 'G':
                    rnaNucleotide += 'C';
                    break;
                }
            }
        } else {
            for (int i = 0; i < nucleotide.length(); i++) {
                if (nucleotide.charAt(i) == 'T') {
                    rnaNucleotide += 'U';
                } else {
                    rnaNucleotide += nucleotide.charAt(i);
                }
            }
        }

        for (int currNucleotide = 0; currNucleotide < rnaNucleotide.length() - 2; currNucleotide += 3) {
            String currAminoAcid = "";
            char entry1 = rnaNucleotide.charAt(currNucleotide);
            char entry2 = rnaNucleotide.charAt(currNucleotide + 1);
            char entry3 = rnaNucleotide.charAt(currNucleotide + 2);

            switch (entry1) {
            case 'U':
                switch (entry2) {
                case 'U':
                    switch (entry3) {
                    case 'U':
                        currAminoAcid = PHE;
                        break;
                    case 'C':
                        currAminoAcid = PHE;
                        break;
                    default:
                        currAminoAcid = LEU;
                        break;
                    }
                    break;
                case 'C':
                    currAminoAcid = SER;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        currAminoAcid = TYR;
                        break;
                    case 'C':
                        currAminoAcid = TYR;
                        break;
                    default:
                        currAminoAcid = STOP;
                        break;
                    }
                    break;
                case 'G':
                    switch (entry3) {
                    case 'A':
                        currAminoAcid = STOP;
                        break;
                    case 'G':
                        currAminoAcid = TRP;
                        break;
                    default:
                        currAminoAcid = CYS;
                        break;
                    }
                    break;
                }
                break;
            case 'C':
                switch (entry2) {
                case 'U':
                    currAminoAcid = LEU;
                    break;
                case 'C':
                    currAminoAcid = PRO;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        currAminoAcid = HIS;
                        break;
                    case 'C':
                        currAminoAcid = HIS;
                        break;
                    default:
                        currAminoAcid = GLN;
                        break;
                    }
                    break;
                case 'G':
                    currAminoAcid = ARG;
                    break;
                }
                break;
            case 'A':
                switch (entry2) {
                case 'U':
                    switch (entry3) {
                    case 'G':
                        currAminoAcid = MET;
                        break;
                    default:
                        currAminoAcid = ILE;
                        break;
                    }
                    break;
                case 'C':
                    currAminoAcid = THR;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        currAminoAcid = ASN;
                        break;
                    case 'C':
                        currAminoAcid = ASN;
                        break;
                    default:
                        currAminoAcid = LYS;
                        break;
                    }
                    break;
                case 'G':
                    switch (entry3) {
                    case 'U':
                        currAminoAcid = SER;
                        break;
                    case 'C':
                        currAminoAcid = SER;
                        break;
                    default:
                        currAminoAcid = ARG;
                        break;
                    }
                    break;
                }
                break;
            case 'G':
                switch (entry2) {
                case 'U':
                    currAminoAcid = VAL;
                    break;
                case 'C':
                    currAminoAcid = ALA;
                    break;
                case 'A':
                    switch (entry3) {
                    case 'U':
                        currAminoAcid = ASP;
                        break;
                    case 'C':
                        currAminoAcid = ASP;
                        break;
                    default:
                        currAminoAcid = GLU;
                        break;
                    }
                    break;
                case 'G':
                    currAminoAcid = GLY;
                    break;
                }
                break;
            }
            aminoAcid = aminoAcid + currAminoAcid;
        }
        return aminoAcid;
    }
}