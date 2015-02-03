import java.io.*;

public class MidiReader {
    private DataInputStream input;
    private int headerChunkLength, fileFormat, trackCount, timeDivision;

    public MidiReader(String filename) {
	try {
	    input = new DataInputStream(new FileInputStream(filename));
	} catch (IOException e) {
	    System.err.println(e.toString());
	}
    }

    public boolean readHeaderChunk() {
	try {
	    char headID[] = {'M', 'T', 'h', 'd'};
	    for (int i = 0; i < 4; i++) {
		if ((char)input.readByte() != headID[i]) {
		    System.err.println("Error reading header chunk. Not a valid Midi file.");
		    return false;
		}
	    }
	    headerChunkLength = input.readInt();
	    fileFormat = input.readShort();
	    trackCount = input.readShort();
	    timeDivision = input.readShort();
	} catch (IOException e) {
	    System.err.println(e.toString());
	}
	return true;
    }

    public void printHeaderInformation() {
	System.out.println("File Format: " + fileFormat);
	System.out.println("Number of track chunks: " + trackCount);
	System.out.println("Time division: " + timeDivision + " ticks per beat.");
    }
}
