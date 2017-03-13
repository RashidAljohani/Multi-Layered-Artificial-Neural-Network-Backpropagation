/*
*  
*  Author  : Rashid A. Aljohani
*  Thursday, March 13, 2017
*
*/


import java.util.*;

public class XOR{
	

	public static void main(String[] args) {
	

		Helper helper = new Helper();

		int[][] dataset = { {0,0,0}, {0,1,1}, {1,0,1}, {1,1,0} };	// {input,input,target}
		double lc = 0.3;
		Map<Node, Set<Weight>> connections = new LinkedHashMap<>();

		List<Node> inputs_units = new LinkedList<>();
		List<Node> hiddens_units = new LinkedList<>();
		Node output_unit = new Node("output[1]");

		// MARK: initialized the neural network
		helper.init(inputs_units, hiddens_units, output_unit, connections);


	// MARK: training stage
	int epoch = 0;
	while(epoch < 100000){

		for(int[] row : dataset){

			// MARK: create an input unit, add it to the List
			for(int col=0; col < row.length - 1; col += 1){

				inputs_units.get(col).set_input( row[col] );
			}

			// MARK: set target value
			output_unit.set_target( row[row.length - 1] );

			helper.feedforward(connections);
			helper.backpropagation(connections, output_unit, lc);

		}

		epoch += 1;
	}



	// MARK: testing stage
	for(int[] row : dataset){

		// MARK: create an input unit, add it to the List
		for(int col=0; col < row.length - 1; col += 1){

			inputs_units.get(col).set_input( row[col] );
		}

		// MARK: set target value
		output_unit.set_target( row[row.length - 1] );
			
		helper.feedforward(connections);
		helper.backpropagation(connections, output_unit, lc);

		print("output\t target: " + output_unit.get_target() + "\tactual: " + output_unit.get_actual() + "\n");

		}

	}


	static void print(String str){
		System.out.print(str);
	}
}
