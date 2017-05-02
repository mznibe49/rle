import java.io.*;
import java.util.*;

class Main {

    public static void main(String [] args) throws IOException {
	File f = new File("test.txt");
	RLE r = new RLE (f);
StringBuffer s =	r.lire();
r.compression(s);
BitInputStream input = new BitInputStream(
		 new FileInputStream("rl.txt"));
r.decompression(input);
	
    }
}
