
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Liuxuan Huang
 */
public class Main {
    public static void main(String[] args) {
    	// run 10 input files together.
    	for (int i=1; i<=10; i++) {
    		String inputName = "input/" + i + ".in";
    		String outputName = i + ".out";
    		File input = new File(inputName);
    		File output = new File(outputName);
    		getOneResult(input, output);
    	}
    }

    public static void getOneResult(File inputFile, File outputFile) {
    	try (Scanner scanner1 = new Scanner(inputFile)) {
            PrintWriter pw = new PrintWriter(outputFile);
            scanner1.useDelimiter("\n");

            // get the total number of safeguards first and declare an array to store the shift info.
            int num_guard = scanner1.nextInt();
            int[] shift = new int[num_guard]; // int array to store contribution of each safeguard.
            TreeMap<Integer, Integer> map = new TreeMap<>(); // treemap to store covered time zone
            int start, end;
            long covered = 0; // otherwise might overflow
            int index = 0; // current safeguard index;

            // loop through each safeguard
            while (scanner1.hasNext()){
                try (Scanner scanner2 = new Scanner(scanner1.next())) {
                    scanner2.useDelimiter(" ");
                    start = scanner2.nextInt();
                    end = scanner2.nextInt();
                    
                    // store info to data structure
                    int new_start, new_end = end, position;

                    // start point
                    if (map.floorKey(start) == null || map.get(map.floorKey(start)) < start) {
                    	new_start = start;
                    	position = start;
                    }
                    else {
                    	new_start = map.floorKey(start);
                    	position = map.get(map.floorKey(start));
					}
					// end point
					while (position < end) {
						// end is within position - start of next position
						if (map.ceilingKey(position) == null || map.ceilingKey(position) > end) {
							shift[index] += end - position;
							new_end = end;
							position = end;
						}else {
							// proceed to next interval
							// increment contribution
							shift[index] += map.ceilingKey(position) - position;
							// remove the existing interval
							position = map.ceilingKey(position);
							new_end = map.get(position);
							map.remove(position);
							position = new_end;
						}
					}
					// put current updated interval to map
                    map.put(new_start, new_end);
                    // increment total time covered.
                    covered += shift[index];
                }
                index++;
            }
            scanner1.close();

            // after all info are stored in data structure, sort the array
            // get the minimum contribution, and write the final answer to file.
            Arrays.sort(shift);
            pw.write(String.valueOf(covered - shift[0]));
            System.out.println(covered - shift[0]);
        	pw.close();

        // catch exception
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}