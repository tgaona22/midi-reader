public class MidiTest {
    public static void main(String[] args) {
	MidiReader reader = new MidiReader("bach_bourree.mid");
	reader.readHeaderChunk();
	reader.printHeaderInformation();
    }
}
