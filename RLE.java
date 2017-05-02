import java.io.*;
import java.util.*;

public class RLE {
	private File f;

	public RLE(File f) {
		this.f = f;
	}

	// une fnction qui renvoie un string contenant le fichier
	public StringBuffer lire() throws IOException {
		Scanner in = new Scanner(this.f);
		StringBuffer s = new StringBuffer ("");
		while (in.hasNextLine()) {
		    s.append( in.nextLine());
		    s.append("\n");
		}
		//	System.out.print(s);
		return s;
	}

	 public StringBuffer intToByte(int a){
			if(a == 0) return new StringBuffer("0");
		        ArrayList<Integer> list = new ArrayList<Integer>();
			while( a != 0 ){
			    int x = a%2;
			    list.add(x);
			    a /= 2;
			}	
			StringBuffer b = new StringBuffer("");
			for(int i = list.size()-1; i>=0; i--){
			    char tmp = intToChar(list.get(i));
			    b.append(tmp);		     
			}
			return b;
		    }
	 
	 public char intToChar(int a){
			String s = ""+a;
			return s.charAt(0);
		    }
	 
	  public void writeLengthInFile(BitOutputStream x, StringBuffer res)throws IOException{
			int z = 0;
			while (z < 8-res.length()){
			    x.writeBit(0);
			    z++;
			}
			for(int i = 0; i<res.length(); i++){
			    if(res.charAt(i) == '1')
				x.writeBit(1);
			    else
				x.writeBit(0);    
			}
		    }
	 
	   public void writeCharInFile(BitOutputStream x, char c) throws IOException {
			int a = (int)(c);
			int z = 0;
			StringBuffer res = intToByte(a);
			//System.out.println(" "+res);
			while(z<8-res.length()){
			    x.writeBit(0);
			    z++;
			}
			for(int i = 0; i<res.length(); i++){
			    if(res.charAt(i) == '1')
				x.writeBit(1);
			    else
				x.writeBit(0);
			}
		    }
	public void compression( StringBuffer s) throws IOException {
		BitOutputStream output =  new BitOutputStream(
				 new FileOutputStream("rl.txt"));
		char tmp = s.charAt(0);
		int cpt = 1;
		StringBuffer res;
		for (int j = 1; j < s.length(); j++) {
			if (s.charAt(j) == tmp) {
				cpt++;
			} else { 
				if(cpt<255){
				this.writeCharInFile(output,tmp);
				 res = intToByte(cpt);
			this.writeLengthInFile(output, res);
				cpt = 1;
				tmp = s.charAt(j);
				}else{
					while(cpt>255){
						this.writeCharInFile(output,tmp);
						 res = intToByte(255);
					this.writeLengthInFile(output, res);
						cpt = cpt -255;
					}
					this.writeCharInFile(output,tmp);
					 res = intToByte(cpt);
				this.writeLengthInFile(output, res);
					cpt = 1;
					tmp = s.charAt(j);
				}
			}
		}
	}
	
	
	 public int[] readBit16(int nombre, BitInputStream x)throws IOException {
			int[] bits = new int[nombre];
			for(int i=0; i<bits.length;i++)
			    bits[i] = -1;
				
			int j = 0;
			int tmp=-1;
			for(int i = 0;i < bits.length; i++){
			    tmp = x.readBit();
			    if(tmp < 0){
				break;
			    }
			    else{
				bits[j] = tmp;
				j++;
			    }
			}
			return bits;
		    }
			
		    public LinkedList<Integer> bits(int [] bits){
			LinkedList<Integer> tmp = new LinkedList<Integer>();
			for(int i=0; i< bits.length;i++){
			    if(bits[i] == 1){
				tmp.add(bits[i]);
				for(i=i+1; i<bits.length; i++){
				    tmp.add(bits[i]);
				}
			    }
			}
			return tmp;
		    }
			
			
		    public int convertInDecimal(LinkedList<Integer> binaires){
			int n = binaires.size();
			int p = n -1;
			int res = 0;
			for(int val : binaires){
			    res =  (int)(res+ val*Math.pow(2, p));
			    p--;
			}
			return res;
		    }
			
		    public static char intToCharr(int n){
		            return (char) n;
		    }
	public void decompression (BitInputStream input) throws IOException{
		StringBuffer t =new StringBuffer("");
		int j;
		char lettre;
		while(input.available()!=0){
			lettre = intToCharr(convertInDecimal(bits(readBit16(8, input))));
			j = convertInDecimal(bits(readBit16(8, input )));
			for (int i=0; i<j; i++){
				t.append(lettre);
			}
		}
		
		String end = t.substring(0,t.length());
				
		//	System.out.println(end);	
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("rle.txt")));
		out.write(end);
		out.close();
		}
	

}
