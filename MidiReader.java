import java.io.*;
import java.nio.ByteBuffer;

public class MidiReader {
    private DataInputStream input;
    private int headerChunkLength, fileFormat, trackCount, timeDivision;
    private int bytesRead;

    public MidiReader(String filename) {
	try {
	    bytesRead = 0;
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
	    /* NOTE: timeDivision can be interpreted in two ways...
	       This needs to be fixed. */
	    timeDivision = input.readShort();
	} catch (IOException e) {
	    System.err.println(e.toString());
	}
	return true;
    }
    public boolean readTrackChunk() {
	try {
	    char trkID[] = {'M', 'T', 'r', 'k'};
	    for (int i = 0; i < 4; i++) {
		if ((char)input.readByte() != trkID[i]) {
		    System.err.println("Error reading track chunk.");
		    return false;
		}
	    }
	    int chunkLength = input.readInt();
	    bytesRead = 0;
	    while (bytesRead != chunkLength) {
		int dt = readDeltaTime();
		byte status = readByte();
		if (isStatusByte(status)) {
		    //parse status
		}
		else {
		    //treat as running status
		}
		
	    }
	} catch (IOException e) {
	    System.err.println(e.toString());
	}
	return true;
    }
    private int readDeltaTime() {
	byte[] arr = new byte[4];
	int byteCount = 0;
	arr[byteCount] = readByte();
	int mask = 0b10000000;
	/* Read bytes until the most significant bit is set to 0. */
	while ((arr[byteCount++] & mask) == mask) {
	    arr[byteCount] = readByte();
	}
	/* Pad with 0's until arr contains 4 bytes. */
	while (byteCount < 4) {
	    arr[byteCount++] = 0;
	}
	/* Use ByteBuffer to return the bytes of arr as an integer. */
	return ByteBuffer.wrap(arr, 0, byteCount).getInt();
    }
    private boolean isStatusByte(byte b) {
	int mask = 0b10000000;
	return ((b & mask) == mask);
    }
    /* A simple wrapper function that removes the burden of updating bytesRead count 
     * from the programmer. */
    private byte readByte() {
	try {
	    bytesRead++;
	    return input.readByte();
	} catch (IOException e) {
	    System.err.println(e.toString());
	} finally {
	    return 0;
	}
    }
    public void printHeaderInformation() {
	System.out.println("File Format: " + fileFormat);
	System.out.println("Number of track chunks: " + trackCount);
	System.out.println("Time division: " + timeDivision + " ticks per beat.");
    }
	   
}
